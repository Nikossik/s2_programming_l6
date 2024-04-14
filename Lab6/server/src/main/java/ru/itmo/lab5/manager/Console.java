package ru.itmo.lab5.manager;

import ru.itmo.lab5.util.Task;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {

    /**
     * нвокер команд для выполнения операций над коллекцией билетов.
     */
    private final CommandInvoker commandInvoker;

    /**
     * Сканер для чтения команд из стандартного ввода.
     */
    private final Scanner scanner;

    /**
     * Конструктор, инициализирующий Console с заданным инвокером команд.
     *
     * @param commandInvoker нвокер команд, который будет использоваться для обработки вводимых команд.
     */
    public Console(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерактивную сессию командной строки для управления коллекцией билетов.
     * Пользователь вводит команды до тех пор, пока не решит выйти из программы командой 'exit'.
     *
     * @return
     */
    public Task start(Task task) {
        try {
            return commandInvoker.executeCommand(task);
        } catch (NoSuchElementException | IllegalStateException e) {
            return new Task(new String[]{"Ошибка ввода. Экстренное завершение программы."});
        }
    }

    /**
     * Обрабатывает выход из программы с выполнением команды 'exit'.
     */

    /**
     * Выполняет безопасный выход из программы, предварительно сохраняя данные с помощью команды 'save'.
     */

}
