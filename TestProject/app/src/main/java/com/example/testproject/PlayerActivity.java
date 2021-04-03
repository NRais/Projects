package com.example.testproject;

import android.app.AlertDialog;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerActivity extends AppCompatActivity {

    HashMap<String, ArrayList<Token>> tokens;
    HashMap<String, ArrayList<Token>> cityTokens;

    TextView cityInfoText;
    LinearLayout tokenRow;
    TableRow clueRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // load data from intent
        Integer playerNum = getIntent().getIntExtra("PLAYER", -100) + 1;
        Bundle bundleOfTokens = getIntent().getBundleExtra("BUNDLE");
        tokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("PLAYER_TOKENS");
        cityTokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("CITY_TOKENS");

        // setup views
        NumberPicker picker = findViewById(R.id.picker);
        TextView banner = findViewById(R.id.bannerTextPlayer);
        banner.append(" " + playerNum);
        cityInfoText = findViewById(R.id.cityInfoText);
        tokenRow = findViewById(R.id.tokenRow_1);
        clueRow = findViewById(R.id.clueRow);

        initializePlayerTokens();

        // setup scroll picker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picker.setTextColor(Color.WHITE);
        }
        picker.setMinValue(0);
        picker.setMaxValue(Token.CITIES.length-1);
        picker.setDisplayedValues(Token.CITIES);
        // listen for value changes :)
        picker.setOnValueChangedListener((picker1, oldVal, newVal) -> setLocation(newVal));

        setLocation(0); // initialize the textview starting position

        // setup draw token button
        Button b = new Button(this);
        b.setOnClickListener(v -> {
            // load the location and see if there are any ones left
            String location = getLocation(picker.getValue());
            if (!location.equals("NONE")) {
                
            }
        });
    }

    /**
     * loop through the token map and display them on screen
     */
    private void initializePlayerTokens() {

        Log.d("INIT", "Showing");

        // relative layout to put everything in
        LinearLayout layout = new LinearLayout(this);
        // Defining the layout parameters of the layout
        layout.setLayoutParams(GameActivity.defaultRLP());

        // for each location in the players token collection
        for (ArrayList<Token> location : tokens.values()) {


            // name of the city
            String name = "";
            if (!location.isEmpty()) {

                // box to put everything into
                FrameLayout wrapper = new FrameLayout(this);
                    ImageView background = new ImageView(this);
                    background.setImageResource(R.drawable.background_box);
                    background.setAlpha(0.5f);
                wrapper.addView(background);

                // inside box to hold children
                LinearLayout box = new LinearLayout(this);
                box.setOrientation(LinearLayout.VERTICAL);
                box.setGravity(Gravity.CENTER_HORIZONTAL);

                // generate the text view
                TextView tv = new TextView(this);
                tv.setTextColor(Color.WHITE);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                //tv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                //tv.setMovementMethod(LinkMovementMethod.getInstance());

                LinearLayout images = new LinearLayout(this);
                images.setGravity(Gravity.CENTER_HORIZONTAL);
                images.setLayoutParams(GameActivity.defaultRLP());
                for (Token t : location) {

                    ImageView iv = new ImageView(this);
                    // icon depends on the token's value
                    if (t.getValue()) {iv.setImageResource(R.drawable.active_true);}
                    else {iv.setImageResource(R.drawable.active_false);}

                    images.addView(iv);
                }

                name = location.get(0).getCity();

                Log.d("INIT", "Token " + name + " " + location.size());

                // append facedown icon and number
                if (name.contains(" ")) {
                    tv.setText("  " + name.substring(0, name.indexOf(" ")) + "\n" + name.substring(name.indexOf(" ")));
                } else {
                    tv.setText(" " + name + " ");
                }


                // Defining the layout parameters of the TextView
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,90);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                tv.setLayoutParams(lp2);

                box.addView(tv);
                box.addView(images);

                wrapper.addView(box);


                Space margin = new Space(this);
                margin.setLayoutParams(new RelativeLayout.LayoutParams(50, RelativeLayout.LayoutParams.WRAP_CONTENT));

                layout.addView(wrapper);
                layout.addView(margin);
            }
        }

        tokenRow.addView(layout);
    }

    /**
     * Add an image into a string
     *
     * @param string - the text string
     * @param img - the image number
     * @param name - the city name to display in the popup
     * @return - the ouputed CharSeq containing all this data
     */
    private CharSequence addImageAsterisk(String string, Integer img, String name) {
        Integer[] drawable = new Integer[] {R.drawable.ornate_stop_25, R.drawable.ornate_stop_25_2, R.drawable.ornate_stop_25_3, R.drawable.ornate_stop_25_4};
        ImageSpan imageSpan = new ImageSpan(this, drawable[img-1], ImageSpan.ALIGN_BOTTOM);
        final SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, string.length()-1, string.length(), 0);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d("POPUP", "Stirng " + name);
                //Toast.makeText(getApplicationContext(), name , Toast.LENGTH_LONG).show();
                popupMessage(drawable[img-1], name);
                widget.invalidate();
            }
        },string.length()-1, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // this will add a clickable span and on click will delete the span and text
        return spannableString;
    }
    /**
     *  Popup message for describing your city
     */
    AlertDialog alertDialog;
    public void popupMessage(Integer drawable, String string){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(string);
        alertDialogBuilder.setIcon(drawable);
        alertDialogBuilder.setTitle("Location");
        alertDialogBuilder.setNegativeButton("ok", (dialogInterface, i) -> {
            alertDialog.cancel();
            alertDialog.dismiss();
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Set the textview to display the info from the current position of the scroll wheel
     * @param value
     */
    private void setLocation(int value) {
        int num = 0;
        if (cityTokens.containsKey(Token.CITIES[value])) {
            num = cityTokens.get(Token.CITIES[value]).size();
        }

        cityInfoText.setText( num + " tokens");
    }

    /**
     * Get the value of the current wheel scroller which the textview is reffering too
     * @param value
     * @return - "NONE" if the tokens are all used up or the value is out of bounds
     */
    private String getLocation(int value) {
        String s = "NONE";
        if (Token.CITIES.length < value) {
            if (cityTokens.get(Token.CITIES[value]).size() > 0) {
                s = Token.CITIES[value];
            }
        }

        return s;
    }

}
