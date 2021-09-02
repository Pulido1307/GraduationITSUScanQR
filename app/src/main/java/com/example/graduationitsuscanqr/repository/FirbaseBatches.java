package com.example.graduationitsuscanqr.repository;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.graduationitsuscanqr.Interfaces.Messages;
import com.example.graduationitsuscanqr.helpers.models.Alumno;
import com.example.graduationitsuscanqr.helpers.utility.StringHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import static com.example.graduationitsuscanqr.repository.FirestoreHelper.AlumnosCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirbaseBatches {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StringHelper stringHelper = new StringHelper();

    public void getAllCollection(Messages information, final ProgressDialog dialog) {
        AlumnosCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            delete(information, dialog, task.getResult());
                        } else {
                            information.getMessage("Error al eliminar, realicelo de forma manual, desde la consola de Firebase");
                        }
                    }
                });
    }

    static int cant = 0;
    private void delete(Messages information, final ProgressDialog dialog, QuerySnapshot result) {
        int elementos = result.size();
        if (elementos > 0) {
            for (QueryDocumentSnapshot doc : result) {
                doc.getReference().delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    dialog.setMessage((++cant) + "/" + elementos + " eliminados de la lista");
                                    cerrar(elementos, dialog, information);

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                information.getMessage("Reintentar eliminados de la lista, cerrar y abrir la aplicación");
                            }
                        });
            }
        } else {
            dialog.dismiss();
            information.getMessage("Todos los elementos han sido eliminados de la lista");
        }
    }

    private void cerrar(int elementos, ProgressDialog dialog, Messages information) {
        if (elementos == cant) {
            dialog.dismiss();
            information.getMessage(cant + "/" + elementos + " eliminados de la lista");
            Log.e("err", cant + " cerrado");
        }
        Log.e("err", cant + "");
    }

    // este proceso solo se puede hacer de 500 en 500 no en más
    public void setupCollection(Messages information, final ProgressDialog dialog, ArrayList<Alumno> alumnos) {
        WriteBatch batch = db.batch();
        DocumentReference nycRef;

        for (int i = 0; i < alumnos.size(); i++) {
            nycRef = AlumnosCollection.document(alumnos.get(i).getNumeroControl());
            batch.set(nycRef, new Alumno());
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    information.getMessage(alumnos.size() + " configuraciones con éxito");
                    dialog.dismiss();
                }
            }
        });

    }

    // este proceso solo se puede hacer de 500 en 500 no en más
    public void sendAllInformation(Messages information, final ProgressDialog dialog, ArrayList<Alumno> alumnos) {
        WriteBatch batch = db.batch();
        dialog.show();
        DocumentReference nycRef;

        for (int i = 0; i < alumnos.size(); i++) {
            nycRef = AlumnosCollection.document(alumnos.get(i).getNumeroControl());
            Map<String, Object> data = new HashMap<>();
            data.put("nombre", alumnos.get(i).getNombre());
            data.put("asiento", alumnos.get(i).getNumeroSilla());
            data.put("carrera", alumnos.get(i).getCarrera());
            data.put("correo", alumnos.get(i).getCorreo());
            data.put("grupo", alumnos.get(i).getGrupo());
            data.put("status", alumnos.get(i).getStatus());
            batch.update(nycRef, data);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    information.getMessage(alumnos.size() + " escritos con éxito");
                    dialog.dismiss();
                }
            }
        });
    }
}
