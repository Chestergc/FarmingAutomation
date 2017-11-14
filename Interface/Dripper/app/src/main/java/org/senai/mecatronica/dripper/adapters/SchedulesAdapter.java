package org.senai.mecatronica.dripper.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.activities.IrrigationEditor;
import org.senai.mecatronica.dripper.activities.IrrigationFragment;
import org.senai.mecatronica.dripper.activities.MainActivity;
import org.senai.mecatronica.dripper.beans.IrrigationData;
import org.senai.mecatronica.dripper.managers.DataManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Felipe on 05/11/2017.
 */

public class SchedulesAdapter extends BaseAdapter {

    private Context context;
    private List<IrrigationData> irrigationDataList;

    //initialize view elements
    private IrrigationData data;
    private TextView irrigationType;
    private TextView startTime;
    private TextView duration ;
    private HashMap<String, TextView> weekMap;
    private ImageButton optionsBtn;

    private IrrigationEditor irrigationEditor;

    public SchedulesAdapter(Context context, List<IrrigationData> irrigationDataList, IrrigationEditor editor){
        this.context = context;
        this.irrigationDataList = irrigationDataList;
        this.irrigationEditor = editor;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.item_schedule, null);
        }

        initializeViewElements(position, convertView);
        setupClickListeners();

        //set type of irrigation
        if(data.getOneTime()){
            irrigationType.setText(context.getString(R.string.one_time_irrigation));
        }else{
            irrigationType.setText(context.getString(R.string.scheduled_irrigation));
        }

        //set time and duration texts
        startTime.setText(data.getStartTime());
        StringBuilder durationString = new StringBuilder();
        durationString.append(data.getDuration().get(0).toString());
        durationString.append("h ");
        durationString.append(data.getDuration().get(1).toString());
        durationString.append("m ");
        durationString.append(data.getDuration().get(2).toString());
        durationString.append("s");

        duration.setText(durationString.toString());

        //put colors to selected weekdays
        HashMap<String, Boolean> weekdays = data.getWeekDays();
        for(String weekday : weekdays.keySet()){
            if(weekdays.get(weekday)){
                weekMap.get(weekday).setTextColor(ContextCompat.getColor(this.context, R.color.colorAccent));
            } else {
                weekMap.get(weekday).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
            }
        }

        return convertView;
    }

    private void initializeViewElements(int position, View convertView){
        data = getItem(position);
        optionsBtn = (ImageButton) convertView.findViewById(R.id.img_btn_item_schedule_menu);
        optionsBtn.setTag(position);
        irrigationType = (TextView) convertView.findViewById(R.id.txt_scheduled_irrigation);
        startTime = (TextView) convertView.findViewById(R.id.txt_schedule_start_time);
        duration = (TextView) convertView.findViewById(R.id.txt_schedule_duration);
        weekMap = new HashMap<>(7);
        weekMap.put("Sunday", (TextView) convertView.findViewById(R.id.txt_schedule_sunday));
        weekMap.put("Monday",(TextView) convertView.findViewById(R.id.txt_schedule_monday));
        weekMap.put("Tuesday",(TextView) convertView.findViewById(R.id.txt_schedule_tuesday));
        weekMap.put("Wednesday",(TextView) convertView.findViewById(R.id.txt_schedule_wednesday));
        weekMap.put("Thursday",(TextView) convertView.findViewById(R.id.txt_schedule_thursday));
        weekMap.put("Friday",(TextView) convertView.findViewById(R.id.txt_schedule_friday));
        weekMap.put("Saturday",(TextView) convertView.findViewById(R.id.txt_schedule_saturday));
    }

    private void setupClickListeners(){
        //initialize clickers
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit menu passing data
                final PopupMenu popup = new PopupMenu(context, v);
                final int irrigationDataId = (Integer) v.getTag();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if(id == R.id.menu_schedule_item_edit){
                            irrigationEditor.editIrrigation(irrigationDataId);
                        } else if(id == R.id.menu_schedule_item_delete){
                            irrigationEditor.deleteIrrigation(irrigationDataId);
                        }
                        return false;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_irrigation_item, popup.getMenu());
                popup.show();
            }
        });
    }
}
