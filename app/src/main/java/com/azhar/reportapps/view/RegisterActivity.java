package com.azhar.reportapps.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.azhar.reportapps.databinding.ActivityRegisterBinding; // Pastikan layout xml-nya ada

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewBinding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnDaftar.setOnClickListener(view -> {
            String nama = binding.etNama.getText().toString();
            String username = binding.etUsername.getText().toString();
            String password = binding.etPassword.getText().toString();
            String telp = binding.etTelp.getText().toString();

            if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || telp.isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke Login
            }
        });

        binding.tvLogin.setOnClickListener(view -> {
            finish(); // Kembali ke Login
        });
    }
}