package org.senai.mecatronica.dripper.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.senai.mecatronica.dripper.beans.IrrigationData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Reads from and writes to database (JSON local file)
 * Stores all data used by the aplication
 */

public class DataManager {

    private static final String TAG = "Data Manager";

    //data labels
    private static final String LABEL_AUTO = "auto";
    private static final String LABEL_TRIGGERS_SIZE = "numberOfTriggers";
    private static final String LABEL_TRIGGERS = "triggers";
    private static final String LABEL_IRRIGATION_TYPE = "oneTime";
    private static final String LABEL_START_TIME = "startTime";
    private static final String LABEL_START_DATE = "startDate";
    private static final String LABEL_DURATION = "duration";
    private static final String LABEL_WEEKDAYS = "daysOfTheWeek";

    private static final String LABEL_LAST_IRRIGATION = "lastIrrigationTime";
    private static final String LABEL_LOG_FREQUENCY = "logFrequency";
    private static final String LABEL_NUM_LOGS = "numberOfLogs";
    private static final String LABEL_LOGS = "logs";
    private static final String LABEL_TEMPERATURE = "temperature";
    private static final String LABEL_MOISTURE = "moisture";
    private static final String LABEL_LUMINOSITY = "luminosity";
    private static final String LABEL_SOIL_MOISTURE = "soilMoisture";

    private static final String LABEL_SHAREDPREFS = "sharedPreferences";

    private static final String IRRIGATION_FILE = "default_irrigation_data.json";
    private static final String FIELD_DATA_FILE = "default_field_data.json";
    private static final String SHAREDPREFS_FILE = "org.senai.mecatronica.dripper.sharedprefs";

    //internal variables
    private Context context;
    private SharedPreferences sharedPreferences;

    //Field Data
    private Integer currentTemperature;
    private Integer currentMoisture;
    private String currentLuminosity;
    private String currentSoilMoisture;
    private String lastIrrigation;
    private String rawSensorData;
    private String lastSync;

    //Irrigation Data
    private Boolean autoMode = false;
    private List<IrrigationData> irrigationDataList;

    //singleton pattern
    private DataManager(Context context) {
        super();
        this.context = context;
        this.irrigationDataList = new ArrayList<>();
        this.rawSensorData = "";
        this.lastSync = "-";
        sharedPreferences = context.getSharedPreferences(SHAREDPREFS_FILE, Context.MODE_PRIVATE);
    }

    private static DataManager dataManager;

