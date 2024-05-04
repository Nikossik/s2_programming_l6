package ru.itmo.lab5.network;

import ru.itmo.lab5.util.Task;

import java.io.*;
import java.net.*;

public class Client {
    private final DatagramSocket socket= new DatagramSocket();
    private final InetAddress address= InetAddress.getByName("localhost");
    private final byte[] buffer = new byte[4096];
    private static final int BUFFER_SIZE = 4096;
    private final DatagramPacket packet= new DatagramPacket(buffer, buffer.length);

    public Client() throws SocketException, UnknownHostException {
    }

    public void sendTask(Task task) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(task);
        objectOutputStream.flush();
        byte[] fullData = byteArrayOutputStream.toByteArray();
        int totalPackets = (int) Math.ceil(fullData.length / (double) BUFFER_SIZE);

        for (int i = 0; i < totalPackets; i++) {
            int start = i * BUFFER_SIZE;
            int length = Math.min(BUFFER_SIZE, fullData.length - start);
            byte[] buffer = new byte[length];
            System.arraycopy(fullData, start, buffer, 0, length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(i);
            dos.writeInt(totalPackets);
            dos.write(buffer);

            byte[] packetData = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(packetData, packetData.length, address, 8000);
            socket.send(packet);
        }
        getAnswer();
    }

    public void getAnswer() throws IOException, ClassNotFoundException, SocketTimeoutException {
        try {
            socket.setSoTimeout(5000);
            socket.receive(packet);
            byte[] data = packet.getData();
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            Task answer = (Task) objectInputStream.readObject();
            System.out.println(answer.describe[0]);
        } catch (SocketTimeoutException e) {
            System.out.println("Сервер не доступен. Пожалуйста, попробуйте позже.");
        } catch (IOException e) {
            System.out.println("Произошла ошибка ввода/вывода: " + e.getMessage());
        }
    }   }