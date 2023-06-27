package com.example.controlefinanceiro.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.controlefinanceiro.entities.Documento;
import com.example.controlefinanceiro.entities.DocumentoDAO;
import com.example.controlefinanceiro.entities.Usuario;
import com.example.controlefinanceiro.entities.UsuarioDAO;

@Database(entities = {Documento.class, Usuario.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    UsuarioDAO usuarioDAO;

    private static final String nomedb = "mydatabase";
    private static MyDatabase myDatabase;

    public static synchronized MyDatabase getMyDatabase(Context contexto) {
      if (myDatabase == null){
          myDatabase = Room.databaseBuilder(contexto, MyDatabase.class, nomedb)
                  .fallbackToDestructiveMigration()
                  .build();
      }
      return myDatabase;
    };

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public abstract UsuarioDAO usuarioDAO();

    public abstract DocumentoDAO documentoDAO();
}
