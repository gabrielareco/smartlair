package com.example.gabri.smartlair.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabri.smartlair.DAO.ConfiguracaoFirebase;
import com.example.gabri.smartlair.Entidades.Usuarios;
import com.example.gabri.smartlair.Esqueceu;
import com.example.gabri.smartlair.Linkagem;
import com.example.gabri.smartlair.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnEntrar;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (EditText) findViewById(R.id.editText);
        edtSenha = (EditText) findViewById(R.id.editText2);
        btnEntrar = (Button) findViewById(R.id.buttonentrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")){
                    usuarios = new Usuarios();
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setSenha(edtSenha.getText().toString());
                    validarLogin();

                }else{
                    Toast.makeText(MainActivity.this, "Preencha o campo de E-mail e Senha, por favor.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(),usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                    entrar();
                }
                else{
                    Toast.makeText(MainActivity.this, "Cadastro não encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void entrar() {
        Intent intent1 = new Intent(MainActivity.this, Linkagem.class);
        startActivity(intent1);
    }


    public void irParaCadastro(View view){
        Intent intent1 = new Intent(getApplicationContext(), Cadastro.class);
        startActivity(intent1);
    }
}

