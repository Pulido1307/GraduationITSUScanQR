package com.tecnm_campus_uruapan.graduationitsuscanqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Configuration;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.CaptureActivityPortrait;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.utility.Encriptacion;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.utility.SharedPreferencesHelper;
import com.tecnm_campus_uruapan.graduationitsuscanqr.repository.FirebaseConfiguration;
import com.tecnm_campus_uruapan.graduationitsuscanqr.repository.FirestoreHelper;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.models.Alumno;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Invitado;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Messages;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Invitado, Messages, Configuration {

    private TextView textView_about;
    private IntentResult result = null;
    private Button button_scannear;
    private FirestoreHelper fireStoreHelper = new FirestoreHelper();
    LottieAnimationView animationView;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private String passIntoUser = "";
    private int numInvitaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animationView = findViewById(R.id.animationViewG);
        button_scannear = findViewById(R.id.button_scannear);
        textView_about = findViewById(R.id.textView_about);
        textView_about.setText(textView_about.getText() + " " + new SimpleDateFormat("yyyy").format(new Date()));
        button_scannear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearQR();
            }
        });
        numInvitaciones = 0;
        animationView.setMinAndMaxFrame(193, 194);
        sharedPreferencesHelper = new SharedPreferencesHelper(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Actualizando...", true);
        new FirebaseConfiguration().getCantidadInvitaciones("invitaciones", dialog, MainActivity.this, MainActivity.this);
    }

    private void escanearQR() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Buscando invitación...", true);
                String values = result.getContents();

                try {
                    final String array[] = new Encriptacion().decryptAE(values).split("\\|");
                    if (array[0].length() == 8 && isNumeric(array[0])) {
                        fireStoreHelper.getData(array[0], dialog, MainActivity.this, MainActivity.this, MainActivity.this);
                    } else {
                        showAlertQRInvalid(dialog);

                    }
                } catch (Exception e) {
                    showAlertQRInvalid(dialog);
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Cancelaste escaneo.", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this,"Cancelaste escaneo.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNumeric(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void showAlertQRInvalid(Dialog dialog) {
        dialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("QR Inválido.");
        alertDialogBuilder.setMessage("El QR escaneado no contiene la información requerida.");
        alertDialogBuilder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alertDialog, int i) {
                        alertDialog.cancel();

                    }
                }
        );
        alertDialogBuilder.show();

    }

    private void showCancelInvitation(Alumno alumno) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_cancel_invitation, null);
        builder.setView(view);

        final AlertDialog dialogSearchInvitation = builder.create();
        dialogSearchInvitation.setCancelable(false);
        Button buttonCancelar = view.findViewById(R.id.button_cancelar);
        /*Button buttonClose = view.findViewById(R.id.buttonClose);*/

        TextView textView_Nsilla = view.findViewById(R.id.textView_NSilla);
        TextView textView_Ncontrol = view.findViewById(R.id.textView_Ncontrol);
        TextView textView_Nombre = view.findViewById(R.id.textView_Nombre);
        TextView textView_Carrera = view.findViewById(R.id.textView_Carrera);
        TextView textView_Grupo = view.findViewById(R.id.textView_Grupo);

        textView_Nsilla.setText("Código de asiento: " + alumno.getNumeroSilla());
        textView_Ncontrol.setText("Número de control: " + alumno.getNumeroControl());
        textView_Nombre.setText("Nombre: " + alumno.getNombre());
        textView_Carrera.setText("Carrera: " + alumno.getCarrera());
        textView_Grupo.setText("Grupo: " + alumno.getGrupo());

        dialogSearchInvitation.show();

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alumno.setStatus(alumno.getStatus() + 1);
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Cancelando invitación...", true);
                fireStoreHelper.UpdateData(dialog, MainActivity.this, alumno, MainActivity.this);
                dialogSearchInvitation.dismiss();
            }
        });

            /*buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSearchInvitation.dismiss();
                }
            });*/
    }

    @Override
    public void getAlumno(Alumno alumno) {
        Log.e("numInvitaciones", numInvitaciones+"");
        if (alumno.getStatus() < numInvitaciones) {
            showCancelInvitation(alumno);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle("Invitaciones canceladas.");
            alertDialogBuilder.setMessage("Los invitados ya están dentro de la ceremonia.");
            alertDialogBuilder.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface alertDialog, int i) {
                            alertDialog.cancel();
                        }
                    }
            );
            alertDialogBuilder.show();
        }

    }

    @Override
    public void getMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        switch (id) {
            case R.id.item_management:
                showAlertDialogPassword();
                break;

            case R.id.item_sign_off:
                sharedPreferencesHelper.deletePreferences();
                Toast.makeText(MainActivity.this, getResources().getText(R.string.cerrar_sesi_n) + "...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SecurityActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialogPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_check_pass, null);
        builder.setView(view);

        final AlertDialog dialogPassword = builder.create();
        //dialogPassword.setCancelable(false);
        Button buttonCheck = view.findViewById(R.id.button_CheckSecurity);
        TextInputLayout editTextPassword = view.findViewById(R.id.editText_PassSecurity_check);

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextPassword.getEditText().getText().toString().isEmpty()) {
                    passIntoUser = editTextPassword.getEditText().getText().toString();
                    ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Verificando contraseña...", true);
                    new FirebaseConfiguration().getPassword("master_key", dialog, MainActivity.this, MainActivity.this);
                    dialogPassword.cancel();
                } else {
                    editTextPassword.setError("Campo requerido");
                }
            }
        });

        dialogPassword.show();
    }

    @Override
    public void getPasswordFirebase(String pass) {
        if (pass.equals(passIntoUser)) {
            Toast.makeText(MainActivity.this, getResources().getText(R.string.gestor_central) + "...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ManagementActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Contraseña incorrecta, intenta de nuevo.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void getNumeroInvitaciones(String num) {
        numInvitaciones = Integer.parseInt(num);
    }
}