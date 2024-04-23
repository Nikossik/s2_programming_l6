package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
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
    private final byte[] buffer = new byte[4096];
    private final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    public Server() throws SocketException, UnknownHostException {

    }

    public Server(Console console) throws SocketException, UnknownHostException {
        this.console = console;
    }

    public void runServer() throws IOException, ClassNotFoundException {

        fixedThreadPoolTask.submit(() -> {
            try {
                while (true) {
                    logger.log(Level.INFO, "Получение информации");
                    socket.receive(packet);
                    byte[] data = packet.getData();
                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
                    Task task = (Task) objectInputStream.readObject();
                    blockingQueueTask.put(task);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при получении задачи: " + e.getMessage());
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
                    sendAnswer(address, packet.getPort(), answer);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при отправке ответа: " + e.getMessage());
            }
        });
    }

    public void processTask() throws InterruptedException {
        Task task = blockingQueueTask.take();
        System.out.println(task.describe[0]);
        logger.log(Level.INFO, "Выполнение команды");
        blockingQueueAnswer.put(console.start(task));
    }

    public void sendAnswer(InetAddress address, int port, Task answer) throws IOException {
        logger.log(Level.INFO, "Отправка ответа");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(answer);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}
