package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public Button buttonAlterar;
    public EditText editTextNome;
    public EditText editTextIdade;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        buttonAlterar = findViewById(R.id.btnSaveEdit);
        editTextNome = findViewById(R.id.txtNomeEdit);
        editTextIdade = findViewById(R.id.txtIdadeEdit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        loadData();

        buttonAlterar.setOnClickListener(view -> edit());

    }

    public void loadData(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT nome, idade FROM pessoa WHERE id = " + id.toString(), null);
            cursor.moveToFirst();
            editTextNome.setText(cursor.getString(0));
            editTextIdade.setText(cursor.getString(1));
            cursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void edit() {
        try{
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE pessoa SET nome=?, idade=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, editTextNome.getText().toString());
            stmt.bindLong(2, Integer.parseInt(editTextIdade.getText().toString()));
            stmt.bindLong(3, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finish();
        }
    }
}