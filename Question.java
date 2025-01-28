public class Question {
    private int id;
    private String text;
    private String correctAnswer;
    private String[] answers; // Tablica z czterema odpowiedziami

    public Question(int id, String text, String correctAnswer, String[] answers) {
        this.id = id;
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getId() {
        return id;
    }
}