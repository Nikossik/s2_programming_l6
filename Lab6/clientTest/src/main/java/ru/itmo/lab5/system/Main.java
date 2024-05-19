package ru.itmo.lab5.system;

import ru.itmo.lab5.manager.Console;
import ru.itmo.lab5.network.Client;
import sun.misc.Signal;

import java.io.IOException;

public class Main {
    private static void setSignalProcessing(String... signalNames) {
        for (String signalName : signalNames) {
            try {
                Signal.handle(new Signal(signalName), signal -> System.out.print("\nUse 'help' for listing all commands, for exit the program use -> 'exit'\n"));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Console console = new Console(new Client());

        console.start();
        setSignalProcessing(
                "INT", "TERM", "TSTP", "BREAK", "EOF");
    }
}
