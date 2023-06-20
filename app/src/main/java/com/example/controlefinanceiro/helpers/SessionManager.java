package com.example.controlefinanceiro.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context contexto){
        sharedPreferences = contexto.getSharedPreferences("appkey", 0);
        editor=sharedPreferences.edit();
        editor.commit();
    }

    public void setLogin(boolean login){
        editor.putBoolean("key_login", login);
        editor.commit();
    }

    public boolean getLogin(){
        return sharedPreferences.getBoolean("key_login", false);

    }

    public void setNomeUsuario(String nomeUsuario){
        editor.putString("key_nomeusuario", nomeUsuario);
        editor.commit();
    }

    public String getNomeUsuario(){
        return sharedPreferences.getString("key_nomeusuario", "");
    }
}
