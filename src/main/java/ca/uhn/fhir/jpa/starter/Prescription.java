package ca.uhn.fhir.jpa.starter;

public class Prescription {
  private String code;
  private String patientId;
  private String system;
  private String display;

//  Prescription(String code, String patientId, String system, String display){
//    this.code = code;
//    this.patientId = patientId;
//    this.system = system;
//    this.display = display;
//  }

  public String getCode(){
    return code;
  }

  public String getPatientId(){
    return patientId;
  }

  public String getSystem(){
    return system;
  }

  public String getDisplay(){
    return display;
  }

  public void setCode(String code){
     this.code = code;
  }

  public void setPatientId(String patientId){
     this.patientId = patientId;
  }

  public void setSystem(String system){
     this.system = system;
  }

  public void setDisplay(String display){
     this.display = display;
  }
}