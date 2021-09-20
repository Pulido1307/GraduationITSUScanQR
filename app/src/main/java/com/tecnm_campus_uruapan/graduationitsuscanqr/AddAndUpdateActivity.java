package com.tecnm_campus_uruapan.graduationitsuscanqr;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.utility.StringHelper;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Invitado;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.models.Alumno;
import com.tecnm_campus_uruapan.graduationitsuscanqr.repository.FirestoreHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AddAndUpdateActivity extends AppCompatActivity implements Invitado {

    private TextInputLayout textInputLayout_numeroControl,textInputLayout_nombreCompleto,textInputLayout_Carrera,textInputLayout_Grupo,textInputLayout_email,textInputLayout_asiento,textInputLayout_status;
    private ArrayAdapter<String> arrayAdapter_carreras, arrayAdapter_grupos;
    private MaterialButton button_add_update,button_cancelar,button_delete;
    private String ERROR_ESTANDAR = "Campo requerido.";
    private FirestoreHelper firestoreHelper = new FirestoreHelper();
    private Alumno alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_update);

        Bundle parametros = this.getIntent().getExtras();

        textInputLayout_numeroControl = findViewById(R.id.textInputLayout_numeroControl);
        textInputLayout_nombreCompleto = findViewById(R.id.textInputLayout_nombreCompleto);
        textInputLayout_Carrera = findViewById(R.id.textInputLayout_Carrera);
        textInputLayout_Grupo = findViewById(R.id.textInputLayout_Grupo);
        textInputLayout_email = findViewById(R.id.textInputLayout_email);
        textInputLayout_asiento = findViewById(R.id.textInputLayout_asiento);
        textInputLayout_status = findViewById(R.id.textInputLayout_status);

        button_add_update = findViewById(R.id.button_add_update);
        button_cancelar = findViewById(R.id.button_exit_update_add);
        button_delete = findViewById(R.id.button_delete);

        arrayAdapter_carreras  = new ArrayAdapter<>(AddAndUpdateActivity.this, R.layout.custom_spinner_item, StringHelper.CARRERAS);
        ((AutoCompleteTextView)textInputLayout_Carrera.getEditText()).setAdapter(arrayAdapter_carreras);

        arrayAdapter_grupos  = new ArrayAdapter<>(AddAndUpdateActivity.this, R.layout.custom_spinner_item, StringHelper.GRUPOS);
        ((AutoCompleteTextView)textInputLayout_Grupo.getEditText()).setAdapter(arrayAdapter_grupos);

        button_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(parametros != null){
            String aux = parametros.getString("OPERATION");

            if(aux.equals("ADD")){
                configurationADD();
            }
            else if(aux.equals("UPDATE")){
                configurationUPDATE();
            }
            else{
                configurationSEARCH();
            }

        }

        textInputLayout_numeroControl.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numControl = textInputLayout_numeroControl.getEditText().getText().toString();
                if(isNumeric(numControl)) {
                    if (numControl.length() == 8 && !numControl.isEmpty()) {
                        ProgressDialog dialog = ProgressDialog.show(AddAndUpdateActivity.this, "", "Buscando...", true);
                        firestoreHelper.getDataAlumno(AddAndUpdateActivity.this, dialog, AddAndUpdateActivity.this, numControl);
                        hiddenKeyBoard();
                    } else {
                        textInputLayout_numeroControl.setError("Número de control inválido.");
                    }
                }else {
                    textInputLayout_numeroControl.setError("El campo solo puede recibir numeros");
                }
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(AddAndUpdateActivity.this);
                dialogConfirm.setTitle("Eliminar Alumno");
                dialogConfirm.setMessage("¿Esta seguro de borrar a "+alumno.getNombre()+"?");
                dialogConfirm.setCancelable(false);
                dialogConfirm.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = ProgressDialog.show(AddAndUpdateActivity.this, "", "Eliminando...", true);
                        firestoreHelper.validDeleteAlumno(progressDialog, AddAndUpdateActivity.this, alumno.getNumeroControl());
                        cleanText();
                        estandarUpdate();
                        button_delete.setVisibility(View.GONE);
                    }
                });
                dialogConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogConfirm.show();
            }
        });

        textWacherEmail();
        textWachersNumControl();
    }

    //Método para SEARCH
    private void configurationSEARCH(){
        this.setTitle("Buscar Alumno");
        textInputLayout_status.setVisibility(View.VISIBLE);
        estandarUpdate();
        button_add_update.setVisibility(View.GONE);
    }
    //Fin de método SEARCH

    //Método para UPDATE
    private void configurationUPDATE() {
        this.setTitle("Actualizar Alumno");
        estandarUpdate();
        button_add_update.setText(getResources().getString(R.string.update));

        button_add_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlumno();
            }
        });
    }
    private void updateAlumno(){
        hiddenErrors();
        boolean flag_num = false;
        boolean flag_nom = false;
        boolean flag_carrera = false;
        boolean flag_grupo = false;
        boolean flag_email = false;
        boolean flag_asiento = false;

        String num_control = textInputLayout_numeroControl.getEditText().getText().toString();
        String nombre = textInputLayout_nombreCompleto.getEditText().getText().toString().trim().toUpperCase();
        String carrera = textInputLayout_Carrera.getEditText().getText().toString();
        String grupo = textInputLayout_Grupo.getEditText().getText().toString();
        String email = textInputLayout_email.getEditText().getText().toString();
        String asiento = textInputLayout_asiento.getEditText().getText().toString().trim().toUpperCase();

        if(num_control.length() == 8 && isNumeric(num_control) && !num_control.isEmpty()){
            flag_num = true;
        }else {
            textInputLayout_numeroControl.setError("Número de control inválido.");
        }

        if(!nombre.isEmpty()){
            flag_nom = true;
        }else {
            textInputLayout_nombreCompleto.setError(ERROR_ESTANDAR);
        }

        if(!carrera.isEmpty()){
            flag_carrera = true;
        }else {
            textInputLayout_Carrera.setError(ERROR_ESTANDAR);
        }

        if(!grupo.isEmpty()){
            flag_grupo = true;
        }else {
            textInputLayout_Grupo.setError(ERROR_ESTANDAR);
        }

        if (!email.isEmpty() && StringHelper.isEmail(email)){
            flag_email = true;
        }else {
            if(email.isEmpty()){
                textInputLayout_email.setError(ERROR_ESTANDAR);
            }else if(!StringHelper.isEmail(email)){
                textInputLayout_email.setError("Correo inválido");
            }
        }

        if(!asiento.isEmpty()){
            flag_asiento = true;
        }else {
            textInputLayout_asiento.setError(ERROR_ESTANDAR);
        }

        if(flag_num && flag_nom && flag_carrera && flag_grupo && flag_email && flag_asiento){
            ProgressDialog dialog = ProgressDialog.show(AddAndUpdateActivity.this, "", "Actualizando...", true);
            firestoreHelper.updateAlumno(dialog, AddAndUpdateActivity.this, num_control, nombre, StringHelper.getNumCarrera(carrera), asiento, email, grupo);
            estandarUpdate();
            cleanText();
        }else {
            Toast.makeText(AddAndUpdateActivity.this, "Alguno de los campos contiene valores inválidos,",Toast.LENGTH_SHORT).show();
        }
    }
    private void estandarUpdate(){
        textInputLayout_nombreCompleto.getEditText().setEnabled(false);
        textInputLayout_Carrera.getEditText().setEnabled(false);
        textInputLayout_Grupo.getEditText().setEnabled(false);
        textInputLayout_email.getEditText().setEnabled(false);
        textInputLayout_asiento.getEditText().setEnabled(false);
        textInputLayout_status.getEditText().setEnabled(false);
        hiddenErrors();
        button_delete.setVisibility(View.GONE);
        textInputLayout_Carrera.setHint(getResources().getString(R.string.carre));
    }
    //Fin de método para Update


    //Métodos para CREATE
    private void configurationADD() {
        this.setTitle("Agregar Alumno");
        textInputLayout_numeroControl.setEndIconVisible(false);
        button_delete.setVisibility(View.GONE);

        button_add_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlumno();
            }
        });
    }
    private void addAlumno() {
        hiddenErrors();

        boolean flag_num = false;
        boolean flag_nom = false;
        boolean flag_carrera = false;
        boolean flag_grupo = false;
        boolean flag_email = false;
        boolean flag_asiento = false;

        String num_control = textInputLayout_numeroControl.getEditText().getText().toString();
        String nombre = textInputLayout_nombreCompleto.getEditText().getText().toString().trim().toUpperCase();
        String carrera = textInputLayout_Carrera.getEditText().getText().toString();
        String grupo = textInputLayout_Grupo.getEditText().getText().toString();
        String email = textInputLayout_email.getEditText().getText().toString();
        String asiento = textInputLayout_asiento.getEditText().getText().toString().trim().toUpperCase();

        if(num_control.length() == 8 && isNumeric(num_control) && !num_control.isEmpty()){
            flag_num = true;
        }else {
            textInputLayout_numeroControl.setError("Número de control inválido.");
        }

        if(!nombre.isEmpty()){
            flag_nom = true;
        }else {
            textInputLayout_nombreCompleto.setError(ERROR_ESTANDAR);
        }

        if(!carrera.isEmpty()){
            flag_carrera = true;
        }else {
            textInputLayout_Carrera.setError(ERROR_ESTANDAR);
        }

        if(!grupo.isEmpty()){
            flag_grupo = true;
        }else {
            textInputLayout_Grupo.setError(ERROR_ESTANDAR);
        }

        if (!email.isEmpty() && StringHelper.isEmail(email)){
            flag_email = true;
        }else {
            if(email.isEmpty()){
                textInputLayout_email.setError(ERROR_ESTANDAR);
            }else if(!StringHelper.isEmail(email)){
                textInputLayout_email.setError("Correo inválido");
            }
        }

        if(!asiento.isEmpty()){
            flag_asiento = true;
        }else {
            textInputLayout_asiento.setError(ERROR_ESTANDAR);
        }

        if(flag_num && flag_nom && flag_carrera && flag_grupo && flag_email && flag_asiento){
            ProgressDialog dialog = ProgressDialog.show(AddAndUpdateActivity.this, "", "Registrando...", true);
            firestoreHelper.validAlumno(dialog, AddAndUpdateActivity.this, num_control, nombre, StringHelper.getNumCarrera(carrera), asiento, email, grupo);
            cleanText();
            hiddenKeyBoard();
        }else {
            Toast.makeText(AddAndUpdateActivity.this, "Alguno de los campos contiene valores inválidos",Toast.LENGTH_SHORT).show();
        }

    }
    //Fin de métodos para CREATE

    //Métodos para toda la clase
    private void hiddenErrors(){
        textInputLayout_numeroControl.setError(null);
        textInputLayout_nombreCompleto.setError(null);
        textInputLayout_Carrera.setError(null);
        textInputLayout_Grupo.setError(null);
        textInputLayout_email.setError(null);
        textInputLayout_asiento.setError(null);
    }
    private boolean isNumeric(String number){
        try {
            Double.valueOf(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    @Override
    public void getAlumno(Alumno alumno) {
        if(alumno != null){
            this.alumno = alumno;
            textInputLayout_nombreCompleto.getEditText().setEnabled(true);
            textInputLayout_Carrera.getEditText().setEnabled(true);
            textInputLayout_Grupo.getEditText().setEnabled(true);
            textInputLayout_email.getEditText().setEnabled(true);
            textInputLayout_asiento.getEditText().setEnabled(true);

            textInputLayout_nombreCompleto.getEditText().setText(alumno.getNombre());
            ((AutoCompleteTextView)textInputLayout_Carrera.getEditText()).setText(alumno.getCarrera(), false);
            ((AutoCompleteTextView)textInputLayout_Grupo.getEditText()).setText(alumno.getGrupo(), false);
            textInputLayout_email.getEditText().setText(alumno.getCorreo());
            textInputLayout_asiento.getEditText().setText(alumno.getNumeroSilla());
            textInputLayout_status.getEditText().setText(alumno.getStatus()+"");

            textInputLayout_email.setError(null);

            button_delete.setVisibility(View.VISIBLE);
        }else {
            cleanText();
            button_delete.setVisibility(View.GONE);
            estandarUpdate();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void textWachersNumControl(){
        textInputLayout_numeroControl.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() == 0){
                    textInputLayout_numeroControl.setError("Campo vacío");
                }else if(charSequence.length() != 8 ){
                    textInputLayout_numeroControl.setError("Debe tener 8 dígitos");
                }else {
                    textInputLayout_numeroControl.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void textWacherEmail() {
        textInputLayout_email.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                    {
                        if(charSequence.length()==0)
                        {
                            textInputLayout_email.setError("Campo vacío");
                        }
                        else if(!StringHelper.isEmail(charSequence.toString()))
                        {
                            textInputLayout_email.setError("Correo electrónico invalido");
                        }
                        else
                        {
                            textInputLayout_email.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
    }
    private void hiddenKeyBoard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void cleanText(){
        textInputLayout_numeroControl.getEditText().getText().clear();
        textInputLayout_nombreCompleto.getEditText().getText().clear();
        textInputLayout_Carrera.getEditText().getText().clear();
        textInputLayout_Grupo.getEditText().getText().clear();
        textInputLayout_email.getEditText().getText().clear();
        textInputLayout_asiento.getEditText().getText().clear();
        textInputLayout_status.getEditText().getText().clear();
        hiddenErrors();
        textInputLayout_numeroControl.requestFocus();
    }
}