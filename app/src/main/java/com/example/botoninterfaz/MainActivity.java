package com.example.botoninterfaz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * MainActivity para Wear OS - Receptor del contador desde dispositivo móvil
 * Implementa comunicación vía Data Layer API con soporte para valores negativos
 */
public class MainActivity extends Activity implements DataClient.OnDataChangedListener {
    
    private static final String TAG = "WearMainActivity";
    private static final String COUNTER_PATH = "/counter";
    private static final String COUNTER_KEY = "counter_value";
    
    private TextView counterText;
    private TextView statusText;
    private DataClient dataClient;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupDataClient();
    }
    
    private void initializeViews() {
        counterText = findViewById(R.id.counterText);
        statusText = findViewById(R.id.statusText);
        
        // Valores iniciales
        counterText.setText("0");
        statusText.setText("Esperando datos del móvil...");
    }
    
    private void setupDataClient() {
        dataClient = Wearable.getDataClient(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        dataClient.addListener(this);
        Log.d(TAG, "Data listener agregado");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        dataClient.removeListener(this);
        Log.d(TAG, "Data listener removido");
    }
    
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged called");
        
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                Log.d(TAG, "Data item path: " + item.getUri().getPath());
                
                if (COUNTER_PATH.equals(item.getUri().getPath())) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    updateCounter(dataMap.getInt(COUNTER_KEY));
                }
            }
        }
    }
    
    private void updateCounter(final int counterValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Actualizar texto del contador
                counterText.setText(String.valueOf(counterValue));
                
                // Cambiar color según el valor
                if (counterValue > 0) {
                    counterText.setTextColor(0xFF03DAC6); // Verde para positivos
                } else if (counterValue < 0) {
                    counterText.setTextColor(0xFFCF6679); // Rojo para negativos
                } else {
                    counterText.setTextColor(0xFF03DAC6); // Verde para cero
                }
                
                // Actualizar estado con timestamp formateado
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String timestamp = sdf.format(new Date());
                
                String status = "Actualizado: " + timestamp;
                if (counterValue != 0) {
                    status += "\n(" + (counterValue > 0 ? "Positivo" : "Negativo") + ")";
                }
                statusText.setText(status);
                
                Log.d(TAG, "Counter actualizado a: " + counterValue);
            }
        });
    }
}