package org.senai.mecatronica.dripper.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.beans.IrrigationData;

import java.util.List;

/**
 * Created by Felipe on 05/11/2017.
 */

public class SchedulesAdapter extends BaseAdapter {

    private Context context;
    private List<IrrigationData> irrigationDataList;

    public SchedulesAdapter(Context context, List<IrrigationData> irrigationDataList){
        this.context = context;
        this.irrigationDataList = irrigationDataList;
    }

    @Override
    public int getCount() {

        return irrigationDataList.size();
    }

    @Override
    public IrrigationData getItem(int position) {

        return irrigationDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.item_schedule, null);
        }

        //initialize view elements
        IrrigationData data = getItem(position);

        TextView irrigationType = (TextView) convertView.findViewById(R.id.txt_scheduled_irrigation);
        TextView startTime = (TextView) convertView.findViewById(R.id.txt_schedule_start_time);
        TextView duration = (TextView) convertView.findViewById(R.id.txt_schedule_duration);

        TextView monday = (TextView) convertView.findViewById(R.id.txt_schedule_monday);
        TextView tuesday = (TextView) convertView.findViewById(R.id.txt_schedule_tuesday);
        TextView wednesday = (TextView) convertView.findViewById(R.id.txt_schedule_wednesday);
        TextView thursday = (TextView) convertView.findViewById(R.id.txt_schedule_thursday);
        TextView friday = (TextView) convertView.findViewById(R.id.txt_schedule_friday);
        TextView saturday = (TextView) convertView.findViewById(R.id.txt_schedule_saturday);
        TextView sunday = (TextView) convertView.findViewById(R.id.txt_schedule_sunday);

        ImageButton optionsBtn = (ImageButton) convertView.findViewById(R.id.img_btn_item_schedule_menu);

        //initialize clickers
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit menu passing data
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_irrigation_item, popup.getMenu());
                popup.show();
            }
        });

        //set type of irrigation
        if(data.getOneTime()){
            irrigationType.setText(context.getString(R.string.one_time_irrigation));
        }else{
            irrigationType.setText(context.getString(R.string.scheduled_irrigation));
        }

        //set time and duration texts
        startTime.setText(data.getStartTime());
        duration.setText(data.getDuration().toString()+"s");

        //reset colors
        monday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
        tuesday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
        wednesday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
        thursday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
        friday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
        saturday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
        sunday.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));

        //put colors to selected weekdays
        List<String> weekdays = data.getWeekDays();
        for(String weekday : weekdays){
            switch (weekday){
                case "Monday":
                    monday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
                case "Tuesday":
                    tuesday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
                case "Wednesday":
                    wednesday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
                case "Thursday":
                    thursday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
                case "Friday":
                    friday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
                case "Saturday":
                    saturday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
                case "Sunday":
                    sunday.setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                    break;
            }
        }

        return convertView;
    }
}
