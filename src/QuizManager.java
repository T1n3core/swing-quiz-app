import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the state and flow of a single quiz session.
 * Handles question selection, answer recording, timing, and scoring.
 * 
 * @author GasTheJuice
 */
public class QuizManager {
    private final List<Question> allQuestions;
    private List<Question> currentQuizQuestions;
    private int currentIndex = 0;
    private int correctCount = 0;
    private int wrongCount = 0;
    private Instant startTime, endTime;
    private int timeLimitSeconds = 0;
    private Map<Question, String> userAnswers;

    /**
     * Creates a manager with access to all available questions.
     * 
     * @param allQuestions complete question bank
     */
    public QuizManager(List<Question> allQuestions) {
        this.allQuestions = new ArrayList<>(allQuestions);
    }

    /**
     * Starts a new quiz for the given theme with specified number of questions.
     * 
     * @param theme theme to filter questions
     * @param questionCount number of questions to include
     */
    public void startQuizForTheme(String theme, int questionCount) {
        List<Question> filtered = new ArrayList<>();
        for (Question q : allQuestions) {
            if (q.getTheme().equals(theme)) {
                filtered.add(q);
            }
        }
        Collections.shuffle(filtered);
        if (questionCount > filtered.size()) {
            questionCount = filtered.size();
        }
        currentQuizQuestions = filtered.subList(0, questionCount);
        currentIndex = 0;
        correctCount = 0;
        wrongCount = 0;
        startTime = Instant.now();
        endTime = null;
        userAnswers = new HashMap<>();
    }

    /** @return total number of questions in current quiz */
    public int getQuestionCount() { return currentQuizQuestions.size(); }

    /** @return current question */
    public Question getCurrentQuestion() { return currentQuizQuestions.get(currentIndex); }

    /** @return zero-based index of current question */
    public int getCurrentIndex() { return currentIndex; }

    /** @return number of correct answers */
    public int getCorrectCount() { return correctCount; }

    /** @return number of wrong answers */
    public int getWrongCount() { return wrongCount; }

    /** @return time limit in seconds */
    public int getTimeLimitSeconds() { return timeLimitSeconds; }

    /** @return map of questions to user answers */
    public Map<Question, String> getUserAnswers() { return userAnswers; }

    /** @return percentage of correct answers */
    public double getPercentage() {
        int total = correctCount + wrongCount;
        return total == 0 ? 0.0 : (100.0 * correctCount / total);
    }

    /** @return elapsed time in seconds */
    public long getElapsedSeconds() {
        Instant end = (endTime == null) ? Instant.now() : endTime;
        return Duration.between(startTime, end).getSeconds();
    }

    /** Advances to the next question if available */
    public void nextQuestion() {
        if (currentIndex < currentQuizQuestions.size() - 1) {
            currentIndex++;
        }
    }

    /**
     * Records whether the current answer was correct.
     * 
     * @param correct true if answer was correct
     */
    public void recordAnswer(boolean correct) {
        if (correct) {
            correctCount++;
        } else {
            wrongCount++;
        }
    }

    /**
     * Stores the user's raw answer string.
     * 
     * @param ans user's answer
     */
    public void storeAnswer(String ans) {
        this.userAnswers.put(getCurrentQuestion(), ans);
    }

    /** Marks the quiz as finished and records end time */
    public void finish() {
        endTime = Instant.now();
    }

    /**
     * Sets the time limit for the quiz.
     * 
     * @param seconds time limit in seconds
     */
    public void setTimeLimitSeconds(int seconds) {
        this.timeLimitSeconds = seconds;
    }
}