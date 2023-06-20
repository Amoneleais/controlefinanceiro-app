package com.example.controlefinanceiro;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.FragmentDocumentoSelecionadoBinding;
import com.example.controlefinanceiro.databinding.FragmentInicioBinding;
import com.example.controlefinanceiro.entities.Documento;
import com.example.controlefinanceiro.entities.DocumentoDAO;

import java.util.List;

public class DocumentoSelecionado extends Fragment {

    private FragmentDocumentoSelecionadoBinding binding;
    private DocumentoDAO documentoDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyDatabase db = MyDatabase.getMyDatabase(requireContext().getApplicationContext());
        documentoDAO = db.documentoDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDocumentoSelecionadoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        String tituloDocumento = args.getString("titulo");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Documento documento = documentoDAO.buscarDocumento(tituloDocumento);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (documento != null) {
                            binding.titulo.setText(documento.getTitulo());
                            binding.descricao.setText(documento.getDescricao());
                            binding.data.setText(documento.getData().toString());
                            binding.valor.setText(String.valueOf("R$"+documento.getValor()));
                        } else {
                            Toast.makeText(requireContext(), "Documento not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Documento documento = documentoDAO.buscarDocumento(tituloDocumento);
                        documentoDAO.deleteDocumento(documento);
                    }
                });

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}



