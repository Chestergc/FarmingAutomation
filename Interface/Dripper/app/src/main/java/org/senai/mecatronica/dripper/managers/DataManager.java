package org.senai.mecatronica.dripper.managers;

import android.content.Context;


/**
 * Created by Felipe on 24/10/2017.
 */

public class DataManager {

    private DataManager(Context context) {
        super();
    }

    private static DataManager dataManager;

    public static DataManager getInstance(Context context) {
        if (dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }

}
