package com.example.botoninterfaz;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * MainActivity para Wear OS - Receptor de imágenes desde dispositivo móvil
 * Implementa comunicación vía Data Layer API con Asset para transferencia de imágenes
 */
public class MainActivity extends Activity implements DataClient.OnDataChangedListener {
    
    private static final String TAG = "WearMainActivity";
    private static final String IMAGE_PATH = "/image";
    private static final String IMAGE_KEY = "photo";
    
    private ImageView imageView;
    private TextView statusText;
    private DataClient dataClient;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupDataClient();
        
        Log.d(TAG, "MainActivity created successfully");
    }
    
    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        statusText = findViewById(R.id.statusText);
        
        // Valores iniciales
        statusText.setText("Esperando imagen del móvil...");
        
        Log.d(TAG, "Vistas inicializadas");
    }
    
    private void setupDataClient() {
        dataClient = Wearable.getDataClient(this);
        Log.d(TAG, "DataClient configurado");
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
                String path = item.getUri().getPath();
                Log.d(TAG, "Data item path: " + path);
                
                if (IMAGE_PATH.equals(path)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Asset imageAsset = dataMap.getAsset(IMAGE_KEY);
                    
                    if (imageAsset != null) {
                        Log.d(TAG, "Asset de imagen recibido, cargando...");
                        loadBitmapFromAsset(imageAsset);
                    } else {
                        Log.w(TAG, "Asset de imagen es null");
                        runOnUiThread(() -> {
                            statusText.setText("Error: Asset vacío");
                        });
                    }
                }
            }
        }
    }
    
    private void loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            Log.e(TAG, "Asset es null");
            return;
        }
        
        // Usar addOnSuccessListener en lugar de getResult() para evitar bloqueos
        dataClient.getFdForAsset(asset)
            .addOnSuccessListener(response -> {
                if (response == null) {
                    Log.e(TAG, "Response es null");
                    runOnUiThread(() -> {
                        statusText.setText("Error: Response nulo");
                    });
                    return;
                }
                
                // Procesar bitmap en thread separado
                new Thread(() -> {
                    InputStream inputStream = null;
                    try {
                        inputStream = response.getInputStream();
                        
                        if (inputStream == null) {
                            Log.e(TAG, "InputStream es null");
                            runOnUiThread(() -> {
                                statusText.setText("Error: Stream nulo");
                            });
                            return;
                        }
                        
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        
                        if (bitmap != null) {
                            Log.d(TAG, "Bitmap cargado: " + bitmap.getWidth() + "x" + bitmap.getHeight());
                            
                            runOnUiThread(() -> {
                                imageView.setImageBitmap(bitmap);
                                updateStatus();
                            });
                        } else {
                            Log.e(TAG, "Bitmap es null después de decodificar");
                            runOnUiThread(() -> {
                                statusText.setText("Error al decodificar imagen");
                            });
                        }
                        
                    } catch (Exception e) {
                        Log.e(TAG, "Error al procesar bitmap", e);
                        runOnUiThread(() -> {
                            statusText.setText("Error: " + e.getMessage());
                        });
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            response.release();
                        } catch (Exception e) {
                            Log.e(TAG, "Error al cerrar recursos", e);
                        }
                    }
                }).start();
            })
            .addOnFailureListener(exception -> {
                Log.e(TAG, "Error al obtener FD para Asset", exception);
                runOnUiThread(() -> {
                    statusText.setText("Error al cargar imagen");
                });
            });
    }
    
    private void updateStatus() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        statusText.setText("✓ Imagen recibida\n" + timestamp);
        Log.d(TAG, "Status actualizado: imagen mostrada");
    }
}
