package com.example.controlefinanceiro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Database;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.FragmentRegistroBinding;
import com.example.controlefinanceiro.entities.Usuario;
import com.example.controlefinanceiro.entities.UsuarioDAO;

public class Registro extends Fragment {

    private FragmentRegistroBinding binding;

    public Registro(){
        super(R.layout.fragment_registro);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        final View view = inflater.inflate(R.layout.fragment_registro, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario(0, binding.usuario.getText().toString(), binding.senha.getText().toString());

                String senha = binding.senha.getText().toString();
                String confirmarSenha = binding.confirmarSenha.getText().toString();

                if (!senha.equals(confirmarSenha)) {
                    Toast.makeText(getActivity().getApplicationContext(), "As senhas não correspondem", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (validarInput(usuario)) {
                    MyDatabase db = MyDatabase.getMyDatabase(getActivity().getApplicationContext());
                    UsuarioDAO usuarioDAO = db.usuarioDAO();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Check if a user with the same name already exists
                            boolean userExists = usuarioDAO.verificarUsuario(usuario.getNome());

                            if (userExists) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Já existe um usuário com o mesmo nome", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            usuarioDAO.registrarUsuario(usuario);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Usuário Criado!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).start();
                    Navigation.findNavController(view).navigate(R.id.action_registro_to_login);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Preencha Todos os Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validarInput(Usuario usuario){
        if (usuario.getNome().isEmpty() || usuario.getSenha().isEmpty()) {
            return false;
        }
        return true;
    }

}