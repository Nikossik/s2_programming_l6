package ru.itmo.lab5.data;

import jakarta.xml.bind.annotation.*;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.data.models.Venue;
import ru.itmo.lab5.exceptions.DuplicateException;
import ru.itmo.lab5.manager.DumpManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;




@XmlRootElement(name = "ticketCollection")
@XmlAccessorType(XmlAccessType.FIELD)


public class TicketCollection {
    @XmlElement(name = "ticket")
    private final ArrayList<Ticket> tickets;
    @XmlTransient
    private final LocalDateTime initializationDate;
    @XmlTransient
    private LocalDateTime lastSaveTime;
    @XmlTransient
    private DumpManager dumpManager;

    public TicketCollection() {
        this.tickets = new ArrayList<>();
        this.initializationDate = LocalDateTime.now(); // Устанавливаем текущую дату инициализации
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Конструктор, создающий коллекцию билетов и загружающий её содержимое из файла, используя указанный DumpManager.
     * После загрузки коллекции обновляет следующие доступные ID для билетов и мест проведения.
     *
     * @param dumpManager Менеджер для работы с файлом данных.
     */
    public TicketCollection(DumpManager dumpManager){
        this();
        this.dumpManager = dumpManager;
        loadCollection();
        updateNextId();
    }
    public TicketCollection(String filePath) {
        this(new DumpManager(filePath));
    }

    /**
     * Загружает коллекцию билетов из файла. Проверяет наличие дубликатов среди ID билетов.
     * В случае обнаружения дубликатов выбрасывает исключение.
     */
    public void loadCollection() {
        Collection<Ticket> loadedTickets = dumpManager.readCollection().getTickets();
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
        }
    }

    /**
     * Проверяет наличие билета с указанным ID в коллекции.
     *
     * @param id идентификатор билета для поиска.
     * @return true, если билет с таким ID присутствует в коллекции, иначе false.
     */
    public boolean contains(int id) {
        return tickets.stream().anyMatch(ticket -> ticket.getId() == id);
    }

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return Дата инициализации.
     */
    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    /**
     * Возвращает время последнего сохранения коллекции.
     *
     * @return Время последнего сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Возвращает коллекцию билетов.
     *
     * @return Коллекция билетов.
     */
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Добавляет билет в коллекцию.
     *
     * @param ticket Билет для добавления.
     * @return true, если билет был успешно добавлен, иначе false.
     */
    public boolean add(Ticket ticket) {
        return tickets.add(ticket);
    }


    /**
    * Обновляет билет в коллекции, заменяя его на предоставленный билет с тем же ID.
    *
    * @param newTicket Билет с обновленной информацией.
     */
    public void update(Ticket newTicket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId() == newTicket.getId()) {
                tickets.set(i, newTicket);
                return;
            }
        }
    }

    /**
     * Удаляет билет с указанным ID из коллекции.
     *
     * @param id идентификатор билета для удаления.
     * @return true, если билет был найден и удален, иначе false.
     */
    public boolean remove(long id) {
        return tickets.removeIf(ticket -> ticket.getId() == id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TicketCollection{");
        for (Ticket ticket : tickets) {
            sb.append("\n").append(ticket.toString());
        }
        sb.append("\n}");
        return sb.toString();
    }

    /**
     * Удаляет билет из коллекции, расположенный на указанной позиции.
     *
     * @param index индекс билета для удаления.
     * @return true, если билет на заданной позиции был успешно удален, иначе false.
     */
    public boolean removeAt(int index) {
        if (index >= 0 && index < tickets.size()) {
            tickets.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Обновляет идентификаторы nextId для классов Ticket и Venue на основе максимального существующего ID в коллекции.
     */
    public void updateNextId() {
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
    }
}