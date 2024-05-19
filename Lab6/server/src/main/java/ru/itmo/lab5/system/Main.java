package ru.itmo.lab5.system;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.network.Server;
import sun.misc.Signal;

import java.io.IOException;

public class Main {
    private static void setSignalProcessing(String... signalNames) {
        for (String signalName : signalNames) {
            try {
                Signal.handle(new Signal(signalName), signal -> System.out.print("\nДля получения справки введите 'help', для завершения программы введите 'exit'\n"));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();

        new Thread(() -> {
            try {
                Server server = new Server(dbHandler);
                server.runServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        setSignalProcessing("INT", "TERM", "TSTP", "BREAK", "EOF");
    }
}
