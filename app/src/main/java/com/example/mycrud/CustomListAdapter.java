package com.example.mycrud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private ArrayList<Pessoa> listData;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase bancoDados;

    static class ViewHolder {
        TextView textViewNome;
        ImageView imageViewEdit, imageViewDelete;
    }

    public CustomListAdapter(Context aContext, ArrayList<Pessoa> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.textViewNome = (TextView) convertView.findViewById(R.id.textViewNome);
            holder.imageViewEdit = (ImageView) convertView.findViewById(R.id.imageViewEdit);
            holder.imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewNome.setText(listData.get(position).getNome());

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarPessoa(listData.get(position).getId());
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(layoutInflater.getContext());
                builder.setTitle("Excluir");
                builder.setMessage("Deseja realmente excluir?");
                builder.setPositiveButton("Sim", (arg0, arg1) -> {
                    removerPessoa(listData.get(position).getId());
                    listData.remove(position);
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("Não", (arg0, arg1) -> {
                });

                alerta = builder.create();
                alerta.show();


            }
        });

        return convertView;
    }

    public void removerPessoa(Integer id){
        try {
            bancoDados = layoutInflater.getContext().openOrCreateDatabase("clicklist", layoutInflater.getContext().MODE_PRIVATE , null);
            String sql = "DELETE FROM pessoa WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void alterarPessoa(Integer id){
        Intent intent = new Intent(layoutInflater.getContext(),EditActivity.class);
        intent.putExtra("id",id);
        layoutInflater.getContext().startActivity(intent);
    }
}
