package com.example.tuim.station;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuim.R;
import com.example.tuim.user.UserData;

import java.util.ArrayList;
import java.util.List;

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
        protected ImageView stationImageView;
        protected Button chooseStationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stationImageView = itemView.findViewById(R.id.station_imageView);
            this.pensilImageView = itemView.findViewById(R.id.pensil_imageView);
            this.chooseStationButton = itemView.findViewById(R.id.button_choose_station);

            this.topLabelTextView = itemView.findViewById(R.id.top_label_station);
            this.secondLabelTextView = itemView.findViewById(R.id.second_label_station);
            this.thirdLabelTextView = itemView.findViewById(R.id.third_label_station);
            this.leftLabelTextView = itemView.findViewById(R.id.left_label_station);
            this.rightLabelTextView = itemView.findViewById(R.id.right_label_station);
        }
    }
}
