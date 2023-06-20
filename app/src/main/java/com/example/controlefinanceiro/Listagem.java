package com.example.controlefinanceiro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.FragmentInicioBinding;
import com.example.controlefinanceiro.databinding.FragmentListagemBinding;
import com.example.controlefinanceiro.entities.Documento;
import com.example.controlefinanceiro.entities.DocumentoDAO;
import com.example.controlefinanceiro.helpers.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class Listagem extends Fragment {

    private FragmentListagemBinding binding;
    private SessionManager sessionManager;

    public Listagem() {
        super(R.layout.fragment_listagem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListagemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyDatabase db = MyDatabase.getMyDatabase(getActivity().getApplicationContext());
        DocumentoDAO documentoDAO = db.documentoDAO();

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        String nome_usuario = sessionManager.getNomeUsuario();

        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Documento> documentos = (ArrayList<Documento>) documentoDAO.buscarDocumentos(nome_usuario);
                ArrayList<String> listaDocumentos = new ArrayList<>();

                for (Documento documento : documentos) {
                    listaDocumentos.add(documento.getTitulo());
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                android.R.layout.simple_list_item_1, listaDocumentos);

                        binding.listView.setAdapter(adapter);
                    }
                });
            }
        });

        databaseThread.start();

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String stringSelecionada = (String) adapterView.getItemAtPosition(position);
                navigateToDocumentoSelecionado(stringSelecionada);
            }
        });
    }

    private void navigateToDocumentoSelecionado(String tituloDocumento) {
        binding.listView.setVisibility(View.GONE);
        binding.selecionado.setVisibility(View.VISIBLE);
        Bundle args = new Bundle();
        args.putString("titulo", tituloDocumento);

        DocumentoSelecionado fragment = new DocumentoSelecionado();
        fragment.setArguments(args);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.selecionado, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public ListView getListView() {
        return binding.listView;
    }
}
