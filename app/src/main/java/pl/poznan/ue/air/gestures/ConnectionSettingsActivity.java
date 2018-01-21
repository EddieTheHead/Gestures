package pl.poznan.ue.air.gestures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ConnectionSettingsActivity extends AppCompatActivity {

    private EditText ipEditText;
    private  EditText portEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);
        ipEditText = findViewById(R.id.ipTextEdit);
        ipEditText.setText(((GlobalDataApplication) this.getApplication()).getIP());
        portEditText = findViewById(R.id.editTextPort);
        portEditText.setText(((GlobalDataApplication) this.getApplication()).getPort());
    }

    @Override
    public void onBackPressed() {
        ipEditText = findViewById(R.id.ipTextEdit);
        portEditText = findViewById(R.id.editTextPort);

        ((GlobalDataApplication) this.getApplication()).setPort(portEditText.getText().toString());
        ((GlobalDataApplication) this.getApplication()).setIP(ipEditText.getText().toString());
        super.onBackPressed();
    }
}
