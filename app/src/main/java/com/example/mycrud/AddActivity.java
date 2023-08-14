package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {
    EditText editTextName, editTextIdade;
    Button buttonAdd;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextIdade = (EditText) findViewById(R.id.editTextIdade);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });


    }

    public void cadastrar(){
        try{
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "INSERT INTO pessoa (nome,idade) VALUES (?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1,editTextName.getText().toString());
            stmt.bindLong(2, Integer.parseInt(editTextIdade.getText().toString()) );
            stmt.executeInsert();
            bancoDados.close();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}