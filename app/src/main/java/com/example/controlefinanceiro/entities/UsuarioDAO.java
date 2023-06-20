package com.example.controlefinanceiro.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsuarioDAO {

    @Insert
    void registrarUsuario(Usuario usuario);

    @Query("SELECT * from usuario where nome=(:nome) and senha=(:senha)")
    Usuario login(String nome, String senha);

    @Query("SELECT * from usuario where nome=(:nome)")
    Usuario buscarUsuario(String nome);

    @Update
    void atualizarUsuario(Usuario usuario);
    @Query("SELECT COUNT(*) FROM usuario WHERE nome = :nome")
    int buscarUsuariosNome(String nome);

    default boolean verificarUsuario(String nome) {
        int count = buscarUsuariosNome(nome);
        return count > 0;
    }
}
