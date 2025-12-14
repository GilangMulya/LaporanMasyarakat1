package com.azhar.reportapps.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.azhar.reportapps.database.DatabaseClient;
import com.azhar.reportapps.database.dao.DatabaseDao;
import com.azhar.reportapps.model.ModelDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InputDataViewModel extends AndroidViewModel {

    private DatabaseDao databaseDao;

    public InputDataViewModel(@NonNull Application application) {
        super(application);
        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void addLaporan(final String kategori, final String image,
                           final String namaPelapor, final String lokasi,
                           final String tanggal, final String isiLaporan,
                           final String telepon) {

        Completable.fromAction(() -> {
                    ModelDatabase modelDatabase = new ModelDatabase();

                    modelDatabase.kategori = kategori;
                    modelDatabase.foto = image; // Ini sekarang menyimpan Base64 String panjang
                    modelDatabase.lokasi = lokasi;
                    modelDatabase.tanggal = tanggal;

                    // Gabungkan data pelapor ke deskripsi biar simple
                    String laporanLengkap = isiLaporan + "\n\n(Pelapor: " + namaPelapor + " - " + telepon + ")";
                    modelDatabase.isiLaporan = laporanLengkap;

                    // Set default status
                    modelDatabase.status = "Baru";

                    databaseDao.insertData(modelDatabase);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}