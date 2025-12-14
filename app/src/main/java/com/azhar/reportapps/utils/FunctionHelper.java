package com.azhar.reportapps.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FunctionHelper {

    // Method untuk mengambil tanggal hari ini dengan format "29 November 2025"
    public static String getToday() {
        Date date = new Date();
        // Menggunakan Locale ID (Indonesia) atau Default HP
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Method convert harga ke Rupiah (Siapa tau nanti butuh)
    public static String rupiahFormat(int price) {
        java.text.NumberFormat formatter = new java.text.DecimalFormat("#,###");
        return "Rp " + formatter.format(price).replaceAll(",", ".");
    }
}