package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.util.Task;

import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.channels.DatagramChannel;

public class Server {
    public Console console;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final DatagramChannel channel;
    private int expectedTotalPackets = -1;
    private int lastReceivedPacket = -1;

    private final Map<Integer, ByteBuffer> receivedPackets = new TreeMap<>();

    private final ForkJoinPool receivePool = new ForkJoinPool();
    private final ForkJoinPool processPool = new ForkJoinPool();
    private final ExecutorService responsePool = Executors.newCachedThreadPool();

    public Server(Console console) throws IOException {
        this.console = console;
        channel = DatagramChannel.open();
        SocketAddress localAddress = new InetSocketAddress(1488);
        channel.bind(localAddress);
    }

    public void runServer() {
        logger.info("Server is running...");
        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                SocketAddress remoteAddr = channel.receive(buffer);
                buffer.flip();
                if (buffer.remaining() >= 8) {
                    receivePool.execute(() -> processPacket(buffer, remoteAddr));
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении задачи: " + e.getMessage());
        }
    }

    private void processPacket(ByteBuffer buffer, SocketAddress remoteAddr) {
        try {
            int packetNumber = buffer.getInt();
            int totalPackets = buffer.getInt();

            if (expectedTotalPackets == -1) {
                expectedTotalPackets = totalPackets;
                logger.info("Ожидается общее количество пакетов: " + totalPackets);
            }

            if (!receivedPackets.containsKey(packetNumber)) {
                receivedPackets.put(packetNumber, buffer);
                lastReceivedPacket = Math.max(lastReceivedPacket, packetNumber);
            }

            if (receivedPackets.size() == expectedTotalPackets) {
                ByteBuffer completeData = assembleData();
                processPool.execute(() -> {
                    try {
                        Task task = deserializeTask(completeData);
                        logger.info("Выполнение команды");
                        Task responseTask = console.start(task); // Передаем dbHandler
                        logger.info("Отправка ответа");
                        responsePool.execute(() -> {
                            try {
                                sendResponse(responseTask, remoteAddr);
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "Ошибка при отправке ответа: " + e.getMessage());
                            }
                        });
                        resetState();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Ошибка при обработке задачи: " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при обработке задачи: " + e.getMessage());
        }
    }

    private ByteBuffer assembleData() {
        ByteBuffer completeData = ByteBuffer.allocate(expectedTotalPackets * 1024);
        for (Map.Entry<Integer, ByteBuffer> entry : receivedPackets.entrySet()) {
            ByteBuffer part = entry.getValue();
            part.position(8);
            completeData.put(part);
        }
        completeData.flip();
        return completeData;
    }

    private Task deserializeTask(ByteBuffer data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data.array(), 0, data.limit());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Task) ois.readObject();
    }

    private void sendResponse(Task task, SocketAddress remoteAddr) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(task);
        oos.flush();
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        channel.send(buffer, remoteAddr);
    }

    private void resetState() {
        receivedPackets.clear();
        expectedTotalPackets = -1;
        lastReceivedPacket = -1;
    }
}
