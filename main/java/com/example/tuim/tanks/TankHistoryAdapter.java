package com.example.tuim.tanks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuim.ClientAPI;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TankHistoryAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<TankHistoryAdapter.ViewHolder>{
    private List<TankUpRecord> tankList;
    private TankUpRecord tankUpRecord;
    private Context context;

    public TankHistoryAdapter(Context context, List<TankUpRecord> tankList) {
        this.tankList = tankList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tankUpRecord = tankList.get(position);
        holder.activityImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_tank, null));
        DateFormat dateFormat = DateFormat.getDateInstance();
        if (tankUpRecord.getTankUpDate() != null) {
            holder.topLabelTextView.setText(context.getString(R.string.date) + " " + dateFormat.format(tankUpRecord.getTankUpDate()));
        } else {
            holder.topLabelTextView.setText(context.getString(R.string.date) + " No date");
        }
        holder.secondLabelTextView.setText(context.getString(R.string.millage) + " " + tankUpRecord.getMillage().toString() + " km");
        holder.thirdLabelTextView.setText(context.getString(R.string.liters) + " " + tankUpRecord.getLiters() + " L");
        holder.bottomLabelTextView.setText(context.getString(R.string.cost) + " " + tankUpRecord.getCostInPLN() + " PLN");

        holder.trashImageView.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.GERMANY);
            TankRecordToRequest record = new TankRecordToRequest(tankList.get(position).getIdTankRecords(), sdf.format(tankList.get(position).getTankUpDate()),
                    tankList.get(position).getMillage(), tankList.get(position).getLiters(),
                    tankList.get(position).getCostInPLN(), tankList.get(position).getFkAuto());
            deleteTank(record);
            tankList.remove(position);
            TankHistoryAdapter.this.notifyDataSetChanged();
        });
    }

    private void deleteTank(TankRecordToRequest record) {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.deleteTank(record);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tankList == null) {
            return 0;
        } else
            return tankList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView topLabelTextView;
        protected TextView secondLabelTextView;
        protected TextView thirdLabelTextView;
        protected TextView bottomLabelTextView;
        protected ImageView trashImageView;
        protected ImageView activityImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.activityImageView = itemView.findViewById(R.id.activity_imageView);
            this.trashImageView = itemView.findViewById(R.id.trash_imageView);

            this.topLabelTextView = itemView.findViewById(R.id.top_label);
            this.secondLabelTextView = itemView.findViewById(R.id.second_label);
            this.thirdLabelTextView = itemView.findViewById(R.id.third_label);
            this.bottomLabelTextView = itemView.findViewById(R.id.bottom_label);
        }
    }
}
