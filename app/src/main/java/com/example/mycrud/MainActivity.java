package com.example.mycrud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase dataBase;
    public ListView listViewDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("sharedpref", MODE_PRIVATE);
        String login = sharedPref.getString("login","");
        if (login == null)
            OpenLoginScreen();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = findViewById(R.id.listViewDados);

        CreateDataBase();

        if (!HasData())
            InsertTempData();

        ListData();

        registerForContextMenu(listViewDados);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ListData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                OpenAddScreen();
                return true;
            case R.id.exit:
                Logoff();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreateDataBase(){
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

    private boolean HasData() {
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

    private void ListData(){
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

    private void InsertTempData(){
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

    private void OpenAddScreen(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    private void OpenLoginScreen(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void Logoff() {
        SharedPreferences sharedPref = getSharedPreferences("sharedpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        OpenLoginScreen();
    }
}