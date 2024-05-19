package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.time.format.DateTimeFormatter;

/**
 * Команда для вывода информации о коллекции билетов.
 */
public class InfoCommand extends Command {

    public InfoCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "info";
        this.description = "Выводит информацию о коллекции";
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
