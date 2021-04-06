package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(this);

        et = findViewById(R.id.editText);

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // check if its a valid thing
                    if (!et.getText().toString().matches("-?\\d+")) {
                        // IF NOT clear
                        et.setText("");
                    }
                }
            }
        });

        // add these two lines, if you wish to close the app:
        //finishAffinity();
        //System.exit(0);
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.playButton:
                // if the survey button is clicked!

                i = new Intent(this, GameActivity.class); // we intend to have the onClick listener of our view launch our give survey activity

                if (et.getText().toString().matches("-?\\d+")) {
                    i.putExtra("GAMENUMBER", Integer.parseInt(et.getText().toString()));
                }

                startActivity(i);

                break;
        }
    }
}
