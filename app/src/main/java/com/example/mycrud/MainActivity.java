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
    public Button botao;
    public ArrayList<Integer> arrayIds;
    public Integer selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = findViewById(R.id.listViewDados);
        botao = findViewById(R.id.btnAdd);

        botao.setOnClickListener(view -> OpenAddScreen());

        listViewDados.setOnItemLongClickListener((adapterView, view, i, l) -> {
            selectedId = arrayIds.get(i);
            ConfirmDelete();
            return true;
        });

        listViewDados.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedId = arrayIds.get(i);
            OpenEditScreen();
        });

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
            dataBase.execSQL("CREATE TABLE IF NOT EXISTS pessoa(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR)");
            dataBase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean HasData() {
        boolean result = false;
        Log.w("Entrou", "" + result);

        try {
            int howMany = 0;
            arrayIds = new ArrayList<>();
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
            arrayIds = new ArrayList<>();
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor myCursor = dataBase.rawQuery("SELECT id, nome FROM pessoa", null);
            ArrayList<String> lines = new ArrayList<>();
            ArrayAdapter<String> meuAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    lines
            );
            listViewDados.setAdapter(meuAdapter);
            myCursor.moveToFirst();
            while(myCursor!=null) {
                lines.add(myCursor.getString(1));
                arrayIds.add(myCursor.getInt(0));
                myCursor.moveToNext();
            }

            myCursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void InsertTempData(){
        try{
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "INSERT INTO pessoa (nome) VALUES (?)";
            SQLiteStatement stmt = dataBase.compileStatement(sql);

            stmt.bindString(1,"Coisa 1");
            stmt.executeInsert();

            stmt.bindString(1,"Coisa abc");
            stmt.executeInsert();

            stmt.bindString(1,"Coisa Terceira");
            stmt.executeInsert();

            dataBase.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ConfirmDelete() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Você realmente deseja Delete esse registro?");
        msgBox.setPositiveButton("Sim", (dialogInterface, i) -> {
            Delete();
            ListData();
        });
        msgBox.setNegativeButton("Não", (dialogInterface, i) -> {
        });
        msgBox.show();
    }

    public void Delete(){
        //Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show();
        try{
            dataBase = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM pessoa WHERE id =?";
            SQLiteStatement stmt = dataBase.compileStatement(sql);
            stmt.bindLong(1, selectedId);
            stmt.executeUpdateDelete();
            ListData();
            dataBase.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void OpenEditScreen(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", selectedId);
        startActivity(intent);
    }

    public void OpenAddScreen(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
}