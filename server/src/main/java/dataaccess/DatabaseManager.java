package dataaccess;

import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Problem in Database Manger executing an update: " + e.getMessage() + e);
        }
    }

    public static void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        System.out.println("Created the Database");
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Finished User table");
            for (var statement : createAuthTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Finished Auth table");
            for (var statement : createGameTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Finished Game table");
        } catch (SQLException ex) {
            throw new DataAccessException("User/Auth/Game table not working" + ex.getMessage());
        }
    }


    private static final String[] createUserTable = {
            """
    CREATE TABLE IF NOT EXISTS `user` (
      `username` varchar(256) NOT NULL,
      `password` varchar(256) NOT NULL,
      `email` varchar(256) NOT NULL,
      PRIMARY KEY (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
    };

    private static final String[] createAuthTable = {
            """
    CREATE TABLE IF NOT EXISTS `auth` (
      `id` int AUTO_INCREMENT,
      `username` varchar(256) NOT NULL,
      `authToken` varchar(256),
      PRIMARY KEY (`id`),
      INDEX (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
    };

    private static final String[] createGameTable = {
            """
    CREATE TABLE IF NOT EXISTS `game` (
    `game_id` INT NOT NULL PRIMARY KEY,
    `game_name` VARCHAR(255) NOT NULL,
    `white_username` VARCHAR(256),
    `black_username` VARCHAR(256),
    `game_info` LONGTEXT NOT NULL,
    FOREIGN KEY (`white_username`) REFERENCES `User`(`username`),
    FOREIGN KEY (`black_username`) REFERENCES `User`(`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
    };

}
