package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.to.model.HomeRequest;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Organization;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Controller
public class DemoController {

  FhirContext ctx = FhirContext.forDstu3();
  String serverBase = "http://localhost:8080/fhir/";
  IGenericClient client = ctx.newRestfulGenericClient(serverBase);

  @RequestMapping(value = {"/demo"}, method = RequestMethod.GET)
  public String showDemoView(final HttpServletRequest theReq, final HomeRequest theRequest) {
    return "demo";
  }

  @RequestMapping(value = {"/doctor-view"}, method = RequestMethod.GET)
  public String showDoctorPanel(final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Patient.class).returnBundle(Bundle.class).prettyPrint().execute();
    List<Patient> patients = new ArrayList<>();
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      Patient patient = (Patient) entry.getResource();
      patients.add(patient);
      patient.getId();
    }
    theModel.addAttribute("patientBundle", patients);
    return "doctor-panel";
  }

  @RequestMapping(value = {"/patient-view"}, method = RequestMethod.GET)
  public String showPatientPanel(final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    String patientId = theReq.getParameter("patientId");
    Bundle bundle = client.search().forResource(Patient.class).where(new TokenClientParam("_id").exactly().code(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<Patient> patients = new ArrayList<>();
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      Patient patient = (Patient) entry.getResource();
      patients.add(patient);
      patient.getId();
      break;
    }
    theModel.addAttribute("patientBundle", patients);
    return "patient-panel";
  }

  @RequestMapping(value = {"/patient-login"}, method = RequestMethod.GET)
  public String showPatientLoginView(final HttpServletRequest theReq, final HomeRequest theRequest,
                                 final BindingResult theBindingResult, final ModelMap theModel) {
    return "patient-view";
  }

  @RequestMapping(value = {"/saas-admin"}, method = RequestMethod.GET)
  public String showAdminPanel(final HttpServletRequest theReq, final HomeRequest theRequest,
                                 final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Organization.class).returnBundle(Bundle.class).prettyPrint().execute();
    List<Organization> organizations = new ArrayList<>();
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {

      Organization organization = (Organization) entry.getResource();
      if(organization.getPartOf().getReference() == null) {
        organizations.add(organization);
      }
    }
    theModel.addAttribute("organizationBundle", organizations);
    return "saas-admin";
  }

  @RequestMapping(value = {"/patient/{id}/observations"}, method = RequestMethod.GET)
  public String getObservations(@PathVariable("id") String patientId, final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Observation.class).where(new ReferenceClientParam("patient").hasId(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<Observation> observations = new ArrayList<>();
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      Observation observation = (Observation) entry.getResource();
      observations.add(observation);
      observation.getId();
    }
    theModel.addAttribute("observationBundle", observations);
    return "observation";
  }

  @RequestMapping(value = {"/patient/{id}/conditions"}, method = RequestMethod.GET)
  public String getConditions(@PathVariable("id") String patientId, final HttpServletRequest theReq, final HomeRequest theRequest,
                              final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Condition.class).where(new ReferenceClientParam("patient").hasId(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<Condition> conditions = new ArrayList<>();
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      Condition condition = (Condition) entry.getResource();
      conditions.add(condition);
      condition.getId();
    }
    theModel.addAttribute("conditionBundle", conditions);
    return "condition";
  }

  @RequestMapping(value = {"/patient/{id}/diagnosticReports"}, method = RequestMethod.GET)
  public String getDiagnosticReport(@PathVariable("id") String patientId, final HttpServletRequest theReq, final HomeRequest theRequest,
                                    final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(DiagnosticReport.class).where(new ReferenceClientParam("patient").hasId(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<DiagnosticReport> diagnosticReports = new ArrayList<>();
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      DiagnosticReport diagnosticReport = (DiagnosticReport) entry.getResource();
      diagnosticReports.add(diagnosticReport);
    }
    theModel.addAttribute("diagnosticReportBundle", diagnosticReports);
    return "diagnosticReport";
  }

  @RequestMapping(value = {"/patient/{id}/showPrescription"}, method = RequestMethod.GET)
  public String showPrescriptionForm(@PathVariable("id") String patientId,final HttpServletRequest theReq,
                                     final HomeRequest theRequest, final ModelMap theModel) {
    Prescription prescription = new Prescription();
    theModel.addAttribute("prescription", prescription);
    theModel.addAttribute("PatientId", patientId);
    return "create-prescription";
  }

  @RequestMapping(value = {"/showOrganization"}, method = RequestMethod.GET)
  public String showOrganizationForm(final HttpServletRequest theReq,
                                     final HomeRequest theRequest, final ModelMap theModel) {
    Organisation organisation = new Organisation();
    theModel.addAttribute("organisation", organisation);
    return "add-organization";
  }

  @RequestMapping(value = {"/organisation/{orgid}/createDepartment"}, method = RequestMethod.GET)
  public String showDepartmentCreateForm(@PathVariable("orgid") String orgid, final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    Organisation organisation = new Organisation();
    theModel.addAttribute("organisation", organisation);
    theModel.addAttribute("parentorg", orgid);
    return "add-department";
  }

  @RequestMapping(value = {"/organisation/{orgid}/createDepartment"}, method = RequestMethod.POST)
  public String createDepartment(@PathVariable("orgid") String orgid, final HttpServletRequest theReq,
                                 final HomeRequest theRequest, final BindingResult theBindingResult,
                                 final ModelMap theModel, @ModelAttribute("organization") Organisation organisation) {
      Reference ref = new Reference();
      ref.setReference("Organization/" + orgid);
      Organization org = new Organization();
      org.setName(organisation.getName());
      ContactPoint contact = new ContactPoint();
      contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
      contact.setValue(organisation.getPhone());
      List<ContactPoint> contactPoints = new ArrayList<>();
      contactPoints.add(contact);
      org.setTelecom(contactPoints);
      Identifier iden = new Identifier();
      iden.setUse(Identifier.IdentifierUse.OFFICIAL);
      iden.setSystem(organisation.getWebsite());
      List<Identifier> idenList = new ArrayList<>();
      idenList.add(iden);
      org.setIdentifier(idenList);
      org.setPartOf(ref);
      client.create().resource(org).execute();
      theModel.addAttribute("successMsg", "Organization Created Successfully. ");
      return "redirect:/organisation/" + orgid + "/departments";
  }

  @RequestMapping(value = {"/organisation/{orgid}/departments"}, method = RequestMethod.GET)
  public String showDepartments(@PathVariable("orgid") String orgid, final HttpServletRequest theReq, final HomeRequest theRequest,
                                         final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Organization.class).where(new ReferenceClientParam("partof").hasId(orgid)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<Organization> departments = new ArrayList<>();

    for(Bundle.BundleEntryComponent entry : bundle.getEntry()){
      departments.add((Organization) entry.getResource());
    }
    theModel.addAttribute("departments", departments);
    return "show-department";
  }

  @RequestMapping(value = {"/createPrescription"})
  public String createPrescription(final HttpServletRequest theReq, final HomeRequest theRequest,
                                    final BindingResult theBindingResult, final ModelMap theModel,
                                 @ModelAttribute("prescription") Prescription prescription) {
    Observation obs  = new Observation();
    Coding coding = new Coding();
    coding.setCode(prescription.getCode());
    coding.setDisplay(prescription.getDisplay());
    coding.setSystem(prescription.getSystem());
    Reference ref = new Reference("Patient/"+prescription.getPatientId());
    List<Coding> codings = new ArrayList<>();
    codings.add(coding);
    CodeableConcept codeableConcept = new CodeableConcept();
    codeableConcept.setCoding(codings);
    obs.setCode(codeableConcept);
    obs.setStatus(Observation.ObservationStatus.FINAL);
    obs.setSubject(ref);
    Narrative narrative = new Narrative();
    narrative.setDivAsString("<div>" + prescription.getDisplay() + "</div>");
    obs.setText(narrative);
    obs.setEffective(new DateType(new Date()));
    obs.setEffective(new DateTimeType(
      new Date()
    ));
    client.create().resource(obs).execute();
    theModel.addAttribute("successMsg", "Patient Observation Created Successfully. ");
    return "create-prescription-success";
  }

  @RequestMapping(
    value = {"/addOrganization"}
  )
  public String addOrganization(final HttpServletRequest theReq, final HomeRequest theRequest,
                                   final BindingResult theBindingResult, final ModelMap theModel,
                                   @ModelAttribute("organisation") Organisation organisation) {
    Organization org = new Organization();
    org.setName(organisation.getName());
    ContactPoint contact = new ContactPoint();
    contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
    contact.setValue(organisation.getPhone());
    List<ContactPoint> contactPoints = new ArrayList<>();
    contactPoints.add(contact);
    org.setTelecom(contactPoints);
    Identifier iden = new Identifier();
    iden.setUse(Identifier.IdentifierUse.OFFICIAL);
    iden.setSystem(organisation.getWebsite());
    List<Identifier> idenList = new ArrayList<>();
    idenList.add(iden);
    org.setIdentifier(idenList);
    client.create().resource(org).execute();
    theModel.addAttribute("successMsg", "Organization Created Successfully. ");
    return "add-organization-success";
  }

  @RequestMapping(value = {"/patient/{id}/"}, method = RequestMethod.GET)
  public String showPatientView(@PathVariable("id") String patientId, final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Patient.class).where(new TokenClientParam("_id").exactly().code(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    Patient patient = null;
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      patient = (Patient) entry.getResource();
    }
    theModel.addAttribute("Patient", patient);
    return "patient-profile";
  }

  @RequestMapping(value = {"/media-viewer/{id}"}, method = RequestMethod.GET)
  public String showMediaViewerView(@PathVariable("id") String mediaId, final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Media.class).where(new TokenClientParam("_id").exactly().code(mediaId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    Media media = null;
    for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
      media = (Media) entry.getResource();
    }
    theModel.addAttribute("media", media);

    return "media-viewer";
  }
}
