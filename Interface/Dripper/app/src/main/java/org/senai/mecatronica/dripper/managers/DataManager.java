package org.senai.mecatronica.dripper.managers;

import android.content.Context;
import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.senai.mecatronica.dripper.beans.IrrigationData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Reads from and writes to database (JSON local file)
 *
 * Created by Felipe on 24/10/2017.
 */

public class DataManager {

    //data labels
    private static final String LABEL_AUTO = "auto";
    private static final String LABEL_TRIGGERS_SIZE = "numberOfTriggers";
    private static final String LABEL_TRIGGERS = "auto";
    private static final String LABEL_IRRIGATION_TYPE = "oneTime";
    private static final String LABEL_START_TIME = "startTime";
    private static final String LABEL_START_DATE = "startDate";
    private static final String LABEL_DURATION = "duration";
    private static final String LABEL_WEEKDAYS = "daysOfTheWeek";

    //internal variables
    private Context context;
    private String irrigationFileName;
    private String fieldDataFileName;
//    private Boolean readSuccessful;

    //Field Data
    private Integer currentTemperature;
    private Integer currentMoisture;
    private Integer currentLuminosity;
    private String currentSoilMoisture;
//    private List<FieldData> historicalDataList;

    //Irrigation Data
    Boolean autoMode = false;
    List<IrrigationData> irrigationDataList;

    //singleton pattern
    private DataManager(Context context, String irrigationFileName, String fieldDataFileName) {
        super();
        this.context = context;
        this.irrigationFileName = irrigationFileName;
        this.fieldDataFileName = fieldDataFileName;
//        this.readSuccessful = false;
        irrigationDataList = new ArrayList<>();
    }

    private static DataManager dataManager;

    public static DataManager getInstance(Context context, String irrigationFileName, String fieldDataFileName) {
        if (dataManager == null) {
            dataManager = new DataManager(context, irrigationFileName, fieldDataFileName);
        }
        return dataManager;
    }

    /**
     * Get data from database (JSON File) and update data manager's fields.
     * */
    public void updateIrrigationData() throws JSONException, IOException{
        //get data from json file and set to variables
        String jsonString = getJSONData(context, irrigationFileName);
        JSONObject irrigationDataObject = new JSONObject(jsonString);
        setIrrigationData(irrigationDataObject);

    }

    /**
     * Read file from internal storage and return a string with the contents of the file.
     * */
    private String getJSONData(Context context, String jsonFileName) throws IOException{
        String strJSON;
        StringBuilder buf = new StringBuilder();
        FileInputStream json;

        json = context.openFileInput(jsonFileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
        while ((strJSON = in.readLine()) != null) {
            buf.append(strJSON);
        }
        in.close();

        return buf.toString();
    }

    /**
     * Read data from JSON Object and set to data manager's instance
     * */
    private void setIrrigationData(JSONObject irrigationDataObject) throws JSONException{
        setAutoMode(irrigationDataObject.getBoolean(LABEL_AUTO));
        Integer numberOfTriggers = irrigationDataObject.getInt(LABEL_TRIGGERS_SIZE);
        JSONArray triggers = irrigationDataObject.getJSONArray(LABEL_TRIGGERS);

        //reset old list
        irrigationDataList = new ArrayList<>();

        //set new values
        for(int i = 0; i < numberOfTriggers; i++){
            JSONObject trigger = triggers.getJSONObject(i);
            IrrigationData data = new IrrigationData();
            data.setOneTime(trigger.getBoolean(LABEL_IRRIGATION_TYPE));
            data.setStartTime(trigger.getString(LABEL_START_TIME));
            data.setStartDate(trigger.getString(LABEL_START_DATE));
            data.setDuration(trigger.getInt(LABEL_DURATION));

            JSONArray daysOfTheWeek = trigger.getJSONArray(LABEL_WEEKDAYS);
            List<String> weekDays = new ArrayList<>();
            for(int j = 0; j < daysOfTheWeek.length(); j++){
                weekDays.add(daysOfTheWeek.getString(j));
            }
            data.setWeekDays(weekDays);
            irrigationDataList.add(data);
        }
    }

    public List<IrrigationData> getIrrigationDataList(){
        return this.irrigationDataList;
    }

    public Boolean getAutoMode(){
        return autoMode;
    }

    public void setAutoMode(Boolean autoMode){
        this.autoMode = autoMode;
    }

    private void writeIrrigationFile(Context context, String jsonFileName) throws IOException{

        //create a new writer with the file name given
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(context.openFileOutput(jsonFileName, Context.MODE_PRIVATE), "UTF-8"));
        writer.setIndent("  ");
        writer.beginObject();
            //write current autoMode
            writer.name(LABEL_AUTO).value(autoMode);

            //write number of triggers
            writer.name(LABEL_TRIGGERS_SIZE).value(irrigationDataList.size());

            //write triggers list
            writer.beginArray();
            for(IrrigationData data : irrigationDataList){
                //trigger object
                writer.beginObject();
                    writer.name(LABEL_IRRIGATION_TYPE).value(data.getOneTime());
                    writer.name(LABEL_START_TIME).value(data.getStartTime());
                    writer.name(LABEL_START_DATE).value(data.getStartDate());
                    writer.name(LABEL_DURATION).value(data.getDuration());
                    //weekdays array
                    writer.name(LABEL_WEEKDAYS);
                    writer.beginArray();
                    List<String> weekDays = data.getWeekDays();
                    for(String weekDay : weekDays){
                        writer.value(weekDay);
                    }
                    writer.endArray();
                writer.endObject();
            }
            writer.endArray();
        writer.endObject();

        writer.flush();
        writer.close();
    }

}
