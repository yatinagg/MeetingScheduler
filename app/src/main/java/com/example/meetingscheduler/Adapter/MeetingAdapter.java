package com.example.meetingscheduler.Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetingscheduler.Meeting;
import com.example.meetingscheduler.R;

import java.util.List;

public class MeetingAdapter extends ArrayAdapter<Meeting> {

    /**
     * constructor for meeting mp
     *
     * @param context     the context
     * @param meetingList the list of meetings
     */
    public MeetingAdapter(@NonNull Context context, List<Meeting> meetingList) {
        super(context, 0, meetingList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }


        // get the position of the view from the ArrayAdapter
        Meeting currentNumberPosition = getItem(position);

        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            TextView tvStartEnd = currentItemView.findViewById(R.id.tv_start_end_time);
            TextView tvDesc = currentItemView.findViewById(R.id.tv_description_potrait);
            tvStartEnd.setText(String.format("%s - %s", currentNumberPosition.getStartTime(), currentNumberPosition.getEndTime()));
            tvDesc.setText(currentNumberPosition.getDescription());
        } else {
            TextView tvStart = currentItemView.findViewById(R.id.tv_start);
            TextView tvEnd = currentItemView.findViewById(R.id.tv_end);
            TextView tvLabelLand = currentItemView.findViewById(R.id.tv_description_land);
            tvStart.setText(currentNumberPosition.getStartTime());
            tvEnd.setText(currentNumberPosition.getEndTime());
            tvLabelLand.setText(currentNumberPosition.getDescription());
        }


        return currentItemView;
    }

}
