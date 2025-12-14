package com.azhar.reportapps.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_laporan")
public class ModelDatabase implements Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "nama_jalan")
    public String lokasi;

    @ColumnInfo(name = "kategori")
    public String kategori;

    @ColumnInfo(name = "isi_laporan")
    public String isiLaporan;

    @ColumnInfo(name = "tanggal")
    public String tanggal;

    @ColumnInfo(name = "foto")
    public String foto;

    // --- KOLOM BARU ---
    @ColumnInfo(name = "status")
    public String status;

    public ModelDatabase() {
    }

    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getIsiLaporan() { return isiLaporan; }
    public void setIsiLaporan(String isiLaporan) { this.isiLaporan = isiLaporan; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    protected ModelDatabase(Parcel in) {
        uid = in.readInt();
        lokasi = in.readString();
        kategori = in.readString();
        isiLaporan = in.readString();
        tanggal = in.readString();
        foto = in.readString();
        status = in.readString(); // Baca status
    }

    public static final Creator<ModelDatabase> CREATOR = new Creator<ModelDatabase>() {
        @Override
        public ModelDatabase createFromParcel(Parcel in) {
            return new ModelDatabase(in);
        }

        @Override
        public ModelDatabase[] newArray(int size) {
            return new ModelDatabase[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(lokasi);
        dest.writeString(kategori);
        dest.writeString(isiLaporan);
        dest.writeString(tanggal);
        dest.writeString(foto);
        dest.writeString(status); // Tulis status
    }
}