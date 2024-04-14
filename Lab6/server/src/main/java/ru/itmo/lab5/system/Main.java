package ru.itmo.lab5.system;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.manager.CommandInvoker;
import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.manager.DumpManager;
import ru.itmo.lab5.network.Server;
import sun.misc.Signal;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

public class Main {
    private static void setSignalProcessing(String... signalNames) {
        for (String signalName : signalNames) {
            try {
                Signal.handle(new Signal(signalName), signal -> System.out.print("\nДля получения справки введите 'help', для завершения программы введите 'exit'\n"));
            } catch (IllegalArgumentException ignored) {
                // гнорируем исключение, если сигнал с таким названием уже существует или такого сигнала не существует
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "data/file.xml";
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("Переменная среды 'TICKETS_FILE_PATH' не задана или пуста.");
            return; // Завершаем работу приложения, если путь не задан
        }

        new Thread(() ->{
            try {
                DumpManager dumpManager = new DumpManager(filePath);

                TicketCollection ticketCollection = new TicketCollection(dumpManager);

                CommandInvoker commandInvoker = new CommandInvoker(ticketCollection, dumpManager);
                Server server= new Server(new Console(commandInvoker));
                server.runServer();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
        setSignalProcessing(
                "INT", "TERM", "TSTP", "BREAK", "EOF");
    }
}
