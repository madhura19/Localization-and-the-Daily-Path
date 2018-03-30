package com.example.madhura.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MarkerNameActivity extends AppCompatActivity {

    Button addMaker;
    EditText markerName;
    String mname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_name);

        addMaker = (Button) findViewById(R.id.add_marker);
        markerName = (EditText) findViewById(R.id.markerName);

        addMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mname = markerName.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("name", mname);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
