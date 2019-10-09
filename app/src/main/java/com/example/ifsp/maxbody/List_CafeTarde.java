package com.example.ifsp.maxbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import java.util.ArrayList;
/*
----------------------------------------------------------------------------------------------------
    Essa tela lista os alimentos que o usuário ingeriu no café da tarde
    Filtrar por "DebugMax" no logcat para ver as mensagens de debug do programador
----------------------------------------------------------------------------------------------------*/

public class List_CafeTarde extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewConsumidos;
    Integer ref;
    ArrayList<Alimento_Relacionamento> alimento_relacionamentoArray = new ArrayList<Alimento_Relacionamento>();

    @Override
    public void onResume(){
        super.onResume();

        criarRefeicaoDeHoje();
        carregarDados();
        consultaId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cafe);

        listViewConsumidos = (ListView) findViewById(R.id.listViewConsumidos);

        criarRefeicaoDeHoje();
        carregarDados();
        consultaId();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Add_Alimento.class);
                intent.putExtra("ref", ref);

                startActivity(intent);
            }
        });
    }
    /*
----------------------------------------------------------------------------------------------------
    Essa parte do código é destinada as funções de exclusão e consulta no banco
----------------------------------------------------------------------------------------------------*/

    public void carregarDados(){
        CustomListAdapter_Relacionamento customListAdapter_relacionamento = new CustomListAdapter_Relacionamento(this, alimento_relacionamentoArray);
        Alimento_Relacionamento alimento_relacionamento;
        alimento_relacionamentoArray = new ArrayList<Alimento_Relacionamento>();

        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_alimentos = bancoDados.rawQuery("SELECT Alimento_Refeicao.id, Alimento_Refeicao.qntd, alimentos.nome" +
                    " FROM Alimento_Refeicao" +
                    " INNER JOIN alimentos ON alimentos.id = Alimento_Refeicao.id_alimento" +
                    " INNER JOIN refeicoes ON refeicoes.id = Alimento_Refeicao.id_refeicao" +
                    " WHERE refeicoes.data = current_date AND refeicoes.tipo = '3'",null); // Trocar conforme refeição

            cursor_alimentos.moveToFirst();
            while(cursor_alimentos!=null){
                alimento_relacionamento = new Alimento_Relacionamento(cursor_alimentos.getInt(cursor_alimentos.getColumnIndex("id")), cursor_alimentos.getInt(cursor_alimentos.getColumnIndex("qntd")), cursor_alimentos.getString(cursor_alimentos.getColumnIndex("nome")));
                alimento_relacionamentoArray.add(alimento_relacionamento);
                cursor_alimentos.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listViewConsumidos.setAdapter(customListAdapter_relacionamento);
    }

    //Consulta o id da refeição para efetuar o cadastro na tabela de relacionamento
    public void consultaId() {
        bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
        Cursor cursor = bancoDados.rawQuery("SELECT id FROM refeicoes WHERE data = current_date AND tipo = '3'",null); //trocar o tipo da refeição conforme a tela
        cursor.moveToFirst();
        ref = cursor.getInt(cursor.getColumnIndex("id"));

        bancoDados.close();
    }

    //Cria A refeição no banco refeicoes caso não tenha uma cadastrada para hoje (se já existir o programa irá cadastrar na existente)
    public void criarRefeicaoDeHoje () {
        bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
        Cursor cursor = bancoDados.rawQuery("SELECT * FROM refeicoes WHERE data = current_date AND tipo = '3'",null); //trocar o tipo da refeição conforme a tela
        if(!cursor.moveToFirst()) {

            String sql = "INSERT INTO refeicoes(tipo,data) values ('3',current_date)";  //trocar o tipo da refeição conforme a tela
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.executeInsert();

            System.out.println("DebugMax: Criando uma nova refeição (Café da manhã) na tabela refeicao no dia de hoje");
        }
        bancoDados.close();
    }



}

