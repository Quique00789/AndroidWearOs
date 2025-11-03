package com.example.devicesynccounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {
    
    private TextView counterDisplay;
    private TextView statusDisplay;
    private Button incrementButton;
    private Button resetButton;
    
    private int currentCounter = 0;
    private DataClient dataClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Crear layout programáticamente (sin XML)
        createLayout();
        
        // Inicializar Data Client
        dataClient = Wearable.getDataClient(this);
    }
    
    private void createLayout() {
        // Crear layout programáticamente
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        // Titulo
        TextView title = new TextView(this);
        title.setText("Counter Controller (Mobile)");
        title.setTextSize(24);
        title.setGravity(android.view.Gravity.CENTER);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        // Counter Display
        counterDisplay = new TextView(this);
        counterDisplay.setText("Contador: 0");
        counterDisplay.setTextSize(20);
        counterDisplay.setGravity(android.view.Gravity.CENTER);
        counterDisplay.setPadding(0, 0, 0, 20);
        layout.addView(counterDisplay);
        
        // Increment Button
        incrementButton = new Button(this);
        incrementButton.setText("▲ Incrementar");
        incrementButton.setTextSize(18);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter();
            }
        });
        layout.addView(incrementButton);
        
        // Reset Button
        resetButton = new Button(this);
        resetButton.setText("🔄 Reiniciar");
        resetButton.setTextSize(18);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCounter();
            }
        });
        layout.addView(resetButton);
        
        // Status Display
        statusDisplay = new TextView(this);
        statusDisplay.setText("Estado: Listo");
        statusDisplay.setTextSize(16);
        statusDisplay.setGravity(android.view.Gravity.CENTER);
        statusDisplay.setPadding(0, 30, 0, 0);
        layout.addView(statusDisplay);
        
        setContentView(layout);
    }
    
    private void incrementCounter() {
        currentCounter++;
        updateDisplay();
        sendCounterToWear();
    }
    
    private void resetCounter() {
        currentCounter = 0;
        updateDisplay();
        sendCounterToWear();
    }
    
    private void updateDisplay() {
        counterDisplay.setText("Contador: " + currentCounter);
    }
    
    private void sendCounterToWear() {
        statusDisplay.setText("Estado: Enviando...");
        
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/counter");
        putDataMapReq.getDataMap().putInt("counter_value", currentCounter);
        putDataMapReq.getDataMap().putLong("timestamp", System.currentTimeMillis());
        
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        putDataReq.setUrgent(); // Envío prioritario
        
        Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);
        
        putDataTask.addOnSuccessListener(dataItem -> {
            statusDisplay.setText("Estado: Enviado ✓");
        });
        
        putDataTask.addOnFailureListener(exception -> {
            statusDisplay.setText("Estado: Error ✗");
        });
    }
}