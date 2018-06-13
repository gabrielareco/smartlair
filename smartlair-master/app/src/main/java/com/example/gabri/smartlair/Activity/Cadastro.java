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
import com.example.gabri.smartlair.Helper.Base64Custom;
import com.example.gabri.smartlair.Helper.Preferencias;
import com.example.gabri.smartlair.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Cadastro extends AppCompatActivity {

    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadSenhaConf;
    private Button btnCadastrar;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtCadEmail = (EditText)findViewById(R.id.editText3);
        edtCadSenha = (EditText)findViewById(R.id.editText4);
        edtCadSenhaConf = (EditText)findViewById(R.id.editText5);
        btnCadastrar = (Button) findViewById(R.id.buttoncadastro);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCadSenha.getText().toString().equals(edtCadSenhaConf.getText().toString())){
                    usuarios = new Usuarios();
                    usuarios.setEmail(edtCadEmail.getText().toString());
                    usuarios.setSenha(edtCadSenha.getText().toString());
                    cadastrarUsuario();
                }else{
                    Toast.makeText(Cadastro.this, "As senhas não correspondem.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();
                    Preferencias preferencias = new Preferencias(Cadastro.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuarios.getEmail());
                    abrirLoginUsuario();
                }else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é inválido, digite outro e-mail.";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esse e-mail já está cadastrado no sistema.";
                    }catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro.";
                        e.printStackTrace();
                    }
                    Toast.makeText(Cadastro.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(Cadastro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
