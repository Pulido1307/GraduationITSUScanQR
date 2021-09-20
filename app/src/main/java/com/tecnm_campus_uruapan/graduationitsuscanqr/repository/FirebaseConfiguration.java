package com.tecnm_campus_uruapan.graduationitsuscanqr.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Messages;
import com.tecnm_campus_uruapan.graduationitsuscanqr.Interfaces.Configuration;
import com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.AlertDialogPersonalized;
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

public class FirebaseConfiguration {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final CollectionReference ConfigurationCollection = db.collection("configuration");

    public void getPassword(String document, final ProgressDialog dialog, Messages messages, Configuration configuration) {
        dialog.show();
        ConfigurationCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();
                    if (Objects.requireNonNull(document).exists()) {
                        String pass = String.valueOf(data.get("key"));
                        Log.e("pass: ", pass);
                        configuration.getPasswordFirebase(pass);
                        dialog.dismiss();
                    } else {
                        messages.getMessage("Error, intenta de nuevo.");
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public void getCantidadInvitaciones(String document, final ProgressDialog dialog, Messages messages, Configuration configuration) {
        dialog.show();
        ConfigurationCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();
                    if (Objects.requireNonNull(document).exists()) {
                        String cantidad_inv = String.valueOf(data.get("numero_invitaciones"));
                        Log.e("cantidad_inv: ", cantidad_inv + "");
                        configuration.getNumeroInvitaciones(cantidad_inv);
                        dialog.dismiss();
                    } else {
                        messages.getMessage("Error, intenta de nuevo.");
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public void updateCantidadInvitaciones(final ProgressDialog dialog, final Context context, final String doc, final int num) {
        final Map<String, Object> map = new HashMap<>();
        map.put("numero_invitaciones", num);
        ConfigurationCollection.document(doc).update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        new AlertDialogPersonalized().alertDialogInformacion("Se actualizó la cantidad de invitaciones disponibles por alumno.", context);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        new AlertDialogPersonalized().alertDialogInformacion("No se actualizó la cantidad de invitaciones, verifica tu conexión a Internet.", context);
                    }
                });
    }
}
