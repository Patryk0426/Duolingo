import java.sql.*;
import java.util.Random;
import java.util.Scanner;

class English extends Language {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public English() {
        super("Angielski");
    }

    @Override
    public void learn() {
        System.out.println("Nauka angielskiego: Odpowiedz na poniższe pytania.");
        Question[] questions = loadQuestionsFromDatabase();
        if (questions == null || questions.length == 0) {
            System.out.println("Brak pytań w bazie danych.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Wyświetlanie pytań i weryfikacja odpowiedzi
        for (Question question : questions) {
            if (question == null) continue;

            System.out.println("\nPytanie: " + question.getText());
            String[] answers = question.getAnswers();
            for (int i = 0; i < answers.length; i++) {
                System.out.println((i + 1) + ". " + answers[i]);
            }

            System.out.print("Wybierz numer odpowiedzi: ");
            int userChoice = scanner.nextInt();

            // Weryfikacja poprawności odpowiedzi
            if (answers[userChoice - 1].equals(question.getCorrectAnswer())) {
                System.out.println("Poprawna odpowiedź!");
            } else {
                System.out.println("Niepoprawna odpowiedź. Poprawna odpowiedź to: " + question.getCorrectAnswer());
            }
        }
    }

    private Question[] loadQuestionsFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement questionStatement = connection.createStatement();
             Statement allAnswersStatement = connection.createStatement()) {

            // Pobieranie pytań i odpowiedzi dla języka angielskiego
            String questionSql = "SELECT q.id_question, q.question, a.answer " +
                    "FROM questions q " +
                    "JOIN answers a ON q.id_question = a.id_question " +
                    "WHERE q.id_language = 1";
            ResultSet questionResultSet = questionStatement.executeQuery(questionSql);

            // Pobieranie wszystkich odpowiedzi z bazy danych
            String allAnswersSql = "SELECT answer FROM answers";
            ResultSet allAnswersResultSet = allAnswersStatement.executeQuery(allAnswersSql);

            // Pobranie wszystkich odpowiedzi do tablicy
            String[] allAnswers = getAnswersArray(allAnswersResultSet);

            // Tworzenie pytań
            Question[] questions = new Question[10]; // Maks. 10 pytań
            int index = 0;

            while (questionResultSet.next() && index < questions.length) {
                int questionId = questionResultSet.getInt("id_question");
                String questionText = questionResultSet.getString("question");
                String correctAnswer = questionResultSet.getString("answer");

                // Wybór trzech losowych odpowiedzi
                String[] randomAnswers = getRandomAnswers(allAnswers, correctAnswer);

                // Dodanie poprawnej odpowiedzi do zestawu
                String[] answers = new String[4];
                answers[0] = correctAnswer; // Dodanie poprawnej odpowiedzi
                System.arraycopy(randomAnswers, 0, answers, 1, 3); // Kopiowanie trzech losowych

                // Losowanie kolejności odpowiedzi
                shuffleArray(answers);

                // Tworzenie pytania
                questions[index++] = new Question(questionId, questionText, correctAnswer, answers);
            }

            return questions;

        } catch (SQLException e) {
            System.err.println("Błąd podczas ładowania pytań z bazy danych: " + e.getMessage());
            return null;
        }
    }

    private String[] getAnswersArray(ResultSet resultSet) throws SQLException {
        resultSet.last(); // Przejdź na ostatni wiersz, aby policzyć wiersze
        int size = resultSet.getRow();
        resultSet.beforeFirst(); // Wróć na początek

        String[] answers = new String[size];
        int index = 0;

        while (resultSet.next()) {
            answers[index++] = resultSet.getString("answer");
        }

        return answers;
    }

    private String[] getRandomAnswers(String[] allAnswers, String correctAnswer) {
        Random random = new Random();
        String[] randomAnswers = new String[3];
        int count = 0;

        while (count < 3) {
            int randomIndex = random.nextInt(allAnswers.length);
            String randomAnswer = allAnswers[randomIndex];

            // Upewnij się, że odpowiedź nie jest poprawna i nie została już wybrana
            if (!randomAnswer.equals(correctAnswer) && !isAnswerAlreadySelected(randomAnswers, randomAnswer)) {
                randomAnswers[count++] = randomAnswer;
            }
        }

        return randomAnswers;
    }

    private boolean isAnswerAlreadySelected(String[] selectedAnswers, String answer) {
        for (String selected : selectedAnswers) {
            if (selected != null && selected.equals(answer)) {
                return true;
            }
        }
        return false;
    }

    private void shuffleArray(String[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}