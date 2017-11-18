package pl.poznan.ue.air.gestures;
import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.app.DialogFragment;
import android.view.inputmethod.InputMethodManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import pl.poznan.ue.air.gestures.model.Gesture;
import pl.poznan.ue.air.gestures.model.GestureDatabase;
import pl.poznan.ue.air.gestures.services.FileAccessService;

public class GestureEditActivity extends AppCompatActivity
        implements ConfirmSaveDialogFragment.ConfirmSaveDialogListener,
        ConfirmRemoveDialogFragment.ConfirmRemoveDialogListener,
        SensorEventListener {
    private GestureDatabase database;
    private Gesture currentElement;

    private EditText nameTextEdit;
    private Spinner actionSpinner;
    private Boolean elementChanged = false;
    private int currentPosition;
    private Boolean editingExisting = false;
    private SensorManager sensorManager;
    private Boolean recordingMovement = false;
    private List<Float> accelerationX;
    private List<Float> accelerationY;
    private List<Float> accelerationZ;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        database = ((GlobalDataApplication) this.getApplication()).getDatabase();
        currentElement = (Gesture) getIntent().getSerializableExtra("Gesture");
        currentPosition = (int) getIntent().getSerializableExtra("int");
        editingExisting = (Boolean) getIntent().getSerializableExtra("Boolean");
        //data buffers
        accelerationX = new LinkedList<>();
        accelerationY = new LinkedList<>();
        accelerationZ = new LinkedList<>();

        if(!editingExisting) {
            currentElement = new Gesture( "New one");
        }

        //sensor
        this.sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);


        nameTextEdit = findViewById(R.id.editTextName);
        nameTextEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    currentElement.setTitle(textView.getText().toString());
                    elementChanged = true;

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    try{
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (NullPointerException ex){

                    }
                    return true;
                }
                return false;
            }
        });
        actionSpinner = findViewById(R.id.spinnerAction);
        actionSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Gesture.Actions.values()));
        actionSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                currentElement.setAction((Gesture.Actions) parent.getItemAtPosition(position));
                elementChanged = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){ }
        });


        nameTextEdit.setText(currentElement.getTitle());
        actionSpinner.setSelection(((ArrayAdapter) actionSpinner.getAdapter())
                .getPosition(currentElement.getAction()), false);
        Button recordButton = findViewById(R.id.buttonTrain);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN: {
                        recordingMovement = true;
                        return true;
                    }
                    case MotionEvent.ACTION_UP:{
                        recordingMovement = false;
                        currentElement.setTraceX(accelerationX);
                        currentElement.setTraceY(accelerationY);
                        currentElement.setTraceZ(accelerationZ);
                        Toast.makeText(GestureEditActivity.this, "Saved", Toast.LENGTH_LONG).show();

                        return true;
                    }
                }
                return false;
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmRemoveDialogFragment dialog = new ConfirmRemoveDialogFragment();
                dialog.show(getFragmentManager(), "ConfirmRemoveDialogFragment");
            }
        });
        elementChanged = false;
    }

    @Override
    public void onBackPressed() {
        if (!elementChanged){
            super.onBackPressed();
            return;
        }
        ConfirmSaveDialogFragment dialog = new ConfirmSaveDialogFragment();
        dialog.show(getFragmentManager(), "ConfirmSaveDialogFragment");
    }

    @Override
    public void onDialogSavePositiveClick(DialogFragment dialog) {
        if(editingExisting) database.update(currentPosition, currentElement);
        else database.put(currentElement);
        FileAccessService fas = new FileAccessService(database);
        fas.saveDatabaseToJsonFile();
        super.onBackPressed();
    }

    @Override
    public void onDialogSaveNegativeClick(DialogFragment dialog) {
        super.onBackPressed();
    }

    @Override
    public void onSensorChanged (SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && recordingMovement) {
            accelerationX.add( event.values[0]);
            accelerationY.add( event.values[1]);
            accelerationZ.add( event.values[2]);
            if(accelerationX.size() > 64){
                accelerationX.remove(0);
                accelerationY.remove(0);
                accelerationZ.remove(0);
            }
        }
    }

    @Override
    public void onDialogSaveNeutralClick(DialogFragment dialog) { }

    @Override
    public void onAccuracyChanged(Sensor s, int acc){ }

    @Override
    protected final void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onDialogRemoveNegativeClick(DialogFragment dialog) {
    }

    @Override
    public void onDialogRemovePositiveClick(DialogFragment dialog) {
        database.remove(currentPosition);
        super.onBackPressed();
    }

    @Override
    protected final void onResume() {
        // Implement the Parent Definition.
        super.onResume();
        // Register for updates on the SensorManager. (We want to listen to accelerometer data.)
        sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }
}
