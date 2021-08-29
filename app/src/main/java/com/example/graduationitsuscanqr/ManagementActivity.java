package com.example.graduationitsuscanqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ManagementActivity extends AppCompatActivity {

    private CardView button_new_generation, button_add,button_update,button_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setTitle(getResources().getString(R.string.gestor_central));

        button_new_generation = findViewById(R.id.button_new_generation_management);
        button_add = findViewById(R.id.button_add_management);
        button_update = findViewById(R.id.button_update_management);
        button_search = findViewById(R.id.button_search_management);

        buttons();
    }

    private void buttons() {


        button_new_generation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManagementActivity.this, getResources().getString(R.string.nueva_generacion), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ManagementActivity.this, NewGenerationActivity.class));
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManagementActivity.this, getResources().getString(R.string.agregar), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManagementActivity.this, AddAndUpdateActivity.class);
                intent.putExtra("OPERATION", "ADD");
                startActivity(intent);
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManagementActivity.this, getResources().getString(R.string.actualizar_registro), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManagementActivity.this, AddAndUpdateActivity.class);
                intent.putExtra("OPERATION", "UPDATE");
                startActivity(intent);
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManagementActivity.this, getResources().getString(R.string.consultar), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManagementActivity.this, AddAndUpdateActivity.class);
                intent.putExtra("OPERATION", "SEARCH");
                startActivity(intent);
            }
        });
    }
}