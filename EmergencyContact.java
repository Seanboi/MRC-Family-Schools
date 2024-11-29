public class EmergencyContact {
    private String contactName;
    private String relationship;
    private String contactinfo;
    private String email;

    public EmergencyContact(String contactName, String relationship, String contactinfo, String email) {
        this.contactName = contactName;
        this.relationship = relationship;
        this.contactinfo = contactinfo;
        this.email = email;
    }

    // Getters and setters
    public String getcontactName() {
        return contactName;
    }

    public void setcontactName(String contactName) {
        this.contactName = contactName;
    }

    public String getrelationship() {
        return relationship;
    }

    public void setrelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getcontactinfo() {
        return contactinfo;
    }

    public void setcontactinfo(String contactinfo) {
        this.contactinfo = contactinfo;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }
}
