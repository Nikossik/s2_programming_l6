package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода справки по доступным командам.
 */
public class HelpCommand extends Command {
    /**
     * Конструктор команды help.
     *
     * @param dbHandler Обработчик базы данных.
     */
    public HelpCommand(DatabaseHandler dbHandler) {
        super("help", "Выводит справку по доступным командам", dbHandler);
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        String answer = """
                Доступные команды:
                add - Добавляет новый билет в коллекцию
                update_id - Обновляет значение элемента коллекции, ID которого равен заданному
                remove_at - Удаляет элемент из коллекции по его индексу
                add_if_min - Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                execute_script - Выполняет скрипт из указанного файла
                remove_first - Удаляет первый элемент из коллекции
                show - Выводит все элементы коллекции
                clear - Очищает коллекцию
                save - Сохраняет коллекцию в файл
                remove_by_id - Удаляет элемент из коллекции по его ID
                print_field_descending_venue - Выводит значения поля venue всех элементов в порядке убывания
                help - Выводит справку по доступным командам
                exit - Завершает программу
                print_descending - Выводит элементы коллекции в порядке убывания
                filter_less_than_venue - Выводит элементы, значение поля venue которых меньше заданного
                info - Выводит информацию о коллекции""";
        return new Task(new String[]{answer});
    }
}
