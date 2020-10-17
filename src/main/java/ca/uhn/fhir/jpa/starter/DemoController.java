package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.model.dstu3.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.to.model.HomeRequest;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
public class DemoController {

  FhirContext ctx = FhirContext.forDstu3();
  String serverBase = "http://localhost:8080/fhir/";
  IGenericClient client = ctx.newRestfulGenericClient(serverBase);

  @RequestMapping(value = {"/demo"}, method = RequestMethod.GET)
  public String getAllPatients(final HttpServletRequest theReq, final HomeRequest theRequest,
                               final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Patient.class).returnBundle(Bundle.class).prettyPrint().execute();
    List<Patient> patients = new ArrayList<>();
    for(Bundle.BundleEntryComponent entry: bundle.getEntry()){
      Patient patient =  (Patient)entry.getResource();
      patients.add(patient);
      patient.getId();
    }
    theModel.addAttribute("patientBundle", patients);
    return "demo";
  }

  @RequestMapping(value = {"/patient/{id}/observations"}, method = RequestMethod.GET)
  public String getObservations(@PathVariable("id") String patientId, final HttpServletRequest theReq, final HomeRequest theRequest,
                               final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(Observation.class).where(new ReferenceClientParam("patient").hasId(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<Observation> observations = new ArrayList<>();
    for(Bundle.BundleEntryComponent entry: bundle.getEntry()){
      Observation observation =  (Observation) entry.getResource();
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
    for(Bundle.BundleEntryComponent entry: bundle.getEntry()){
      Condition condition =  (Condition) entry.getResource();
      conditions.add(condition);
      condition.getId();
    }
    theModel.addAttribute("conditionBundle", conditions);
    return "condition";
  }

  @RequestMapping(value = {"/patient/{id}/diagnosticReport"}, method = RequestMethod.GET)
  public String getDiagnosticReport(@PathVariable("id") String patientId, final HttpServletRequest theReq, final HomeRequest theRequest,
                                final BindingResult theBindingResult, final ModelMap theModel) {
    Bundle bundle = client.search().forResource(DiagnosticReport.class).where(new ReferenceClientParam("patient").hasId(patientId)).
      returnBundle(Bundle.class).prettyPrint().execute();
    List<DiagnosticReport> diagnosticReports = new ArrayList<>();
    for(Bundle.BundleEntryComponent entry: bundle.getEntry()){
      DiagnosticReport diagnosticReport =  (DiagnosticReport) entry.getResource();
      diagnosticReports.add(diagnosticReport);
      diagnosticReport.getId();
    }
    theModel.addAttribute("diagnosticReportBundle", diagnosticReports);
    return "diagnosticReport";
  }
}
