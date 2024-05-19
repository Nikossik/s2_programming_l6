package ru.itmo.lab5.manager;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.data.models.Venue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

@Getter
@Setter
public class CollectionManager {
    private static CollectionManager instance;
    private final ArrayList<Ticket> tickets;
    private final LocalDateTime initializationDate;
    private LocalDateTime lastSaveTime;
    private final DatabaseHandler dbHandler;
    private String currentUsername;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private static final Logger logger = Logger.getLogger("logger");

    private CollectionManager(DatabaseHandler dbHandler) {
        this.tickets = new ArrayList<>();
        this.initializationDate = LocalDateTime.now();
        this.lastSaveTime = LocalDateTime.now();
        this.dbHandler = dbHandler;

        clearAll();
        loadCollection();
        updateNextId();
    }

    public static synchronized CollectionManager getInstance(DatabaseHandler dbHandler) {
        if (instance == null) {
            instance = new CollectionManager(dbHandler);
        }
        return instance;
    }

    public ArrayList<Ticket> getTickets() {
        readLock.lock();
        try {
            return new ArrayList<>(tickets);
        } finally {
            readLock.unlock();
        }
    }

    public void clearAll() {
        writeLock.lock();
        try {
            tickets.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public void addFromDB(Ticket ticket) {
        writeLock.lock();
        try {
            logger.info("Added ticket: " + ticket + ", username: " + ticket.getUsername());

            tickets.add(ticket);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean add(Ticket ticket) {
        writeLock.lock();
        try {
            ticket.setUsername(currentUsername);

            return tickets.add(ticket);
        } finally {
            writeLock.unlock();
        }
    }

    public void update(Ticket newTicket) {
        writeLock.lock();
        try {
            for (int i = 0; i < tickets.size(); i++) {
                if (tickets.get(i).getId() == newTicket.getId()) {
                    tickets.set(i, newTicket);
                    return;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public boolean remove(long id) {
        writeLock.lock();
        try {
            return tickets.removeIf(ticket -> ticket.getId() == id && ticket.getUsername().equals(currentUsername));
        } finally {
            writeLock.unlock();
        }
    }

    public boolean removeFirst() {
        writeLock.lock();
        try {
            if (!tickets.isEmpty() && tickets.get(0).getUsername().equals(currentUsername)) {
                tickets.remove(0);
                return true;
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            StringBuilder sb = new StringBuilder("TicketCollection{");
            for (Ticket ticket : tickets) {
                sb.append("\n").append(ticket.toString());
            }
            sb.append("\n}");
            return sb.toString();
        } finally {
            readLock.unlock();
        }
    }

    public boolean removeAt(int index) {
        writeLock.lock();
        try {
            if (index >= 0 && index < tickets.size() && tickets.get(index).getUsername().equals(currentUsername)) {
                tickets.remove(index);
                return true;
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    public void updateNextId() {
        writeLock.lock();
        try {
            int maxTicketId = tickets.stream()
                    .mapToInt(Ticket::getId)
                    .max()
                    .orElse(0);

            long maxVenueId = tickets.stream()
                    .filter(ticket -> ticket.getVenue() != null)
                    .mapToLong(ticket -> ticket.getVenue().getId())
                    .max()
                    .orElse(0L);

            long nextId = Math.max(maxTicketId, maxVenueId) + 1;

            Ticket.updateNextId((int) nextId);
            Venue.updateNextId(nextId);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean saveToDatabase() {
        writeLock.lock();
        try {
            return dbHandler.saveCollectionToDatabase(this);
        } finally {
            writeLock.unlock();
        }
    }

    public void clearAllInDatabase() {
        writeLock.lock();
        try {
            dbHandler.clearTickets();
        } finally {
            writeLock.unlock();
        }
    }

    public void loadCollection() {
        writeLock.lock();
        try {
            dbHandler.loadAllTicketsToMemory(this);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear(String currentUsername) {
        writeLock.lock();
        try {
            tickets.removeIf(ticket -> ticket.getUsername().equals(currentUsername));
        } finally {
            writeLock.unlock();
        }
    }

    public Ticket getTicketById(long id) {
        readLock.lock();
        try {
            return tickets.stream()
                    .filter(ticket -> ticket.getId() == id)
                    .findFirst()
                    .orElse(null);
        } finally {
            readLock.unlock();
        }
    }


    public LocalDateTime getLastSaveTime() {
        readLock.lock();
        try {
            return lastSaveTime;
        } finally {
            readLock.unlock();
        }
    }

    public void setLastSaveTime(LocalDateTime lastSaveTime) {
        writeLock.lock();
        try {
            this.lastSaveTime = lastSaveTime;
        } finally {
            writeLock.unlock();
        }
    }
}
