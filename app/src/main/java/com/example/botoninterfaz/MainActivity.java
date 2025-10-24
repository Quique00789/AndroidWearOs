package com.example.botoninterfaz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;

/**
 * MainActivity simple para proyecto Hola Mundo en Wear OS
 */
public class MainActivity extends Activity {
    
    private TextView helloText;
    private Button changeButton;
    private int clickCount = 0;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupButton();
    }
    
    private void initializeViews() {
        helloText = findViewById(R.id.helloText);
        changeButton = findViewById(R.id.changeButton);
    }
    
    private void setupButton() {
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                updateMessage();
            }
        });
    }
    
    private void updateMessage() {
        switch (clickCount % 4) {
            case 1:
                helloText.setText("¡Hola Mundo!");
                changeButton.setText("Cambiar idioma");
                break;
            case 2:
                helloText.setText("Hello World!");
                changeButton.setText("Change language");
                break;
            case 3:
                helloText.setText("Bonjour Monde!");
                changeButton.setText("Changer langue");
                break;
            default:
                helloText.setText("Hola Mundo");
                changeButton.setText("Tocar para cambiar");
                clickCount = 0;
                break;
        }
    }
}