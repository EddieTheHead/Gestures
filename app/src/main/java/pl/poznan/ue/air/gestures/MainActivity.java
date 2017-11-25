package pl.poznan.ue.air.gestures;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.ue.air.gestures.model.Gesture;
import pl.poznan.ue.air.gestures.model.Gesture.Actions;
import pl.poznan.ue.air.gestures.model.GestureDatabase;
import io.github.cawfree.dtw.alg.DTW;


// TODO: 2017-11-18 Add communication with PC

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private GestureDatabase database;

    private List<Float> accelerationX;
    private List<Float> accelerationY;
    private List<Float> accelerationZ;
    private Boolean recordingMovement = false;
    private SensorManager sensorManager;

    private static float[] primitive(final List<Float> pList) {
        final float[] lT = new float[pList.size()];
        for(int i = 0; i < pList.size(); i++) {
            lT[i] = pList.get(i);
        }
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
//            else {
//                double x = dtw.compute(primitive(accelerationX),primitive(g.getTraceX())).getDistance();
//                double y = dtw.compute(primitive(accelerationY),primitive(g.getTraceY())).getDistance();
//                double z = dtw.compute(primitive(accelerationZ),primitive(g.getTraceZ())).getDistance();
//                Toast.makeText(MainActivity.this, ("x " + x + " y " + y + " z " + z + " s1 "
//                        + accelerationX.size() + " s2 "
//                        + g.getTraceX().size()) , Toast.LENGTH_LONG).show();
//            }
        }
        return null;
    }

    public void init() {
        Button toGesturesGalleryButton;
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
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        recordingMovement = true;
                        accelerationX.clear();
                        accelerationY.clear();
                        accelerationZ.clear();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        final AsyncTask task = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                final Gesture gesture = findMatchingGesture();
                                if (gesture == null) {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    return null;
                                }
                                //// TODO: 2017-11-18 Run action associated with found gesture
                                final Actions action = gesture.getAction();
                                switch (action) {
                                    case NEXT_SLIDE: {

                                        MainActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                LayoutInflater inflater = getLayoutInflater();
                                                View layout = inflater.inflate(R.layout.action_next_slide_invoked_toast,
                                                        (ViewGroup) findViewById(R.id.action_next_slide_invoked_toast_container));

                                                TextView text = layout.findViewById(R.id.text);
                                                text.setText("Found Gesture: " +
                                                        gesture.getTitle() + ": " + "action Next Slide Invoked");
                                                Toast toast = new Toast(getApplicationContext());
                                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                toast.setView(layout);
                                                toast.show();
                                            }
                                        });
                                        break;
                                    }
                                    case PREV_SLIDE: {
                                        MainActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                LayoutInflater inflater = getLayoutInflater();
                                                View layout = inflater.inflate(R.layout.action_prev_slide_invoked_toast,
                                                        (ViewGroup) findViewById(R.id.action_prev_slide_invoked_toast_container));

                                                TextView text = layout.findViewById(R.id.text);
                                                text.setText("Found Gesture: " +
                                                        gesture.getTitle() + ": " +"action Prev Slide Invoked");
                                                Toast toast = new Toast(getApplicationContext());
                                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                toast.setView(layout);
                                                toast.show();
                                            }
                                        });
                                        break;
                                    }
                                }
                                return null;
                            }
                        };
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
                    }
                    return true;
                }
                return true;
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
