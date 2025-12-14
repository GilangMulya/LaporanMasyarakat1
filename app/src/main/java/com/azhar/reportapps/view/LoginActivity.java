package com.azhar.reportapps.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// --- IMPORT BINDING (Otomatis dari layout XML activity_login.xml) ---
import com.azhar.reportapps.databinding.ActivityLoginBinding;

// --- IMPORT ACTIVITY TUJUAN (Main Menu) ---
import com.azhar.reportapps.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    // 1. Deklarasi Variable Binding
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- TAMBAHKAN BARIS INI (PAKSA MODE TERANG) ---
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        // -----------------------------------------------

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Inisialisasi ViewBinding (PENGGANTI setContentView R.layout...)
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        // 3. Pasang Root View ke Layar
        setContentView(binding.getRoot());

        // -----------------------------------------------------------
        // LOGIKA PROGRAM DI BAWAH SINI
        // -----------------------------------------------------------

        // A. KLIK TOMBOL LOGIN (btnMasuk / btnLogin)
        binding.btnLogin.setOnClickListener(view -> {
            // Ambil text dari inputan
            String username = binding.etUsername.getText().toString();
            String password = binding.etPassword.getText().toString();

            // Cek apakah kosong?
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Mohon isi username dan password!", Toast.LENGTH_SHORT).show();
            } else {
                // Simulasi Login Sukses
                Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                // Pindah ke MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                // Tutup LoginActivity agar kalau di-Back tidak balik ke login lagi
                finish();
            }
        });

        // B. KLIK TEKS DAFTAR (Register)
        binding.tvRegister.setOnClickListener(view -> {
            // Pindah ke RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}