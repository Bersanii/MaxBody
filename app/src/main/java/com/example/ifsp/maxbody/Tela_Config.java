package com.example.ifsp.maxbody;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Tela_Config extends AppCompatActivity {

    ImageButton vwSair, vwAtualizarMeta;
    EditText txtMetaParaAtualizar, txtNome, txtPorcao, txtKcal;
    ImageButton vwRefeicoes, vwConfig, vwHome, btnAdd;
    double metaParaAtualizar;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_config);

        vwSair = (findViewById(R.id.vwSair));
        btnAdd = (findViewById(R.id.btnAdd));
        vwAtualizarMeta = (findViewById(R.id.vwAtualizarMeta));
        txtMetaParaAtualizar = (findViewById(R.id.txtMetaParaAtualizar));
        vwRefeicoes = (findViewById(R.id.vwRefeicoes));
        vwConfig = (findViewById(R.id.vwConfig));
        vwHome = (findViewById(R.id.vwHome));
        txtNome = (findViewById(R.id.txtNome));
        txtPorcao = (findViewById(R.id.txtPorcao));
        txtKcal = (findViewById(R.id.txtKcal));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdicionarAlimentoBanco();
            }
        });

        vwAtualizarMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtMetaParaAtualizar.getText().toString())) {
                    txtMetaParaAtualizar.setError("Insira um valor para atualizar");
                    return;
                } else {
                    metaParaAtualizar = Double.parseDouble(txtMetaParaAtualizar.getText().toString());

                    atualizarMeta();

                    txtMetaParaAtualizar.setText("");
                }
            }
        });

        vwSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
                    bancoDados.execSQL("drop table Alimento_Refeicao");
                    bancoDados.execSQL("drop table refeicoes");
                    bancoDados.execSQL("drop table user");

                    bancoDados.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        vwHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
                startActivity(intent);
            }
        });

        vwRefeicoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Refeicoes.class);
                startActivity(intent);
            }
        });
    }

    public void atualizarMeta() { //Subtrai 1kg ao peso do usuário
        bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
        String sql = "UPDATE user SET meta = ?";
        SQLiteStatement stmt = bancoDados.compileStatement(sql);

        stmt.bindDouble(1, metaParaAtualizar);
        stmt.executeUpdateDelete();
        bancoDados.close();

        Toast.makeText(getApplicationContext(), "Sua meta foi atualizada", Toast.LENGTH_LONG).show();
    }

    public void AdicionarAlimentoBanco(){
        try{
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            String sql = "INSERT INTO alimentos (nome, kcal) VALUES (?, ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, txtNome.getText().toString() + " - " + txtPorcao.getText().toString() + "g");
            stmt.bindDouble(2, Double.parseDouble(txtKcal.getText().toString()));
            stmt.executeInsert();

            bancoDados.close();

        }catch (Exception e){
            System.out.println("Debugmax: erro ao inserir novo alimento:");
        }
    }
}
