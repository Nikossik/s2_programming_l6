package ru.itmo.lab5.manager;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import ru.itmo.lab5.data.TicketCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Класс управления дампом данных, предназначенный для сериализации и десериализации коллекции билетов.
 * Позволяет сохранять состояние коллекции билетов в файл в формате XML и загружать его обратно.
 */
public class DumpManager {
    private final String fileName;

    /**
     * Конструктор класса DumpManager.
     *
     * @param fileName Путь к файлу, который будет использоваться для сохранения и загрузки данных.
     */
    public DumpManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Сериализует коллекцию билетов в XML и сохраняет в файл.
     *
     * @param ticketCollection Коллекция билетов для сохранения.
     */
    public void writeCollection(TicketCollection ticketCollection) {
        try {
            JAXBContext context = JAXBContext.newInstance(TicketCollection.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
                marshaller.marshal(ticketCollection, writer);
                System.out.println("Коллекция успешно сохранена в файл!");
            }
        } catch (JAXBException | IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    /**
     * Загружает коллекцию билетов из файла XML и десериализует ее в объект.
     *
     * @return Возвращает десериализованную коллекцию билетов. В случае ошибки или отсутствия файла возвращает новую пустую коллекцию.
     */
    public TicketCollection readCollection() {
        TicketCollection ticketCollection = new TicketCollection();
        try {
            JAXBContext context = JAXBContext.newInstance(TicketCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("Файл не найден.");
                return ticketCollection;
            }

            try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
                if (scanner.hasNextLine()) {
                    String xmlContent = scanner.useDelimiter("\\A").next();
                    try (StringReader reader = new StringReader(xmlContent)) {
                        System.out.println("Коллекция успешно считана с файла!");
                        ticketCollection = (TicketCollection) unmarshaller.unmarshal(reader);
                    }
                }
            }
        } catch (JAXBException | IOException e) {
            System.err.println("Ошибка при чтении из файла: " + e.getMessage());
        }
        return ticketCollection;
    }
}
