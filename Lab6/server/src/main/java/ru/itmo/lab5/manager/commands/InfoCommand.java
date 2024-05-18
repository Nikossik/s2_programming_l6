package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

import java.time.format.DateTimeFormatter;

/**
 * Команда для вывода информации о коллекции билетов.
 */
public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды info.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public InfoCommand(CollectionManager collectionManager) {
        super("info", "Выводит информацию о коллекции", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String answer = "";
        answer = answer + "информация о коллекции:\n";

        int ticketCount = collectionManager.getTickets().size();
        answer = answer + "Количество элементов: " + ticketCount + "\n";

        String initializationDate = collectionManager.getInitializationDate().format(formatter);
        answer = answer + "Дата инициализации коллекции: " + initializationDate + "\n";

        String lastSaveTime = collectionManager.getLastSaveTime() != null
                ? collectionManager.getLastSaveTime().format(formatter)
                : "Коллекция ещё не сохранялась.";
        answer = answer + "Дата последнего сохранения: " + lastSaveTime + "\n";

        return new Task(new String[]{answer});
    }
}
