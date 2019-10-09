package com.example.ifsp.maxbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
----------------------------------------------------------------------------------------------------
    Essa tela é o menu principal onde ficam registrados as calorias ingeridas, aqui na primeira
vez que o app abre são feitos os calculos de TMB e IMC.
----------------------------------------------------------------------------------------------------*/

public class Tela_Principal extends AppCompatActivity {
    TextView txtIMC, txtPeso, txtMetaKcal, txtKcalConsumidos;
    ImageButton vwMais, vwMenos, vwRefeicoes, vwConfig, vwHome;
    Double Peso, Altura, Meta, MetaKcal, KcalConsumidos;
    Integer Idade;
    String Sexo;
    private SQLiteDatabase bancoDados;

    @Override
    public void onResume(){
        super.onResume();

        consultaMeta();
        calcularMetaKcal();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        txtIMC = (findViewById(R.id.txtIMC));
        txtMetaKcal = (findViewById(R.id.txtMetaKcal));
        txtPeso = (findViewById(R.id.txtPeso));
        txtKcalConsumidos = (findViewById(R.id.txtKcalConsumidos));
        vwMais = (findViewById(R.id.vwMais));
        vwMenos = (findViewById(R.id.vwMenos));
        vwRefeicoes = (findViewById(R.id.vwRefeicoes));
        vwConfig = (findViewById(R.id.vwConfig));
        vwHome = (findViewById(R.id.vwHome));

        consultaIdade();
        consultaPeso();
        consultaAltura();
        consultaMeta();
        consultaSexo();
        calcularIMC();
        calcularTMB();
        calcularMetaKcal();
        calcularKcalTotal();

        vwMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maispeso();
                calcularIMC();
                verificarMeta();
            }
        });

        vwMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menospeso();
                calcularIMC();
                verificarMeta();
            }
        });

        vwRefeicoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Refeicoes.class);
                startActivity(intent);
            }
        });

        vwConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Config.class);
                startActivity(intent);
            }
        });
    }



    // \/ Funções \/
    public void calcularMetaKcal(){ //Calcula a dieta que será recomendada
        if (Meta < Peso){
            MetaKcal = 1400.0;
        }else{
            MetaKcal = 3000.0;
        }

        txtMetaKcal.setText(MetaKcal.toString());
        System.out.println("Debug12: Meta de Kcal = " + MetaKcal);
    }
    public double calcularTMB() { //Calcula o TMB (taxa metabólica basal)
        double TMB;

        if (Sexo == "masculino") {
            if (Idade < 3) {
                TMB = (60.9 * Peso) - 54;
            } else if (Idade < 10) {
                TMB = (22.7 * Peso) + 495;
            } else if (Idade < 18) {
                TMB = (17.5 * Peso) + 651;
            } else if (Idade < 30) {
                TMB = (15.3 * Peso) + 679;
            } else if (Idade < 60) {
                TMB = (11.6 * Peso) + 879;
            } else {
                TMB = (13.5 * Peso) + 487;
            }
        } else {
            if (Idade < 3) {
                TMB = (61.9 * Peso) - 51;
            } else if (Idade < 10) {
                TMB = (22.5 * Peso) + 499;
            } else if (Idade < 18) {
                TMB = (12.2 * Peso) + 746;
            } else if (Idade < 30) {
                TMB = (14.7 * Peso) + 496;
            } else if (Idade < 60) {
                TMB = (8.7 * Peso) + 829;
            } else {
                TMB = (10.5 * Peso) + 596;
            }
        }
        return TMB;
    }

    public void calcularIMC() { //calcula o IMC (indice de massa corporea)
        double imc = Peso / (Altura * Altura);

        txtIMC.setText(String.valueOf(imc));
        if (imc < 18.5){
            txtIMC.setText("Abaixo do Peso(" + String.valueOf(imc) + ")");
        } else if(imc < 25){
            txtIMC.setText("Peso Normal   (" + String.valueOf(imc) + ")");
        } else if(imc < 30){
            txtIMC.setText("Acima do Peso (" + String.valueOf(imc) + ")");
        }
        else{
            txtIMC.setText("Obeso         (" + String.valueOf(imc) + ")");
        }
    }

    public void maispeso() { //Adiciona 1kg ao peso do usuário
        Peso ++;

        bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
        String sql = "UPDATE user SET peso = ?";
        SQLiteStatement stmt = bancoDados.compileStatement(sql);

        stmt.bindDouble(1, Double.parseDouble(Peso.toString()));
        stmt.executeUpdateDelete();
        bancoDados.close();

        txtPeso.setText(String.valueOf(Peso));


    }

    public void menospeso() { //Subtrai 1kg ao peso do usuário
        Peso --;
        txtPeso.setText(String.valueOf(Peso));
        bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
        String sql = "UPDATE user SET peso = ?";
        SQLiteStatement stmt = bancoDados.compileStatement(sql);

        stmt.bindDouble(1, Double.parseDouble(Peso.toString()));
        stmt.executeUpdateDelete();
        bancoDados.close();

    }

    public void verificarMeta() {
        if(Peso.equals(Meta)) {
            Toast.makeText(this,"Parabens você atingiu sua meta, va até as configurações e defina uma nova :)", Toast.LENGTH_LONG).show();

        }

    }

/*
----------------------------------------------------------------------------------------------------
    Essa parte do código é destinada apenas para consultas no banco para retorno de dados
----------------------------------------------------------------------------------------------------*/

    public  void consultaPeso() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_user = bancoDados.rawQuery("SELECT peso FROM user",null);
            cursor_user.moveToFirst();
            Peso = (cursor_user.getDouble(0));
            bancoDados.close();

            txtPeso.setText(String.valueOf(Peso));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public  void consultaAltura() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_user = bancoDados.rawQuery("SELECT altura FROM user",null);
            cursor_user.moveToFirst();
            Altura = (cursor_user.getDouble(0));
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void consultaIdade() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_user = bancoDados.rawQuery("SELECT idade FROM user",null);
            cursor_user.moveToFirst();
            Idade = (cursor_user.getInt(0));
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultaSexo() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_user = bancoDados.rawQuery("SELECT sexo FROM user",null);
            cursor_user.moveToFirst();
            Sexo = cursor_user.getString(0);
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void consultaMeta() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_user = bancoDados.rawQuery("SELECT meta FROM user",null);
            cursor_user.moveToFirst();
            Meta = (cursor_user.getDouble(0));
            bancoDados.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    public void calcularKcalTotal() {
        bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
        Cursor cursor_user = bancoDados.rawQuery("SELECT SUM(Alimentos.kcal * Alimento_Refeicao.qntd)\n" +
                "FROM Alimento_Refeicao \n" +
                "INNER JOIN alimentos ON alimentos.id = Alimento_Refeicao.id_alimento \n" +
                "INNER JOIN refeicoes ON refeicoes.id = Alimento_Refeicao.id_refeicao\n" +
                "WHERE refeicoes.data = current_date",null);
        cursor_user.moveToFirst();

        KcalConsumidos = cursor_user.getDouble(0);

        txtKcalConsumidos.setText(KcalConsumidos.toString());

        bancoDados.close();
    }
}


