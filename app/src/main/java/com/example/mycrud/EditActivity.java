package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public Button buttonAlterar;
    public EditText EditTextNome;
    public EditText EditTextIdade;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        buttonAlterar = findViewById(R.id.btnSaveEdit);
        EditTextNome = findViewById(R.id.txtNomeEdit);
        EditTextIdade = findViewById(R.id.txtIdadeEdit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        LoadData();

        buttonAlterar.setOnClickListener(view -> Edit());

    }

    public void LoadData() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT nome, idade FROM pessoa WHERE id = " + id.toString(), null);
            cursor.moveToFirst();
            EditTextNome.setText(cursor.getString(0));
            EditTextIdade.setText(cursor.getString(1));
            cursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Edit() {
        try{
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE pessoa SET nome=?, idade=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, EditTextNome.getText().toString());
            stmt.bindLong(2, Integer.parseInt(EditTextIdade.getText().toString()));
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