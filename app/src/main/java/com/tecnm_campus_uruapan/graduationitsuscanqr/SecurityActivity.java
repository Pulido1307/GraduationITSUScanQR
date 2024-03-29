package com.tecnm_campus_uruapan.graduationitsuscanqr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Messages;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Configuration;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.utility.SharedPreferencesHelper;
import com.tecnm_campus_uruapan.graduationitsuscanqr.repository.FirebaseConfiguration;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class SecurityActivity extends AppCompatActivity implements Configuration, Messages {

    private Button button_CheckSecurity;
    private TextInputLayout editText_PassSecurity;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private FirebaseConfiguration firebaseConfiguration;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        button_CheckSecurity = findViewById(R.id.button_Security_Principal);
        editText_PassSecurity = findViewById(R.id.editText_Security_Principal);

        sharedPreferencesHelper = new SharedPreferencesHelper(SecurityActivity.this);
        firebaseConfiguration = new FirebaseConfiguration();

        button_CheckSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPreferencesHelper.hasData()) {
            pass = sharedPreferencesHelper.getPreferences().get("pass").toString();
            if (!pass.isEmpty()) {
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
            //ProgressDialog dialog = ProgressDialog.show(SecurityActivity.this, "", "Verificando contraseña...", true);
            //firebasePasswords.getPassword("super_master_key", dialog, SecurityActivity.this, SecurityActivity.this);
        }
    }

    private void checkPassword() {
        if (!editText_PassSecurity.getEditText().getText().toString().isEmpty()) {
            pass = editText_PassSecurity.getEditText().getText().toString();
            ProgressDialog dialog = ProgressDialog.show(SecurityActivity.this, "", "Verificando contraseña...", true);
            firebaseConfiguration.getPassword("super_master_key", dialog, SecurityActivity.this, SecurityActivity.this);
        } else {
            editText_PassSecurity.setError("Campo requerido");
        }
    }

    @Override
    public void getMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void getPasswordFirebase(String pass) {
        if (pass.equals(this.pass)) {
            Map<String, Object> savePass = new HashMap<>();
            savePass.put("pass", pass);
            sharedPreferencesHelper.addPreferences(savePass);
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Contraseña incorrecta, intenta de nuevo.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void getNumeroInvitaciones(String num) {
    }
}