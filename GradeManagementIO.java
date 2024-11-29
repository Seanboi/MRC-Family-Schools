import java.io.*;
import java.util.logging.*;

public class GradeManagementIO {
    private static final Logger logger = Logger.getLogger(GradeManagementIO.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("grade_log.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console output
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    /**
     * Logs action with a description.
     *
     * @param actionDescription A string describing the action to log.
     */
    public static void logAction(String actionDescription) {
        logger.info(actionDescription);
    }

    /**
     * Saves grade data to a file for persistence.
     *
     * @param gradeBook GradeBook object containing test data.
     * @param filename  file to save the data.
     */
    public static void saveData(Gradebook gradeBook, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gradeBook);
            logAction("GradeBook data saved to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Loads grade data from a file to initialize GradeBook.
     *
     * @param filename file to load the data from.
     * @return GradeBook object loaded from the file.
     */
    public static Gradebook loadData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Gradebook gradeBook = (Gradebook) ois.readObject();
            logAction("GradeBook data loaded from file: " + filename);
            return gradeBook;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new Gradebook();
        }
    }
}
