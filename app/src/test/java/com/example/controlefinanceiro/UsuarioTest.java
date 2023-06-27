package com.example.controlefinanceiro;

import static org.junit.Assert.assertEquals;

import com.example.controlefinanceiro.entities.Usuario;

import org.junit.Test;

public class UsuarioTest {

    @Test
    public void getProfilePicturePath_verificarCaminho() {
        String caminhoEsperado = "path/to/profile_picture.jpg";
        Usuario usuario = new Usuario(1, "teste", "123");
        usuario.setProfilePicturePath(caminhoEsperado);

        String caminhoCorreto = usuario.getProfilePicturePath();

        assertEquals(caminhoEsperado, caminhoCorreto);
    }

    @Test
    public void setProfilePicturePath_adicionarCaminho() {
        String novoCaminho = "new/path/to/profile_picture.jpg";
        Usuario usuario = new Usuario(1, "teste", "123");

        usuario.setProfilePicturePath(novoCaminho);

        assertEquals(novoCaminho, usuario.getProfilePicturePath());
    }

}
