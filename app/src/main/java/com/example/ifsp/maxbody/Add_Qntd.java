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

public class Add_Qntd extends AppCompatActivity {
    ImageButton vwOk;
    EditText txtQntd;
    Integer ref, alimento;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qntd);

        vwOk = (ImageButton) findViewById(R.id.vwOk);
        txtQntd = (EditText) findViewById(R.id.txtQntd);

        Intent intentTela = getIntent();
        ref = intentTela.getIntExtra("ref", 0);
        alimento = intentTela.getIntExtra("alimento", 0);

        vwOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txtQntd.getText().toString())){
                    txtQntd.setError("Este campo é obrigatório");
                    return;
                }else{
                    cadastrarAlimento();

                    Intent intent = new Intent(getApplicationContext(), Tela_Refeicoes.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void cadastrarAlimento(){
        try{
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            String sql = "INSERT INTO Alimento_Refeicao(id_refeicao,id_alimento,qntd) values (?,?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindLong(1, ref);
            stmt.bindLong(2, alimento);
            stmt.bindLong(3, Integer.parseInt(txtQntd.getText().toString()));

            stmt.executeInsert();

            bancoDados.close();

            Intent intent = new Intent(getApplicationContext(), Add_Alimento.class);
            startActivity(intent);


            System.out.println("Debug12: Alimeto_refeição. cadastrado ID da refeição: " + ref  + " Alimento: " + alimento + " Quantidade: " + txtQntd.getText().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
