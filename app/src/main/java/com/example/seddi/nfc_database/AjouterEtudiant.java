package com.example.seddi.nfc_database;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AjouterEtudiant extends AppCompatActivity {
    String Id_NFC;
    DataBaseNFC db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DataBaseNFC(this, null);
        setContentView(R.layout.activity_ajouter_etudiant);
        Bundle extras = getIntent().getExtras();
        Id_NFC = extras.getString("Id");

    }

    public void Ajouter(View view) {
        DataBaseNFC db = new DataBaseNFC(this, null);

        EditText nom = findViewById(R.id.nom);
        String nomStr = nom.getText().toString();
        EditText prenom = findViewById(R.id.prenom);
        String prenomStr = prenom.getText().toString();
        EditText numEtudiant = findViewById(R.id.nume_etudiant);
        Long numE = Long.parseLong(numEtudiant.getText().toString());

        db.insertItem(Id_NFC, numE, nomStr, prenomStr);
        db.close();
        this.finish();
    }

}
