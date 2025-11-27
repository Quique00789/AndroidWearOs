package com.example.devicesynccounter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {
    
    private static final String TAG = "MobileMainActivity";
    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_PERMISSION = 1002;
    
    private ImageView imagePreview;
    private TextView statusDisplay;
    private Button selectImageButton;
    private Button sendImageButton;
    
    private Bitmap selectedBitmap = null;
    private DataClient dataClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Crear layout programáticamente
        createLayout();
        
        // Inicializar Data Client
        dataClient = Wearable.getDataClient(this);
        
        // Verificar permisos
        checkPermissions();
    }
    
    private void createLayout() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);
        layout.setGravity(android.view.Gravity.CENTER);
        
        // Título
        TextView title = new TextView(this);
        title.setText("Enviar Imagen al Reloj");
        title.setTextSize(22);
        title.setGravity(android.view.Gravity.CENTER);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        // Preview de imagen
        imagePreview = new ImageView(this);
        android.widget.LinearLayout.LayoutParams imageParams = 
            new android.widget.LinearLayout.LayoutParams(600, 600);
        imageParams.gravity = android.view.Gravity.CENTER;
        imageParams.setMargins(0, 0, 0, 20);
        imagePreview.setLayoutParams(imageParams);
        imagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imagePreview.setBackgroundColor(0xFF333333);
        layout.addView(imagePreview);
        
        // Botón seleccionar imagen
        selectImageButton = new Button(this);
        selectImageButton.setText("📷 Seleccionar Imagen");
        selectImageButton.setTextSize(16);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        layout.addView(selectImageButton);
        
        // Botón enviar imagen
        sendImageButton = new Button(this);
        sendImageButton.setText("📤 Enviar al Reloj");
        sendImageButton.setTextSize(16);
        sendImageButton.setEnabled(false);
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImageToWear();
            }
        });
        layout.addView(sendImageButton);
        
        // Estado
        statusDisplay = new TextView(this);
        statusDisplay.setText("Estado: Selecciona una imagen");
        statusDisplay.setTextSize(14);
        statusDisplay.setGravity(android.view.Gravity.CENTER);
        statusDisplay.setPadding(0, 30, 0, 0);
        layout.addView(statusDisplay);
        
        setContentView(layout);
    }
    
    private void checkPermissions() {
        // Para Android 13+ (API 33+) necesitamos READ_MEDIA_IMAGES
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 
                    REQUEST_PERMISSION);
            }
        } else {
            // Para versiones anteriores
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 
                    REQUEST_PERMISSION);
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                statusDisplay.setText("Estado: Permisos concedidos");
            } else {
                statusDisplay.setText("Estado: Permisos denegados");
                Toast.makeText(this, "Necesitas dar permisos para seleccionar imágenes", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                selectedBitmap = BitmapFactory.decodeStream(inputStream);
                
                // Mostrar preview
                imagePreview.setImageBitmap(selectedBitmap);
                
                // Habilitar botón de enviar
                sendImageButton.setEnabled(true);
                statusDisplay.setText("Estado: Imagen seleccionada ✓");
                
                Log.d(TAG, "Imagen cargada: " + selectedBitmap.getWidth() + "x" + selectedBitmap.getHeight());
                
            } catch (Exception e) {
                Log.e(TAG, "Error al cargar imagen", e);
                statusDisplay.setText("Estado: Error al cargar imagen");
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void sendImageToWear() {
        if (selectedBitmap == null) {
            Toast.makeText(this, "No hay imagen seleccionada", Toast.LENGTH_SHORT).show();
            return;
        }
        
        statusDisplay.setText("Estado: Enviando imagen...");
        sendImageButton.setEnabled(false);
        
        // Redimensionar imagen para optimizar transferencia (máximo 400x400)
        Bitmap resizedBitmap = resizeBitmap(selectedBitmap, 400, 400);
        
        // Convertir a Asset
        Asset imageAsset = createAssetFromBitmap(resizedBitmap);
        
        // Crear DataMap con la imagen
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/image");
        putDataMapReq.getDataMap().putAsset("photo", imageAsset);
        putDataMapReq.getDataMap().putLong("timestamp", System.currentTimeMillis());
        
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        putDataReq.setUrgent(); // Envío prioritario
        
        Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);
        
        putDataTask.addOnSuccessListener(dataItem -> {
            statusDisplay.setText("Estado: Imagen enviada al reloj ✓");
            sendImageButton.setEnabled(true);
            Toast.makeText(this, "Imagen enviada correctamente", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Imagen enviada exitosamente");
        });
        
        putDataTask.addOnFailureListener(exception -> {
            statusDisplay.setText("Estado: Error al enviar ✗");
            sendImageButton.setEnabled(true);
            Toast.makeText(this, "Error al enviar imagen", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error al enviar imagen", exception);
        });
    }
    
    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        float ratio = Math.min(
            (float) maxWidth / width,
            (float) maxHeight / height
        );
        
        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
    
    private Asset createAssetFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}
