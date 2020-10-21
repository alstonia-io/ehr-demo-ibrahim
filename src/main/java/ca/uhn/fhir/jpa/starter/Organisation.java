package ca.uhn.fhir.jpa.starter;

public class Organisation {
  private String name;
  private String website;
  private String phone;

  public String getName(){
    return name;
  }

  public String getWebsite(){
    return website;
  }

  public String getPhone(){
    return phone;
  }

  public void setName(String name){
    this.name = name;
  }

  public void setWebsite(String website){
    this.website = website;
  }

  public void setPhone(String phone){ this.phone = phone; }

}
