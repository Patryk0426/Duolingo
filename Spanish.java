import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Spanish extends Language {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Spanish() {
        super("Hiszpański");
    }

    @Override
    public void learn() {
        System.out.println("Nauka Hiszpańskiego: Zapoznaj się z podstawami gramatyki.");
        Scanner scanner = new Scanner(System.in);
        int correctAnswers = 0;
        System.out.println("Co byś chciał dzisiaj robic?\n1.Nauczyc sie od podstaw\n2.Uruchomic quiz i przejsc na nastepny etap\n3.Powrocic do wyboru jezyka");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                System.out.println("Hola – Hiszpańskie \"cześć\". Wymowa: /ˈo.la/.\n" +
                        "Dziękuję – Po polsku \"thank you\". Można też powiedzieć \"Dzięki\" (less formal).\n" +
                        "Adiós – Hiszpańskie \"do widzenia\" lub \"żegnaj\".\n" +
                        "Dzień dobry – Oznacza \"good morning\" lub \"good day\" po polsku.\n" +
                        "¿Cómo estás? – Hiszpańskie \"Jak się masz?\". Odpowiedź: \"Bien, gracias\".\n" +
                        "Proszę – Może oznaczać \"please\" lub \"you’re welcome\" w zależności od kontekstu.\n" +
                        "Lo siento – Hiszpańskie \"Przepraszam\" lub \"Współczuję\".\n" +
                        "Przyjaciel – Znaczy \"friend\" po polsku.\n" +
                        "Buen provecho – Hiszpańskie \"smacznego\".");
                learn();
                break;
            case 3:
                SelectLanguage sl = new SelectLanguage();
                sl.selectLanguage();
                break;
            case 2:
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String questionQuery = "SELECT * FROM questions WHERE id_stage = 1 AND id_language = 2";
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

                String randomAnswersQuery = "SELECT answer FROM answers WHERE id_question != ? AND id_language = 2 ORDER BY RAND() LIMIT 3";
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

            if (correctAnswers >= 7) {
                System.out.println("Gratulacje! Przeszedłeś na etap 2.");
                learn();
            } else {
                System.out.println("Musisz odpowiedzieć poprawnie na co najmniej 7 pytań, aby przejść na kolejny poziom.");
                learn();
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas nauki angielskiego: " + e.getMessage());
        }
    }}
    public String[] getAnswersArray(String correctAnswer, ResultSet randomAnswersResultSet) throws SQLException {
        // Utwórz tablicę odpowiedzi
        String[] answers = new String[4];
        answers[0] = correctAnswer;

        int index = 1;
        while (randomAnswersResultSet.next() && index < 4) {
            answers[index] = randomAnswersResultSet.getString("answer");
            index++;
        }

        shuffleArray(answers);
        return answers;
    }
    private void shuffleArray(String[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
