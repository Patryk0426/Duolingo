import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Japanese extends Language {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Japanese() {
        super("Japonski");
    }

    @Override
    /**.
     *
     */
    public void learn() {
        System.out.println("Nauka Japonskiego: Zapoznaj się z podstawami gramatyki.");
        Scanner scanner = new Scanner(System.in);
        int correctAnswers = 0;
        System.out.println("Co byś chciał dzisiaj robic?\n1.Nauczyc sie od podstaw\n2.Uruchomic quiz i przejsc na nastepny etap\n3.Powrocic do wyboru jezyka");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                System.out.println("O ha yō (おはよう) – Japońskie \"dzień dobry\" (rano).\n" +
                        "\n" +
                        "A ri ga tō (ありがとう) – \"Dziękuję\" po japońsku, wersja nieformalna.\n" +
                        "\n" +
                        "Ha i (はい) – \"Tak\" po japońsku.\n" +
                        "\n" +
                        "Ī e (いいえ) – \"Nie\" po japońsku.\n" +
                        "\n" +
                        "O ya su mi (おやすみ) – \"Dobranoc\" po japońsku.\n" +
                        "\n" +
                        "I chi (いち) – Oznacza \"jeden\" po japońsku.\n" +
                        "\n" +
                        "Su mi ma sen (すみません) – \"Przepraszam\" po japońsku, używane w różnych sytuacjach.\n" +
                        "\n" +
                        "Sa yō na ra (さようなら) – Oznacza \"do widzenia\", ale bardziej formalnie.\n" +
                        "\n" +
                        "Ne ko (ねこ) – \"Kot\" po japońsku.\n" +
                        "\n" +
                        "Mi zu (みず) – \"Woda\" po japońsku.\n" +
                        "\n" +
                        "Inu (犬) – \"Pies\" po japońsku.");

                learn();
                break;
            case 3:
                SelectLanguage sl = new SelectLanguage();
                sl.selectLanguage();
                break;
            case 2:

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String questionQuery = "SELECT * FROM questions WHERE id_stage = 1 AND id_language = 3";
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

                String randomAnswersQuery = "SELECT answer FROM answers WHERE id_question != ? AND id_language = 3 ORDER BY RAND() LIMIT 3";
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
    }
    }

    public String[] getAnswersArray(String correctAnswer, ResultSet randomAnswersResultSet) throws SQLException {
        String[] answers = new String[4];
        answers[0] = correctAnswer;

        int index = 1;
        while (randomAnswersResultSet.next() && index < 4) {
            answers[index] = randomAnswersResultSet.getString("answer");
            index++;
        }

        while (index < 4) {
            answers[index] = "Brak odpowiedzi";
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
