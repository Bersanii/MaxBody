package com.example.ifsp.maxbody;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/*
----------------------------------------------------------------------------------------------------
Essa tela lista os alimentos cadastrados no banco para o usuário escolher qual quer cadastrar
----------------------------------------------------------------------------------------------------*/

public class Add_Alimento extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewAlimentos;
    EditText txtPesquisa;
    public ImageButton btnSearch;
    Integer ref;
    ArrayList<Alimento> alimentosArray = new ArrayList<Alimento>();

    @Override
    public void onRestart(){
        super.onRestart();
        carregarDados();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alimento);

        Intent intentTela = getIntent();
        ref = intentTela.getIntExtra("ref", 0);

        listViewAlimentos = (ListView) findViewById(R.id.listViewAlimentos);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        txtPesquisa = (EditText) findViewById(R.id.txtPesquisa);

        carregarDados();


        listViewAlimentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selecionarAlimento(i);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarDadosPesquisa();
            }
        });
    }

    public void selecionarAlimento(int x) {
        Integer idalimento;
        idalimento = alimentosArray.get(x).getId();

        Intent intent = new Intent(getApplicationContext(), Add_Qntd.class);
        intent.putExtra("ref", ref);
        intent.putExtra("alimento", idalimento);

        startActivity(intent);

        System.out.println("DebugMax: Enviando ID da refeição: " + ref + " Alimento: " + x);
    }


    //Lista os alimentos cadastrados no banco por uma list view personalizada
    public void carregarDados(){
            CustomListAdapter customListAdapter = new CustomListAdapter(this, alimentosArray);
            Alimento alimento;

            try {
                bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
                Cursor cursor_alimentos = bancoDados.rawQuery("SELECT nome, kcal, id FROM alimentos",null);

                cursor_alimentos.moveToFirst();
                while(cursor_alimentos!=null){
                    alimento = new Alimento(cursor_alimentos.getString(cursor_alimentos.getColumnIndex("nome")), cursor_alimentos.getDouble(cursor_alimentos.getColumnIndex("kcal")), cursor_alimentos.getInt(cursor_alimentos.getColumnIndex("id")));
                    alimentosArray.add(alimento);
                    cursor_alimentos.moveToNext();
                }
                bancoDados.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            listViewAlimentos.setAdapter(customListAdapter);

    }

    //Lista os alimentos cadastrados no banco por uma list view personalizada com um filtro de pesquisa
    public void carregarDadosPesquisa(){
        alimentosArray = new ArrayList<Alimento>();
        CustomListAdapter customListAdapter = new CustomListAdapter(this, alimentosArray);
        Alimento alimento;

        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_alimentos = bancoDados.rawQuery("SELECT nome, kcal, id FROM alimentos WHERE nome LIKE '%" + txtPesquisa.getText().toString() + "%'",null);

            cursor_alimentos.moveToFirst();
            while(cursor_alimentos!=null){
                alimento = new Alimento(cursor_alimentos.getString(cursor_alimentos.getColumnIndex("nome")), cursor_alimentos.getDouble(cursor_alimentos.getColumnIndex("kcal")), cursor_alimentos.getInt(cursor_alimentos.getColumnIndex("id")));
                alimentosArray.add(alimento);
                cursor_alimentos.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listViewAlimentos.setAdapter(customListAdapter);

    }
}
