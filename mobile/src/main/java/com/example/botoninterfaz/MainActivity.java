package com.example.botoninterfaz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * MainActivity para dispositivo móvil - Emisor del contador hacia Wear OS
 * Envía datos utilizando Data Layer API con botones de incrementar y decrementar
 */
public class MainActivity extends Activity {
    
    private static final String TAG = "MobileMainActivity";
    private static final String COUNTER_PATH = "/counter";
    private static final String COUNTER_KEY = "counter_value";
    
    private TextView counterText;
    private TextView statusText;
    private Button incrementButton;
    private Button decrementButton;
    private Button resetButton;
    
    private DataClient dataClient;
    private int currentCounter = 0;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupDataClient();
        setupButtons();
    }
    
    private void initializeViews() {
        counterText = findViewById(R.id.counterText);
        statusText = findViewById(R.id.statusText);
        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);
        resetButton = findViewById(R.id.resetButton);
        
        updateDisplay();
    }
    
    private void setupDataClient() {
        dataClient = Wearable.getDataClient(this);
    }
    
    private void setupButtons() {
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter();
            }
        });
        
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementCounter();
            }
        });
        
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCounter();
            }
        });
    }
    
    private void incrementCounter() {
        currentCounter++;
        updateDisplay();
        sendCounterToWear();
        Log.d(TAG, "Counter incrementado a: " + currentCounter);
    }
    
    private void decrementCounter() {
        currentCounter--;
        updateDisplay();
        sendCounterToWear();
        Log.d(TAG, "Counter decrementado a: " + currentCounter);
    }
    
    private void resetCounter() {
        currentCounter = 0;
        updateDisplay();
        sendCounterToWear();
        Log.d(TAG, "Counter reseteado");
    }
    
    private void updateDisplay() {
        counterText.setText(String.valueOf(currentCounter));
        
        // Actualizar texto de estado con información de valor
        String status = "Valor actual: " + currentCounter;
        if (currentCounter > 0) {
            status += " (Positivo)";
        } else if (currentCounter < 0) {
            status += " (Negativo)";
        } else {
            status += " (Cero)";
        }
        statusText.setText(status);
        
        // Cambiar color del contador según el valor
        if (currentCounter > 0) {
            counterText.setTextColor(0xFF03DAC6); // Verde (positivo)
        } else if (currentCounter < 0) {
            counterText.setTextColor(0xFFCF6679); // Rojo (negativo)
        } else {
            counterText.setTextColor(0xFF03DAC6); // Verde (cero)
        }
    }
    
    private void sendCounterToWear() {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(COUNTER_PATH);
        putDataMapReq.getDataMap().putInt(COUNTER_KEY, currentCounter);
        putDataMapReq.getDataMap().putLong("timestamp", System.currentTimeMillis());
        
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        putDataReq.setUrgent(); // Envío inmediato
        
        Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);
        putDataTask.addOnSuccessfulness(dataItem -> {
            Log.d(TAG, "Data enviada exitosamente: " + dataItem.getUri());
            runOnUiThread(() -> {
                statusText.setText("✓ Enviado al smartwatch (" + currentCounter + ")");
            });
        });
        
        putDataTask.addOnFailureListener(exception -> {
            Log.e(TAG, "Error enviando data", exception);
            runOnUiThread(() -> {
                statusText.setText("✗ Error de envío");
            });
        });
    }
}