public class contactinfo {
  private String telnum;
  private String email;
  private String address;

  public contactinfo(String telnum, String email, String address) {
      this.telnum = telnum;
      this.email = email;
      this.address = address;
  }

  public String gettelnum() {
      return telnum;
  }

  public void setTelnum(String telnum) {
      this.telnum = telnum;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public String getaddress() {
      return address;
  }

  public void setaddress(String address) {
      this.address = address;
  }

  public void updateContactinfo(String telnum, String email, String address) {
      if (telnum != null && !telnum.isEmpty()) this.telnum = telnum;
      if (email != null && !email.isEmpty()) this.email = email;
      if (address != null && !address.isEmpty()) this.address = address;
  }

  public String toCSV() {
      return telnum + "," + email + "," + address;
  }

  public static contactinfo fromCSV(String csv) {
    String[] parts = csv.split(",");
    if (parts.length < 3) {
        System.err.println("Invalid contact CSV: " + csv);
        return null;
    }
    return new contactinfo(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }

}

