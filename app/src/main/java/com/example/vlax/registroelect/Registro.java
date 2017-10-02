package com.example.vlax.registroelect;

import android.content.ContentValues;
import android.database.Cursor;
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

                Cursor c = bd.rawQuery("select count(*) from Registros where correo = '"+correoElectronico+"'", null);

                //saber si devolvio registros
                if (c.moveToFirst()) {
                    // capturo la consulta
                    int cantidad = Integer.parseInt(c.getString(0));
                    //
                    if(cantidad == 0){
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("correo", correoElectronico);
                        nuevoRegistro.put("contrasena",contrasena);

                        bd.insert("Registros", null, nuevoRegistro);

                        Toast.makeText(getApplicationContext(),"Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Ya hay un usuario registrado como "+correoElectronico, Toast.LENGTH_SHORT).show();
                    }

                }

                /*ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("correo", correoElectronico);
                nuevoRegistro.put("contrasena",contrasena);

                bd.insert("Registros", null, nuevoRegistro);

                Toast.makeText(getApplicationContext(),correoElectronico+" - "+contrasena, Toast.LENGTH_SHORT).show();*/


            }
        });


    }

}