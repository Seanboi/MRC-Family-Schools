public class EmergencyContact {
    private String contactName;
    private String relationship;
    private String contactInfo;

    public EmergencyContact(String contactName, String relationship, String contactInfo){
        this.contactName = contactName;
        this.relationship = relationship;
        this.contactInfo = contactInfo;
    }

    public String getcontactName(){
        return contactName;
    }

    public void setContactName(String contactName){
        this.contactName = contactName;
    }

    public String getrelationship(){
        return relationship;
    }

    public void setRelationship(String relationship){
        this.relationship = relationship;
    }

    public String getcontactinfo(){
        return contactInfo;
    }

    public void setContactInfo(String contactInfo){
        this.contactInfo = contactInfo;
    }

    public void updateEmergencyContact(String contactName, String relationship, String contactInfo){
        this.contactName = contactName;
        this.relationship = relationship;
        this.contactInfo = contactInfo;
        System.out.println("Emergency contact information has been successfully updated.");
    }
    
    @Override
    public String toString() {
        return "Name: " + contactName + ", Relationship: " + relationship + ", Contact Info: [" + contactInfo + "]";
    }

}