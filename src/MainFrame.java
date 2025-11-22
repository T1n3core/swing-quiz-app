import javax.swing.Timer;
import java.util.List;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main application window for the C Programming Quiz.
 * Manages UI layout, quiz flow, timer, music, and user interaction.
 * 
 * @author GasTheJuice
 */
public class MainFrame extends JFrame {
    // === Menu Components ===
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menuExit = new JMenu("Exit");
    private final JMenu menuHelp = new JMenu("Options");
    private final JMenu menuStats = new JMenu("Stats");
    private final JMenuItem menuItemExit = new JMenuItem("Are you sure?");
    private final JMenuItem menuItemHelp = new JMenuItem("Help");
    private final JMenuItem menuItemAbout = new JMenuItem("About");
    private final JMenuItem menuItemHistory = new JMenuItem("History");
    private final JCheckBoxMenuItem menuItemDarkMode = new JCheckBoxMenuItem("Dark Mode");
    private final JCheckBoxMenuItem menuItemMusic = new JCheckBoxMenuItem("Disable Music");

    // === Layout Panels ===
    private final JPanel panelMainContainer = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelStartScreen = new JPanel();
    private final JPanel panelQuestionContainer = new JPanel(new BorderLayout(10, 10));

    // === Start Screen Components ===
    private final JComboBox<String> themeSelector = new JComboBox<>();
    private final JButton startButton = new JButton("Start Quiz");

    // === Dynamic Quiz Components ===
    private JButton hintButton;
    private JButton nextButton;
    private JButton submitButton;
    private JLabel progressLabel;
    private JLabel timerLabel;
    private JSlider questionSlider;
    private JSlider timeSlider;
    private QuestionPanel currentQuestionPanel;
    private Timer countdownTimer;
    private BackgroundMusicPlayer musicPlayer;

    // === Quiz State ===
    private QuizManager quizManager;
    private int hintsUsed = 0;
    private int remainingSeconds;

