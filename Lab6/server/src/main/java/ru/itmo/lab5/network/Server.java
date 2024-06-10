package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.CommandInvoker;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final DatagramChannel channel;
    private final CollectionManager collectionManager;
    private final CommandInvoker commandInvoker;
    private final ExecutorService responsePool = Executors.newCachedThreadPool();

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
                ByteBuffer buffer = ByteBuffer.allocate(65000);
                SocketAddress remoteAddr = channel.receive(buffer);
                buffer.flip();
                processPacket(buffer, remoteAddr);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error receiving task: " + e.getMessage());
        }
    }

    private void processPacket(ByteBuffer buffer, SocketAddress remoteAddr) {
        try {
            Task task = deserializeTask(buffer);
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing task: " + e.getMessage());
        }
    }

    private Task deserializeTask(ByteBuffer data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data.array(), data.position(), data.remaining());
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

    private void startConsoleInput() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Server> ");
                String input = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(input)) {
                    System.exit(0);
                } else if ("save".equalsIgnoreCase(input)) {
                    String[] userCommand = new String[]{"save", ""};
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
        });
    }
}
