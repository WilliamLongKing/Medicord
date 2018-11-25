package com.kingapps.medicord;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kingapps.medicord.utils.FhirConverterFactory;
import com.kingapps.medicord.utils.PatientInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Provenance;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button filebutton = findViewById(R.id.fileButton);
        Button uploadbutton = findViewById(R.id.uploadButton);
        filebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileIntent = new Intent(MainActivity.this, fileActivity.class);
                MainActivity.this.startActivity(fileIntent);
            }
        });
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("POOP", "I HIT THE BUTTON");
                testNetwork();
                dispatchTakePictureIntent();
            }
        });
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
        Call<Bundle> call = service.getPatient();

        call.enqueue(new Callback<Bundle>() {

            @Override
            public void onResponse(Call<Bundle> call, Response<Bundle> response) {
                Log.d("POOP", "Starting");

                for (Bundle.Entry e: response.body().getEntry()) Log.d("POOP", e.toString());

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
            public void onFailure(Call<Bundle> call, Throwable t) {
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

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
