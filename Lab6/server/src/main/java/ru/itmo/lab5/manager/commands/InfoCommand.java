package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.time.format.DateTimeFormatter;

/**
 * Команда для вывода информации о коллекции билетов в базе данных.
 */
public class InfoCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды info.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public InfoCommand(DatabaseHandler dbHandler) {
        super("info", "Выводит информацию о коллекции", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String answer = "";
        answer = answer + "информация о коллекции:\n";

        try {
            int ticketCount = this.dbHandler.getTicketCount();
            answer = answer + "Количество элементов: " + ticketCount + "\n";

            String initializationDate = this.dbHandler.getInitializationDate().format(formatter);
            answer = answer + "Дата инициализации коллекции: " + initializationDate + "\n";

            String lastSaveTime = this.dbHandler.getLastSaveTime() != null
                    ? this.dbHandler.getLastSaveTime().format(formatter)
                    : "Коллекция ещё не сохранялась.";
            answer = answer + "Дата последнего сохранения: " + lastSaveTime + "\n";

        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при получении информации о коллекции: " + e.getMessage()});
        }

        return new Task(new String[]{answer});
    }
}
