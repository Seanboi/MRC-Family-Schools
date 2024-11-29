import java.util.ArrayList;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;



public class studentinfo {
  private static final HashSet<String> usedIDs = new HashSet<>();
  private String studentID;
  private String fname;
  private String lname;
  private String dob;
  private int age; 
  private String currentGrade; 
  private String currentSchool; 
  private contactinfo contactinfo;
  private guardianinfo guardianinfo;
  private ArrayList<studentinfo> students = new ArrayList<>();
    
    
      public studentinfo(String studentID, String fname, String lname, String dob, String currentGrade, String currentSchool, contactinfo contactinfo, guardianinfo guardianinfo){
        this.studentID = studentID;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.age = calculateAge(dob);
        this.currentGrade = currentGrade;
        this.currentSchool = currentSchool;
        this.contactinfo = contactinfo;
        this.guardianinfo = guardianinfo;
      }
    
      private int calculateAge(String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate birthDate = LocalDate.parse(dob, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
      }
    
      public int getAge() {
          return age;
      }
    
      public String getstudentID(){
        return studentID;
      }
      
      public String getfirstname(){
        return fname;
      }
      
      public void setfirstname(String fname){
        this.fname = fname;
      }
    
      public String getlastname(){
        return lname;
      }
    
      public void setlastname(String lname){
        this.lname = lname;
      }
    
      public String getDob(){
        return dob;
      }
    
      public void setDob(String dob){
        this.dob = dob;
        this.age = calculateAge(dob);
      }
      public String getCurrentGrade() {
        return currentGrade;
      }
    
      public void setCurrentGrade(String currentGrade) {
          this.currentGrade = currentGrade;
      }
    
      public String getCurrentSchool() {
          return currentSchool;
      }
    
      public void setCurrentSchool(String currentSchool) {
        this.currentSchool = currentSchool;
      }
    
      public contactinfo getcontactinfo(){
        return contactinfo;
      }
    
      public void setcontactinfo(contactinfo contactinfo){
        this.contactinfo = contactinfo;
      }
    
      public guardianinfo getguardianinfo(){
        return guardianinfo;
      }
    
      public void setguardianinfo(guardianinfo guardianinfo){
        this.guardianinfo = guardianinfo;
      }
    
      public void updateStudent(String fname, String lname, String dob, String currentGrade, String currentSchool, contactinfo contactinfo, guardianinfo guardianinfo) {
        if (fname != null && !fname.isEmpty()) {
            this.fname = fname;
        }
        if (lname != null && !lname.isEmpty()) {
            this.lname = lname;
        }
        if (dob != null && !dob.isEmpty()) {
            this.dob = dob;
            this.age = calculateAge(dob); 
        }
        if (currentGrade != null && !currentGrade.isEmpty()) {
            this.currentGrade = currentGrade;
        }
        if (currentSchool != null && !currentSchool.isEmpty()) {
            this.currentSchool = currentSchool;
        }
        if (contactinfo != null) {
            this.contactinfo = contactinfo;
        }
        if (guardianinfo != null) {
            this.guardianinfo = guardianinfo;
        }
      }
    
        public String toCSV() {
           return studentID + "," + fname + "," + lname + "," + dob + "," + age + "," + currentGrade + "," + currentSchool + "," +
           (contactinfo != null ? contactinfo.toCSV() : "") + "," +
           (guardianinfo != null ? guardianinfo.toCSV() : "");
        }
    
        public static studentinfo fromCSV(String csv) {
          String[] parts = csv.split(",");
          if (parts.length < 15) {
              System.err.println("Invalid student CSV: " + csv);
              return null;
          }
  
          contactinfo contact = new contactinfo(
              parts[7].trim(),
              parts[8].trim(),
              parts[9].trim()
          );
  
          guardianinfo guardian = new guardianinfo(
              parts[10].trim(),
              parts[11].trim(),
              parts[12].trim(),
              parts[13].trim(),
              parts[14].trim()
          );
  
          return new studentinfo(
              parts[0].trim(),  
              parts[1].trim(),  
              parts[2].trim(),  
              parts[3].trim(),  
              parts[5].trim(),  
              parts[6].trim(),  
              contact,
              guardian
          );
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        studentinfo other = (studentinfo) obj;
        return studentID != null && studentID.equals(other.studentID);
      }

      public String[] split(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'split'");
      }

      
}
