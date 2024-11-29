import java.util.HashMap;
import java.util.Map;

public class Test {
    private String testId;
    private String subject;
    private String classId;
    private Map<String, Double> scores;

    public Test(String testId, String subject, String classId) {
        this.testId = testId;
        this.subject = subject;
        this.classId = classId;
        this.scores = new HashMap<>();
    }

    public String getTestId() {
        return testId;
    }

    public String getSubject() {
        return subject;
    }

    public String getClassId() {
        return classId;
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public void addScore(String studentID, double score) {
        scores.put(studentID, score);
    }

    public void updateScore(String studentID, double newScore) {
        if (scores.containsKey(studentID)) {
            scores.put(studentID, newScore);
        } else {
            System.out.println("No existing score found for student ID: " + studentID);
        }
    }
}
