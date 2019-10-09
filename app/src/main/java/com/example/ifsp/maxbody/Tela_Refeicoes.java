package com.example.ifsp.maxbody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tela_Refeicoes extends AppCompatActivity {

    ImageButton btnMeuCafe, btnMeuAlmoco, btnMeuCafeTarde, btnMinhaJanta, vwRefeicoes, vwConfig, vwHome;;
    private SQLiteDatabase bancoDados;

    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_refeicoes);

        vwConfig = (ImageButton) findViewById(R.id.vwConfig);
        vwHome = (ImageButton) findViewById(R.id.vwHome);
        btnMeuCafe = (ImageButton) findViewById(R.id.btnMeuCafe);
        btnMeuAlmoco = (ImageButton) findViewById(R.id.btnMeuAlmoco);
        btnMeuCafeTarde = (ImageButton) findViewById(R.id.btnMeuCafeTarde);
        btnMinhaJanta = (ImageButton) findViewById(R.id.btnMinhaJanta);

        btnMeuCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), List_Cafe.class);
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

        vwConfig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Config.class);
                startActivity(intent);
            }
        });

        btnMeuAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), List_Almoco.class);
                startActivity(intent);
            }
        });

        btnMeuCafeTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), List_CafeTarde.class);
                startActivity(intent);
            }
        });

        btnMinhaJanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), List_Janta.class);
                startActivity(intent);
            }
        });
    }
}
