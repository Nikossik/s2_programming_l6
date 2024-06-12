package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.CommandInvoker;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final DatagramChannel channel;
    private final CommandInvoker commandInvoker;
    private final ExecutorService requestPool = Executors.newCachedThreadPool();

    public Server(DatabaseHandler dbHandler) throws IOException {
        this.commandInvoker = new CommandInvoker(dbHandler);
        channel = DatagramChannel.open();
        SocketAddress localAddress = new InetSocketAddress(1488);
        channel.bind(localAddress);
    }

    public void runServer() {
        logger.info("Server is running...");
        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(65000);
                SocketAddress remoteAddr = channel.receive(buffer);
                buffer.flip();
                requestPool.execute(() -> processPacket(buffer, remoteAddr));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error receiving task: " + e.getMessage());
        }
    }

    private void processPacket(ByteBuffer buffer, SocketAddress remoteAddr) {
        try {
            Task task = deserializeTask(buffer);
            logger.info("Executing: " + task.getDescribe()[0]);
            Task responseTask = commandInvoker.executeCommand(task);
            sendResponse(responseTask, remoteAddr);
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
}
