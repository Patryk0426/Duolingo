import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Questions {
    private final int id;
    private final String question;
    private final String correctAnswer;
    private final List<String> answers;

    public Questions(int id, String question, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void addAnswer(String answer) {
        answers.add(answer);
    }

    public void shuffleAnswers() {
        Collections.shuffle(answers);
    }
}
