package com.example.vlax.registroelectiva;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    //Vistas
    EditText EditCorreo;
    EditText EditContra;
    Button BtnRegistro;

    String correoElectronico;
    String contrasena;


    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Prueba",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Vistas
        EditCorreo = (EditText) findViewById(R.id.EtCorreo);
        EditContra = (EditText) findViewById(R.id.EtCntrasena);
        BtnRegistro = (Button) findViewById(R.id.BtmRegistrar);

        BtnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correoElectronico = EditCorreo.getText().toString();
                contrasena = EditContra.getText().toString();

                SQLiteDatabase bd = admin.getWritableDatabase();
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("correo", correoElectronico);
                nuevoRegistro.put("contrasena",contrasena);

                bd.insert("Registros", null, nuevoRegistro);

                Toast.makeText(getApplicationContext(),correoElectronico+" - "+contrasena, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
