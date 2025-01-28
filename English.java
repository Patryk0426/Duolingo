import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<Questions> questions = loadQuestionsFromDatabase();
        if (questions.isEmpty()) {
            System.out.println("Brak pytań w bazie danych.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Wyświetlanie pytań i weryfikacja odpowiedzi
        for (Questions question : questions) {
            System.out.println("\nPytanie: " + question.getQuestion());
            List<String> answers = question.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                System.out.println((i + 1) + ". " + answers.get(i));
            }

            System.out.print("Wybierz numer odpowiedzi: ");
            int userChoice = scanner.nextInt();

            // Weryfikacja poprawności odpowiedzi
            if (answers.get(userChoice - 1).equals(question.getCorrectAnswer())) {
                System.out.println("Poprawna odpowiedź!");
            } else {
                System.out.println("Niepoprawna odpowiedź. Poprawna odpowiedź to: " + question.getCorrectAnswer());
            }
        }
    }

    private List<Questions> loadQuestionsFromDatabase() {
        List<Questions> questions = new ArrayList<>();

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

            List<String> allAnswers = new ArrayList<>();
            while (allAnswersResultSet.next()) {
                allAnswers.add(allAnswersResultSet.getString("answer"));
            }

            // Przetwarzanie pytań
            while (questionResultSet.next()) {
                int questionId = questionResultSet.getInt("id_question");
                String questionText = questionResultSet.getString("question");
                String correctAnswer = questionResultSet.getString("answer");

                Questions question = new Questions(questionId, questionText, correctAnswer);

                // Tworzenie zestawu odpowiedzi: poprawna + trzy losowe
                List<String> answersForQuestion = new ArrayList<>(allAnswers);
                answersForQuestion.remove(correctAnswer); // Usuwamy poprawną odpowiedź z losowych
                Collections.shuffle(answersForQuestion); // Tasujemy listę

                // Dodajemy poprawną odpowiedź i trzy losowe do pytania
                question.addAnswer(correctAnswer);
                for (int i = 0; i < 3; i++) {
                    question.addAnswer(answersForQuestion.get(i));
                }

                // Tasujemy odpowiedzi w pytaniu
                question.shuffleAnswers();
                questions.add(question);
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas ładowania pytań z bazy danych: " + e.getMessage());
        }

        return questions;
    }
}
