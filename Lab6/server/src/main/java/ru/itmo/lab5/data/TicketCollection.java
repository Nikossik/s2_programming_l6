package ru.itmo.lab5.data;

import jakarta.xml.bind.annotation.*;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.data.models.Venue;
import ru.itmo.lab5.exceptions.DuplicateException;
import ru.itmo.lab5.manager.DumpManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@XmlRootElement(name = "ticketCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketCollection {
    @XmlElement(name = "ticket")
    private final ArrayList<Ticket> tickets;
    private final LocalDateTime initializationDate;
    private LocalDateTime lastSaveTime;
    @XmlTransient
    private DumpManager dumpManager;

    @XmlTransient
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @XmlTransient
    private final Lock readLock = lock.readLock();
    @XmlTransient
    private final Lock writeLock = lock.writeLock();

    public TicketCollection() {
        this.tickets = new ArrayList<>();
        this.initializationDate = LocalDateTime.now();
        lastSaveTime = LocalDateTime.now();
    }

    public TicketCollection(DumpManager dumpManager) {
        this();
        this.dumpManager = dumpManager;
        loadCollection();
        updateNextId();
    }

    public TicketCollection(String filePath) {
        this(new DumpManager(filePath));
    }

    public void loadCollection() {
        Collection<Ticket> loadedTickets = dumpManager.readCollection().getTickets();
        writeLock.lock();
        try {
            for (Ticket ticket : loadedTickets) {
                if (ticket != null) {
                    int id = ticket.getId();
                    if (this.contains(id)) {
                        throw new DuplicateException("Duplicate Ticket ID: " + id);
                    } else {
                        tickets.add(ticket);
                    }
                }
            }
        } catch (DuplicateException e) {
            System.err.println("Ошибка загрузки коллекции: " + e.getMessage());
        } finally {
            writeLock.unlock();
        }
    }

    public boolean contains(int id) {
        readLock.lock();
        try {
            return tickets.stream().anyMatch(ticket -> ticket.getId() == id);
        } finally {
            readLock.unlock();
        }
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public ArrayList<Ticket> getTickets() {
        readLock.lock();
        try {
            return new ArrayList<>(tickets);
        } finally {
            readLock.unlock();
        }
    }

    public boolean add(Ticket ticket) {
        writeLock.lock();
        try {
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
            return tickets.removeIf(ticket -> ticket.getId() == id);
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
            if (index >= 0 && index < tickets.size()) {
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
}