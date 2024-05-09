package ru.itmo.lab5.manager;

import ru.itmo.lab5.network.Client;
import ru.itmo.lab5.util.Task;
import ru.itmo.lab5.util.TicketBuilder;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {

    /**
     * Сканер для чтения команд из стандартного ввода.
     */
    private final Scanner scanner;
    private final Client client;

    public Console(Client client) throws SocketException, UnknownHostException {
        this.scanner = new Scanner(System.in);
        this.client =  new Client();
    }

    /**
     * Запускает интерактивную сессию командной строки для управления коллекцией билетов.
     * Пользователь вводит команды до тех пор, пока не решит выйти из программы командой 'exit'.
     */
    public void start() {
        try {
            System.out.println("Добро пожаловать в консоль управления билетами. Введите 'help' для получения списка команд.");
            String[] userCommand;
            while (true) {
                Task task= new Task();
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine().trim();
                userCommand = (input + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                if ("exit".equalsIgnoreCase(userCommand[0])) {
                    System.exit(0);
                }
                if ("add".equalsIgnoreCase(userCommand[0]) || "add_if_min".equalsIgnoreCase(userCommand[0]) || "update_id".equalsIgnoreCase(userCommand[0])) {
                    task.ticket = TicketBuilder.buildTicket();
                }
                try {
                    if ("execute_script".equalsIgnoreCase(userCommand[0])) {

                    }
                    else{
                        task.describe= userCommand;
                        client.sendTask(task);
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Команда '" + input + "' не найдена. Введите 'help' для помощи");
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Ошибка ввода. Экстренное завершение программы.");
        }
    }


}
