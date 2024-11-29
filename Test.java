import java.io.*;
import java.util.*;

public class Test {
    private String testID;
    private String testName;
    private String testDate;
    private String classID;
    private String subject;

    // Constructor
    public Test(String testID, String testName, String testDate, String classID, String subject) {
        this.testID = testID;
        this.testName = testName;
        this.testDate = testDate;
        this.classID = classID;
        this.subject = subject;
    }

    public static String generateUniqueTestID() {
        String id;
        do {
            id = "T" + (1000 + (int) (Math.random() * 9000));
        } while (isIDUsedInFile(id));
        return id;
    }
    
    private static boolean isIDUsedInFile(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Tests.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading test file: " + e.getMessage());
        }
        return false;
    }

    public void saveTest() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Tests.txt", true))) {
            writer.write(String.join(",", testID, testName, testDate, classID, subject));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving test: " + e.getMessage());
        }
    }

    public void addToGradebook() {
        String gradebookFile = classID + "_gradebook.txt";
        File file = new File(gradebookFile);
        List<String> lines = new ArrayList<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading gradebook: " + e.getMessage());
                return;
            }
        }

        if (lines.isEmpty()) {
            lines.add("Student ID,Student Name,Average");
        }

        String header = lines.get(0);
        String testColumn = testID + " (" + testName + " - " + testDate + ")";

        if (!header.contains(testID)) {
            header += "," + testColumn;
            lines.set(0, header);

            for (int i = 1; i < lines.size(); i++) {
                lines.set(i, lines.get(i) + ",");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating gradebook: " + e.getMessage());
        }
    }

    public static void updateTest(String testID, String newTestName, String newTestDate, String classID) {
        List<String> testLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Tests.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(testID)) {
                    parts[1] = newTestName;
                    parts[2] = newTestDate;
                    testLines.add(String.join(",", parts));
                    found = true;
                } else {
                    testLines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading test file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.err.println("Test ID not found: " + testID);
            return;
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Tests.txt"))) {
            for (String line : testLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing test file: " + e.getMessage());
            return;
        }

        updateGradebookTest(testID, newTestName, newTestDate, classID);
    }

    private static void updateGradebookTest(String testID, String newTestName, String newTestDate, String classID) {
        String gradebookFile = classID + "_gradebook.txt";
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(gradebookFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(testID)) {
                    line = line.replaceAll(
                        testID + "\\s*\\(.*?\\)",
                        testID + " (" + newTestName + " - " + newTestDate + ")"
                    );
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading gradebook: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gradebookFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating gradebook: " + e.getMessage());
        }
    }

    public static List<Test> loadTestsForClass(String classID) {
        List<Test> tests = new ArrayList<>();
        File testFile = new File("Tests.txt");

        if (testFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5 && parts[3].equals(classID)) {
                        tests.add(new Test(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading tests: " + e.getMessage());
            }
        }

        return tests;
    }

    public String getTestID() { return testID; }
    public String getTestName() { return testName; }
    public String getTestDate() { return testDate; }
    public String getClassID() { return classID; }
    public String getSubject() { return subject; }

    public void setTestName(String testName) { this.testName = testName; }
    public void setTestDate(String testDate) { this.testDate = testDate; }
    public void setClassID(String classID) { this.classID = classID; }
    public void setSubject(String subject) { this.subject = subject; }
}