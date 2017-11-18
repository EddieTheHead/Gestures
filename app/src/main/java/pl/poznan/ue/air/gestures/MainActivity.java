package pl.poznan.ue.air.gestures;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import pl.poznan.ue.air.gestures.model.Gesture;
import pl.poznan.ue.air.gestures.model.GestureDatabase;
import io.github.cawfree.dtw.alg.DTW;


// TODO: 2017-11-18 Add communication with PC

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private GestureDatabase database;
    private Button toGesturesGalleryButton;

    private List<Float> accelerationX;
    private List<Float> accelerationY;
    private List<Float> accelerationZ;
    private Boolean recordingMovement = false;
    private SensorManager sensorManager;

    /** Converts a List of Floats into a primitive equivalent. */
    private static final float[] primitive(final List<Float> pList) {
        // Declare the Array.
        final float[] lT = new float[pList.size()];
        // Iterate the List.
        for(int i = 0; i < pList.size(); i++) {
            // Buffer the Element.
            lT[i] = pList.get(i);
        }
        // Return the Array.
        return lT;
    }

    private final float TRESHOLD = 10.0f;

    private Gesture findMatchingGesture(){
        List<Gesture> gestures = database.getAll();
        DTW dtw = new DTW();
        for(Gesture g : gestures){

            if(dtw.compute(primitive(accelerationX),primitive(g.getTraceX())).getDistance() < TRESHOLD &&
                dtw.compute(primitive(accelerationY),primitive(g.getTraceY())).getDistance() < TRESHOLD &&
                    dtw.compute(primitive(accelerationZ),primitive(g.getTraceZ())).getDistance() < TRESHOLD){
                return g;
            }
            else {
                double x = dtw.compute(primitive(accelerationX),primitive(g.getTraceX())).getDistance();
                double y = dtw.compute(primitive(accelerationY),primitive(g.getTraceY())).getDistance();
                double z = dtw.compute(primitive(accelerationZ),primitive(g.getTraceZ())).getDistance();
                Toast.makeText(MainActivity.this, ("x " + x + " y " + y + " z " + z + " s1 "
                        + accelerationX.size() + " s2 "
                        + g.getTraceX().size()) , Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }

    public void init(){
        toGesturesGalleryButton = findViewById(R.id.toGesturesGallery);
        toGesturesGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryView = new Intent(MainActivity.this, GesturesGalleryActivity.class);
                startActivity(galleryView);
            }
        });
        Button recordButton = findViewById(R.id.buttonRecognise);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN: {
                        recordingMovement = true;
                        accelerationX.clear();
                        accelerationY.clear();
                        accelerationZ.clear();
                        return true;
                    }
                    case MotionEvent.ACTION_UP:{
                        if(findMatchingGesture() != null)
                            Toast.makeText(MainActivity.this, ("Success"), Toast.LENGTH_LONG).show();
                        else Toast.makeText(MainActivity.this, ("Not found"), Toast.LENGTH_LONG).show();
                        //// TODO: 2017-11-18 Run action associated with found gesture
                        //// TODO: 2017-11-18 Add shplash screen with action symbol
                        return true;
                    }
                }
                return false;
            }
        });
        accelerationX = new ArrayList<>(64);
        accelerationY = new ArrayList<>(64);
        accelerationZ = new ArrayList<>(64);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = ((GlobalDataApplication) this.getApplication()).getDatabase();
        this.sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        init();
    }

    @Override
    public void onSensorChanged (SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && recordingMovement) {
            accelerationX.add(event.values[0]);
            accelerationY.add(event.values[1]);
            accelerationZ.add(event.values[2]);
            if(accelerationX.size() > 64){
                accelerationX.remove(0);
                accelerationY.remove(0);
                accelerationZ.remove(0);
            }
        }
    }

    @Override
    protected final void onResume() {
        // Implement the Parent Definition.
        super.onResume();
        // Register for updates on the SensorManager. (We want to listen to accelerometer data.)
        sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected final void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor s, int acc){ }
}