    /**
     * Constructs and displays the main quiz application window.
     * Initializes all UI components and starts background music.
     */
    public MainFrame() {
        setTitle("C Programming Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initMenuBar();
        initMainPanels();
        initStartScreen();
        initQuestionContainer();
        setupQuizManager();
        startBackgroundMusic();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
                System.exit(0);
            }
        });
    }

    /** Initializes the menu bar with Exit, Stats, and Options menus. */
    private void initMenuBar() {
        menuItemExit.addActionListener(e -> System.exit(0));
        menuExit.add(menuItemExit);

        menuItemHistory.addActionListener(e -> showHistory());
        menuStats.add(menuItemHistory);

        menuItemHelp.addActionListener(e -> showHelp());
        menuItemAbout.addActionListener(e -> showAbout());
        menuItemDarkMode.addActionListener(e -> setDarkMode(menuItemDarkMode.isSelected()));
        menuItemMusic.addActionListener(e -> {
            if (menuItemMusic.isSelected()) {
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
            } else {
                musicPlayer = new BackgroundMusicPlayer("audio/background.wav");
                musicPlayer.start();
            }
        });

        menuHelp.add(menuItemHelp);
        menuHelp.add(menuItemAbout);
        menuHelp.add(menuItemDarkMode);
        menuHelp.add(menuItemMusic);

        menuBar.add(menuExit);
        menuBar.add(menuStats);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    /** Sets up the main card layout container. */
    private void initMainPanels() {
        panelMainContainer.setLayout(cardLayout);
        getContentPane().add(panelMainContainer, BorderLayout.CENTER);
    }

    /** Builds the start screen with theme selector, sliders, and start button. */
    private void initStartScreen() {
        panelStartScreen.setLayout(new BoxLayout(panelStartScreen, BoxLayout.Y_AXIS));
        panelStartScreen.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("C Programming Quiz");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel selectLabel = new JLabel("Select a theme:");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        themeSelector.addItem("ifs loops and operators");
        themeSelector.addItem("strings arrays and pointers");
        themeSelector.addItem("functions structs and memory");
        themeSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeSelector.setMaximumSize(new Dimension(400, 30));

        JLabel sliderLabel = new JLabel("Select number of questions:");
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionSlider = new JSlider(JSlider.HORIZONTAL, 1, 12, 12);
        questionSlider.setMajorTickSpacing(1);
        questionSlider.setPaintTicks(true);
        questionSlider.setPaintLabels(true);
        questionSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionSlider.setMaximumSize(new Dimension(400, 50));

        JLabel sliderValueLabel = new JLabel("Number of questions: 12");
        sliderValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionSlider.addChangeListener(e -> {
            int value = questionSlider.getValue();
            sliderValueLabel.setText("Number of questions: " + value);
        });

        JLabel timeSliderLabel = new JLabel("Select time limit (seconds):");
        timeSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeSlider = new JSlider(JSlider.HORIZONTAL, 30, 600, 300);
        timeSlider.setMajorTickSpacing(90);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);
        timeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeSlider.setMaximumSize(new Dimension(400, 50));

        JLabel timeValueLabel = new JLabel("Time limit: 300 seconds");
        timeValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeSlider.addChangeListener(e -> {
            int value = timeSlider.getValue();
            timeValueLabel.setText("Time limit: " + value + " seconds");
        });

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> startQuiz());

        panelStartScreen.add(title);
        panelStartScreen.add(Box.createVerticalStrut(30));
        panelStartScreen.add(selectLabel);
        panelStartScreen.add(themeSelector);
        panelStartScreen.add(Box.createVerticalStrut(40));
        panelStartScreen.add(sliderLabel);
        panelStartScreen.add(questionSlider);
        panelStartScreen.add(sliderValueLabel);
        panelStartScreen.add(Box.createVerticalStrut(40));
        panelStartScreen.add(timeSliderLabel);
        panelStartScreen.add(timeSlider);
        panelStartScreen.add(timeValueLabel);
        panelStartScreen.add(Box.createVerticalStrut(20));
        panelStartScreen.add(startButton);

        panelMainContainer.add(panelStartScreen, "StartScreen");
    }

    /** Initializes the question container with navigation buttons. */
    private void initQuestionContainer() {
        JPanel bottomPanel = new JPanel();
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");
        progressLabel = new JLabel("Question 1 of X");

        nextButton.addActionListener(e -> goNext());
        submitButton.addActionListener(e -> finishQuiz());

        bottomPanel.add(progressLabel);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);

        panelQuestionContainer.add(bottomPanel, BorderLayout.SOUTH);
        panelMainContainer.add(panelQuestionContainer, "QuestionContainer");
    }

    /** Creates the quiz manager and shows the start screen. */
    private void setupQuizManager() {
        quizManager = new QuizManager(QuestionBank.getAllQuestions());
        cardLayout.show(panelMainContainer, "StartScreen");
    }

    /** Starts playing background music. */
    private void startBackgroundMusic() {
        musicPlayer = new BackgroundMusicPlayer("audio/background.wav");
        musicPlayer.start();
    }

    /** Begins a new quiz with selected theme, question count, and time limit. */
    private void startQuiz() {
        String selectedTheme = (String) themeSelector.getSelectedItem();
        int questionCount = questionSlider.getValue();
        quizManager.startQuizForTheme(selectedTheme, questionCount);
        quizManager.setTimeLimitSeconds(timeSlider.getValue());

        hintsUsed = 0;
        remainingSeconds = quizManager.getTimeLimitSeconds();

        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }

        timerLabel = new JLabel(formatTime(remainingSeconds));
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText(formatTime(remainingSeconds));
            if (remainingSeconds <= 0) {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(this, "Time has run out!", "Time's up", JOptionPane.INFORMATION_MESSAGE);
                finishQuiz();
            }
        });
        countdownTimer.start();

        showCurrentQuestion();
        cardLayout.show(panelMainContainer, "QuestionContainer");
    }

    /** Displays the current question with answer input and navigation. */
    private void showCurrentQuestion() {
        panelQuestionContainer.removeAll();

        Question q = quizManager.getCurrentQuestion();
        currentQuestionPanel = new QuestionPanel(q);
        panelQuestionContainer.add(currentQuestionPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        progressLabel = new JLabel("Question " + (quizManager.getCurrentIndex() + 1) + " of " + quizManager.getQuestionCount());
        hintButton = new JButton("Hint");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        hintButton.addActionListener(e -> showHint());
        nextButton.addActionListener(e -> goNext());
        submitButton.addActionListener(e -> finishQuiz());

        bottomPanel.add(progressLabel);
        bottomPanel.add(hintButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        if (timerLabel != null) {
            bottomPanel.add(timerLabel);
        }

        panelQuestionContainer.add(bottomPanel, BorderLayout.SOUTH);

        panelQuestionContainer.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ENTER"), "pressEnter");
        panelQuestionContainer.getActionMap().put("pressEnter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quizManager.getCurrentIndex() < quizManager.getQuestionCount() - 1) {
                    goNext();
                } else {
                    finishQuiz();
                }
            }
        });

        panelQuestionContainer.revalidate();
        panelQuestionContainer.repaint();
    }

    /** Shows a hint for the current question if available. */
    private void showHint() {
        String hint = currentQuestionPanel.getQuestion().getHint();
        if (hint == null || hint.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hint available for this question.");
        } else {
            JOptionPane.showMessageDialog(this, hint, "Hint", JOptionPane.INFORMATION_MESSAGE);
            hintsUsed++;
        }
    }

    /** Advances to the next question after recording the current answer. */
    private void goNext() {
        if (quizManager.getCurrentIndex() < quizManager.getQuestionCount() - 1) {
            boolean correct = currentQuestionPanel.checkAnswer();
            quizManager.recordAnswer(correct);
            quizManager.storeAnswer(currentQuestionPanel.getUserAnswer());
            quizManager.nextQuestion();
            showCurrentQuestion();
        } else {
            JOptionPane.showMessageDialog(this, "That was the last question. Click Submit to finish.");
        }
    }

    /** Finalizes the quiz, records stats, and shows results. */
    private void finishQuiz() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }

        boolean correct = currentQuestionPanel.checkAnswer();
        quizManager.recordAnswer(correct);
        quizManager.storeAnswer(currentQuestionPanel.getUserAnswer());
        quizManager.finish();

        double percentage = quizManager.getPercentage();
        String theme = (String) themeSelector.getSelectedItem();

        StatsManager.append(
            theme,
            quizManager.getCorrectCount(),
            quizManager.getWrongCount(),
            percentage,
            hintsUsed,
            quizManager.getElapsedSeconds()
        );

        int option = JOptionPane.showOptionDialog(
            this,
            String.format(
                "Quiz finished!\nCorrect: %d\nWrong: %d\nScore: %.2f%%\nTime: %ds",
                quizManager.getCorrectCount(),
                quizManager.getWrongCount(),
                percentage,
                quizManager.getElapsedSeconds()
            ),
            "Quiz Completed",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"View Submitted Answers", "Close"},
            "Close"
        );

        if (option == JOptionPane.YES_OPTION) {
            StringBuilder answersSummary = new StringBuilder("Your submitted answers:\n\n");
            quizManager.getUserAnswers().forEach((question, answer) -> {
                answersSummary.append(question.getPrompt())
                    .append("\nYour answer: ").append(answer)
                    .append("\nCorrect answer: ").append(question.getAnswers())
                    .append("\n\n");
            });
            JOptionPane.showMessageDialog(this, answersSummary.toString(), "Submitted Answers", JOptionPane.INFORMATION_MESSAGE);
        }

        cardLayout.show(panelMainContainer, "StartScreen");
    }

    /** Displays help dialog with usage instructions. */
    private void showHelp() {
        JOptionPane.showMessageDialog(this, """
            Select a theme, number of questions and time limit, then click 'Start Quiz'.
            Answer each question, then press Next.
            At the end, click Submit to finish and record your score.
            Pressing ENTER will trigger Next/Submit automatically.""",
            "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Shows about dialog with app info and credits. */
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "C Programming Quiz App 1.0\n© Tine Štakul\n6. 11. 2025\nmentor: Tomaž Mavri",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Displays quiz history from CSV file in a scrollable dialog. */
    private void showHistory() {
        List<String[]> history = StatsManager.readHistory();
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No history recorded yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Date | Theme | Correct | Wrong | % | Hints Used | Time(s)\n");
        sb.append("-----------------------------------------------------------\n");
        for (String[] row : history) {
            sb.append(String.join(" | ", row)).append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scroll, "Quiz History", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Switches between dark and light themes using FlatLaf.
     * 
     * @param dark true for dark mode, false for light mode
     */
    private void setDarkMode(boolean dark) {
        try {
            if (dark) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats total seconds into MM:SS string.
     * 
     * @param totalSeconds total time in seconds
     * @return formatted time string (e.g., "05:30")
     */
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Application entry point. Sets look and feel and launches the GUI.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}