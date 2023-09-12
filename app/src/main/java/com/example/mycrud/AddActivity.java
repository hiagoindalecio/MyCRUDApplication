package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    EditText editTextName, editTextIdade;
    Button buttonAdd;
    SQLiteDatabase bancoDados;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        SharedPreferences sharedPref = getSharedPreferences("sharedpref", MODE_PRIVATE);
        idUsuario = sharedPref.getInt("idUser", 0);

        if (idUsuario == 0) {
            Toast.makeText(this, "Faça o login para prosseguir", Toast.LENGTH_SHORT).show();
            OpenLoginScreen();
        } else {
            editTextName = (EditText) findViewById(R.id.editTextName);
            editTextIdade = (EditText) findViewById(R.id.editTextIdade);
            buttonAdd = (Button) findViewById(R.id.buttonAdd);

            buttonAdd.setOnClickListener(view -> Add());
        }
    }

    private void OpenLoginScreen(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void Add() {
        try{
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "INSERT INTO pessoa (nome, idade, idUsuario) VALUES (?, ?, ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, editTextName.getText().toString());
            stmt.bindLong(2, Integer.parseInt(editTextIdade.getText().toString()));
            stmt.bindLong(3, idUsuario);
            stmt.executeInsert();
            bancoDados.close();
            finish();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            finish();
        }
    }
}