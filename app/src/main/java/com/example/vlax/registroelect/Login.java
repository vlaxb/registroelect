package com.example.vlax.registroelect;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by VlaX on 30/09/2017.
 */

public class Login extends AppCompatActivity {

    EditText EditCorreo;
    EditText EditContra;
    Button BtnIngresar;

    String correoElectronico;
    String contrasena;

    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Prueba",null,1);

    /////////////////////////////////////////////////////////////////////////////

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.loginButton);

        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };

        //////////////////////////////////////////////////////////////////////////////////
        EditCorreo = (EditText) findViewById(R.id.usuario);
        EditContra = (EditText) findViewById(R.id.clave);
        BtnIngresar = (Button) findViewById(R.id.button2);

        BtnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correoElectronico = EditCorreo.getText().toString();
                contrasena = EditContra.getText().toString();

                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor c1 = bd.rawQuery("select count(*) from Registros where correo = '"+correoElectronico+"'", null);

                if (c1.moveToFirst()) {
                    // capturo la consulta
                    int cantidad = Integer.parseInt(c1.getString(0));


                    if(cantidad == 0){
                        Intent intent = new Intent(Login.this, Registro.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(),"Usuario no registrado. LLene el formulario para crear un usuario", Toast.LENGTH_SHORT).show();
                    }else{
                        c1 = bd.rawQuery("select count(*) from Registros where correo = '"+correoElectronico+"' and contrasena='"+contrasena+"'", null);

                        if (c1.moveToFirst()) {
                            //
                            int cantidad2 = Integer.parseInt(c1.getString(0));

                            if(cantidad2 == 0){
                                Toast.makeText(getApplicationContext(),"Contrasena incorrecta", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(Login.this, Firelist.class);
                                startActivity(intent);
                            }
                        }

                    }

                }
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
