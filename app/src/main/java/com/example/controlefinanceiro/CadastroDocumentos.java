package com.example.controlefinanceiro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.FragmentCadastroDocumentosBinding;
import com.example.controlefinanceiro.entities.Documento;
import com.example.controlefinanceiro.entities.DocumentoDAO;
import com.example.controlefinanceiro.helpers.SessionManager;

public class CadastroDocumentos extends Fragment {

    private FragmentCadastroDocumentosBinding binding;
    private SessionManager sessionManager;
    public CadastroDocumentos(){
        super(R.layout.fragment_cadastro_documentos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCadastroDocumentosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        String nome_usuario = sessionManager.getNomeUsuario();
        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = binding.valor.getText().toString();
                Documento documento = new Documento(0, binding.titulo.getText().toString(), binding.descricao.getText().toString(),
                        Integer.parseInt(valor), nome_usuario);
                if(validarInput(documento)){
                    MyDatabase db = MyDatabase.getMyDatabase(getActivity().getApplicationContext());
                    DocumentoDAO documentoDAO = db.documentoDAO();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            documentoDAO.registrarDocumento(documento);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Documento Registrado!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                    binding.titulo.setText("");
                    binding.descricao.setText("");
                    binding.valor.setText("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validarInput(Documento documento){
        if (documento.getTitulo().isEmpty() || documento.getDescricao().isEmpty() || documento.getValor() == 0) {
            return false;
        }
        return true;
    }

}