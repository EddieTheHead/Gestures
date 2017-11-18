package pl.poznan.ue.air.gestures.services;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import pl.poznan.ue.air.gestures.model.GestureDatabase;

/**
 * Created by Hubert Bossy on 2017-11-18.
 */

public class FileAccessService {
    public FileAccessService(GestureDatabase database) {
        this.database = database;
    }

    public FileAccessService() {
        this.database = new GestureDatabase();
    }

    private GestureDatabase database;

    public GestureDatabase readDataBaseFromJsonFile(){
        StringBuffer jsonStringBuffer = new StringBuffer();

        String pathStr = Environment.getExternalStorageDirectory() + File.separator  + "SavedGestures/Gestures.JSON";
        File file = new File(pathStr);
        if(!file.exists()) return new GestureDatabase();
        else try {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);

                String readString = reader.readLine();
                while ( readString != null ) {
                    jsonStringBuffer.append(readString);
                    readString = reader.readLine();
                }
                isr.close();
                database = database.fromJSON(jsonStringBuffer.toString());
            } catch (IOException e) {
                Log.e("Exception", "File read failed: " + e.toString());
            }
            return new GestureDatabase();
    }

    public void saveDatabaseToJsonFile(){
        String jsonString = database.toJSON();
        String pathStr = Environment.getExternalStorageDirectory() + File.separator  + "SavedGestures";
        final File path = new File(pathStr);
        if(!path.exists()) path.mkdirs();

        final File file = new File(path, "Gestures.JSON");
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(jsonString);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
