package com.example.proyectedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProyectoDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_COMENTARIOS = "comentarios";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_COMENTARIO = "comentario";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_COMENTARIOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_COMENTARIO + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertComentario(String nombre, String comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_COMENTARIO, comentario);

        db.insert(TABLE_COMENTARIOS, null, values);
        db.close();
    }

    public ArrayList<Comentario> getAllComentarios() {
        ArrayList<Comentario> comentarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_COMENTARIOS;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Comentario comentario = new Comentario(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_COMENTARIO))
                    );
                    comentarios.add(comentario);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return comentarios;
    }



    public Comentario getComentarioByPosition(int position) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_COMENTARIOS + " LIMIT 1 OFFSET " + position;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            Comentario comentario = new Comentario(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_COMENTARIO))
            );
            cursor.close();
            db.close();
            return comentario;
        }

        db.close();
        return null;
    }
    public void deleteComentarioById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMENTARIOS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


}

