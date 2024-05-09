package ru.itmo.lab5.network;

import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public Console console;
    private int port = 1488;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final DatagramSocket socket = new DatagramSocket(port);


    public Server(Console console, int port) throws SocketException, UnknownHostException {
        this.port = port;
        this.console = console;
    }

    public void runServer() {
        logger.log(Level.INFO, "Получение информации");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int BUFFER_SIZE = 4096;
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                processPacket(packet);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка при получении пакетов: " + e.getMessage());
            }
        }
    }

    private void processPacket(DatagramPacket packet) {
        logger.log(Level.INFO, "Выполнение команды");
        try {
            Task task = deserializeTask(packet.getData(), packet.getLength());
            logger.log(Level.INFO, "Задача получена: " + task.describe[0]);
            Task responseTask = console.start(task);
            sendTask(responseTask, packet.getSocketAddress());
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Ошибка при обработке задачи: " + e.getMessage());
        }
    }

    private Task deserializeTask(byte[] data, int length) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, length);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Task) ois.readObject();
    }

    private void sendTask(Task task, SocketAddress clientAddress) throws IOException {
        byte[] data = serializeTask(task);

        DatagramPacket sendPacket = new DatagramPacket(data, data.length, clientAddress);
        socket.send(sendPacket);
    }

    private byte[] serializeTask(Task task) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(task);
        return baos.toByteArray();
    }
}