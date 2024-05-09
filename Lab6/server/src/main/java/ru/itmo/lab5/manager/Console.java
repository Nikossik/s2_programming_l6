package ru.itmo.lab5.manager;

import ru.itmo.lab5.util.Task;

import java.util.NoSuchElementException;

public class Console {

    /**
     * нвокер команд для выполнения операций над коллекцией билетов.
     */
    private final CommandInvoker commandInvoker;

    /**
     * Конструктор, инициализирующий Console с заданным инвокером команд.
     *
     * @param commandInvoker нвокер команд, который будет использоваться для обработки вводимых команд.
     */
    public Console(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    /**
     * Запускает интерактивную сессию командной строки для управления коллекцией билетов.
     * Пользователь вводит команды до тех пор, пока не решит выйти из программы командой 'exit'.
     */
    public Task start(Task task) {
        try {
            return commandInvoker.executeCommand(task);
        } catch (NoSuchElementException | IllegalStateException e) {
            return new Task(new String[]{"Ошибка ввода. Экстренное завершение программы."});
        }
    }

}
