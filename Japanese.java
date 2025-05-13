import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Japanese extends Language {

    public Japanese() {
        super("Japonski");
    }

    /**
     * Metoda umożliwiająca naukę języka japońskiego.
     * Sprawdza postęp użytkownika i oferuje opcje nauki oraz quizu.
     */
    @Override
    public void learn() {
        int completedStage = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String checkProgress = "SELECT j_completed FROM user_progress WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(checkProgress);
            ps.setInt(1, User.currentUser.id_user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean completed = rs.getInt("j_completed") == 1;
                if (completed) {
                    completedStage = 1;
                    System.out.println("Już ukończyłeś podstawowy etap tego języka!");
                    System.out.println("1. Wróć do wyboru języka");
                    System.out.println("2. Kontynuuj naukę na kolejnym poziomie");
                    Scanner scanner = new Scanner(System.in);
                    int choice = scanner.nextInt();
                    if (choice == 1) {
                        SelectLanguage sl = new SelectLanguage();
                        sl.selectLanguage();
                        return;
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Błąd podczas pobierania postępu: " + ex.getMessage());
        }

        System.out.println("Nauka japońskiego: " + (completedStage == 0 ? "Zapoznaj się z podstawami gramatyki." : "Kontynuuj naukę na poziomie zaawansowanym."));
        Scanner scanner = new Scanner(System.in);
        int correctAnswers = 0;
        System.out.println("Co byś chciał dzisiaj robic?\n1.Nauczyc sie " + (completedStage == 0 ? "od podstaw" : "zaawansowanych zagadnień") +
                "\n2.Uruchomic quiz i przejsc na nastepny etap\n3. Powrocic do wyboru jezyka");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                if (completedStage == 0) {
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
                } else {
                    System.out.println("l");
                }
                learn();
                break;
            case 3:
                SelectLanguage sl = new SelectLanguage();
                sl.selectLanguage();
                break;
            case 2:
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    // Wybieramy pytania z odpowiedniego etapu
                    int currentStage = completedStage + 1;
                    String questionQuery = "SELECT * FROM questions WHERE id_stage = ? AND id_language = 3";
                    PreparedStatement questionStatement = connection.prepareStatement(questionQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    questionStatement.setInt(1, currentStage);
                    ResultSet questionResultSet = questionStatement.executeQuery();

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
                        System.out.println("Gratulacje! Przeszedłeś na etap " + (currentStage + 1) + ".");
                        // Aktualizacja postępu w bazie danych
                        if (currentStage == 1) {
                            updateProgress(connection);
                        } else if (currentStage == 2) {
                            // Tutaj możemy dodać kolejną kolumnę w bazie danych dla etapu 2
                            // Na razie wyświetlamy tylko komunikat
                            System.out.println("Ukończyłeś wszystkie dostępne etapy tego języka!");
                        }
                        learn();
                    } else {
                        System.out.println("Musisz odpowiedzieć poprawnie na co najmniej 7 pytań, aby przejść na kolejny poziom.");
                        learn();
                    }

                } catch (SQLException e) {
                    System.err.println("Błąd podczas nauki japońskiego: " + e.getMessage());
                }
                break;
        }
    }

    /**
     * Sprawdza postęp użytkownika w nauce języka.
     *
     * @param connection Aktywne połączenie z bazą danych
     * @return true jeśli użytkownik ukończył naukę, false w przeciwnym wypadku
     * @throws SQLException w przypadku problemów z bazą danych
     */
    @Override
    protected boolean checkProgress(Connection connection) throws SQLException {
        String checkProgress = "SELECT j_completed FROM user_progress WHERE id_user = ?";
        try (PreparedStatement ps = connection.prepareStatement(checkProgress)) {
            ps.setInt(1, User.currentUser.id_user);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("j_completed") == 1;
                }
                return false;
            }
        }
    }

    /**
     * Aktualizuje postęp użytkownika w nauce języka japońskiego.
     *
     * @param connection Aktywne połączenie z bazą danych
     * @throws SQLException w przypadku problemów z bazą danych
     */
    @Override
    protected void updateProgress(Connection connection) throws SQLException {
        // Najpierw sprawdzamy, czy rekord istnieje
        String checkQuery = "SELECT * FROM user_progress WHERE id_user = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, User.currentUser.id_user);
            try (ResultSet rs = checkStatement.executeQuery()) {
                if (rs.next()) {
                    // Rekord istnieje, więc aktualizujemy
                    String updateQuery = "UPDATE user_progress SET j_completed = 1 WHERE id_user = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, User.currentUser.id_user);
                        updateStatement.executeUpdate();
                        System.out.println("Zapisano postęp nauki języka japońskiego!");
                    }
                } else {
                    // Rekord nie istnieje, więc tworzymy nowy
                    String insertQuery = "INSERT INTO user_progress (id_user, e_completed, s_completed, j_completed) VALUES (?, 0, 0, 1)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, User.currentUser.id_user);
                        insertStatement.executeUpdate();
                        System.out.println("Utworzono nowy rekord postępu dla języka japońskiego!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas zapisywania postępu: " + e.getMessage());
            throw e; // Propagowanie wyjątku zgodnie z sygnaturą metody abstrakcyjnej
        }
    }

    /**
     * Tworzy tablicę odpowiedzi zawierającą poprawną odpowiedź i losowe odpowiedzi.
     *
     * @param correctAnswer Poprawna odpowiedź
     * @param randomAnswersResultSet ResultSet zawierający losowe odpowiedzi
     * @return Tablica odpowiedzi
     * @throws SQLException w przypadku problemów z bazą danych
     */
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

    /**
     * Miesza elementy tablicy w sposób losowy.
     *
     * @param array Tablica do wymieszania
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