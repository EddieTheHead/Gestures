package pl.poznan.ue.air.gestures;

import android.app.Application;

import pl.poznan.ue.air.gestures.model.GestureDatabase;
import pl.poznan.ue.air.gestures.services.FileAccessService;

/**
 * Created by Hubert Bossy on 2017-11-15.
 */

public class GlobalDataApplication extends Application {
    public GestureDatabase database;

    public GlobalDataApplication() {
        FileAccessService fas = new FileAccessService();
        this.database = fas.readDataBaseFromJsonFile();
    }

    public GlobalDataApplication(GestureDatabase database) {
        this.database = database;
    }

    public GestureDatabase getDatabase() {
        return database;
    }

    public void setDatabase(GestureDatabase database) {
        this.database = database;
    }


}
