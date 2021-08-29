package com.example.graduationitsuscanqr.repository;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;

import com.example.graduationitsuscanqr.Interfaces.Messages;
import com.example.graduationitsuscanqr.helpers.utility.StringHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.graduationitsuscanqr.repository.FirestoreHelper.AlumnosCollection;

public class FirbaseBatches {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private StringHelper stringHelper = new StringHelper();


}
