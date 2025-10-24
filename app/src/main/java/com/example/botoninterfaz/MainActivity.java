package com.example.botoninterfaz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

/**
 * MainActivity con sistema de login simple
 */
public class MainActivity extends Activity {
    
    private EditText etUsuario;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvEstado;
    
    // Credenciales predefinidas
    private final String USUARIO_CORRECTO = "admin";
    private final String PASSWORD_CORRECTA = "1234";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Verificar si ya está logueado
        if (isLoggedIn()) {
            goToHome();
            return;
        }
        
        setContentView(R.layout.activity_main);
        initializeViews();
        setupButton();
    }
    
    private void initializeViews() {
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvEstado = findViewById(R.id.tvEstado);
    }
    
    private void setupButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }
    
    private void attemptLogin() {
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        if (usuario.isEmpty() || password.isEmpty()) {
            tvEstado.setText("Complete todos los campos");
            return;
        }
        
        if (usuario.equals(USUARIO_CORRECTO) && password.equals(PASSWORD_CORRECTA)) {
            // Login exitoso
            saveLoginState(true);
            tvEstado.setText("¡Login exitoso!");
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            goToHome();
        } else {
            // Login fallido
            tvEstado.setText("Credenciales incorrectas");
            etPassword.setText(""); // Limpiar password
        }
    }
    
    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        return prefs.getBoolean("is_logged_in", false);
    }
    
    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", isLoggedIn);
        editor.apply();
    }
    
    private void goToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}