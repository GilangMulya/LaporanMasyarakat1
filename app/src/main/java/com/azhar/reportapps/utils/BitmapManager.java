package com.azhar.reportapps.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Azhar Rivaldi on 17-10-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

public class BitmapManager {

    /**
     * Mengonversi Bitmap menjadi string Base64.
     * @param bitmap Bitmap yang akan dikonversi.
     * @return String Base64 dari bitmap.
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Gunakan JPEG untuk hasil yang lebih kecil daripada PNG
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Mengecilkan dan mengompres bitmap ke ukuran yang lebih wajar untuk disimpan atau dikirim.
     * Mencegah TransactionTooLargeException.
     * @param sourceBitmap Bitmap asli dari kamera atau galeri.
     * @param quality Kualitas kompresi (0-100). Nilai 50-80 biasanya seimbang.
     * @return Bitmap baru yang ukurannya sudah lebih kecil.
     */
    public static Bitmap getCompressedBitmap(Bitmap sourceBitmap, int quality) {
        // Tentukan lebar maksimum gambar. 1080px adalah resolusi yang baik untuk mobile.
        int targetWidth = 1080;

        // Jika lebar gambar lebih besar dari target, ubah ukurannya dengan menjaga rasio aspek.
        if (sourceBitmap.getWidth() > targetWidth) {
            double aspectRatio = (double) sourceBitmap.getHeight() / (double) sourceBitmap.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, targetWidth, targetHeight, false);

            // Kompres bitmap yang ukurannya sudah diubah
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            // Jika gambar sudah cukup kecil, langsung kompres kualitasnya saja.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
    }
}
