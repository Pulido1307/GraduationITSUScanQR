package com.tecnm_campus_uruapan.graduationitsuscanqr.repository;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.AlertDialogPersonalized;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.models.Alumno;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Invitado;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Messages;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.utility.StringHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FirestoreHelper {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final CollectionReference AlumnosCollection = db.collection("alumnos");
    private Alumno alumno;

    public void getData(String document, final ProgressDialog dialog, final Context context, Invitado invitado, Messages message)
    {
        dialog.show();
        AlumnosCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        Map<String,Object>data=document.getData();
                        if (Objects.requireNonNull(document).exists())
                        {
                            alumno = new Alumno(document.getId(),String.valueOf(data.get("asiento")), String.valueOf(data.get("nombre")), StringHelper.getCarrara(String.valueOf(data.get("carrera"))),String.valueOf(data.get("grupo")), Integer.parseInt(data.get("status").toString()));
                            Log.e("Alumno: ", alumno.getNombre() +" " + alumno.getGrupo());
                            invitado.getAlumno(alumno);
                            dialog.dismiss();
                        }
                        else
                        {
                            message.getMessage("Este alumno no esta en la lista de invitados.");
                            dialog.dismiss();
                        }
                }
            }
        });
    }

    public void UpdateData(final ProgressDialog dialog, final Context context, Alumno alumno, Messages message)
    {
        dialog.show();

        Map<String,Object> dataUpdate = new HashMap<>();
        dataUpdate.put("status", alumno.getStatus());

        AlumnosCollection.document(alumno.getNumeroControl()).update(dataUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        message.getMessage("Invitacion cancelada.");
                        dialog.dismiss();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        message.getMessage("Invitacion no cancelada, verifica tu conexión a Internet.");
                        dialog.dismiss();
                    }
                });
    }

    public void validAlumno(final ProgressDialog dialog, final Context context, final String num, final String nombre, final String carrera, final String asiento, final String correo, final String grupo){
        AlumnosCollection.document(num).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        dialog.dismiss();
                        new AlertDialogPersonalized().alertDialogInformacion("El número de control ya existe en la base de datos",context);
                    }else {
                        final Map<String, Object> alumno = new HashMap<>();
                        alumno.put("nombre", nombre);
                        alumno.put("carrera", carrera);
                        alumno.put("asiento", asiento);
                        alumno.put("correo", correo);
                        alumno.put("grupo", grupo);
                        alumno.put("status", 0);

                        addAlumno(dialog, context, num, alumno);
                    }
                } else {
                    Toast.makeText(context, "Error, verifique su conexión a Internet, si los problemas continuan contacte al administrador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addAlumno(final ProgressDialog dialog, final Context context, final String num, final Map<String, Object> data){
        AlumnosCollection.document(num).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    new AlertDialogPersonalized().alertDialogInformacion("Alumno registrado en el banco de datos.", context);
                }else {
                    new AlertDialogPersonalized().alertDialogInformacion("Error al registrar alumno en el banco de datos.", context);
                }
            }
        });
    }

    public void updateAlumno(final ProgressDialog dialog, final Context context, final String num, final String nombre, final String carrera, final String asiento, final String correo, final String grupo){
        final Map<String, Object> alumno = new HashMap<>();
        alumno.put("nombre", nombre);
        alumno.put("carrera", carrera);
        alumno.put("asiento", asiento);
        alumno.put("correo", correo);
        alumno.put("grupo", grupo);

        AlumnosCollection.document(num).update(alumno)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();

                        new AlertDialogPersonalized().alertDialogInformacion("Se actualizaron los datos del alumno.", context);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        new AlertDialogPersonalized().alertDialogInformacion("No se actualizaron los datos del alumno, verifica tu conexión a Internet.", context);
                    }
                });
    }

    public void validDeleteAlumno(final ProgressDialog dialog, final Context context, final String num){
        AlumnosCollection.document(num).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        deleteDataAlumno(dialog, context, num);
                    } else {
                        new AlertDialogPersonalized().alertDialogInformacion("El número de control no existe en la base de datos.",context);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, "Error, verifique su conexión a Internet, si los problemas continuan contacte al administrador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteDataAlumno(ProgressDialog dialog, Context context, String num) {
        AlumnosCollection.document(num).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        new AlertDialogPersonalized().alertDialogInformacion("Se eliminarón los datos del alumno",context);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        new AlertDialogPersonalized().alertDialogInformacion("No se eliminarón los datos del alumno, verifica tu conexión a Internet.",context);
                        dialog.dismiss();
                    }
                });
    }


    public void getDataAlumno(final Invitado respondAlumno,final ProgressDialog dialog, final Context context, final String num){
        AlumnosCollection.document(num).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();

                    if(Objects.requireNonNull(document).exists()){
                        alumno = new Alumno(document.getId(), data.get("asiento").toString(), data.get("nombre").toString(), StringHelper.getCarrara(data.get("carrera").toString()), data.get("grupo").toString(), Integer.valueOf(data.get("status").toString()), data.get("correo").toString());
                        respondAlumno.getAlumno(alumno);
                    } else {
                        new AlertDialogPersonalized().alertDialogInformacion("El alumno buscado no existe en el banco de datos.", context);
                        alumno = null;
                        respondAlumno.getAlumno(alumno);
                    }

                }else {
                    new AlertDialogPersonalized().alertDialogInformacion("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrador", context);
                }
            }
        });

    }

}
