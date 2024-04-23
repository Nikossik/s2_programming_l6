package ru.itmo.lab5.network;

import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;

public class Client {
    private final DatagramSocket socket= new DatagramSocket();
    private final InetAddress address= InetAddress.getByName("localhost");
    private final byte[] buffer = new byte[4096];
    private final DatagramPacket packet= new DatagramPacket(buffer, buffer.length);

    public Client() throws SocketException, UnknownHostException {
    }

    public void sendTask(Task task) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream= new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(task);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        DatagramPacket packet= new DatagramPacket(buffer, buffer.length, address, 8000);
        socket.send(packet);
        getAnswer();
    }

    public void getAnswer() throws IOException, ClassNotFoundException {
        socket.receive(packet);
        byte[] data = packet.getData();
        ObjectInputStream objectInputStream= new ObjectInputStream(new ByteArrayInputStream(data));
        Task answer= (Task) objectInputStream.readObject();
        System.out.println(answer.describe[0]);
    }
}
