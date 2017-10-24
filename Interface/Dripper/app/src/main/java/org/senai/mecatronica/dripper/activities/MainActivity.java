package org.senai.mecatronica.dripper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.senai.mecatronica.dripper.R;

public class MainActivity extends AppCompatActivity {

    private Button btnInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInit = (Button) findViewById(R.id.btnInit);

        //pega input do usuário
        btnInit.setOnClickListener(new View.OnClickListener(){
            @Override
            //ao clicar
            public void onClick(View view) {

                //intenção: sair do main e ir para login
                Intent i = new Intent(MainActivity.this, GraphsActivity.class);
                //inicia uma nova activity com a intenção i
                startActivity(i);

                //fecha a activity atual quando abre a próxima
                finish();
            }
        });
    }
}
