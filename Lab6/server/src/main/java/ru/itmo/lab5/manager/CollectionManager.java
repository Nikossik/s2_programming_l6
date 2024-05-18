package ru.itmo.lab5.manager;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.data.models.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
@Setter
@Getter
public class CollectionManager {
    private static CollectionManager instance;
    private final TicketCollection ticketCollection;
    private final DatabaseHandler dbHandler;

    private String currentUsername;

    public CollectionManager(DatabaseHandler dbHandler) {
        this.ticketCollection = new TicketCollection();
        this.dbHandler = dbHandler;
        clear();
        loadCollection();
    }

    public static synchronized CollectionManager getInstance(DatabaseHandler dbHandler) {
        if (instance == null) {
            instance = new CollectionManager(dbHandler);
        }
        return instance;
    }

    public ArrayList<Ticket> getTickets() {
        return ticketCollection.getTickets();
    }

    public boolean add(Ticket ticket) {
        ticket.setUsername(currentUsername);
        return ticketCollection.add(ticket);
    }

    public void update(Ticket newTicket) {
        ticketCollection.update(newTicket);
    }

    public boolean remove(long id) {
        return ticketCollection.remove(id);
    }

    public boolean removeAt(int index) {
        return ticketCollection.removeAt(index);
    }

    public LocalDateTime getInitializationDate() {
        return ticketCollection.getInitializationDate();
    }

    public LocalDateTime getLastSaveTime() {
        return ticketCollection.getLastSaveTime();
    }

    public boolean saveToDatabase() {
        return dbHandler.saveCollectionToDatabase(this, currentUsername);
    }

    public void loadCollection() {
        dbHandler.loadAllTicketsToMemory(this);
    }

    public void clear() {
        ticketCollection.getTickets().clear();
    }

    public Ticket getTicketById(long id) {
        return ticketCollection.getTickets().stream()
                .filter(ticket -> ticket.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean removeFirst() {
        return ticketCollection.removeFirst();
    }

    @Override
    public String toString() {
        return ticketCollection.toString();
    }
}
