package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final DatagramSocket socket = new DatagramSocket(8000);
    public Console console;
    private final InetAddress address = InetAddress.getByName("localhost");
    private static final Logger logger = Logger.getLogger("logger");
    private final ExecutorService fixedThreadPoolTask = Executors.newFixedThreadPool(10);
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private final ExecutorService fixedThreadPoolAnswer = Executors.newFixedThreadPool(10);
    private final LinkedBlockingQueue<Task> blockingQueueTask = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Task> blockingQueueAnswer = new LinkedBlockingQueue<>();
    private final Map<String, Map<Integer, byte[]>> sessionData = new ConcurrentHashMap<>();
    private final Map<String, Integer> sessionTotalPackets = new ConcurrentHashMap<>();
    private final byte[] buffer = new byte[4096];
    private final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    public Server() throws SocketException, UnknownHostException {

    }


    public Server(Console console) throws SocketException, UnknownHostException {
        this.console = console;
    }

    public void runServer() throws IOException {
        fixedThreadPoolTask.submit(() -> {
            try {
                while (true) {
                    logger.log(Level.INFO, "Получение информации");
                    byte[] buffer = new byte[4096];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    processPacket(packet);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при получении пакетов: " + e.getMessage());
            }
        });

        cachedThreadPool.submit(() -> {
            try {
                while (true) {
                    processTask();
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при обработке задачи: " + e.getMessage());
            }
        });

        fixedThreadPoolAnswer.submit(() -> {
            try {
                while (true) {
                    Task answer = blockingQueueAnswer.take();
                    sendAnswer(address, 8000, answer);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при отправке ответа: " + e.getMessage());
            }
        });
    }

    public void processTask() throws InterruptedException {
        Task task = blockingQueueTask.take();
        logger.log(Level.INFO, "Выполнение команды");
        blockingQueueAnswer.put(console.start(task));
    }

    private void processPacket(DatagramPacket packet) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
        DataInputStream dis = new DataInputStream(bais);
        int packetId = dis.readInt();
        int totalPackets = dis.readInt();
        byte[] data = new byte[dis.available()];
        dis.readFully(data);

        String sessionId = packet.getAddress().toString() + ":" + packet.getPort();
        sessionTotalPackets.put(sessionId, totalPackets);

        Map<Integer, byte[]> packets = sessionData.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        packets.put(packetId, data);

        if (packets.size() == totalPackets) assembleData(sessionId, packets, totalPackets);
    }

    private void assembleData(String sessionId, Map<Integer, byte[]> packets, int totalPackets) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < totalPackets; i++) {
            baos.write(packets.get(i));
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            Task task = (Task) ois.readObject();
            blockingQueueTask.put(task);
        } catch (ClassNotFoundException | InterruptedException e) {
            logger.log(Level.SEVERE, "Ошибка при десериализации задачи: " + e.getMessage());
        }
    }


    private void sendAnswer(InetAddress address, int port, Task answer) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(answer);
        byte[] sendBuffer = byteArrayOutputStream.toByteArray();
        DatagramPacket responsePacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        socket.send(responsePacket);
    }
}