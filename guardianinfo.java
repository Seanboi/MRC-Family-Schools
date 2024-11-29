public class guardianinfo {
  private String guardianfName;
  private String guardianlName;
  private String relation;
  private String telnum;
  private String email;

  public guardianinfo(String guardianfName, String guardianlName, String relation, String telnum, String email) {
      this.guardianfName = guardianfName;
      this.guardianlName = guardianlName;
      this.relation = relation;
      this.telnum = telnum;
      this.email = email;
  }

  public String getguardianfName() {
      return guardianfName;
  }

  public void setguardianfName(String guardianfName) {
      this.guardianfName = guardianfName;
  }

  public String getguardianlName() {
      return guardianlName;
  }

  public void setguardianlName(String guardianlName) {
      this.guardianlName = guardianlName;
  }

  public String getrelation() {
      return relation;
  }

  public void setrelation(String relation) {
      this.relation = relation;
  }

  public String gettelnum() {
      return telnum;
  }

  public void settelnum(String telnum) {
      this.telnum = telnum;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public void updateguardianinfo(String guardianfName, String guardianlName, String relation, String telnum, String email) {
      if (guardianfName != null) {
          this.guardianfName = guardianfName;
      }
      if (guardianlName != null) {
          this.guardianlName = guardianlName;
      }
      if (relation != null) {
          this.relation = relation;
      }
      if (telnum != null) {
          this.telnum = telnum;
      }
      if (email != null) {
          this.email = email;
      }
  }

  public String toCSV() {
      return guardianfName + "," + guardianlName + "," + relation + "," + telnum + "," + email;
  }

  public static guardianinfo fromCSV(String csv) {
      String[] parts = csv.split(",");
      if (parts.length < 5) return null;
      return new guardianinfo(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
  }
}