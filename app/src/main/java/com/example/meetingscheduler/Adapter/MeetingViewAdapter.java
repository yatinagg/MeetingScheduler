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

import com.example.meetingscheduler.MeetingView;
import com.example.meetingscheduler.R;

import java.util.List;

public class MeetingViewAdapter extends ArrayAdapter<MeetingView> {

    public MeetingViewAdapter(@NonNull Context context, List<MeetingView> meetingList) {
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
        MeetingView currentNumberPosition = getItem(position);

        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            TextView textView1 = currentItemView.findViewById(R.id.tv_start_end_time);
            TextView textView2 = currentItemView.findViewById(R.id.tv_description_potrait);
            textView1.setText(String.format("%s - %s", currentNumberPosition.getMeeting().getStartTime(), currentNumberPosition.getMeeting().getEndTime()));
            textView2.setText(currentNumberPosition.getMeeting().getDescription());
        } else {
            TextView textView1Start = currentItemView.findViewById(R.id.tv_start);
            TextView textView1End = currentItemView.findViewById(R.id.tv_end);
            TextView textViewLabel2Land = currentItemView.findViewById(R.id.tv_description_land);
            textView1Start.setText(currentNumberPosition.getMeeting().getStartTime());
            textView1End.setText(currentNumberPosition.getMeeting().getEndTime());
            textViewLabel2Land.setText(currentNumberPosition.getMeeting().getDescription());
        }


        return currentItemView;
    }

}
