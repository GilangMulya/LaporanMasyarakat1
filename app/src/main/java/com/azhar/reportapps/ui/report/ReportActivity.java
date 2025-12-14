package com.azhar.reportapps.ui.report;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.azhar.reportapps.BuildConfig;
import com.azhar.reportapps.R;
import com.azhar.reportapps.utils.BitmapManager;
import com.azhar.reportapps.utils.Constant;
import com.azhar.reportapps.viewmodel.InputDataViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    public static final int REQUEST_PICK_PHOTO = 1;
    int REQ_CAMERA = 101;
    String strTitle, strFilePath, strBase64Photo;
    InputDataViewModel inputDataViewModel;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView imageLaporan;
    LinearLayout layoutImage;
    ExtendedFloatingActionButton fabSend;
    EditText inputNama, inputTelepon, inputLokasi, inputTanggal, inputLaporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setStatusBar();
        setInitLayout();
        setSendLaporan();
    }

    private void setInitLayout() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        imageLaporan = findViewById(R.id.imageLaporan);
        layoutImage = findViewById(R.id.layoutImage);
        fabSend = findViewById(R.id.fabSend);
        inputNama = findViewById(R.id.inputNama);
        inputTelepon = findViewById(R.id.inputTelepon);
        inputLokasi = findViewById(R.id.inputLokasi);
        inputTanggal = findViewById(R.id.inputTanggal);
        inputLaporan = findViewById(R.id.inputLaporan);

        //get data intent
        strTitle = getIntent().getExtras().getString(DATA_TITLE);
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        inputLokasi.setText(Constant.lokasiPengaduan);

        inputDataViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(InputDataViewModel.class);

        layoutImage.setOnClickListener(v -> {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Upload Foto Bukti Laporan");
            String[] pictureDialogItems = {"Pilih foto dari galeri", "Ambil foto lewat kamera"};

            pictureDialog.setItems(pictureDialogItems,
                    (dialog, which) -> {
                        switch (which) {
                            case 0: // GALERI
                                String permissionGallery;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionGallery = Manifest.permission.READ_MEDIA_IMAGES;
                                } else {
                                    permissionGallery = Manifest.permission.READ_EXTERNAL_STORAGE;
                                }

                                Dexter.withContext(ReportActivity.this)
                                        .withPermissions(permissionGallery)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                if (report.areAllPermissionsGranted()) {
                                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                    startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                                                } else {
                                                    Toast.makeText(ReportActivity.this, "Izinkan akses galeri untuk upload foto", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                                token.continuePermissionRequest();
                                            }
                                        }).check();
                                break;

                            case 1: // KAMERA
                                Dexter.withContext(ReportActivity.this)
                                        .withPermissions(Manifest.permission.CAMERA)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                if (report.areAllPermissionsGranted()) {
                                                    try {
                                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                                                                ReportActivity.this,
                                                                BuildConfig.APPLICATION_ID + ".provider",
                                                                createImageFile()));
                                                        startActivityForResult(intent, REQ_CAMERA);
                                                    } catch (IOException ex) {
                                                        Toast.makeText(ReportActivity.this, "Gagal membuka kamera!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(ReportActivity.this, "Izinkan akses kamera", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                                token.continuePermissionRequest();
                                            }
                                        }).check();
                                break;
                        }
                    });
            pictureDialog.show();
        });

        inputTanggal.setOnClickListener(view -> {
            Calendar tanggalJemput = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                tanggalJemput.set(Calendar.YEAR, year);
                tanggalJemput.set(Calendar.MONTH, monthOfYear);
                tanggalJemput.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String strFormatDefault = "d MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                inputTanggal.setText(simpleDateFormat.format(tanggalJemput.getTime()));
            };

            new DatePickerDialog(ReportActivity.this, date,
                    tanggalJemput.get(Calendar.YEAR),
                    tanggalJemput.get(Calendar.MONTH),
                    tanggalJemput.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setSendLaporan() {
        fabSend.setOnClickListener(v -> {
            String strNama = inputNama.getText().toString();
            String strTelepon = inputTelepon.getText().toString();
            String strLokasi = inputLokasi.getText().toString();
            String strTanggal = inputTanggal.getText().toString();
            String strLaporan = inputLaporan.getText().toString();

            if (strBase64Photo == null || strNama.isEmpty() || strTelepon.isEmpty() || strLokasi.isEmpty() || strTanggal.isEmpty() || strLaporan.isEmpty()) {
                Toast.makeText(ReportActivity.this, "Data tidak boleh ada yang kosong dan Foto harus ada!", Toast.LENGTH_SHORT).show();
            } else {
                inputDataViewModel.addLaporan(strTitle, strBase64Photo, strNama, strLokasi, strTanggal, strLaporan, strTelepon);
                Toast.makeText(ReportActivity.this, "Laporan Anda terkirim, tunggu info selanjutnya ya!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private File createImageFile() throws IOException {
        String strTimeStamp = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date());
        String strImageName = "IMG_" + strTimeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                strImageName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        strFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CAMERA) {
                File file = new File(strFilePath);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(strFilePath);
                    displayImage(bitmap);
                }
            } else if (requestCode == REQUEST_PICK_PHOTO && data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    displayImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Gagal mengambil foto dari galeri", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void displayImage(Bitmap bitmap) {
        // Tampilkan pratinjau
        Glide.with(this)
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_upload)
                .into(imageLaporan);

        // --- PERBAIKAN UTAMA: Kompresi Gambar ---
        // Atur kualitas kompresi (0-100). 50 adalah nilai yang seimbang.
        int compressionQuality = 50;
        // Dapatkan bitmap yang sudah dikompres dari BitmapManager
        Bitmap compressedBitmap = BitmapManager.getCompressedBitmap(bitmap, compressionQuality);

        // Convert BITMAP YANG SUDAH DIKOMPRES ke Base64
        strBase64Photo = BitmapManager.bitmapToBase64(compressedBitmap);
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
