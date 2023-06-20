package com.example.controlefinanceiro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.controlefinanceiro.database.MyDatabase;
import com.example.controlefinanceiro.databinding.ActivityMain2Binding;
import com.example.controlefinanceiro.entities.Documento;
import com.example.controlefinanceiro.entities.DocumentoDAO;
import com.example.controlefinanceiro.helpers.SessionManager;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;
    private Inicio inicio = new Inicio();
    private Listagem listagem = new Listagem();
    private CadastroDocumentos cadastroDocumentos = new CadastroDocumentos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.container, inicio).commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.inicio){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, inicio).commit();
                    return true;
                }
                if (item.getItemId() == R.id.cadastro){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, cadastroDocumentos).commit();
                    return true;
                }
                if (item.getItemId() == R.id.resumo){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, listagem).commit();
                    return true;
                }

                return false;
            }
        });

    }

}