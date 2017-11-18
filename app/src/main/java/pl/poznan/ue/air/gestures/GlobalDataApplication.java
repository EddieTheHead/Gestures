package pl.poznan.ue.air.gestures;

import android.app.Application;

import pl.poznan.ue.air.gestures.model.GestureDatabase;

/**
 * Created by Hubert Bossy on 2017-11-15.
 */

public class GlobalDataApplication extends Application {
    public GestureDatabase getDatabase() {
        return database;
    }

    public void setDatabase(GestureDatabase database) {
        this.database = database;
    }

    GestureDatabase database = new GestureDatabase();

}