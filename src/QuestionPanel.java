import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * UI panel that displays a single question and collects user input.
 * Supports radio, checkbox, text, numeric, image-click, combobox, and slider questions.
 *
 * @author GasTheJuice
 */
public class QuestionPanel extends JPanel {
    private final Question question;
    private List<AbstractButton> optionButtons;
    private List<JComboBox<?>> comboBoxes;
    private List<JSlider> sliders;
    private ButtonGroup radioGroup;
    private JTextField textField;
    private boolean answerCorrect;
    private boolean answered;

    /**
     * Creates a panel for the given question.
     *
     * @param question the question to display
     */
    public QuestionPanel(Question question) {
        this.question = question;
        initComponents();
    }

    /** Builds the appropriate UI based on question type. */
    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        JTextArea promptArea = new JTextArea(question.getPrompt());
        promptArea.setEditable(false);
        promptArea.setLineWrap(true);
        promptArea.setWrapStyleWord(true);
        promptArea.setOpaque(false);
        promptArea.setFocusable(false);
        promptArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(promptArea, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        optionButtons = new ArrayList<>();
        comboBoxes = new ArrayList<>();
        sliders = new ArrayList<>();

        switch (question.getType()) {
            case RADIO -> {
                radioGroup = new ButtonGroup();
                for (String opt : question.getOptions()) {
                    JRadioButton rb = new JRadioButton(opt);
                    radioGroup.add(rb);
                    optionButtons.add(rb);
                    center.add(rb);
                }
            }
            case CHECKBOX -> {
                for (String opt : question.getOptions()) {
                    JCheckBox cb = new JCheckBox(opt);
                    optionButtons.add(cb);
                    center.add(cb);
                }
            }
            case NUMERIC, TEXT -> {
                textField = new JTextField();
                textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
                center.add(new JLabel("Your answer:"));
                center.add(textField);
            }
            case COMBOBOX -> {
                JComboBox<String> combo = new JComboBox<>();
                for (String opt : question.getOptions()) {
                    combo.addItem(opt);
                }
                combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, combo.getPreferredSize().height));
                comboBoxes.add(combo);
                center.add(combo);
            }
            case SLIDER -> {
                if (question.getOptions().size() < 2) {
                    center.add(new JLabel("Invalid slider range."));
                } else {
                    try {
                        int min = Integer.parseInt(question.getOptions().get(0).trim());
                        int max = Integer.parseInt(question.getOptions().get(1).trim());
                        int initial = min;
                        if (question.getOptions().size() > 2) {
                            initial = Integer.parseInt(question.getOptions().get(2).trim());
                            initial = Math.max(min, Math.min(max, initial));
                        }
                        JSlider slider = new JSlider(min, max, initial);
                        slider.setMajorTickSpacing((max - min) / 5);
                        slider.setPaintTicks(true);
                        slider.setPaintLabels(true);
                        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                        JLabel valueLabel = new JLabel(String.valueOf(initial));
                        slider.addChangeListener(e -> valueLabel.setText(String.valueOf(slider.getValue())));
                        sliders.add(slider);
                        JPanel sliderPanel = new JPanel();
                        sliderPanel.setLayout(new BorderLayout(5, 5));
                        sliderPanel.add(slider, BorderLayout.CENTER);
                        sliderPanel.add(valueLabel, BorderLayout.EAST);
                        center.add(sliderPanel);
                    } catch (NumberFormatException ex) {
                        center.add(new JLabel("Invalid slider values."));
                    }
                }
            }
            case IMAGE_CLICK -> {
                ImageIcon icon = new ImageIcon(question.getImagePath());
                JPanel imagePanel = new JPanel(null);
                imagePanel.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
                imagePanel.add(imageLabel);
                JButton wrongBtn = new JButton();
                wrongBtn.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
                wrongBtn.setOpaque(false);
                wrongBtn.setContentAreaFilled(false);
                wrongBtn.setBorderPainted(false);
                wrongBtn.addActionListener(e -> {
                    answerCorrect = false;
                    answered = true;
                });
                imagePanel.add(wrongBtn, 0);
                for (Rectangle r : question.getCorrectAreas()) {
                    JButton correctBtn = new JButton();
                    correctBtn.setBounds(r);
                    correctBtn.setOpaque(false);
                    correctBtn.setContentAreaFilled(false);
                    correctBtn.setBorderPainted(false);
                    correctBtn.addActionListener(e -> {
                        answerCorrect = true;
                        answered = true;
                    });
                    imagePanel.add(correctBtn, 0);
                }
                JPanel wrapper = new JPanel(new BorderLayout());
                wrapper.add(imagePanel, BorderLayout.CENTER);
                add(wrapper, BorderLayout.CENTER);
                return;
            }
        }
        add(center, BorderLayout.CENTER);
    }

    /**
     * Retrieves the user's answer as a string.
     *
     * @return user input in string form
     */
    public String getUserAnswer() {
        switch (question.getType()) {
            case RADIO -> {
                for (AbstractButton b : optionButtons) {
                    if (b.isSelected()) {
                        return b.getText();
                    }
                }
            }
            case CHECKBOX -> {
                List<String> selected = new ArrayList<>();
                for (AbstractButton b : optionButtons) {
                    if (b.isSelected()) {
                        selected.add(b.getText());
                    }
                }
                return String.join(", ", selected);
            }
            case NUMERIC, TEXT -> {
                return textField.getText();
            }
            case IMAGE_CLICK -> {
                return answered ? (answerCorrect ? "correct" : "wrong") : "";
            }
            case COMBOBOX -> {
                for (JComboBox<?> combo : comboBoxes) {
                    Object selected = combo.getSelectedItem();
                    return selected != null ? selected.toString() : "";
                }
            }
            case SLIDER -> {
                for (JSlider slider : sliders) {
                    return String.valueOf(slider.getValue());
                }
            }
            default -> {
                return "";
            }
        }
        return "";
    }

    /**
     * Checks if the user's answer matches any correct answer.
     *
     * @return true if answer is correct
     */
    public boolean checkAnswer() {
        List<String> correct = question.getAnswers();
        switch (question.getType()) {
            case RADIO -> {
                for (AbstractButton b : optionButtons) {
                    if (b instanceof JRadioButton && b.isSelected()) {
                        String selected = Question.normalize(b.getText());
                        return correct.stream().anyMatch(c -> Question.normalize(c).equals(selected));
                    }
                }
                return false;
            }
            case CHECKBOX -> {
                Set<String> selected = new HashSet<>();
                for (AbstractButton b : optionButtons) {
                    if (b.isSelected()) {
                        selected.add(Question.normalize(b.getText()));
                    }
                }
                Set<String> correctSet = new HashSet<>();
                for (String c : correct) {
                    correctSet.add(Question.normalize(c));
                }
                return selected.equals(correctSet);
            }
            case NUMERIC -> {
                for (String c : correct) {
                    try {
                        double ans = Double.parseDouble(Question.normalize(textField.getText()));
                        double dCorrect = Double.parseDouble(Question.normalize(c));
                        return Math.abs(ans - dCorrect) < 0.000001;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            }
            case TEXT -> {
                for (String c : correct) {
                    if (Question.normalize(textField.getText()).equals(Question.normalize(c))) {
                        return true;
                    }
                }
            }
            case COMBOBOX -> {
                for (JComboBox<?> combo : comboBoxes) {
                    Object selected = combo.getSelectedItem();
                    String userAnswer = selected != null ? Question.normalize(selected.toString()) : "";
                    return correct.stream().anyMatch(c -> Question.normalize(c).equals(userAnswer));
                }
                return false;
            }
            case SLIDER -> {
                for (JSlider slider : sliders) {
                    int userValue = slider.getValue();
                    for (String c : correct) {
                        try {
                            int correctValue = Integer.parseInt(Question.normalize(c));
                            if (userValue == correctValue) {
                                return true;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
                return false;
            }
            case IMAGE_CLICK -> {
                return answered && answerCorrect;
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    /** @return the underlying question object */
    public Question getQuestion() {
        return question;
    }
}