package com.azhar.reportapps.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.azhar.reportapps.database.DatabaseClient;
import com.azhar.reportapps.database.dao.DatabaseDao;
import com.azhar.reportapps.model.ModelDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryViewModel extends AndroidViewModel {

    private LiveData<List<ModelDatabase>> dataLaporan;
    private DatabaseDao databaseDao;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        // Inisialisasi Database
        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
        dataLaporan = databaseDao.getAllLaporan();
    }

    public LiveData<List<ModelDatabase>> getDataLaporan() {
        return dataLaporan;
    }

    // Method Hapus Data (Pakai RxJava agar aplikasi tidak macet)
    public void deleteDataById(int uid) {
        Completable.fromAction(() -> databaseDao.deleteLaporan(uid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    // --- METHOD BARU: UNTUK UPDATE STATUS ---
    // (Ini yang tadi menyebabkan error "cannot find symbol")
    public void updateStatusLaporan(int uid, String status) {
        Completable.fromAction(() -> databaseDao.updateStatus(uid, status))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}