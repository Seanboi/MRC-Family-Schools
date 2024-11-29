import java.util.ArrayList;
import java.util.List;

public class Gradebook {
    private List<Test> tests;

    public Gradebook() {
        this.tests = new ArrayList<>();
    }

    public void addTest(Test test) {
        tests.add(test);
    }

    public void addStudentScore(String testId, String studentID, double score) {
        for (Test test : tests) {
            if (test.getTestId().equals(testId)) {
                test.addScore(studentID, score);
                return;
            }
        }
        System.out.println("Test with ID " + testId + " not found.");
    }

    public void updateStudentScore(String testId, String studentID, double newScore) {
        for (Test test : tests) {
            if (test.getTestId().equals(testId)) {
                test.updateScore(studentID, newScore);
                return;
            }
        }
        System.out.println("Test with ID " + testId + " not found.");
    }

    public List<Test> getTests() {
        return tests;
    }
}

