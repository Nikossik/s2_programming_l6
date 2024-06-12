package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class HelpCommand extends Command {

    public HelpCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "help";
    }

    @Override
    public Task execute(Task task) {
        String answer = """
                Available commands:
                                add - Adds a new ticket to the collection
                                update_id - Updates the value of a collection item whose ID is equal to the specified one
                                remove_at - Removes an item from the collection by its index
                                add_if_min - Adds a new element to the collection if its value is less than that of the smallest element in this collection
                                execute_script - Executes the script from the specified file
                                remove_first - Removes the first item from the collection
                                show - Displays all the elements of the collection
                                clear - Clears the collection
                                remove_by_id - Removes an item from the collection by its ID
                                print_field_descending_venue - Outputs the values of the venue field of all elements in descending order
                                help - Displays help for available commands
                                exit - Ends the program
                                print_descending - Displays the elements of the collection in descending order
                                filter_less_than_venue - Outputs elements whose venue field value is less than the specified one
                                info - Displays information about the collection""";
        return new Task(new String[]{answer});
    }
}
