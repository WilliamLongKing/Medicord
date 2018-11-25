package com.kingapps.medicord.utils;

import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PatientInterface {
    @GET("Patient")
    Call<Bundle> getPatient();

    @GET("Patient?_pretty=true")
    Call<Bundle> getPatients();
}
