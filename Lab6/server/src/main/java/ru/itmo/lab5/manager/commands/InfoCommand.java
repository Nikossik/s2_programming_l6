package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

import java.time.format.DateTimeFormatter;
/**
 * Команда для вывода информации о коллекции билетов.
 */
public class InfoCommand extends Command {
    /**
     * Конструктор команды info.
     *
     * @param ticketCollection Коллекция билетов, о которой выводится информация.
     */
    public InfoCommand(TicketCollection ticketCollection) {
        super("info", "Выводит информацию о коллекции", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String answer ="";
        answer = answer +"информация о коллекции:\n";
        answer = answer +"Тип коллекции: " + ticketCollection.getTickets().getClass().getName()+"\n";
        answer = answer +"Количество элементов: " + ticketCollection.getTickets().size()+"\n";
        if (ticketCollection.getInitializationDate() != null) {
            answer = answer +"Дата инициализации коллекции: " + ticketCollection.getInitializationDate().format(formatter)+"\n";
        } else {
            answer = answer +"Дата инициализации неизвестна.\n";
        }
        if (ticketCollection.getLastSaveTime() != null) {
            answer = answer +"Дата последнего сохранения: " + ticketCollection.getLastSaveTime().format(formatter)+"\n";
        } else {
            answer = answer +"Коллекция ещё не сохранялась.\n";
        }
        return new Task(new String[]{answer});
    }
}
