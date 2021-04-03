package com.example.testproject;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    HashMap<String, ArrayList<Token>> tokens;
    HashMap<String, ArrayList<Token>> cityTokens;

    TextView cityInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Integer playerID = getIntent().getIntExtra("PLAYER_ID", -1);
        Bundle bundleOfTokens = getIntent().getBundleExtra("BUNDLE");
        tokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("PLAYER_TOKENS");
        cityTokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("CITY_TOKENS");

        NumberPicker picker = findViewById(R.id.picker);
        cityInfoText = findViewById(R.id.cityInfoText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picker.setTextColor(Color.WHITE);
        }
        picker.setMinValue(0);
        picker.setMaxValue(Token.CITIES.length-1);
        picker.setDisplayedValues(Token.CITIES);
        // listen for value changes :)
        picker.setOnValueChangedListener((picker1, oldVal, newVal) -> {

            Log.d("SCROLL", " New " + Token.CITIES[newVal]);
            setText(newVal);
        });

        setText(0); // initialize the textview starting position
    }

    /**
     * Set the textview to display the info from the current position of the scroll wheel
     * @param value
     */
    private void setText(int value) {
        int num = 0;
        if (cityTokens.containsKey(Token.CITIES[value])) {
            num = cityTokens.get(Token.CITIES[value]).size();
        }

        cityInfoText.setText( num + " tokens");
    }

    @Override
    public void onClick(View v) {

    }
}
