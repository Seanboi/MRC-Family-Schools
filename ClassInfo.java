import java.io.*;
import java.util.ArrayList;

public class ClassInfo {
    private String classID;
    private String gradeLevel;
    private String subject;
    private String examType;
    private String teacher;
    private ArrayList<String> students;

    // Constructor
    public ClassInfo(String classID, String gradeLevel, String subject, String examType) {
        this.classID = classID;
        this.gradeLevel = gradeLevel;
        this.subject = subject;
        this.examType = examType;
        this.teacher = ""; // Initially, no teacher is assigned
        this.students = new ArrayList<>();
    }

    // Getters and Setters
    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }
    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public ArrayList<String> getStudents() { return students; }
    public void setStudents(ArrayList<String> students) { this.students = students; }

    // Add and Remove Students
    public void addStudent(String student) {
        if (!students.contains(student)) students.add(student);
    }
    public void removeStudent(String student) {
        students.remove(student);
    }

    // Generate Class ID
    public static String generateClassID(String subject, String gradeLevel, String examType) {
        String subjectCode = subject.substring(0, Math.min(4, subject.length())).toUpperCase(); // First 4 letters of the subject
        String randomDigits = String.format("%02d", (int) (Math.random() * 100));
        String examAbbreviation = getExamTypeAbbreviation(examType);
        return subjectCode + gradeLevel + randomDigits + examAbbreviation;
    }

    private static String getExamTypeAbbreviation(String examType) {
        switch (examType.toUpperCase()) {
            case "CSEC": return "SE";
            case "CAPE": return "PE";
            case "GCSE": return "GE";
            default: return "";
        }
    }

    // Save class details to a file
    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(classID + ".txt"))) {
            writer.write("Grade Level: " + gradeLevel);
            writer.newLine();
            writer.write("Subject: " + subject);
            writer.newLine();
            writer.write("Exam Type: " + examType);
            writer.newLine();
            writer.write("Teacher: " + teacher);
            writer.newLine();
            writer.write("Students:");
            writer.newLine();
            for (String student : students) {
                writer.write("- " + student);
                writer.newLine();
            }
        }
    }

    // Load class details from a file
    public static ClassInfo loadFromFile(String classID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(classID + ".txt"))) {
            String gradeLevel = null;
            String subject = null;
            String examType = null;
            String teacher = null;
            ArrayList<String> students = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Grade Level: ")) gradeLevel = line.split(": ", 2)[1].trim();
                else if (line.startsWith("Subject: ")) subject = line.split(": ", 2)[1].trim();
                else if (line.startsWith("Exam Type: ")) examType = line.split(": ", 2)[1].trim();
                else if (line.startsWith("Teacher: ")) teacher = line.split(": ", 2)[1].trim();
                else if (line.startsWith("- ")) students.add(line.substring(2).trim());
            }

            if (gradeLevel != null && subject != null && examType != null) {
                ClassInfo classInfo = new ClassInfo(classID, gradeLevel, subject, examType);
                classInfo.setTeacher(teacher != null ? teacher : "");
                classInfo.setStudents(students);
                return classInfo;
            } else {
                System.err.println("Error: Missing essential class details in file.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading class from file: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "classID='" + classID + '\'' +
                ", gradeLevel='" + gradeLevel + '\'' +
                ", subject='" + subject + '\'' +
                ", examType='" + examType + '\'' +
                ", teacher='" + teacher + '\'' +
                ", students=" + students +
                '}';
    }
}
