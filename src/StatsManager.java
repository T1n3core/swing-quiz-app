import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handles persistence of quiz results to a CSV file in the user's home directory.
 * 
 * @author GasTheJuice
 */
public class StatsManager {
    private static final String STATS_FILE_NAME = System.getProperty("user.home") + File.separator + "quizapp_stats.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Appends a quiz result to the stats file.
     * 
     * @param theme quiz theme
     * @param correct number correct
     * @param wrong number wrong
     * @param percentage score percentage
     * @param hintsUsed number of hints used
     * @param timeElapsedSeconds total time taken
     */
    public static void append(String theme, int correct, int wrong, double percentage, int hintsUsed, long timeElapsedSeconds) {
        String line = String.join(",",
            LocalDateTime.now().format(formatter),
            escapeCsv(theme),
            String.valueOf(correct),
            String.valueOf(wrong),
            String.format(Locale.US, "%.2f", percentage),
            String.valueOf(hintsUsed),
            String.valueOf(timeElapsedSeconds)
        );

        try {
            Files.write(Paths.get(STATS_FILE_NAME),
                (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escapes a string for safe CSV inclusion.
     * 
     * @param input raw string
     * @return CSV-safe string
     */
    public static String escapeCsv(String input) {
        if (input == null) return "";
        if (input.contains(",") || input.contains("\"") || input.contains("\n")) {
            input = input.replace("\"", "\"\"");
            return "\"" + input + "\"";
        }
        return input;
    }

    /**
     * Reads all recorded quiz attempts.
     * 
     * @return list of string arrays, each representing a row
     */
    public static List<String[]> readHistory() {
        List<String[]> rows = new ArrayList<>();
        Path path = Paths.get(STATS_FILE_NAME);
        if (!Files.exists(path)) {
            return rows;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                rows.add(cols);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }
}