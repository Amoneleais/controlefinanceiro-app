package com.example.controlefinanceiro.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DocumentoDAO {

    @Insert
    void registrarDocumento(Documento documento);

    @Query("SELECT * from documento where nome_usuario=(:usuario)")
    List<Documento> buscarDocumentos(String usuario);

    @Query("SELECT * FROM documento WHERE titulo = :titulo LIMIT 1")
    Documento buscarDocumento(String titulo);

    @Delete
    void deleteDocumento(Documento documento);


}
