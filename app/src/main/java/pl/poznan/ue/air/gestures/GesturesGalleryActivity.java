package pl.poznan.ue.air.gestures;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import pl.poznan.ue.air.gestures.model.Gesture;
import pl.poznan.ue.air.gestures.model.GestureDatabase;

public class GesturesGalleryActivity extends AppCompatActivity {
    private ListView gesturesListView;
    private ArrayAdapter<String>  gestureListAdapter;
    private GestureDatabase database;

    private void initList(){
        gesturesListView = findViewById(R.id.gesturesListView);
        ArrayList<String> pupils = new ArrayList<>();
        pupils.addAll(Arrays.asList(database.getAllTitles()));
        gestureListAdapter = new ArrayAdapter<>(this,R.layout.single_gesture_entry,pupils);
        gesturesListView.setAdapter(gestureListAdapter);
        gesturesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gesture item = database.getAt(i);

                Intent editView = new Intent(GesturesGalleryActivity.this,
                        GestureEditActivity.class);
                editView.putExtra("int", i);
                editView.putExtra("Gesture", item);
                editView.putExtra("Boolean", true);
                startActivity(editView);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = ((GlobalDataApplication) this.getApplication()).getDatabase();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editView = new Intent(GesturesGalleryActivity.this,
                        GestureEditActivity.class);
                editView.putExtra("int", 0);
                editView.putExtra("Boolean", false);
                editView.putExtra("Gesture", new Gesture("A new one"));
                startActivity(editView);
            }
        });
        initList();
    }

    @Override
    public void onResume(){
        super.onResume();
        initList();
    }

}
