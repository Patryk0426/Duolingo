import java.sql.*;
import java.util.Scanner;

public class SelectUser {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo"; // Adres bazy danych
    private static final String DB_USER = "root"; // Użytkownik bazy danych
    private static final String DB_PASSWORD = ""; // Hasło do bazy danych

    public static void select() {
        System.out.println("1. Zaloguj się");
        System.out.println("2. Utwórz konto");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        var user = new User();

        switch (choice) {
            case 1:
                scanner.nextLine();
                System.out.println("Podaj login:");
                String login = scanner.nextLine();

                System.out.println("Podaj hasło:");
                String password = scanner.nextLine();

                loginUser(login, password);

                break;

            case 2:
                scanner.nextLine();
                System.out.println("Podaj nazwę konta:");
                user.name = scanner.nextLine();

                System.out.println("Podaj login:");
                user.login = scanner.nextLine();

                System.out.println("Podaj hasło:");
                user.password = scanner.nextLine();
                user.type = UserTypeEnum.User;

                createAccount(user);
                break;

            default:
                System.out.println("Niepoprawna opcja");
                break;
        }
    }

    public static void createAccount(User user) {
        // Tworzenie zapytania SQL
        String sql = "INSERT INTO users (name, login, password, type) VALUES ('" + user.name + "', '"
                + user.login + "', '" + user.password + "', " + user.type.getValue() + ")";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            // Wykonanie zapytania SQL
            int rowsInserted = statement.executeUpdate(sql);
            if (rowsInserted > 0) {
                System.out.println("Konto zostało pomyślnie utworzone.");
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas tworzenia konta: " + e.getMessage());
        }
    }

    public static void loginUser(String login, String password) {
        // Tworzenie zapytania SQL do sprawdzania danych użytkownika
        String sql = "SELECT * FROM users WHERE login = '" + login + "' AND password = '" + password + "'";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                String name = result.getString("name");

                System.out.println("Zalogowano pomyślnie!");
                System.out.println("Witaj, " + name);
                SelectLanguage sl = new SelectLanguage();
                sl.selectLanguage();
            } else {
                System.out.println("Nieprawidłowy login lub hasło.");
                select();
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas logowania: " + e.getMessage());
        }
    }
}
