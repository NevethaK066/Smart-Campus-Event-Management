import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection.java
 * The ONLY class that knows the database address, username, and password.
 * If your MySQL username or password is different, change USER and PASSWORD below.
 */
public class DatabaseConnection {

    // CHANGE the PASSWORD to the password you set when installing MySQL
    private static final String URL =
            "jdbc:mysql://localhost:3306/smart_campus_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    // Returns a new connection to the MySQL database.
    // Every DAO method opens a connection, uses it, and closes it automatically.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
