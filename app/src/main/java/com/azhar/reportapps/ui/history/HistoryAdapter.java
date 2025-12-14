package com.azhar.reportapps.ui.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.reportapps.R;
import com.azhar.reportapps.model.ModelDatabase;

import java.io.Serializable;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<ModelDatabase> modelDatabase;
    Context mContext;
    HistoryAdapterCallback mAdapterCallback; // Callback for delete action
    private int lastPosition = -1;

    public HistoryAdapter(Context context, List<ModelDatabase> modelInput, HistoryAdapterCallback adapterCallback) {
        this.mContext = context;
        this.modelDatabase = modelInput;
        this.mAdapterCallback = adapterCallback;
    }

    public void setData(List<ModelDatabase> items) {
        modelDatabase.clear();
        modelDatabase.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelDatabase data = modelDatabase.get(position);

        holder.tvKategori.setText(data.getKategori());
        holder.tvNamaJalan.setText(data.getLokasi());
        holder.tvDate.setText(data.getTanggal());

        if (data.getIsiLaporan() != null) {
            holder.tvDeskripsi.setText(data.getIsiLaporan());
        } else {
            holder.tvDeskripsi.setText("-");
        }

        // --- STATUS LOGIC ---
        String status = data.getStatus();
        if (status != null && status.equals("Selesai")) {
            holder.tvStatus.setText("Selesai");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green Text
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_green);
        } else if (status != null && (status.equals("Proses") || status.equals("Ditangani"))) {
            holder.tvStatus.setText("Ditangani");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange Text
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_orange);
        } else {
            holder.tvStatus.setText("Baru");
            holder.tvStatus.setTextColor(Color.parseColor("#2196F3")); // Blue Text
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_blue);
        }

        // 1. Normal Click -> Open Detail
        holder.layoutItem.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("DATA_LAPORAN", (Serializable) data);
            mContext.startActivity(intent);
        });

        // 2. Long Click -> Delete Action
        holder.layoutItem.setOnLongClickListener(v -> {
            if (mAdapterCallback != null) {
                mAdapterCallback.onDelete(data);
            }
            return true;
        });

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return modelDatabase.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvKategori, tvNamaJalan, tvDate, tvDeskripsi, tvStatus;
        public CardView layoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvNamaJalan = itemView.findViewById(R.id.tvNamaJalan);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layoutItem = itemView.findViewById(R.id.layoutItem);
        }
    }

    // Interface for delete callback
    public interface HistoryAdapterCallback {
        void onDelete(ModelDatabase modelDatabase);
    }
}