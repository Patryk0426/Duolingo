import java.sql.*;
import java.util.Scanner;

public class SelectUser {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";


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

    /**.
     *
     * @param user
     */
    public static void createAccount(User user) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);

            String userSql = "INSERT INTO users (name, login, password, type) VALUES (?, ?, ?, ?)";
            PreparedStatement userStatement = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, user.name);
            userStatement.setString(2, user.login);
            userStatement.setString(3, user.password);
            userStatement.setInt(4, user.type.getValue());

            int rowsInserted = userStatement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    String progressSql = "INSERT INTO user_progress (id_user, e_completed, s_completed, j_completed) VALUES (?, 0, 0, 0)";
                    PreparedStatement progressStatement = connection.prepareStatement(progressSql);
                    progressStatement.setInt(1, userId);
                    progressStatement.executeUpdate();

                    connection.commit();

                    System.out.println("Konto zostało pomyślnie utworzone.");

                    user.id_user = userId;
                    User.currentUser = user;

                    System.out.println("Zalogowano pomyślnie!");
                    System.out.println("Witaj, " + user.name);

                    SelectLanguage sl = new SelectLanguage();
                    sl.selectLanguage();
                }
            }
        } catch (SQLException e) {
            // W przypadku błędu, wycofaj transakcję
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Błąd podczas wycofywania transakcji: " + ex.getMessage());
                }
            }
            System.err.println("Błąd podczas tworzenia konta: " + e.getMessage());
            select();
        }
    }

    public static void loginUser(String login, String password) {
        String sql = "SELECT * FROM users WHERE login = '" + login + "' AND password = '" + password + "'";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                int userId = result.getInt("id_user");
                String name = result.getString("name");
                User.currentUser = new User();
                User.currentUser.id_user = userId;
                User.currentUser.name = name;
                User.currentUser.login = login;
                User.currentUser.password = password;

                System.out.println("Zalogowano pomyślnie!");
                System.out.println("Witaj, " + name);

                // Sprawdzamy, czy rekord postępu już istnieje
                String checkProgressSql = "SELECT * FROM user_progress WHERE id_user = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkProgressSql);
                checkStatement.setInt(1, userId);
                ResultSet progressResult = checkStatement.executeQuery();

                // Jeśli nie istnieje, tworzymy nowy rekord
                if (!progressResult.next()) {
                    String progressSql = "INSERT INTO user_progress (id_user, e_completed, s_completed, j_completed) " +
                            "VALUES (?, 0, 0, 0)";
                    PreparedStatement progressStatement = connection.prepareStatement(progressSql);
                    progressStatement.setInt(1, userId);
                    progressStatement.executeUpdate();
                    System.out.println("Inicjalizacja postępu nauki.");
                }

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


