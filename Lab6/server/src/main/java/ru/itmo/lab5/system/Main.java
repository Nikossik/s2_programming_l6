package ru.itmo.lab5.system;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.manager.CommandInvoker;
import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.network.Server;
import sun.misc.Signal;

import java.io.IOException;

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
        new Thread(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                CommandInvoker commandInvoker = new CommandInvoker(dbHandler);
                Server server = new Server(new Console(commandInvoker));
                server.runServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        setSignalProcessing("INT", "TERM", "TSTP", "BREAK", "EOF");
    }
}
