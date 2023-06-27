package com.example.controlefinanceiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.FragmentLoginBinding;
import com.example.controlefinanceiro.entities.Usuario;
import com.example.controlefinanceiro.entities.UsuarioDAO;
import com.example.controlefinanceiro.helpers.SessionManager;


public class Login extends Fragment {

    FragmentLoginBinding binding;
    SessionManager sessionManager;


    public Login(){
        super(R.layout.fragment_login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeUsuario = binding.usuario.getText().toString();
                String senhaUsuario = binding.senha.getText().toString();
                if (nomeUsuario.isEmpty() || senhaUsuario.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Algum campo está vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    MyDatabase myDatabase = MyDatabase.getMyDatabase(getActivity().getApplicationContext());
                    UsuarioDAO usuarioDAO = myDatabase.usuarioDAO();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Usuario usuario = usuarioDAO.login(nomeUsuario, senhaUsuario);
                            if(usuario == null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Usuário ou Senha incorretos!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sessionManager.setLogin(true);
                                        sessionManager.setNomeUsuario(nomeUsuario);
                                        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity2.class));
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
        if(sessionManager.getLogin()){
            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity2.class));
        }
        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_registro);
            }
        });
    }

}