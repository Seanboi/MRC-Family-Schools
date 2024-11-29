import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterInfo {
    private static final String REGISTER_INFO_FILE = "registerinfo.txt";

    public static void addRegister(String classID, String registerFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REGISTER_INFO_FILE, true))) {
            writer.write(classID + "," + registerFile);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error adding register info: " + e.getMessage());
        }
    }

    
    public static String getRegisterFile(String classID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(REGISTER_INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(classID)) {
                    return parts[1];
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving register file: " + e.getMessage());
        }
        return null;
    }

    
    public static void removeRegister(String classID) {
        List<String> updatedEntries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(REGISTER_INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[0].equals(classID)) {
                    updatedEntries.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading register info: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REGISTER_INFO_FILE))) {
            for (String entry : updatedEntries) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating register info: " + e.getMessage());
        }
    }

    
    public static List<String[]> getAllRegisters() {
        List<String[]> registers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REGISTER_INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                registers.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading register info: " + e.getMessage());
        }
        return registers;
    }
}

