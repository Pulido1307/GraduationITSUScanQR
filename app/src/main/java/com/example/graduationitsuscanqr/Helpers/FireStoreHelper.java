package com.example.graduationitsuscanqr.Helpers;


import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import com.example.graduationitsuscanqr.Alumno;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;


public class FireStoreHelper {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AlumnosCollection = db.collection("alumnos");

    public void getData(String document, final ProgressDialog dialog, final Context context)
    {

    }
}
