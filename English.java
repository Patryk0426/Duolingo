import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class English extends Language {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public English() {
        super("Angielski");
    }

    @Override
    /**.
     *
     */
    public void learn() {
        System.out.println("Nauka angielskiego: Zapoznaj się z podstawami gramatyki.");
        Scanner scanner = new Scanner(System.in);
        int correctAnswers = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String questionQuery = "SELECT * FROM questions WHERE id_stage = 1 AND id_language = 1";
            Statement questionStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet questionResultSet = questionStatement.executeQuery(questionQuery);

            while (questionResultSet.next()) {
                int questionId = questionResultSet.getInt("id_question");
                String questionText = questionResultSet.getString("question");
                String correctAnswerQuery = "SELECT answer FROM answers WHERE id_question = ?";
                PreparedStatement correctAnswerStatement = connection.prepareStatement(correctAnswerQuery);
                correctAnswerStatement.setInt(1, questionId);
                ResultSet correctAnswerResultSet = correctAnswerStatement.executeQuery();

                String correctAnswer = null;
                if (correctAnswerResultSet.next()) {
                    correctAnswer = correctAnswerResultSet.getString("answer");
                }

                String randomAnswersQuery = "SELECT answer FROM answers WHERE id_question != ? ORDER BY RAND() LIMIT 3";
                PreparedStatement randomAnswersStatement = connection.prepareStatement(randomAnswersQuery);
                randomAnswersStatement.setInt(1, questionId);
                ResultSet randomAnswersResultSet = randomAnswersStatement.executeQuery();

                String[] answers = getAnswersArray(correctAnswer, randomAnswersResultSet);

                System.out.println("Pytanie: " + questionText);
                for (int i = 0; i < answers.length; i++) {
                    System.out.println((i + 1) + ". " + answers[i]);
                }

                System.out.print("Wybierz poprawną odpowiedź (1-4): ");
                int userChoice = scanner.nextInt();

                if (answers[userChoice - 1].equals(correctAnswer)) {
                    System.out.println("Poprawna odpowiedź!");
                    correctAnswers++;
                } else {
                    System.out.println("Błędna odpowiedź. Poprawna odpowiedź to: " + correctAnswer);
                }


            }

            // Sprawdź, czy użytkownik powinien przejść na kolejny poziom
            if (correctAnswers >= 7) {
                System.out.println("Gratulacje! Przeszedłeś na etap 2.");
                // Tu można dodać logikę dla zmiany stage
            } else {
                System.out.println("Musisz odpowiedzieć poprawnie na co najmniej 7 pytań, aby przejść na kolejny poziom.");
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas nauki angielskiego: " + e.getMessage());
        }
    }

    /**.
     *
     * @param correctAnswer
     * @param randomAnswersResultSet
     * @throws SQLException
     */
    public String[] getAnswersArray(String correctAnswer, ResultSet randomAnswersResultSet) throws SQLException {
        // Utwórz tablicę odpowiedzi
        String[] answers = new String[4];
        answers[0] = correctAnswer;

        int index = 1;
        while (randomAnswersResultSet.next() && index < 4) {
            answers[index] = randomAnswersResultSet.getString("answer");
            index++;
        }

        // Jeśli mniej niż 3 losowe odpowiedzi, uzupełnij brakujące wartości pustymi
        while (index < 4) {
            answers[index] = "Brak odpowiedzi";
            index++;
        }

        // Wymieszaj odpowiedzi w tablicy
        shuffleArray(answers);
        return answers;
    }

    /**.
     *
     * @param array
     */
    private void shuffleArray(String[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
