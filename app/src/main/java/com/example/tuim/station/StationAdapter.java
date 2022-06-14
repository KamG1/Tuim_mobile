package com.example.tuim.station;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuim.ClientAPI;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.example.tuim.tanks.TankHistoryAdapter;
import com.example.tuim.tanks.TankRecordToRequest;
import com.example.tuim.user.UserData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<StationAdapter.ViewHolder> {
    public static final String STATION_RECORD = "STATION_RECORD";
    public static final String FIRST_CO = "FIRST_CO";
    public static final String SECOND_CO = "SECOND_CO";
    public static final String USER = "USER";

    private List<StationRecord> stationList;
    private StationRecord stationRecord;
    private Context context;
    private Double distance;
    private Double firstCoordinatePerson;
    private Double secondCoordinatePerson;
    private ArrayList<Double> distances;
    private UserData user;


    public StationAdapter(Context context, List<StationRecord> stationList, double firstCo, double secondCo, ArrayList<Double> distances, UserData user) {
        this.stationList = stationList;
        this.context = context;
        this.firstCoordinatePerson = firstCo;
        this.secondCoordinatePerson = secondCo;
        this.distances = distances;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_station_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        stationRecord = stationList.get(position);
        distance = distances.get(position);
        switch (stationRecord.getName()) {
            case "Orlen":
                holder.stationImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_orlen, null));
                break;
            case "Lotos":
                holder.stationImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lotos, null));
                break;
            case "Statoil":
            case "CycleK":
                holder.stationImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_cycle, null));
                break;
            case "Moya":
                holder.stationImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_moya, null));
                break;
            default:
                holder.stationImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_tank, null));
                break;
        }
        holder.topLabelTextView.setText("P95 : " + stationRecord.getCostBenz());
        holder.secondLabelTextView.setText("ON: " + stationRecord.getCostON());
        holder.thirdLabelTextView.setText("LPG: " + stationRecord.getCostLPG());
        holder.leftLabelTextView.setText("Nazwa: " + stationRecord.getName());
        holder.rightLabelTextView.setText("Odleglosc: " + distance + " km");
        holder.pensilImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditStationActivity.class);
            intent.putExtra(STATION_RECORD, stationList.get(position));
            intent.putExtra(USER, user);
            context.startActivity(intent);
        });
        holder.trashImageView.setOnClickListener(v -> {
            StationRecord record = new StationRecord(stationList.get(position).getId(),stationList.get(position).getName(),stationList.get(position).getLan(),
                    stationList.get(position).getLat(),stationList.get(position).getCostON(),stationList.get(position).getCostBenz()
            ,stationList.get(position).getCostLPG());
            deleteStation(record);
            stationList.remove(position);
            StationAdapter.this.notifyDataSetChanged();
        });
        holder.chooseStationButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra(STATION_RECORD, stationList.get(position));
            intent.putExtra(FIRST_CO, firstCoordinatePerson);
            intent.putExtra(SECOND_CO, secondCoordinatePerson);
            context.startActivity(intent);
        });
    }
    private void deleteStation(StationRecord record) {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.deleteStation(record);
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
        if (stationList == null) {
            return 0;
        } else
            return stationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView topLabelTextView;
        protected TextView secondLabelTextView;
        protected TextView thirdLabelTextView;
        protected TextView leftLabelTextView;
        protected TextView rightLabelTextView;
        protected ImageView pensilImageView;
        protected ImageView trashImageView;
        protected ImageView stationImageView;
        protected Button chooseStationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stationImageView = itemView.findViewById(R.id.station_imageView);
            this.pensilImageView = itemView.findViewById(R.id.pensil_imageView);
            this.chooseStationButton = itemView.findViewById(R.id.button_choose_station);
            this.trashImageView=itemView.findViewById(R.id.trash_imageView2);

            this.topLabelTextView = itemView.findViewById(R.id.top_label_station);
            this.secondLabelTextView = itemView.findViewById(R.id.second_label_station);
            this.thirdLabelTextView = itemView.findViewById(R.id.third_label_station);
            this.leftLabelTextView = itemView.findViewById(R.id.left_label_station);
            this.rightLabelTextView = itemView.findViewById(R.id.right_label_station);
        }
    }
}
