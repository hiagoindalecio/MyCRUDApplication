package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUserActivity extends AppCompatActivity {
    SQLiteDatabase dataBase;
    EditText editTextLogin,editTextSenha;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUser();
            }
        });
    }

    public void AddUser(){
        if(TextUtils.isEmpty(editTextLogin.getText().toString())){
            editTextLogin.setError("Campo obrigatório!");
        } else if(TextUtils.isEmpty(editTextSenha.getText().toString())){
            editTextSenha.setError("Campo obrigatório!");
        } else {
            try {
                dataBase = openOrCreateDatabase("sharedpref", MODE_PRIVATE, null);
                String sql = "INSERT INTO usuario (login,senha) VALUES (?,?)";
                SQLiteStatement stmt = dataBase.compileStatement(sql);
                stmt.bindString(1, editTextLogin.getText().toString());
                stmt.bindString(2, editTextSenha.getText().toString());
                stmt.executeInsert();
                dataBase.close();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}