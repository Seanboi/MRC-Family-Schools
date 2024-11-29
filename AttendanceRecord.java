import java.util.Date;
import java.util.ArrayList;

public class AttendanceRecord {
  private String                  status;
  private Date                    date;
  private ArrayList<studentinfo>  students;
  private ArrayList<ClassInfo>    classList;
  ArrayList<AttendanceRecord>     aRecord;

  public AttendanceRecord(String studentID, String status, Date date){
    this.status = status;
    this.date = date;
  }

  public String getStatus(){
    return status;
  }

  public void setStatus(String status){
    this.status = status;
  }

  public Date getDate(){
    return date;
  }

  public ArrayList<studentinfo> getstudents(){
    return students;
  }

  public ArrayList<ClassInfo> getClassList(){
    return classList;
  }

  private studentinfo studentIDSearch(String studentID){
    for (studentinfo s:students){
      if (s.getstudentID().equals(studentID)){
        return s;
      }
    }
      return null;
  }

  private ClassInfo classListSearch(String classID){
    for (ClassInfo c:classList){
      if (c.getClassID().equals(classID)){
        return c;
      }
    }
      return null;
  }
  

  public void markAttendance(String studentID, Date date, String status){
    ArrayList<AttendanceRecord> aRecord = new ArrayList<AttendanceRecord>();
  }
}
