package ru.itmo.lab5.network;

import ru.itmo.lab5.util.Task;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;

public class Client {
    private final int BUFFER_SIZE = 1700;
    private final SocketAddress serverAddress = new InetSocketAddress("localhost", 1488);
    private final DatagramChannel channel;


    public Client() throws IOException {
        channel = DatagramChannel.open();
    }

    public void sendTask(Task task) throws IOException, ClassNotFoundException {
        byte[] fullData = serializeTask(task);
        sendData(fullData);
        getAnswer();
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

    public void getAnswer() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        channel.receive(buffer);
        buffer.flip();

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), buffer.position(), buffer.limit());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Task answer = (Task) ois.readObject();
        System.out.println(answer.describe[0]);
    }
}
