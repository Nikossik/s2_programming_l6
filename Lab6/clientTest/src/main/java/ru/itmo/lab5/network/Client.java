package ru.itmo.lab5.network;

import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class Client {
    private final int BUFFER_SIZE = 60000;
    private final SocketAddress serverAddress = new InetSocketAddress("localhost", 1488);
    private final DatagramChannel channel;

    public Client() throws IOException {
        channel = DatagramChannel.open();
    }

    public Task sendTask(Task task) throws IOException, ClassNotFoundException {
        byte[] fullData = serializeTask(task);
        sendData(fullData);
        Task response = getResponse();
        return response;
    }

    private byte[] serializeTask(Task task) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(task);
        return byteArrayOutputStream.toByteArray();
    }

    private void sendData(byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.send(buffer, serverAddress);
    }

    private Task getResponse() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        channel.receive(buffer);
        buffer.flip();

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), buffer.position(), buffer.remaining());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Task task = (Task) ois.readObject();
        return task;
    }
}
