package com.example.graduationitsuscanqr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.graduationitsuscanqr.Interfaces.Invitado;
import com.example.graduationitsuscanqr.Interfaces.Messages;
import com.example.graduationitsuscanqr.helpers.models.Alumno;
import com.example.graduationitsuscanqr.repository.FirbaseBatches;

public class NewGenerationActivity extends AppCompatActivity implements Invitado, Messages {

    private Button buttonclear,buttonsetup,buttonadd,buttonclearinfo,buttonadd_firebase;
    private FirbaseBatches firbaseBatches = new FirbaseBatches();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_generation);

        setTitle(getResources().getString(R.string.nueva_generacion));
        buttonadd_firebase = findViewById(R.id.buttonadd_firebase);
        buttonadd = findViewById(R.id.buttonadd);
        buttonclear = findViewById(R.id.buttonclear);
        buttonsetup = findViewById(R.id.buttonsetup);
        buttonclearinfo = findViewById(R.id.buttonclearinfo);
        options();
        
    }

    private void options() {

    }

    @Override
    public void getAlumno(Alumno alumno) {

    }

    @Override
    public void getMessage(String message) {

    }
}