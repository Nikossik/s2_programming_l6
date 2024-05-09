package ru.itmo.lab5.network;

import ru.itmo.lab5.util.Task;
import java.util.Arrays;
import java.io.*;
import java.net.*;

public class Client {
    private final int BUFFER_SIZE = 4096;
    DatagramSocket socket = new DatagramSocket();
    InetAddress address = InetAddress.getByName("localhost");

    public Client() throws SocketException, UnknownHostException {

    }

    public void sendTask(Task task) throws IOException, ClassNotFoundException {
        byte[] fullData = serializeTask(task);
        sendData(fullData);
        Task response = getAnswer();
        System.out.println("Полученный ответ: " + response.describe[0]);
    }

    private byte[] serializeTask(Task task) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(task);
        return byteArrayOutputStream.toByteArray();
    }


    private void sendData(byte[] data) throws IOException {
        int DATA_SIZE = BUFFER_SIZE - 1;
        int totalPackets = (int)Math.ceil(data.length / (double) DATA_SIZE);
        byte[][] packets = new byte[totalPackets][DATA_SIZE + 1]; // +1 для флага в конце

        int start = 0;
        for (int i = 0; i < totalPackets; i++) {
            int end = Math.min(start + DATA_SIZE, data.length);
            packets[i] = Arrays.copyOfRange(data, start, end);
            start += DATA_SIZE;

            byte flag = (byte) (i == totalPackets - 1 ? 1 : 0);
            packets[i] = Arrays.copyOf(packets[i], packets[i].length + 1);
            packets[i][packets[i].length - 1] = flag;


            int serverPort = 1488;
            DatagramPacket packet = new DatagramPacket(packets[i], packets[i].length, address, serverPort);
            socket.send(packet);
        }
    }

    public Task getAnswer() throws IOException, ClassNotFoundException {
        try {
            socket.setSoTimeout(10000);
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Task) ois.readObject();
        } catch (SocketTimeoutException e) {
            System.out.println("Сервер не доступен. Пожалуйста, попробуйте позже.");
        }
        return null;
    }
}
