package com.example.gabri.smartlair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Linkagem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkagem);
    }

    public void irParaAmbienteSelecionado(View view) {
        Intent intent1 = new Intent(getApplicationContext(), Dispositivos.class);
        startActivity(intent1);
    }
}
