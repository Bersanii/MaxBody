package com.example.ifsp.maxbody;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter_Relacionamento extends BaseAdapter {
    private ArrayList<Alimento_Relacionamento> listData;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase bancoDados;

    static class ViewHolder {
        TextView txtNome, txtQntd;
        ImageButton btnDelete;
    }

    public CustomListAdapter_Relacionamento(Context aContext, ArrayList<Alimento_Relacionamento> listData) {
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
        CustomListAdapter_Relacionamento.ViewHolder holder;
        final int pos = position;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_relacionamento, null);
            holder = new ViewHolder();
            holder.txtNome = (TextView) convertView.findViewById(R.id.txtNome);
            holder.txtQntd = (TextView) convertView.findViewById(R.id.txtQntd);
            holder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);

            convertView.setTag(holder);
        } else {
            holder = (CustomListAdapter_Relacionamento.ViewHolder) convertView.getTag();
        }

        holder.txtNome.setText(listData.get(position).getNome());
        holder.txtQntd.setText(listData.get(position).getQntd().toString());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(layoutInflater.getContext());
                builder.setTitle("Excluir");
                builder.setMessage("Deseja realmente excluir?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        efetuarExclusao(listData.get(position).getId());
                        listData.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

                alerta = builder.create();
                alerta.show();
            }
        });

        return convertView;
    }

    public void efetuarExclusao(Integer id) {
        try {
            bancoDados = layoutInflater.getContext().openOrCreateDatabase("bancoUserData", layoutInflater.getContext().MODE_PRIVATE, null);
            String sql = "DELETE FROM Alimento_Refeicao WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, id);
            stmt.executeUpdateDelete();

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
