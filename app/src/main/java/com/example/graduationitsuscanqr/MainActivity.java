package com.example.graduationitsuscanqr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationitsuscanqr.Helpers.CaptureActivityPortrait;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private IntentResult result= null;
    private Button button_scannear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_scannear = findViewById(R.id.button_scannear);
        button_scannear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearQR();
            }
        });
    }

    private void escanearQR()
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Escanear código QR");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents() != null)
            {
                Toast.makeText(MainActivity.this,result.getContents(),Toast.LENGTH_SHORT).show();
                String values = result.getContents();
                showCancelInvitation(values);
            }
            else
            {
                Toast.makeText(MainActivity.this,"Cancelaste escaneo.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showCancelInvitation(String values){
        final String array[] = values.split("\\|");
        Toast.makeText(MainActivity.this,array.length+""+ array[0],Toast.LENGTH_SHORT).show();

        if(array.length==3)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_cancel_invitation, null);
            builder.setView(view);

            final AlertDialog dialogSearchInvitation =builder.create();
            dialogSearchInvitation.setCancelable(false);
                        Button buttonBuscar = view.findViewById(R.id.button_cancelar);
            Button buttonClose = view.findViewById(R.id.buttonClose);

            TextView textView_Ncontrol = view.findViewById(R.id.textView_Ncontrol);
            TextView textView_Nombre = view.findViewById(R.id.textView_Nombre);
            TextView textView_Carrera = view.findViewById(R.id.textView_Carrera);

            textView_Ncontrol.setText("Número de control: "+array[0]);
            textView_Nombre.setText("Nombre: "+array[1]);
            textView_Carrera.setText("Carrera: "+array[2]);

            dialogSearchInvitation.show();

            buttonBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSearchInvitation.dismiss();
                }
            });
        }
        else
        {

        }
    }
}