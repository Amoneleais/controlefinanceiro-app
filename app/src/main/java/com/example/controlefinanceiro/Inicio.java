package com.example.controlefinanceiro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.FragmentCadastroDocumentosBinding;
import com.example.controlefinanceiro.databinding.FragmentInicioBinding;
import com.example.controlefinanceiro.entities.Documento;
import com.example.controlefinanceiro.entities.DocumentoDAO;
import com.example.controlefinanceiro.entities.Usuario;
import com.example.controlefinanceiro.entities.UsuarioDAO;
import com.example.controlefinanceiro.helpers.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Inicio extends Fragment {
    private FragmentInicioBinding binding;
    private SessionManager sessionManager;
    private Uri capturedImageUri;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private MyDatabase db;
    private DocumentoDAO documentoDAO;
    private UsuarioDAO usuarioDAO;

    public Inicio() {
        super(R.layout.fragment_inicio);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        String nome_usuario = sessionManager.getNomeUsuario();

        binding.nomeUsuario.setText(nome_usuario);

        db = MyDatabase.getMyDatabase(requireContext().getApplicationContext());
        documentoDAO = db.documentoDAO();
        usuarioDAO = db.usuarioDAO();

        exibirImagem();

        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Documento> documentos = (ArrayList<Documento>) documentoDAO.buscarDocumentos(nome_usuario);
                AtomicInteger valorTotal = new AtomicInteger(0);

                for (Documento documento : documentos) {
                    valorTotal.addAndGet(documento.getValor());
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.gastos.setText("R$ "+String.valueOf(valorTotal.get()));
                    }
                });
            }

        });


        binding.btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setLogin(false);
                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        });

        databaseThread.start();

        binding.btnCaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
    }

    private void tirarFoto() {
        Intent tirarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tirarFotoIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(tirarFotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            capturedImageUri = salvarImagem(imageBitmap);

            atualizarCaminhoImagem(capturedImageUri.toString());

            exibirImagem();
        }
    }

    private Uri salvarImagem(Bitmap imageBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nomeImagem = "profile_picture_" + timeStamp + ".jpg";
        File storageDir = requireContext().getFilesDir();  // Use internal storage directory
        File imagem = new File(storageDir, nomeImagem);

        try {
            FileOutputStream outputStream = new FileOutputStream(imagem);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(imagem);
    }

    private void loadProfilePicture(String profilePicturePath) {
        if (profilePicturePath != null) {
            Uri imageUri = Uri.parse(profilePicturePath);
            binding.imageView.setImageURI(imageUri);
        }
    }

    private void exibirImagem() {
        String nome_usuario = sessionManager.getNomeUsuario();
        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = usuarioDAO.buscarUsuario(nome_usuario);
                if (usuario != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadProfilePicture(usuario.getProfilePicturePath());
                        }
                    });
                }
            }
        });
        databaseThread.start();
    }

    private void atualizarCaminhoImagem(String imagePath) {
        String nome_usuario = sessionManager.getNomeUsuario();
        Thread atualizarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = usuarioDAO.buscarUsuario(nome_usuario);
                if (usuario != null) {
                    usuario.setProfilePicturePath(imagePath);
                    usuarioDAO.atualizarUsuario(usuario);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exibirImagem();
                        }
                    });
                }
            }
        });
        atualizarThread.start();
    }
}

