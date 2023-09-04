package com.example.mycrud;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase dataBase;
    public ListView listViewDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = findViewById(R.id.listViewDados);
        Button botao = findViewById(R.id.btnAdd);

        botao.setOnClickListener(view -> OpenAddScreen());

        CreateDataBase();

        if (!HasData())
            InsertTempData();

        ListData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        ListData();
    }

    public void CreateDataBase(){
        try {
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            dataBase.execSQL(
                "CREATE TABLE IF NOT EXISTS pessoa (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nome VARCHAR," +
                "  idade INTEGER" +
                ")"
            );
            dataBase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean HasData() {
        boolean result = false;

        try {
            int howMany = 0;
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor myCursor = dataBase.rawQuery("SELECT COUNT(1) AS HowMany FROM pessoa", null);
            if (myCursor.moveToFirst() && myCursor.getCount() > 0)
                howMany = myCursor.getInt(0);
            if (howMany > 0)
                result = true;

            myCursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.w("Tem dados?", "" + result);
        return result;
    }

    public void ListData(){
        try {
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor myCursor = dataBase.rawQuery("SELECT id, nome, idade FROM pessoa", null);
            ArrayList<Pessoa> pessoasArray = new ArrayList<>();
            if(myCursor.moveToFirst()) {
                while (myCursor.moveToNext()) {
                    Integer id = myCursor.getInt(myCursor.getColumnIndexOrThrow("id"));
                    String nome = myCursor.getString(myCursor.getColumnIndexOrThrow("nome"));
                    Integer idade = myCursor.getInt(myCursor.getColumnIndexOrThrow("idade"));

                    pessoasArray.add(new Pessoa(id, nome, idade));
                }
            }

            listViewDados.setAdapter(new CustomListAdapter(this, pessoasArray));
            myCursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void InsertTempData(){
        try{
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "INSERT INTO pessoa (nome, idade) VALUES (?, ?)";
            SQLiteStatement stmt = dataBase.compileStatement(sql);

            stmt.bindString(1,"Exemplo 1");
            stmt.bindLong(2, 20);
            stmt.executeInsert();

            stmt.bindString(1,"Exemplo 2");
            stmt.bindLong(2, 20);
            stmt.executeInsert();

            stmt.bindString(1,"Exemplo 3");
            stmt.bindLong(2, 20);
            stmt.executeInsert();

            dataBase.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void OpenAddScreen(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
}