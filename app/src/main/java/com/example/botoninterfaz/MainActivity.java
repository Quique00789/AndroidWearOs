package com.example.botoninterfaz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

/**
 * MainActivity para aplicación Wear OS con sistema de cambio de interfaz
 * Implementa múltiples layouts intercambiables mediante botón
 */
public class MainActivity extends Activity {
    
    private int currentInterface = 0;
    private final int MAX_INTERFACES = 3;
    
    // Referencias a vistas
    private Button switchButton;
    private TextView titleText;
    private TextView statusText;
    private LinearLayout contentContainer;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initializeInterface();
        setupSwitchButton();
        updateInterface();
    }
    
    /**
     * Inicializa la interfaz principal con elementos base
     */
    private void initializeInterface() {
        setContentView(R.layout.activity_main);
        
        switchButton = findViewById(R.id.switchButton);
        titleText = findViewById(R.id.titleText);
        statusText = findViewById(R.id.statusText);
        contentContainer = findViewById(R.id.contentContainer);
    }
    
    /**
     * Configura el comportamiento del botón de cambio de interfaz
     */
    private void setupSwitchButton() {
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchInterface();
            }
        });
    }
    
    /**
     * Cambia entre las diferentes interfaces disponibles
     */
    private void switchInterface() {
        currentInterface = (currentInterface + 1) % MAX_INTERFACES;
        updateInterface();
        
        // Animación suave de transición
        contentContainer.setAlpha(0.5f);
        contentContainer.animate()
            .alpha(1.0f)
            .setDuration(200)
            .start();
    }
    
    /**
     * Actualiza la interfaz según el modo actual
     */
    private void updateInterface() {
        switch (currentInterface) {
            case 0:
                setupMinimalInterface();
                break;
            case 1:
                setupDetailedInterface();
                break;
            case 2:
                setupTechnicalInterface();
                break;
        }
    }
    
    /**
     * Configuración de interfaz minimalista
     */
    private void setupMinimalInterface() {
        titleText.setText("Interfaz Simple");
        statusText.setText("Modo: Minimalista");
        switchButton.setText("▶ Cambiar");
        
        contentContainer.removeAllViews();
        
        TextView infoText = new TextView(this);
        infoText.setText("• Interfaz limpia\n• Elementos esenciales\n• Fácil navegación");
        infoText.setTextSize(14);
        infoText.setPadding(20, 20, 20, 20);
        
        contentContainer.addView(infoText);
    }
    
    /**
     * Configuración de interfaz detallada
     */
    private void setupDetailedInterface() {
        titleText.setText("Interfaz Detallada");
        statusText.setText("Modo: Completo");
        switchButton.setText("▶ Siguiente");
        
        contentContainer.removeAllViews();
        
        // Información del sistema
        TextView systemInfo = new TextView(this);
        systemInfo.setText("🔋 Sistema\nWear OS 4.0+\nAPI Level 30+");
        systemInfo.setTextSize(12);
        systemInfo.setPadding(20, 10, 20, 10);
        
        // Botones adicionales
        Button actionButton1 = new Button(this);
        actionButton1.setText("⚙️ Configuración");
        actionButton1.setTextSize(10);
        
        Button actionButton2 = new Button(this);
        actionButton2.setText("📊 Estadísticas");
        actionButton2.setTextSize(10);
        
        contentContainer.addView(systemInfo);
        contentContainer.addView(actionButton1);
        contentContainer.addView(actionButton2);
    }
    
    /**
     * Configuración de interfaz técnica avanzada
     */
    private void setupTechnicalInterface() {
        titleText.setText("Interfaz Técnica");
        statusText.setText("Modo: Desarrollador");
        switchButton.setText("▶ Reset");
        
        contentContainer.removeAllViews();
        
        // Información técnica
        TextView techInfo = new TextView(this);
        techInfo.setText(
            "🔧 DEBUG INFO\n" +
            "Package: " + getPackageName() + "\n" +
            "Interface ID: " + currentInterface + "\n" +
            "Build Config: DEBUG"
        );
        techInfo.setTextSize(10);
        techInfo.setTypeface(android.graphics.Typeface.MONOSPACE);
        techInfo.setPadding(15, 10, 15, 10);
        
        // Controles técnicos
        Button debugButton = new Button(this);
        debugButton.setText("📝 Logs");
        debugButton.setTextSize(9);
        debugButton.setOnClickListener(v -> 
            statusText.setText("Logs: Sistema OK - " + System.currentTimeMillis()));
        
        Button resetButton = new Button(this);
        resetButton.setText("🔄 Reiniciar");
        resetButton.setTextSize(9);
        resetButton.setOnClickListener(v -> {
            currentInterface = 0;
            updateInterface();
        });
        
        contentContainer.addView(techInfo);
        contentContainer.addView(debugButton);
        contentContainer.addView(resetButton);
    }
}