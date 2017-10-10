package com.example.vlax.registroelect;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by DESARROLLOTYT on 10/10/2017.
 */

public class Dialog extends DialogFragment {

    Context contexto;

    EditText edittext;
    DatePicker fecha;



    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog, null))
                .setTitle("Tucho Fragment")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //BASE DE DATOS
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContexto(),"Prueba",null,1);
                SQLiteDatabase bd = admin.getWritableDatabase();

                edittext = (EditText) getDialog().findViewById(R.id.tituloDialog);
                fecha = (DatePicker) getDialog().findViewById(R.id.fechaDialog);
                String dia = String.valueOf(fecha.getDayOfMonth());
                String mes = String.valueOf(fecha.getMonth());
                String ano = String.valueOf(fecha.getYear());

                String textFecha = dia+"/"+mes+"/"+ano;
                String textTitulo = edittext.getText().toString();

                Toast.makeText(getContexto(),textFecha,Toast.LENGTH_LONG).show();

                Cursor c1 = bd.rawQuery("select count(*) from Dialog where titulo = 'tucho'",null);

                if(c1.moveToFirst()){
                    int identificador = Integer.parseInt(c1.getString(0));

                    if(identificador == 0){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("titulo",textTitulo);
                        contentValues.put("fecha",textFecha);

                        bd.insert("Dialog",null,contentValues);
                    }else{
                        Toast.makeText(getContexto(),"Titulo ya existe",Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        return builder.create();

    }

    public Context getContexto() {
        return contexto;
    }

    public void setContex(Context contexto) {
        this.contexto = contexto;
    }
}
