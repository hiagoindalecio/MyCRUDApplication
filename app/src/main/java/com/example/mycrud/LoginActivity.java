package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    SQLiteDatabase dataBase;
    EditText editTextLogin, editTextPass;
    Button btnLogin;
    TextView txtAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CreateDataBase();

        txtAdd = (TextView) findViewById(R.id.textViewAdd);
        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        btnLogin = (Button) findViewById(R.id.btnAddUser);

        txtAdd.setOnClickListener(view -> OpenAddNewUser());

        btnLogin.setOnClickListener(view -> Enter());
    }

    public void CreateDataBase() {
        try {
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            dataBase.execSQL("CREATE TABLE IF NOT EXISTS usuario (" +
                            "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "  login VARCHAR NOT NULL," +
                            "  senha VARCHAR NOT NULL" +
                            " ) " );
            dataBase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenAddNewUser() {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    public void Enter() {
        try {
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String login = editTextLogin.getText().toString();
            String senha = editTextPass.getText().toString();
            Cursor cursor = dataBase.rawQuery("SELECT id, login FROM usuario WHERE login = '"+login+"' AND senha = '"+senha+"'", null);

            if (cursor.moveToFirst()) {
                SharedPreferences sharedPref = getSharedPreferences("sharedpref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("login", login);
                editor.putInt("idUser", cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                editor.commit();
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Usuário ou senha inexistente!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ocorreu um erro ao coletar as informações de login!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}