package com.kingapps.medicord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kingapps.medicord.utils.FhirConverterFactory;
import com.kingapps.medicord.utils.PatientInterface;

import java.util.ArrayList;
import java.util.Random;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.fonts.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class fileActivity extends AppCompatActivity {
    private static final String TAG = "fileActivity";
    public Patient patient;
    public String nameList[] = {
            "Alissa Esquivel", "Marek Fuller", "Tehya Potter", "Arslan Rubio", "Cadi Fox",
            "Dominic Austin", "Frank Everett", "Humphrey Wainwright", "Roan Woodley", "Naeem Hendrix"
    };
    public String genders[] = {"male", "female", "unknown"};
    public String DOB[] =
            {"1931-04-02",
             "1937-05-28",
             "1944-06-10",
             "1949-10-04",
             "1990-11-13",
             "1991-03-07",
             "2002-06-29",
             "2007-12-16",
             "2014-07-29",
             "2014-09-22"};
    public String careProviders[] =
            {"Kirstin Berg",
                    "Walid Knox",
                    "Jolyon Oneal",
                    "Zayd Bernard",
                    "Carmen Wicks",
                    "Elisha Cole",
                    "Claudia Draper",
                    "Theon Silva",
                    "Archie Rooney",
                    "Hermione Dodson"};
    public String deceased[] = {"true", "false"};
    public String maritalStatus[] = {"married", "single"};
    public String phoneNumber[] = {"613-555-0198","807-555-0136", "416-555-0180", "418-659-5836",
    "250-228-3802", "819-501-1170", "450-774-3661", "587-521-2266", "306-597-6400", "709-548-8624"};

    //vars
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Log.d(TAG, "onCreate: started.");

        Button exportButton = findViewById(R.id.exportButton);
//        exportButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    makePDF();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        testNetwork();
        initData();
    }

    public void testNetwork() {
        FhirContext ctxDstu2 = FhirContext.forDstu2();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hapi.fhir.org/baseDstu2/")
                .addConverterFactory(FhirConverterFactory.create(ctxDstu2))
                .client(getOkHttpClient())
                .build();

        PatientInterface service = retrofit.create(PatientInterface.class);

        //Make a call to the website
        Call<ca.uhn.fhir.model.dstu2.resource.Bundle> call = service.getPatient();

        call.enqueue(new Callback<ca.uhn.fhir.model.dstu2.resource.Bundle>() {

            @Override
            public void onResponse(Call<ca.uhn.fhir.model.dstu2.resource.Bundle> call, Response<ca.uhn.fhir.model.dstu2.resource.Bundle> response) {
                Log.d("POOP", "Starting");

                for (ca.uhn.fhir.model.dstu2.resource.Bundle.Entry e: response.body().getEntry()) Log.d("POOP", e.toString());
//                patient = response.body().getEntry().get(3);
//                Patient dataPatient = response.body();
//                Log.d("POOP", dataPatient.toString());
            }
//            markiantorno@gmail.com
//            @iantoryes
//
//            Toronto Android Developers Group
//            Eric Funguh
//
//            AndroidTO

            @Override
            public void onFailure(Call<ca.uhn.fhir.model.dstu2.resource.Bundle> call, Throwable t) {
                Log.d("POOP", "Things are bad");
            }
        });

    }

    private static OkHttpClient getOkHttpClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return okClient;
    }
    private void initData() {
        Log.d(TAG, "initData: preparing data.");
        mData.add("Name: Marek Fuller"); //+ patient.getName());
        mData.add("Gender: male");// + patient.getGender());
        mData.add("DOB: 1990-11-13"); //+ patient.getBirthDate());
        mData.add("Care Provider: Walid Knox"); //+ patient.getCareProvider());
        mData.add("Deceased: false");// + patient.getDeceased());
        mData.add("Marital Status: married");// + patient.getMaritalStatus());
        mData.add("Phone Number: 613-555-0198" );//+ patient.getTelecom());
        mData.add("ID: 579648" );//+ patient.getId());
        mData.add("Language: English");
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public String randomElement(String[] array) {
        Random random = new Random();
        int index = random.nextInt(array.length);
        return array[index];
    }

//    public void makePDF() throws FileNotFoundException, DocumentException {
//        Document document = new Document();
//        @SuppressWarnings("unused")
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("medicord.pdf"));
//        document.open();
////        String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String gender = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String birthDate = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String careProvider = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String deceasedBool = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String maritalStatus = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String phoneNumber = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String ID = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
////        String language = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
//        Paragraph paragraph1 = new Paragraph();
//        paragraph1.add("Marek Fuller");
//        Paragraph paragraph2 = new Paragraph();
//        paragraph2.add("male");
//        Paragraph paragraph3 = new Paragraph();
//        paragraph3.add("1990-11-13");
//        Paragraph paragraph4 = new Paragraph();
//        paragraph4.add("Walid Knox");
//        Paragraph paragraph5 = new Paragraph();
//        paragraph5.add("false");
//        Paragraph paragraph6 = new Paragraph();
//        paragraph6.add("married");
//        Paragraph paragraph7 = new Paragraph();
//        paragraph7.add("613-555-0198");
//        Paragraph paragraph8 = new Paragraph();
//        paragraph8.add("579648");
//        Paragraph paragraph9 = new Paragraph();
//        paragraph9.add("English");
//
//        document.add(paragraph1);
//        document.add(paragraph2);
//        document.add(paragraph3);
//        document.add(paragraph4);
//        document.add(paragraph5);
//        document.add(paragraph6);
//        document.add(paragraph7);
//        document.add(paragraph8);
//        document.add(paragraph9);
//    }

//    public Patient fill (Patient patient) {
//        if (patient.getName() == null) {
//            patient.setName(nameList[Integer.parseInt(randomElement(nameList))]);
//        }
//        else if(patient.getGender() == null) {
//            return genders[Integer.parseInt(randomElement(genders))];
//        }
//        else if(patient.getCareProvider() == null) {
//            return careProviders[Integer.parseInt(randomElement(careProviders))];
//        }
//        else if(patient.getDeceased() == null) {
//            return deceased[Integer.parseInt(randomElement(deceased))];
//        }
//        else if(patient.getMaritalStatus() == null) {
//            return maritalStatus[Integer.parseInt(randomElement(maritalStatus))];
//        }
//        else if(patient.getTelecom() == null) {
//            return phoneNumber[Integer.parseInt(randomElement(phoneNumber))];
//        }
//        else {
//            return null;
//        }
//    }
}
