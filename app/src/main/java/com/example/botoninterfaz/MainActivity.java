package com.example.botoninterfaz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;

/**
 * MainActivity con botones para cambiar entre Activities
 */
public class MainActivity extends Activity {
    
    private Button btnPerfil;
    private Button btnConfiguracion;
    private Button btnAcercaDe;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupButtons();
    }
    
    private void initializeViews() {
        btnPerfil = findViewById(R.id.btnPerfil);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);
        btnAcercaDe = findViewById(R.id.btnAcercaDe);
    }
    
    private void setupButtons() {
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
        
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        });
        
        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AcercaDeActivity.class);
                startActivity(intent);
            }
        });
    }
}