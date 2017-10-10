package com.example.vlax.registroelect;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WilliamFelipe on 30/09/2017.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Registros(correo text primary key, contrasena text)");
        db.execSQL("create table Dialog(titulo text primary key, fecha text)");
        //Datos
        db.execSQL("insert into Registros(correo, contrasena) values('tucho@gmail.com','tucho')");
        db.execSQL("insert into Dialog(titulo, fecha) values('tucho','10/10/2017')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
