import java.util.HashSet;
import java.util.Set;

public class UserAuthentication {
    private String staffID;
    private String password;
    private static Set<String> activeSessions = new HashSet<>(); // Tracks logged-in users

    // Constructor
    public UserAuthentication(String staffID, String password) {
        this.staffID = staffID;
        this.password = password;
    }

    public String getstaffID(){
        return staffID;
    }

    public String getPassword(){
        return password;
    }

    // Authenticate method
    public boolean authenticate() {
        // Check if the staffID and password match the database
        String storedPassword = UserLogin.getUserDatabase().get(staffID);
        if (storedPassword != null && storedPassword.equals(password)) {
            activeSessions.add(staffID); // Mark user as logged in
            return true;
        }
        return false;
    }

    // Check if a user is logged in
    public static boolean isLoggedIn(String staffID) {
        return activeSessions.contains(staffID);
    }

    // Terminate session
    public static void terminateSession(String staffID) {
        activeSessions.remove(staffID);
    }
}