    public static DataManager getInstance(Context context) {
        if (dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }

    /**
     * Check if database files exist in internal memory.
     * */
    private boolean fileExists(String fileName){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    /**
     * Get data from database (JSON File) and update data manager's fields.
     * If the file does not exist, create a new default file
     * */
    public void updateIrrigationData() throws JSONException, IOException{

        if(!fileExists(IRRIGATION_FILE)){
            //create file with default settings
            writeDefaultIrrigationFile(IRRIGATION_FILE);
        }
        //get data from json file and set to variables
        String jsonString = getJSONData(IRRIGATION_FILE);
        JSONObject irrigationDataObject = new JSONObject(jsonString);
        setIrrigationData(irrigationDataObject);
    }


    /**
     * Get data from database (JSON File) and calls method to update UI elements
     * If file does not exist in database, creates a new default one
     * */
    public void updateSensorData() throws IOException, JSONException{
        if(!fileExists(FIELD_DATA_FILE)){
            //create file with default settings
            writeDefaultFieldDataFile(FIELD_DATA_FILE);
        }
        //get data from json file and set to variables
        String jsonString = getJSONData(FIELD_DATA_FILE);
        JSONObject fieldDataObject = new JSONObject(jsonString);
        setFieldData(fieldDataObject);
    }

    /**
     * Read file from internal storage and return a string with the contents of the file.
     *
     * */
    @NonNull
    private String getJSONData(String jsonFileName) throws IOException{
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
        autoMode = irrigationDataObject.getBoolean(LABEL_AUTO);
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
            int durationSecs = trigger.getInt(LABEL_DURATION);
            int hours = durationSecs / 3600;
            int minutes = (durationSecs % 3600) / 60;
            int seconds = durationSecs % 60;

            int[] durationArray = new int[]{hours,minutes,seconds};
            data.setDuration(durationArray);

            JSONArray daysOfTheWeek = trigger.getJSONArray(LABEL_WEEKDAYS);

            for(int j = 0; j < daysOfTheWeek.length(); j++){
                data.setWeekDay(daysOfTheWeek.getString(j),true);
            }
            irrigationDataList.add(data);
        }
    }

    /**
     * Read data from JSON Object and set to data manager's instance
     * */
    private void setFieldData(JSONObject fieldDataObject) throws JSONException{

        //set default values
        currentTemperature = null;
        currentMoisture = null;
        currentLuminosity = null;
        currentSoilMoisture = null;

        lastIrrigation = fieldDataObject.getString(LABEL_LAST_IRRIGATION);
        if(fieldDataObject.getInt(LABEL_NUM_LOGS) > 0){
            JSONArray logs = fieldDataObject.getJSONArray(LABEL_LOGS);
            JSONObject lastLog = logs.getJSONObject(0);
            currentTemperature = lastLog.getInt(LABEL_TEMPERATURE);
            currentMoisture = lastLog.getInt(LABEL_MOISTURE);
            currentLuminosity = lastLog.getString(LABEL_LUMINOSITY);
            currentSoilMoisture = lastLog.getString(LABEL_SOIL_MOISTURE);
        }
    }

    /**
     * Write irrigation data from manager to database
     * */
    public void writeIrrigationFile(){

        new Thread(new Runnable() {
            public void run() {
                try{
                    //create a new writer with the file name given
                    JsonWriter writer = new JsonWriter(new OutputStreamWriter(context.openFileOutput(IRRIGATION_FILE, Context.MODE_PRIVATE), "UTF-8"));
                    writer.setIndent("  ");
                    writer.beginObject();
                    //write current autoMode
                    writer.name(LABEL_AUTO).value(autoMode);

                    //write number of triggers
                    writer.name(LABEL_TRIGGERS_SIZE).value(irrigationDataList.size());

                    writer.name(LABEL_TRIGGERS);
                    //write triggers list
                    writer.beginArray();
                    for(IrrigationData data : irrigationDataList){
                        //trigger object
                        writer.beginObject();
                        writer.name(LABEL_IRRIGATION_TYPE).value(data.getOneTime());
                        writer.name(LABEL_START_TIME).value(data.getStartTime());
                        writer.name(LABEL_START_DATE).value(data.getStartDate());
                        int[] durationArray = data.getDuration();
                        //convert hours, mins, secs to secs
                        int finalDuration = (durationArray[0]*3600)+(durationArray[1]*60)+durationArray[2];
                        writer.name(LABEL_DURATION).value(finalDuration);
                        //weekdays array
                        writer.name(LABEL_WEEKDAYS);
                        writer.beginArray();
                        for(String weekDay : data.getWeekDays().keySet()){
                            if(data.getWeekDays().get(weekDay)){
                                writer.value(weekDay);
                            }
                        }
                        writer.endArray();
                        writer.endObject();
                    }
                    writer.endArray();
                    writer.endObject();

                    writer.flush();
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void writeFieldDataFile(final String lastIrrigation, final int numLogs, final int temperature, final int moisture, final String luminosity, final String soilMoisture){

        new Thread(new Runnable() {
            public void run() {
                try{
                    //create a new writer with the file name given
                    JsonWriter writer = new JsonWriter(new OutputStreamWriter(context.openFileOutput(FIELD_DATA_FILE, Context.MODE_PRIVATE), "UTF-8"));
                    writer.setIndent("  ");
                    writer.beginObject();
                    //write last irrigation time
                    writer.name(LABEL_LAST_IRRIGATION).value(lastIrrigation);

                    //write log frequency
                    writer.name(LABEL_LOG_FREQUENCY).value(60);

                    //write log list size
                    writer.name(LABEL_NUM_LOGS).value(numLogs);

                    //write logs list
                    writer.name(LABEL_LOGS);
                    writer.beginArray();
                    for(int i = 0; i < numLogs; i++){
                        writer.beginObject();
                        writer.name(LABEL_TEMPERATURE).value(temperature);
                        writer.name(LABEL_MOISTURE).value(moisture);
                        writer.name(LABEL_LUMINOSITY).value(luminosity);
                        writer.name(LABEL_SOIL_MOISTURE).value(soilMoisture);
                        writer.endObject();
                    }
                    writer.endArray();
                    writer.endObject();

                    writer.flush();
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * Write default irrigation data to database
     * */
    private void writeDefaultIrrigationFile(final String jsonFileName){

        new Thread(new Runnable() {
            public void run() {
                try{
                    //create a new writer with the file name given
                    JsonWriter writer = new JsonWriter(new OutputStreamWriter(context.openFileOutput(jsonFileName, Context.MODE_PRIVATE), "UTF-8"));
                    writer.setIndent("  ");
                    writer.beginObject();
                    //write current autoMode
                    writer.name(LABEL_AUTO).value(false);

                    //write number of triggers
                    writer.name(LABEL_TRIGGERS_SIZE).value(0);

                    //write triggers list
                    writer.name(LABEL_TRIGGERS);
                    writer.beginArray();
                    writer.endArray();
                    writer.endObject();

                    writer.flush();
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * Write default field data to database
     * */
    private void writeDefaultFieldDataFile(final String jsonFileName){
//        {
//        "lastIrrigationTime":"-",
//        "logFrequency":0,
//        "numberOfLogs":0,
//        "logs": []
//    }

        new Thread(new Runnable() {
            public void run() {
                try{
                    //create a new writer with the file name given
                    JsonWriter writer = new JsonWriter(new OutputStreamWriter(context.openFileOutput(jsonFileName, Context.MODE_PRIVATE), "UTF-8"));
                    writer.setIndent("  ");
                    writer.beginObject();
                    //write last irrigation time
                    writer.name(LABEL_LAST_IRRIGATION).value("-");

                    //write log frequency
                    writer.name(LABEL_LOG_FREQUENCY).value(0);

                    //write log list size
                    writer.name(LABEL_NUM_LOGS).value(0);

                    //write logs list
                    writer.name(LABEL_LOGS);
                    writer.beginArray();
                    writer.endArray();
                    writer.endObject();

                    writer.flush();
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }



    public List<IrrigationData> getIrrigationDataList(){
        return this.irrigationDataList;
    }

    public void addIrrigationData(IrrigationData data){
        irrigationDataList.add(data);
        writeIrrigationFile();
    }

    public void removeIrrigationData(int id){
        if(!irrigationDataList.isEmpty() && irrigationDataList.size()>id){
            irrigationDataList.remove(id);
            writeIrrigationFile();
        }
    }

    public void replaceIrrigationData(Integer id, IrrigationData data){
        irrigationDataList.set(id, data);
        writeIrrigationFile();
    }

    public void clearIrrigationData(){
        irrigationDataList = new ArrayList<>();
        writeIrrigationFile();
    }

    public Boolean getAutoMode(){
        return autoMode;
    }

    public void setAutoMode(Boolean autoMode){
        this.autoMode = autoMode;
        writeIrrigationFile();
    }

    public IrrigationData getIrrigationData(int id){
        return irrigationDataList.get(id);
    }

    public int getId(IrrigationData data){
        return irrigationDataList.indexOf(data);
    }

    public Integer getCurrentTemperature() {
        return currentTemperature;
    }

    public Integer getCurrentMoisture() {
        return currentMoisture;
    }

    public String getCurrentLuminosity() {
        return currentLuminosity;
    }

    public String getCurrentSoilMoisture() {
        return currentSoilMoisture;
    }

    public String getLastIrrigation(){
        return lastIrrigation;
    }

    public void clearDataFiles(){
        //TODO erase all json files from memory

    }

    public void setMacAddress(String address){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LABEL_SHAREDPREFS, address);
        editor.apply();
    }

    public String getMacAddress(){
        return sharedPreferences.getString(LABEL_SHAREDPREFS, "00:00:00:00:00:00");
    }

    public Uri getIrrigationDataUri(){
        return Uri.fromFile(context.getFileStreamPath(IRRIGATION_FILE));
    }

    public void appendSensorData(String data, boolean msgOver){
        rawSensorData += data;
        if(msgOver){
            try{
                parseRawSensorData(rawSensorData);
            } catch (IOException e){
                Log.e(TAG, "Error parsing data");
            }

            rawSensorData = "";
        }
    }

    private void parseRawSensorData(String sensorData) throws IOException{
        //System.out.println(sensorData);
        String lastIrrigation = "";
        int numLogs;
        int temperature = 0;
        int moisture = 0;
        String luminosity = "day";
        String soilMoisture = "low";

        //parser for sensor data
        JsonReader reader = new JsonReader(new StringReader(sensorData));
        reader.beginObject();       //{
            reader.nextName();      //"logFrequency"
            reader.nextInt();       //60
            reader.nextName();      //numberOfLogs
            numLogs = reader.nextInt();       //1
            reader.nextName();      //"logs"
            reader.beginArray();    //[
            for(int i = 0; i < numLogs; i++){
                reader.beginObject(); //{
                    reader.nextName();      //"date"
                    reader.nextString();       // %2d/%2d/%4d
                    reader.nextName();      //"time"
                    reader.nextString();       // %2d:%2d:%2d
                    reader.nextName();      //"numberOfSensors"
                    int numSensors = reader.nextInt();       // 4
                    reader.nextName();      //"sensors"
                    reader.beginArray();    //[
                    for(int j = 0; j < numSensors; j++){
                        reader.beginObject(); //{
                        reader.nextName();      //"name"
                        String name = reader.nextString(); // sensorName
                        reader.nextName(); //"data"
                        switch (name){
                            case "Temperature":
                                temperature = reader.nextInt(); //25
                                reader.nextName(); //"unit"
                                reader.nextString(); //"°C"
                                break;
                            case "Moisture":
                                moisture = reader.nextInt(); //32
                                reader.nextName(); //"unit"
                                reader.nextString(); //"%"
                                break;
                            case "Luminosity":
                                luminosity = reader.nextString(); //Day
                                reader.nextName(); //"unit"
                                reader.nextString(); //""
                                break;
                            case "Soil Moisture":
                                soilMoisture = reader.nextString(); //Low
                                reader.nextName(); //"unit"
                                reader.nextString(); //""
                                break;
                        }
                        reader.endObject(); //}
                    }
                    reader.endArray();
                reader.endObject(); //}
            }
            reader.endArray(); //]
        reader.endObject(); //}
        reader.close();

        //writeFieldDataFile(lastIrrigation,numLogs,temperature,moisture,luminosity,soilMoisture);
    }

    public void setLastSync(String time){
        lastSync = time;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void testDataParser(){
        String testString = "{\n" +
                "\t\"logFrequency\":60,\n" +
                "\t\"numberOfLogs\":1,\n" +
                "\t\"logs\":\n" +
                "\t[\n" +
                "\t\t{\n" +
                "\t\t\t\"date\":\"12/11/2017\",\n" +
                "\t\t\t\"time\":\"06:11:00\",\n" +
                "\t\t\t\"numberOfSensors\":4,\n" +
                "\t\t\t\"sensors\":\n" +
                "\t\t\t[\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"name\":\"Temperature\",\n" +
                "\t\t\t\t\t\"data\":22,\n" +
                "\t\t\t\t\t\"unit\":\"°C\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"name\":\"Moisture\",\n" +
                "\t\t\t\t\t\"data\":35,\n" +
                "\t\t\t\t\t\"unit\":\"%\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"name\":\"Luminosity\",\n" +
                "\t\t\t\t\t\"data\":Day,\n" +
                "\t\t\t\t\t\"unit\":\"Lux\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"name\":\"Soil Moisture\",\n" +
                "\t\t\t\t\t\"data\":\"Low\",\n" +
                "\t\t\t\t\t\"unit\":\"\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
        try{
            parseRawSensorData(testString);
        } catch (IOException e){
            Log.e("Test Parser", "Error parsing data");
        }

    }
}
