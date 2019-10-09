package com.example.ifsp.maxbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    EditText alturaPessoa, pesoPessoa, nomePessoa, metaPessoa, idadePessoa;
    Button button;
    RadioGroup radioGroupSexo;
    String valueSexo;
    private SQLiteDatabase bancoDados;

    public void onResume(){
        super.onResume();

        criarBancoDados();
        criarBancoRefeicoes();
        criarTabelaRelacionamento();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrandoPessoa();

        //Linkando as variaveis com os componetes visuais
        nomePessoa = (EditText) findViewById(R.id.nomePessoa);
        pesoPessoa = (EditText) findViewById(R.id.pesoPessoa);
        alturaPessoa = (EditText) findViewById(R.id.alturaPessoa);
        metaPessoa = (EditText) findViewById(R.id.metaPessoa);
        idadePessoa = (EditText) findViewById(R.id.idadePessoa);
        button = (Button) findViewById(R.id.button);
        radioGroupSexo = (RadioGroup) findViewById(R.id.radioGroupSexo);


        //Operações essenciais
        criarBancoDados();
        criarBancoRefeicoes();
        criarBancoAlimentos();
        criarTabelaRelacionamento();
        checagemAlimento();


        //Definindo clicklistener dos botões
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(nomePessoa.getText().toString())){
                    nomePessoa.setError("Este campo é obrigatório");
                    return;
                }
                else if(TextUtils.isEmpty(pesoPessoa.getText().toString())){
                    pesoPessoa.setError("Este campo é obrigatório");
                    return;
                }
                else if(TextUtils.isEmpty(alturaPessoa.getText().toString())){
                    alturaPessoa.setError("Este campo é obrigatório");
                    return;
                }
                else if(TextUtils.isEmpty(metaPessoa.getText().toString())){
                    metaPessoa.setError("Este campo é obrigatório");
                    return;
                }
                else if(TextUtils.isEmpty(idadePessoa.getText().toString())){
                    idadePessoa.setError("Este campo é obrigatório");
                    return;
                }
                else{
                    Integer checkedRadioButtonId = radioGroupSexo.getCheckedRadioButtonId();
                    if(checkedRadioButtonId!=-1) {
                        RadioButton radioButtonSexo = (RadioButton) findViewById(checkedRadioButtonId);
                        if(radioButtonSexo.getId()==R.id.radioMasc){
                            valueSexo="masculino";
                        } else if(radioButtonSexo.getId()==R.id.radioFem){
                            valueSexo="feminino";
                        }
                    }


                    popularBanco();
                    proximaTela();
                }
            }
        });
    }

    // \/ FUNÇÕES \/
    public void proximaTela() {
        Intent intentTela = new Intent(this,Tela_Principal.class);
        startActivity(intentTela);
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            //bancoDados.execSQL("DROP TABLE /*IF EXISTS*/ user");                                                  //APAGAR
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR" +
                    ", peso DOUBLE" +
                    ", altura DOUBLE" +
                    ", meta DOUBLE" +
                    ", idade INTEGER" +
                    ", sexo VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criarBancoRefeicoes() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            //bancoDados.execSQL("DROP TABLE IF EXISTS refeicoes");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS refeicoes(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", tipo VARCHAR" +
                    ", data date)");

            bancoDados.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void criarTabelaRelacionamento() {
        try{
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS Alimento_Refeicao(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", id_refeicao INTEGER" +
                    ", id_alimento INTEGER" +
                    ", qntd INTEGER" +
                    ", FOREIGN KEY(id_refeicao) REFERENCES refeicoes(id)" +
                    ", FOREIGN KEY(id_alimento) REFERENCES alimentos(id))");

            bancoDados.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void criarBancoAlimentos() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            //bancoDados.execSQL("DROP TABLE IF EXISTS alimentos");                                                  //APAGAR
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS alimentos(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR" +
                    ", kcal INTEGER)");
            bancoDados.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void entrandoPessoa(){
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_pessoas = bancoDados.rawQuery("SELECT id,nome,peso,altura,meta,idade,sexo FROM user", null);
            if(cursor_pessoas.moveToFirst()){

                Intent intent = new Intent(this,Tela_Principal.class);

                startActivity(intent);

            }
            else{

            }
            bancoDados.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        }

    public void popularBanco() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            String sql = "INSERT INTO user (nome, peso, altura, meta, idade, sexo) VALUES (?,?,?,?,?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindString(1, nomePessoa.getText().toString());
            stmt.bindDouble(2, Double.parseDouble(pesoPessoa.getText().toString()));
            stmt.bindDouble(3, Double.parseDouble(alturaPessoa.getText().toString()));
            stmt.bindDouble(4, Double.parseDouble(metaPessoa.getText().toString()));
            stmt.bindLong(5, Long.parseLong(idadePessoa.getText().toString()));
            stmt.bindString(6, valueSexo);

            stmt.executeInsert(); //exeUpdate
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checagemAlimento() {
        try{
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);
            Cursor cursor_consulta = bancoDados.rawQuery("SELECT nome FROM alimentos", null);
            if(cursor_consulta.moveToFirst()){


            }else{
                popularBancoAlimentos();
                System.out.println("DebugMax: cadastrando");
            }
            bancoDados.close();

        }catch(Exception e){
            System.out.println("DebugMax: erro:" + e.getMessage());
        }
    }


    public void popularBancoAlimentos() {
        try {
            bancoDados = openOrCreateDatabase("bancoUserData", MODE_PRIVATE, null);

            String sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abóbora, cabotian - 1/4',48)";SQLiteStatement stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abóbora, menina brasileira - 1/4',14)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abóbora, moranga - 1/4',12)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abobora, pescoço - 1/4',24)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abobrinha, italiana - 1/4',15)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abobrinha, paulista - 1/4',31)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Acelga  - 2 folhas',21)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Agrião  - 2 folhas',17)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Aipo  - 2 folhas',19)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Alface, americana - 2 folhas',4)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Alface, crespa - 2 folhas',5)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Batata, baroa - 120g',80)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Batata, doce - 120 g',77)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Batata, frita, tipo chips - 500g',543)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Batata, inglesa - 120 g',96)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Brócolis - 100g',11)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Salmão(filé)',170)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Contra-filé bovino, à milanesa(bife)',284)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Cupim bovino, cru',221)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Frango, à milanesa(Filé)',221)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Hambúrguer, bovino(Unidade)',94)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Iogurte, natural(meia xícara)',120)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Ovo de galinha, frito - Unidade',240)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Estrogonofe de frango(Xicara)',389)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Feijão, carioca(Meia xícara)',76)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Arroz, integral(xícara)',102)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pão francês(Fatia)',77)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Queijo, minas, meia cura',321)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Queijo, mozarela(3 fatias)',125)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Chocolate ao leite(p/ Barra)',648)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Yakisoba(prato fundo nivelado)',113)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Leite, de cabra(1 copo)',563)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Refrigerante, tipo guaraná(1 copo)',97)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Noz, crua(Unidade)',14)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pescada, filé, com farinha de trigo, frito(Filé)',283)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pescada, filé, cru(Filé)',107)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pescada, filé, frito(Filé)',154)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pescada, filé, molho escabeche(Filé)',142)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Lambari, fresco, cru(2 filé)',152)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Lambari, congelado, frito(2 filé)',327)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Lambari, congelado, cru(2 filé)',131)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pitanga, crua(4 Unidades)',20)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Maracujá, cru(Unidade)',129)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Mamão, Papaia, cru(Unidade)',56)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Laranja, lima, crua(Unidade)',89)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Figo, cru(Unidade)',143)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Carambola, crua(Unidade)',184)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Caju, cru(Unidade)',63)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Acerola, crua(3 Unidades)',33)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Couve-flor, crua(3 brotos)',69)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Cebola, crua(Unidade)',39)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Manjericão, cru(5 folhas)',1)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Leite, fermentado(1 copo)',147)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Banana Prata (Unidade)',84)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Banana, nanica, crua(Unidade)',79)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Frango, filé, à milanesa(unidade)',221)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Frango, coxa, com pele, assada(Unidade)',215)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Mortadela(3 Fatias)',121)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Salsicha(Unidade)',120)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Frango, coração, grelhado(3 Unidades)',207)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Carne, bovina, coxão duro, sem gordura, cozido(Bife)',512)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Carne, bovina, fraldinha, com gordura, cozida(1 bife)',365)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Fígado (Unidade)',73)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Bife de Porco(Unidade)',517)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Acém(Bife)',859)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Croquete, de carne, frito, pequeno(Unidade)',63)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Tapioca(6 colheres)',348)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Paçoca, amendoim(Unidade)',103)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pé-de-moleque, amendoim(Unidade)',80)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Castanha-do-Brasil, crua(2 Unidades)',57)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pastel, de queijo, frito(Unidade)',337)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Pastel, de carne, frito(Unidade)',310)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Polenta, pré-cozida(xícara)',461)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Cereais, milho, flocos, com sal(xícara)',370)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Torrada(Unidade)',30)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Tomate, com semente, cru(Unidade)',15)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Graviola, crua,pequena(1/4)',186)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Jabuticaba(7 Unidades)',29)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Goiaba, vermelha, com casca, crua(Meia)',54)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Morango, cru(5 Unidades)',36)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Abacate, cru(Unidade)',76)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Kiwi, cru(Unidade)',39)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Melao,cru(1/4)',348)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Linguiça, frango, frita(Unidade)',245)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Cuscuz, paulista(Xícara)',142)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Feijoada(Xícara)',117)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Coco,cru(Inteiro)',3248)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Mandioca, frita(Porçao com 10 unidades)',130)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Cenoura, cozida(Unidade)',30)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Coxinha de frango, frita(3 Unidades)',283)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Empada de frango, pré-cozida, assada(Unidade)',358)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Peru, congelado, assado(Pedaço)',163)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Porco, pernil, assado(Pedaço)',262)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Quibe, cru(3 Colheres)',109)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();
            sql = "INSERT INTO  alimentos(nome, kcal) VALUES('Salame(Porção)',398)";stmt = bancoDados.compileStatement(sql);stmt.executeInsert();



            bancoDados.close();
        } catch (Exception e) {
            System.out.println("DebugMax: erro cadastrando alimentos: " + e.getMessage());
        }
    }
}
