package ru.itmo.lab5.manager;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.data.models.Venue;
import ru.itmo.lab5.exceptions.CollectionOperationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

@Getter
@Setter
public class CollectionManager {
    private static CollectionManager instance;
    private Collection<Ticket> collection;
    private final LocalDateTime initializationDate;
    private LocalDateTime lastSaveTime;
    private final DatabaseHandler dbHandler;
    private String currentUsername;

    private boolean collectionTypeSet = false;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private static final Logger logger = Logger.getLogger("logger");

    private CollectionManager(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.initializationDate = LocalDateTime.now();
        this.lastSaveTime = LocalDateTime.now();
    }

    public static synchronized CollectionManager getInstance(DatabaseHandler dbHandler) {
        if (instance == null) {
            instance = new CollectionManager(dbHandler);
        }
        return instance;
    }

    public void setCollectionType(String type) {
        writeLock.lock();
        try {
            if (collectionTypeSet) {
                return;
            }
            switch (type) {
                case "1":
                    this.collection = new ArrayList<>();
                    break;
                case "2":
                    this.collection = new HashSet<>(new HashMap<Integer, Ticket>().values());
                    break;
                case "3":
                    this.collection = new ArrayDeque<>();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown collection type: " + type);
            }
            loadCollection();
            updateNextId();
            collectionTypeSet = true;
        } finally {
            writeLock.unlock();
        }
    }

    public void updateNextId() {
        writeLock.lock();
        try {
            int maxTicketId = collection.stream()
                    .mapToInt(Ticket::getId)
                    .max()
                    .orElse(0);

            long maxVenueId = collection.stream()
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

    public boolean removeAt(int index) {
        writeLock.lock();
        try {
            if (collection instanceof List<Ticket> list) {
                if (index >= 0 && index < list.size() && list.get(index).getUsername().equals(currentUsername)) {
                    list.remove(index);
                    updateNextId();
                    return true;
                }
            } else if (collection instanceof Set) {
                throw new UnsupportedOperationException("Remove at index is not supported for Set.");
            } else if (collection instanceof Queue) {
                throw new UnsupportedOperationException("Remove at index is not supported for Queue.");
            } else {
                throw new IllegalStateException("Unknown collection type.");
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean removeFirst() {
        writeLock.lock();
        try {
            if (collection instanceof Queue<Ticket> queue) {
                Ticket firstTicket = queue.peek();
                if (firstTicket != null && firstTicket.getUsername().equals(currentUsername)) {
                    queue.poll();
                    updateNextId();
                    return true;
                } else {
                    throw new CollectionOperationException("First item not found or unauthorized access.");
                }
            } else if (collection instanceof List<Ticket> list) {
                if (!list.isEmpty() && list.get(0).getUsername().equals(currentUsername)) {
                    list.remove(0);
                    updateNextId();
                    return true;
                } else {
                    throw new CollectionOperationException("First item not found or unauthorized access.");
                }
            } else if (collection instanceof Set) {
                throw new CollectionOperationException("Remove first is not supported for Set.");
            } else {
                throw new CollectionOperationException("Unknown collection type.");
            }
        } finally {
            writeLock.unlock();
        }
    }


    public void update(Ticket newTicket) {
        writeLock.lock();
        try {
            if (collection instanceof List<Ticket> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == newTicket.getId()) {
                        list.set(i, newTicket);
                        return;
                    }
                }
            } else if (collection instanceof Set) {
                collection.remove(newTicket);
                collection.add(newTicket);
            } else if (collection instanceof Queue<Ticket> queue) {
                queue.removeIf(ticket -> ticket.getId() == newTicket.getId());
                queue.add(newTicket);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public Collection<Ticket> getTickets() {
        readLock.lock();
        try {
            return new ArrayList<>(collection);
        } finally {
            readLock.unlock();
        }
    }

    public void clearAll() {
        writeLock.lock();
        try {
            collection.clear();
            updateNextId();
        } finally {
            writeLock.unlock();
        }
    }

    public void addFromDB(Ticket ticket) {
        writeLock.lock();
        try {
            collection.add(ticket);
            logger.info("Added ticket: " + ticket + ", username: " + ticket.getUsername());
            updateNextId();
        } finally {
            writeLock.unlock();
        }
    }

    public boolean add(Ticket ticket) {
        writeLock.lock();
        try {
            updateNextId();
            ticket.setId(Ticket.getNextId());
            ticket.setUsername(currentUsername);
            return collection.add(ticket);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean remove(long id) {
        writeLock.lock();
        try {
            return collection.removeIf(ticket -> ticket.getId() == id && ticket.getUsername().equals(currentUsername));
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
            clearAll();
            dbHandler.loadAllTicketsToMemory(this);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear(String currentUsername) {
        writeLock.lock();
        try {
            collection.removeIf(ticket -> ticket.getUsername().equals(currentUsername));
            updateNextId();
        } finally {
            writeLock.unlock();
        }
    }

    public Ticket getTicketById(long id) {
        readLock.lock();
        try {
            return collection.stream()
                    .filter(ticket -> ticket.getId() == id)
                    .findFirst()
                    .orElse(null);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            StringBuilder sb = new StringBuilder("TicketCollection{");
            for (Ticket ticket : collection) {
                sb.append("\n").append(ticket.toString());
            }
            sb.append("\n}");
            return sb.toString();
        } finally {
            readLock.unlock();
        }
    }
}