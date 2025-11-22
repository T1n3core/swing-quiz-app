import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a quiz question with type, options, answers, and metadata.
 * Supports multiple answer types including image-based click areas.
 * 
 * @author GasTheJuice
 */
public class Question implements Serializable {
    
    /** Types of questions supported by the quiz system */
    public enum QuestionType {
        RADIO, CHECKBOX, NUMERIC, TEXT, COMBOBOX, SLIDER, IMAGE_CLICK
    }

    private final String theme;
    private final String prompt;
    private final QuestionType type;
    private final List<String> options;
    private final List<String> answers;
    private final String hint;
    private final String imagePath;
    private final List<Rectangle> correctAreas;

    /**
     * Full constructor for all question types including image-click.
     */
    public Question(String theme, String prompt, QuestionType type, List<String> options, 
                    List<String> answers, String hint, String imagePath, List<Rectangle> correctAreas) {
        this.theme = theme;
        this.prompt = prompt;
        this.type = type;
        this.options = options;
        this.answers = answers;
        this.hint = hint;
        this.imagePath = imagePath;
        this.correctAreas = correctAreas;
    }

    /**
     * Constructor for non-image questions.
     */
    public Question(String theme, String prompt, QuestionType type, List<String> options, 
                    List<String> answers, String hint) {
        this.theme = theme;
        this.prompt = prompt;
        this.type = type;
        this.options = options;
        this.answers = answers;
        this.hint = hint;
        this.imagePath = null;
        this.correctAreas = List.of();
    }

    /** @return the theme category of this question */
    public String getTheme() { return theme; }
    
    /** @return the question text prompt */
    public String getPrompt() { return prompt; }
    
    /** @return the type of answer expected */
    public QuestionType getType() { return type; }
    
    /** @return list of answer options (null for free-form) */
    public List<String> getOptions() { return options; }
    
    /** @return list of correct answers */
    public List<String> getAnswers() { return answers; }
    
    /** @return hint text or null if none */
    public String getHint() { return hint; }
    
    /** @return path to image (for IMAGE_CLICK) or null */
    public String getImagePath() { return imagePath; }
    
    /** @return list of correct click regions (for IMAGE_CLICK) */
    public List<Rectangle> getCorrectAreas() { return correctAreas; }

    /**
     * Normalizes a string for case-insensitive, whitespace-insensitive comparison.
     * 
     * @param str input string
     * @return normalized lowercase string with single spaces
     */
    public static String normalize(String str) {
        if (str == null) {
            return "";
        }
        return str.trim().replaceAll("\\s+", " ").toLowerCase();
    }
}