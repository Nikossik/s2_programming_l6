package ru.itmo.lab5.network;

import ru.itmo.lab5.util.Task;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private final int BUFFER_SIZE = 4096;
    private final SocketAddress serverAddress = new InetSocketAddress("localhost", 1488);
    private final DatagramChannel channel;

    public Client() throws IOException {
        channel = DatagramChannel.open();
    }

    public boolean sendTask(Task task) throws IOException, ClassNotFoundException {
        System.out.println(task.getTicket());
        byte[] fullData = serializeTask(task);
        sendData(fullData);
        return getResponse();
    }

    private byte[] serializeTask(Task task) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(task);
        return byteArrayOutputStream.toByteArray();
    }

    private void sendData(byte[] data) throws IOException {
        int DATA_SIZE = BUFFER_SIZE - 8;
        int totalPackets = (int) Math.ceil((double) data.length / DATA_SIZE);

        for (int i = 0; i < totalPackets; i++) {
            int start = i * DATA_SIZE;
            int end = Math.min(start + DATA_SIZE, data.length);
            byte[] tempBuffer = Arrays.copyOfRange(data, start, end);

            ByteBuffer buffer = ByteBuffer.allocate(tempBuffer.length + 8);
            buffer.putInt(i);
            buffer.putInt(totalPackets);
            buffer.put(tempBuffer);
            buffer.flip();

            channel.send(buffer, serverAddress);
        }
    }

    private boolean getResponse() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        channel.receive(buffer);
        buffer.flip();

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), buffer.position(), buffer.limit());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Task responseTask = (Task) ois.readObject();
        System.out.println(responseTask.getDescribe()[0]);
        return responseTask.getDescribe()[0].equals("Login successful.") || responseTask.getDescribe()[0].equals("Registration successful.");
    }
}
