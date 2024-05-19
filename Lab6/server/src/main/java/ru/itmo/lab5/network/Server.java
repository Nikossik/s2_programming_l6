package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.CommandInvoker;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.channels.DatagramChannel;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final DatagramChannel channel;
    private int expectedTotalPackets = -1;
    private int lastReceivedPacket = -1;

    private final Map<Integer, ByteBuffer> receivedPackets = new TreeMap<>();

    private final ForkJoinPool receivePool = new ForkJoinPool();
    private final ForkJoinPool processPool = new ForkJoinPool();
    private final ExecutorService responsePool = Executors.newCachedThreadPool();
    private final CollectionManager collectionManager;
    private final CommandInvoker commandInvoker;

    public Server(DatabaseHandler dbHandler) throws IOException {
        this.collectionManager = CollectionManager.getInstance(dbHandler);
        this.commandInvoker = new CommandInvoker(collectionManager, dbHandler);
        channel = DatagramChannel.open();
        SocketAddress localAddress = new InetSocketAddress(1488);
        channel.bind(localAddress);
        startConsoleInput();
    }

    public void runServer() {
        logger.info("Server is running...");
        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(9196);
                SocketAddress remoteAddr = channel.receive(buffer);
                buffer.flip();
                if (buffer.remaining() >= 8) {
                    receivePool.execute(() -> processPacket(buffer, remoteAddr));
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error receiving task: " + e.getMessage());
        }
    }

    private void processPacket(ByteBuffer buffer, SocketAddress remoteAddr) {
        try {
            int packetNumber = buffer.getInt();
            int totalPackets = buffer.getInt();

            if (expectedTotalPackets == -1) {
                expectedTotalPackets = totalPackets;
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
                        if (task.getCollectionType() != null) {
                            collectionManager.setCollectionType(task.getCollectionType());
                        }
                        collectionManager.setCurrentUsername(task.getUsername());
                        logger.info("Executing: " + task.getDescribe()[0]);
                        Task responseTask = commandInvoker.executeCommand(task);
                        responsePool.execute(() -> {
                            try {
                                sendResponse(responseTask, remoteAddr);
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "Error sending response: " + e.getMessage());
                            }
                        });
                        resetState();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error processing task: " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing task: " + e.getMessage());
        }
    }

    private ByteBuffer assembleData() {
        ByteBuffer completeData = ByteBuffer.allocate(expectedTotalPackets * 9196);
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

    private void startConsoleInput() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Server> ");
                String input = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(input)) {
                    System.exit(0);
                } else if ("save".equalsIgnoreCase(input)) {
                    String[] userCommand = new String[] { "save", "" };
                    Task task = new Task();
                    task.setDescribe(userCommand);
                    task.setUsername("server");
                    task.setPassword("");

                    Task responseTask = commandInvoker.executeCommand(task);
                    if (responseTask.getDescribe() != null && responseTask.getDescribe().length > 0) {
                        System.out.println(responseTask.getDescribe()[0]);
                    }
                } else {
                    System.out.println("Unknown command. Only 'save' command is allowed.");
                }
            }
        }).start();
    }
}
