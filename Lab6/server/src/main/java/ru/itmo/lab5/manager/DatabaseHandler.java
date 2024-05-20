package ru.itmo.lab5.manager;

import ru.itmo.lab5.data.models.*;
import ru.itmo.lab5.exceptions.UserException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
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

    public boolean saveCollectionToDatabase(CollectionManager collectionManager) {
        clearTickets();
        String resetSequenceQuery = "ALTER SEQUENCE tickets_id_seq RESTART;";
        String updateTicketIdQuery = "UPDATE tickets SET id = DEFAULT;";
        String insertTicketQuery = "INSERT INTO tickets (ticket_name, coordinates_x, coordinates_y, creation_date, price, ticket_type, venue_id, venue_name, venue_capacity, venue_type, address_street, address_zipcode, address_town_x, address_town_y, address_town_name, username) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET ticket_name = EXCLUDED.ticket_name, coordinates_x = EXCLUDED.coordinates_x, coordinates_y = EXCLUDED.coordinates_y, creation_date = EXCLUDED.creation_date, price = EXCLUDED.price, ticket_type = EXCLUDED.ticket_type, venue_id = EXCLUDED.venue_id, venue_name = EXCLUDED.venue_name, venue_capacity = EXCLUDED.venue_capacity, venue_type = EXCLUDED.venue_type, address_street = EXCLUDED.address_street, address_zipcode = EXCLUDED.address_zipcode, address_town_x = EXCLUDED.address_town_x, address_town_y = EXCLUDED.address_town_y, address_town_name = EXCLUDED.address_town_name, username = EXCLUDED.username RETURNING id;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement resetSeqStmt = connection.prepareStatement(resetSequenceQuery)) {
                resetSeqStmt.executeUpdate();
            }

            try (PreparedStatement updateIdStmt = connection.prepareStatement(updateTicketIdQuery)) {
                updateIdStmt.executeUpdate();
            }

            try (PreparedStatement ticketStmt = connection.prepareStatement(insertTicketQuery)) {
                for (Ticket ticket : collectionManager.getTickets()) {
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

                    try (ResultSet rs = ticketStmt.executeQuery()) {
                        if (rs.next()) {
                            ticket.setId(rs.getInt("id"));
                        }
                    }
                }

                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to database: " + e.getMessage());
            return false;
        }
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

    public void clearTickets() {
        String clearTicketsQuery = "DELETE FROM tickets;";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(clearTicketsQuery)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void loadAllTicketsToMemory(CollectionManager collectionManager) {
        String getAllTicketsQuery = "SELECT * FROM tickets;";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(getAllTicketsQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
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

                String username = rs.getString("username");
                ticket.setUsername(username);

                collectionManager.addFromDB(ticket);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


}
