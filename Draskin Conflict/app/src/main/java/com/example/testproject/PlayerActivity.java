package com.example.testproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerActivity extends AppCompatActivity {

    HashMap<String, ArrayList<Token>>[] tokens;
    HashMap<String, ArrayList<Token>> locationTokens;
    ArrayList<Clue>[] clues;

    TextView cityInfoText;
    LinearLayout cityInfoTextLayout;
    LinearLayout tokenRow;
    TableRow clueRow;

    Integer playerNum;
    Integer totalPlayers;
    Integer generatedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // load data from intent
        generatedNumber = getIntent().getIntExtra("GAMENUMBER", -100);
        playerNum = getIntent().getIntExtra("PLAYER", -100);
        Bundle bundleOfTokens = getIntent().getBundleExtra("BUNDLE");
        clues = (ArrayList<Clue>[])bundleOfTokens.getSerializable("CLUES");
        tokens = (HashMap<String, ArrayList<Token>>[]) bundleOfTokens.getSerializable("PLAYER_TOKENS");
        locationTokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("CITY_TOKENS");
        totalPlayers = (Integer)bundleOfTokens.getSerializable("PLAYER_NUMBER");

        // setup views
        TextView banner = findViewById(R.id.bannerTextPlayer);
        banner.append(" " + (playerNum+1));
        cityInfoText = findViewById(R.id.cityInfoText);
        cityInfoTextLayout = findViewById(R.id.cityInfoTextLayout);
        tokenRow = findViewById(R.id.tokenRow_1);
        clueRow = findViewById(R.id.clueRow);

        initializePlayerTokens();

        // SETUP scroll picker -----
        NumberPicker picker = findViewById(R.id.picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picker.setTextColor(Color.WHITE);
        }
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setMinValue(0);
        picker.setMaxValue(Token.CITIES.length-1);
        picker.setDisplayedValues(Token.CITIES);
        // listen for value changes :)
        picker.setOnValueChangedListener((picker1, oldVal, newVal) -> setLocation(newVal));

        setLocation(0); // initialize the textview starting position

        // SETUP draw token button -----
        Button b = findViewById(R.id.drawTokenButton);
        b.setOnClickListener(v -> {
            // load the location and see if there are any ones left
            String location = getLocation(picker.getValue());
            //**Log.d("LOC", " SL " + location + " | " + picker.getValue());
            if (!location.equals("NONE")) {

                // pop the top off the token pile
                Token t = locationTokens.get(location).get(0);
                locationTokens.get(location).remove(0);

                playerAdd(t);

                // refresh
                tokenRow.removeAllViews();
                initializePlayerTokens();
                setLocation(picker.getValue());
            }
        });

        // SETUP floating action button ------
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlertDialogPopup a = new AlertDialogPopup();
            a.setupBuilder(clues[playerNum], "Clues:", R.drawable.book, null, null, this, null,null);
        });

        // SETUP floating action button for HELP ------
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(view -> {
            // TODO popup a list of all cities and their stats
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("GAMENUMBER", generatedNumber);
        Bundle args = new Bundle();
            args.putSerializable("PLAYER_NUMBER", totalPlayers);
            args.putSerializable("PLAYER_TOKENS", tokens);
            args.putSerializable("CITY_TOKENS", locationTokens);
            args.putSerializable("CLUES", clues);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
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
        for (ArrayList<Token> location : tokens[playerNum].values()) {


            // name of the city
            if (!location.isEmpty()) {

                String name = location.get(0).getCity();

                // box to put everything into
                FrameLayout wrapper = new FrameLayout(this);
                    ImageView background = new ImageView(this);
                    background.setImageResource(R.drawable.background_box);
                    background.setAlpha(0.5f);
                wrapper.addView(background);

                Context context = this;
                background.setOnClickListener(v -> {

                    // get the number of revealed tokens
                    ArrayList<Token> revealedTokens = new ArrayList<>();
                    int numTokens = 0;
                    for (Token t : location) {
                        if (t.isRevealed()) {
                            numTokens++;
                            revealedTokens.add(t);
                        }
                    }

                    AlertDialogPopup a = new AlertDialogPopup();
                    SpannableString spannableString = new SpannableString(location.size() + " " + name + " Tokens " /*+ numTokens + " revealed"*/);
                    a.setupBuilder(null, "Location", GameActivity.DRAWABLE[location.size()-1], spannableString, Token.getImages(Token.getType(name)), context, revealedTokens, tokens[playerNum]);

                    // on dialog dismiss check if anything has changed
                    a.alertDialog.setOnDismissListener(dialog -> {
                        if (a.allTokens != null) {
                            Log.d("ALERT", "UPDATE P");
                            tokens[playerNum] = a.allTokens;
                        }
                    });
                });

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
     * Set the textview to display the info from the current position of the scroll wheel
     * @param value
     */
    private void setLocation(int value) {
        int num = 0;
        if (locationTokens.containsKey(Token.CITIES[value])) {
            num = locationTokens.get(Token.CITIES[value]).size();
        }

        cityInfoText.setText( num + " tokens");

        //TODO
        /*LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.sample, null);
        cityInfoTextLayout.addView(view);

        Integer[] images = Token.getImages(Token.getType(Token.CITIES[value]));
        // set the images
        for (int i = 0; i < 4; i++) {
            ImageView iv = view.findViewById(AlertDialogPopup.ALERTIMAGEIDS[i]);
            if (images[i] == null) {
                iv.setImageDrawable(null);
            } else {
                iv.setImageResource(images[i]);
            }
        }*/
    }

    /**
     * Get the value of the current wheel scroller which the textview is reffering too
     * @param value
     * @return - "NONE" if the tokens are all used up or the value is out of bounds
     */
    private String getLocation(int value) {
        String s = "NONE";
        if (value < Token.CITIES.length) {
            Log.d("CHECKING ", "City " + Token.CITIES[value]);
            if (locationTokens.get(Token.CITIES[value]).size() > 0) {
                s = Token.CITIES[value];
            }
        }

        return s;
    }


    /**
     * draw a new token from the city pile
     * @param t
     */
    private void playerAdd(Token t) {
        ArrayList<Token> thisLocationsPile = new ArrayList<>();

        // check if there are already tokens of this type
        if (tokens[playerNum].containsKey(t.getCity())) {
            for (Token next : tokens[playerNum].get(t.getCity())) {
                // if so record them too
                thisLocationsPile.add(next);
            }
        }

        // add the new token
        thisLocationsPile.add(t);

        tokens[playerNum].put(t.getCity(), thisLocationsPile);
    }
}
