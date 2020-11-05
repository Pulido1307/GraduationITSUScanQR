package com.example.graduationitsuscanqr.Helpers;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.example.graduationitsuscanqr.Alumno;
import com.example.graduationitsuscanqr.Invitado;
import com.example.graduationitsuscanqr.Messages;
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


public class FireStoreHelper {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AlumnosCollection = db.collection("alumnos");
    private Alumno alumno;

    public void getData(String document, final ProgressDialog dialog, final Context context, Invitado invitado)
    {
        dialog.show();
        AlumnosCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        Map<String,Object>data=document.getData();
                        alumno = new Alumno(document.getId(), String.valueOf(data.get("nombre")), String.valueOf(data.get("carrera")),String.valueOf(data.get("grupo")), Integer.parseInt(data.get("status").toString()));
                        Log.e("Alumno: ", alumno.getNombre() +" " + alumno.getGrupo());
                        invitado.getAlumno(alumno);
                        dialog.dismiss();
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

}
