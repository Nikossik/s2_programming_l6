package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.manager.DumpManager;
import ru.itmo.lab5.util.Task;

/**
 * Команда для сохранения текущего состояния коллекции в файл.
 */
public class SaveCommand extends Command {
    private final DumpManager dumpManager;
    /**
     * Конструктор команды save.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     * @param dumpManager      Менеджер для работы с файлами, используемый для сохранения коллекции.
     */
    public SaveCommand(TicketCollection ticketCollection, DumpManager dumpManager) {
        super("save", "Сохраняет коллекцию в файл", ticketCollection);
        this.dumpManager = dumpManager;
    }

    @Override
    public Task execute(Task task) {
        dumpManager.writeCollection(ticketCollection);
        return new Task(new String[]{"Коллекция сохранена"});
    }
}
