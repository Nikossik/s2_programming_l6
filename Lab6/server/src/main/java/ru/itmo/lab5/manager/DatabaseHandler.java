package ru.itmo.lab5.manager;

import ru.itmo.lab5.data.models.*;
import ru.itmo.lab5.exceptions.UserException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/proga";
    private static final String username = "postgres";
    private static final String password = "admin";
    private static final Logger logger = Logger.getLogger("logger");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not connect to database: " + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public List<Ticket> getTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ticket ticket = resultSetToTicket(rs);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving tickets from database: " + e.getMessage());
        }
        return tickets;
    }

    private Ticket resultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setName(rs.getString("ticket_name"));
        ticket.setCoordinates(new Coordinates(rs.getInt("coordinates_x"), rs.getDouble("coordinates_y")));
        ticket.setCreationDate(rs.getTimestamp("creation_date"));
        ticket.setPrice(rs.getLong("price"));
        ticket.setType(TicketType.valueOf(rs.getString("ticket_type")));

        Venue venue = new Venue();
        venue.setId(rs.getLong("venue_id"));
        venue.setName(rs.getString("venue_name"));
        venue.setCapacity(rs.getInt("venue_capacity"));
        venue.setType(VenueType.valueOf(rs.getString("venue_type")));

        Address address = new Address();
        address.setStreet(rs.getString("address_street"));
        address.setZipCode(rs.getString("address_zipcode"));
        address.setTown(new Location(rs.getInt("address_town_x"), rs.getDouble("address_town_y"), rs.getString("address_town_name")));
        venue.setAddress(address);

        ticket.setVenue(venue);
        ticket.setUsername(rs.getString("username"));

        return ticket;
    }

    public static boolean checkUserPresence(String username) {
        String selectUserQuery = "SELECT * FROM users WHERE username = ? LIMIT 1";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectUserQuery)) {
            stmt.setString(1, username);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    public static void createUser(String username, String password) throws UserException {
        if (checkUserPresence(username)) {
            throw new UserException("User already exists");
        }
        String addUserQuery = "INSERT INTO users (username, password) VALUES(?, ?);";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(addUserQuery)) {
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new UserException("Failed to create user");
        }
    }

    public static boolean checkUserPassword(String username, String password) throws UserException {
        if (username == null || password == null) {
            throw new UserException("Username or password cannot be null");
        }

        if (!checkUserPresence(username)) {
            throw new UserException("User does not exist");
        }

        String selectUserQuery = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectUserQuery)) {
            stmt.setString(1, username);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("password");
                    return storedHash.equals(hashPassword(password));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new UserException("Failed to check user password");
        }
        return false;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Ticket getMinTicketByPrice() throws SQLException {
        String getMinTicketQuery = "SELECT * FROM tickets ORDER BY price ASC LIMIT 1;";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(getMinTicketQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setName(rs.getString("ticket_name"));
                ticket.setCoordinates(new Coordinates(rs.getInt("coordinates_x"), rs.getDouble("coordinates_y")));
                ticket.setCreationDate(rs.getTimestamp("creation_date"));
                ticket.setPrice(rs.getLong("price"));
                ticket.setType(TicketType.valueOf(rs.getString("ticket_type")));

                Venue venue = new Venue();
                venue.setId(rs.getLong("venue_id"));
                venue.setName(rs.getString("venue_name"));
                venue.setCapacity(rs.getInt("venue_capacity"));
                venue.setType(VenueType.valueOf(rs.getString("venue_type")));

                Address address = new Address();
                address.setStreet(rs.getString("address_street"));
                address.setZipCode(rs.getString("address_zipcode"));
                address.setTown(new Location(rs.getInt("address_town_x"), rs.getDouble("address_town_y"), rs.getString("address_town_name")));
                venue.setAddress(address);

                ticket.setVenue(venue);

                ticket.setUsername(rs.getString("username"));

                return ticket;
            } else {
                return null;
            }
        }
    }

    public void addTicket(Ticket ticket) throws SQLException {
        String insertTicketQuery = "INSERT INTO tickets (ticket_name, coordinates_x, coordinates_y, creation_date, price, ticket_type, venue_id, venue_name, venue_capacity, venue_type, address_street, address_zipcode, address_town_x, address_town_y, address_town_name, username) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = getConnection();
             PreparedStatement ticketStmt = connection.prepareStatement(insertTicketQuery, Statement.RETURN_GENERATED_KEYS)) {

            ticketStmt.setString(1, ticket.getName());
            ticketStmt.setInt(2, ticket.getCoordinates().getX());
            ticketStmt.setDouble(3, ticket.getCoordinates().getY());
            ticketStmt.setTimestamp(4, new Timestamp(ticket.getCreationDate().getTime()));
            ticketStmt.setLong(5, ticket.getPrice());
            ticketStmt.setString(6, ticket.getType() != null ? ticket.getType().name() : null);

            Venue venue = ticket.getVenue();
            ticketStmt.setObject(7, venue != null ? (int) venue.getId() : null, Types.INTEGER);
            ticketStmt.setString(8, venue != null ? venue.getName() : null);
            ticketStmt.setObject(9, venue != null ? venue.getCapacity() : null, Types.INTEGER);
            ticketStmt.setString(10, venue != null && venue.getType() != null ? venue.getType().name() : null);

            Address address = venue != null ? venue.getAddress() : null;
            ticketStmt.setString(11, address != null ? address.getStreet() : null);
            ticketStmt.setString(12, address != null ? address.getZipCode() : null);

            Location town = address != null ? address.getTown() : null;
            ticketStmt.setObject(13, town != null ? town.getX() : null, Types.INTEGER);
            ticketStmt.setObject(14, town != null ? town.getY() : null, Types.DOUBLE);
            ticketStmt.setString(15, town != null ? town.getName() : null);

            ticketStmt.setString(16, ticket.getUsername());

            ticketStmt.executeUpdate();

            try (ResultSet rs = ticketStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt(1));
                }
            }
        }
    }

    public void clearTicketsByUsername(String username) throws SQLException {
        String clearTicketsQuery = "DELETE FROM tickets WHERE username = ?;";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(clearTicketsQuery)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    public List<Ticket> getTicketsWithVenueLessThan(int capacity) {
        String query = "SELECT * FROM tickets WHERE venue_capacity < ? ORDER BY venue_capacity ASC";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, capacity);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Ticket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(resultSetToTicket(rs));
                }
                return tickets;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving tickets: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Ticket> getTicketsSortedByPriceDescending() {
        String query = "SELECT * FROM tickets ORDER BY price DESC";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<Ticket> tickets = new ArrayList<>();
            while (rs.next()) {
                tickets.add(resultSetToTicket(rs));
            }
            return tickets;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving tickets: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Ticket> getTicketsSortedByVenueCapacityDescending() {
        String query = "SELECT * FROM tickets WHERE venue_capacity IS NOT NULL ORDER BY venue_capacity DESC";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<Ticket> tickets = new ArrayList<>();
            while (rs.next()) {
                tickets.add(resultSetToTicket(rs));
            }
            return tickets;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving tickets: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean removeTicketAt(int index) throws SQLException {
        String query = "DELETE FROM tickets WHERE id = (SELECT id FROM tickets ORDER BY id LIMIT 1 OFFSET ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, index);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean removeTicketById(long id) throws SQLException {
        String query = "DELETE FROM tickets WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean removeFirstTicket() throws SQLException {
        String query = "DELETE FROM tickets WHERE id = (SELECT id FROM tickets ORDER BY id LIMIT 1)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateTicketById(long id, Ticket updatedTicket) throws SQLException {
        String query = "UPDATE tickets SET ticket_name = ?, coordinates_x = ?, coordinates_y = ?, creation_date = ?, price = ?, ticket_type = ?, venue_id = ?, venue_name = ?, venue_capacity = ?, venue_type = ?, address_street = ?, address_zipcode = ?, address_town_x = ?, address_town_y = ?, address_town_name = ?, username = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, updatedTicket.getName());
            stmt.setInt(2, updatedTicket.getCoordinates().getX());
            stmt.setDouble(3, updatedTicket.getCoordinates().getY());
            stmt.setTimestamp(4, new java.sql.Timestamp(updatedTicket.getCreationDate().getTime()));
            stmt.setLong(5, updatedTicket.getPrice());
            stmt.setString(6, updatedTicket.getType().name());
            stmt.setObject(7, updatedTicket.getVenue() != null ? (int) updatedTicket.getVenue().getId() : null, java.sql.Types.INTEGER);
            stmt.setString(8, updatedTicket.getVenue() != null ? updatedTicket.getVenue().getName() : null);
            stmt.setObject(9, updatedTicket.getVenue() != null ? updatedTicket.getVenue().getCapacity() : null, java.sql.Types.INTEGER);
            stmt.setString(10, updatedTicket.getVenue() != null && updatedTicket.getVenue().getType() != null ? updatedTicket.getVenue().getType().name() : null);
            stmt.setString(11, updatedTicket.getVenue() != null && updatedTicket.getVenue().getAddress() != null ? updatedTicket.getVenue().getAddress().getStreet() : null);
            stmt.setString(12, updatedTicket.getVenue() != null && updatedTicket.getVenue().getAddress() != null ? updatedTicket.getVenue().getAddress().getZipCode() : null);
            stmt.setObject(13, updatedTicket.getVenue() != null && updatedTicket.getVenue().getAddress() != null ? updatedTicket.getVenue().getAddress().getTown().getX() : null, java.sql.Types.INTEGER);
            stmt.setObject(14, updatedTicket.getVenue() != null && updatedTicket.getVenue().getAddress() != null ? updatedTicket.getVenue().getAddress().getTown().getY() : null, java.sql.Types.DOUBLE);
            stmt.setString(15, updatedTicket.getVenue() != null && updatedTicket.getVenue().getAddress() != null ? updatedTicket.getVenue().getAddress().getTown().getName() : null);
            stmt.setString(16, updatedTicket.getUsername());
            stmt.setLong(17, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
