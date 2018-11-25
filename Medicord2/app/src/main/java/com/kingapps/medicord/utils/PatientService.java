package com.kingapps.medicord.utils;

import retrofit2.Retrofit;

public class PatientService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://hapi.fhir.org/baseDstu3/")
            .build();

    PatientInterface service = retrofit.create(PatientInterface.class);
}
