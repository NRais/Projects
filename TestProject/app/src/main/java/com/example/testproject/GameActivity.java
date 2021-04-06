package com.example.testproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

public class GameActivity extends AppCompatActivity {


    AlertDialog playerDialog = null;
    Integer playerNumber = 4;
    Integer generatedNumber = 0;

    HashMap<String, ArrayList<Token>> locationTokens = new HashMap<>();
    HashMap<String, ArrayList<Token>>[] pTokens;
    ArrayList<Clue>[] pClues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // load data from intent
        Bundle bundleOfTokens = getIntent().getBundleExtra("BUNDLE");

        // IF WE RECEIVED DATA load it
        if (bundleOfTokens != null) {

            playerNumber = (Integer) bundleOfTokens.get("PLAYER_NUMBER");
            pTokens = (HashMap<String, ArrayList<Token>>[]) bundleOfTokens.getSerializable("PLAYER_TOKENS");
            locationTokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("CITY_TOKENS");
            pClues = (ArrayList<Clue>[])bundleOfTokens.getSerializable("CLUES");

            Log.d("LOADING ", " PLAYERS " + playerNumber);

            drawPlayers();
        }
        // OTHERWISE fire startup normally
        else {

            generatedNumber = getIntent().getIntExtra("GAMENUMBER", -1);


            Log.d("LOADING", " GSAME : " + generatedNumber);

            // generate consumer object that can be passed to the popup method
            Consumer<EditText> playerNumberFunction = t -> firePlayerCountDialogue(t);

            // display popups to get user input
            playerDialog = popup("Enter a player count", playerNumberFunction);
        }
    }

    /**
     * Launcher method that sets up all the data and the board
     */
    private void init() {
        pTokens = new HashMap[playerNumber];
        pClues = new ArrayList[playerNumber];

        // initialize
        for (int i = 0; i < playerNumber; i++) {
            pTokens[i] = new HashMap<>();
            pClues[i] = new ArrayList<>();
        }

        if (generatedNumber < 0) {
            generatedNumber = generateNumber();
        }

        //TESTING
        /*HashMap<Boolean[], Integer> map = new HashMap<>();

        Log.d("INIT", "INTIITITNITNITT");

        for (int i = 0; i < 80000; i++) {
            int n = generateNumber();

            Boolean[] terrainBoolean = getTerrainTypes(n);

            int z = map.getOrDefault(terrainBoolean, 0);
            map.put(terrainBoolean, z+1);
        }

        for (Boolean[] b : CODES) {
            Log.d("MAP RANDOM TEST OUTPUT ", "" + map.get(b));
        }*/

        createGame(generatedNumber);

        drawPlayers();
    }

    /**
     *  make a popup to determine the game
     */
    private AlertDialog popup(String textToPrompt, Consumer<EditText> actionToFire) {
        // --- Input text Dialog setup --- //

        // Set up the input field
        final EditText inputField = new EditText(this);
        inputField.setSelectAllOnFocus(true);
        inputField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    actionToFire.accept(inputField);

                    return true;
                }
                return false;
            }
        });

        // create our Alert which asks for their name
        // Setup the view to have an edit field
        // Set to null. We override the onclick
        // Set to null. We override the onclick
        AlertDialog theDialog = new AlertDialog.Builder(this)
                .setView(inputField)                                 // Setup the view to have an edit field
                .setTitle(textToPrompt)
                .setPositiveButton(android.R.string.ok, null) // Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null) // Set to null. We override the onclick
                .create();

        // don't let them cancel it, they must put in a number in
        theDialog.setCancelable(false);
        theDialog.setCanceledOnTouchOutside(false);

        // make a show listener which activates when the dialog appears
        AlertDialog finalTheDialog = theDialog;
        theDialog.setOnShowListener(dialog -> {

            // make our own click listener for when they hit the button
            // NOTE: the reason we re-made the listener is so that it doens't automatically close on click
            Button b = finalTheDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> actionToFire.accept(inputField));

            // then we make our own on click listener for the cancel button
            Button b2 = finalTheDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            b2.setOnClickListener(view -> kill());
        });
        theDialog.show();

        return theDialog;
    }


    /**
     * On enter or click do the loading stuff
     *
     * @param inputField
     */
    private void firePlayerCountDialogue(EditText inputField) {
        String number = inputField.getText().toString();

        // if they enter some number do that
        if (number.matches("-?\\d+")) {

            playerNumber = Integer.parseInt(number);

            // if valid number
            if (playerNumber <= 4 | playerNumber >= 1) {

                // hide it :)
                playerDialog.cancel();
                playerDialog.dismiss();

                Log.d("INIT", "");

                init();
            }
        }
    }

    /**
     * @return randomize a number 10000 to 99999
     */
    private Integer generateNumber() {
        return new Random().nextInt(89999) + 10000;
    }


    /**
     *  generate the game
     */
    private void createGame(Integer gameNumber) {

        Log.d("CREATING", " GSAME : " + gameNumber);

        Boolean[] terrainBoolean = getTerrainTypes(gameNumber);

        for (ArrayList<Clue> clues : pClues) {
            Log.d("PLAYER " , "NUMBER " + playerNumber);
            for (int i = 0; i < playerNumber; i++) {
                Clue c = new Clue(Clue.TYPES[i], terrainBoolean[i], false);
                clues.add(c);
            }
        }

        // TODO currently giving out tokens to test
        giveOutTokens(terrainBoolean);

    }

    /**
     * table of all possible boolean options for the game output
     */
    private static Boolean[][] CODES = {{true, true, true, true}, {true, true, true, false}, {true, true, false, true}, {true, true, false, false},
                                        {true, false, true, true}, {true, false, true, false}, {true, false, false, true}, {true, false, false, false},
                                        {false, true, true, true}, {false, true, true, false}, {false, true, false, true}, {false, true, false, false},
                                        {false, false, true, true}, {false, false, true, false}, {false, false, false, true}, {false, false, false, false}};
    /**
     * return a list of the terrains in true or false form
     *
     * @param gameNumber - a number to generate the terrain
     * @return - list of booleans for each terrain
     */
    private Boolean[] getTerrainTypes(Integer gameNumber) {

        int[] digits = int_to_array(gameNumber);

        // two big nums (+1 to ensure no out of bounds)
        int num1 = digits[0]*100 + digits[1]*10 + digits[2] + 1;
        int num2 = digits[3]*10 + digits[4] + 1;

        // reduce the number of zero results
        if (num1 % num2 == 0 && digits[4] < 4) {
            num1 = num1 / num2;
        }

        // calculate city between 0 and 7
        int result = num1%num2;

        // ensure no out of bounds
        while (result < 0 | result > 7) {
            num1 += 7; // increase it
            result = num1%num2;
        }

        // increase the number of 7's
        if (num2 % (digits[0]*10+digits[1]+1) < 3) result = 7;

        // then 50:50 it to between 0 and 15
        if (digits[4] >= 5) result+=8;

        return CODES[result];
    }

    /**
     * Input an integer return a 5 digit array pad with zeros
     *
     * @param n
     * @return
     */
    private static int[] int_to_array(int n) {
        int j = 0;
        int[] arr = {0,0,0,0,0};
        while(n!=0)
        {
            arr[5-j-1] = n%10;
            n=n/10;
            j++;
        }
        return arr;
    }

    /**
     * Assign tokens based upon the terrains patter
     */
    private void giveOutTokens(Boolean[] gameTerrain) {

        // populate location tokens
        for (String s : Token.CITIES) {
            ArrayList<Token> list = new ArrayList<>();
            Boolean[] types = Token.getTerrain(Token.getType(s));

            // if the terrain and the cities type are the same then we throw a check mark token, otherwise a x marked token
            for (int i = 0; i < 4; i++) {
                list.add(new Token(s, types[i] == gameTerrain[i]));
            }

            locationTokens.put(s, list);
        }
    }

    /**
     * Setup the buttons and stuff for the players and their cities
     */
    private void drawPlayers() {

        LinearLayout[] spots = new LinearLayout[] { findViewById(R.id.t2_l), findViewById(R.id.t2_r), findViewById(R.id.t4_l), findViewById(R.id.t4_r), findViewById(R.id.clueRow)};

        // for each active player draw their stuff
        for (int i = 0; i < playerNumber; i++) {

            int count = 0;
            for (Clue c : pClues[i]) {
                if (c.isVisible()) {
                    count++;
                }
            }

            Log.d("DRAWING", "PLAYER " + i);

            // relative layout to put everything in
            LinearLayout linearLayout = new LinearLayout(this);
            // Defining the layout parameters of the layout
            linearLayout.setLayoutParams(defaultRLP());

            // create a new button
            Button btn = new Button(this);
            btn.setText("Player " + (i+1));
            final int finalI = i;
            btn.setOnClickListener(v -> {
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("PLAYER", finalI);
                Bundle args = new Bundle();
                    args.putSerializable("PLAYER_NUMBER", playerNumber);
                    args.putSerializable("PLAYER_TOKENS", pTokens);
                    args.putSerializable("CITY_TOKENS", locationTokens);
                    Log.d("CLUES ", " C"  + pClues[finalI]);
                    args.putSerializable("CLUES", pClues);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);
            });

            // add the text and icons
            TextView tv = new TextView(this);
            if (i%2 == 0) { // for even players
                tv.setText("   Clues: " + count + "  ");
            }
            tv.setTextColor(Color.WHITE);
            tv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            // for each city in the players token collection
            for (ArrayList<Token> tokens : pTokens[i].values()) {
                // name of the city
                String name = "";
                if (!tokens.isEmpty()) {name = tokens.get(0).getCity();}

                // get the number of revealed tokens
                int revealedTokens = 0;
                for (Token t : tokens) {
                    if (t.isRevealed()) { revealedTokens++; }
                }

                Log.d("REVEALING: ", "CITY "  + name + " | NUM " + revealedTokens);
                // append facedown icon and number
                tv.append(addImageAsterisk(" ", tokens.size(), name, i, revealedTokens));
            }
            tv.append("   ");

            // Defining the layout parameters of the TextView
            RelativeLayout.LayoutParams lp2 = defaultRLP();
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp2.addRule(RelativeLayout.RIGHT_OF, btn.getId());

            // Setting the parameters on the TextView and Button
            btn.setLayoutParams(defaultRLP());
            tv.setLayoutParams(lp2);

            // Adding the stuff to the RelativeLayout as a child
            if (i%2 == 0) { // for even players
                linearLayout.addView(btn);
                linearLayout.addView(tv);
            } else { // for odd players
                tv.append("Clues: " + count + "   ");
                linearLayout.addView(tv);
                linearLayout.addView(btn);
            }

            tv.setMovementMethod(LinkMovementMethod.getInstance());

            spots[i].addView(linearLayout);
        }


    }

    @Override
    public void onBackPressed() {
        Log.d("CGA", "onBackPressed Called");
        Context context = this;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Exit the game?  #" + generatedNumber).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public static RelativeLayout.LayoutParams defaultRLP() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        return lp;
    }


    public static final Integer[] DRAWABLE = new Integer[] {R.drawable.ornate_stop_25, R.drawable.ornate_stop_25_2, R.drawable.ornate_stop_25_3, R.drawable.ornate_stop_25_4};
    public static final Integer[] REVEALED_DRAWABLE = new Integer[] {R.drawable.ornate_stop_25_revealed, R.drawable.ornate_stop_25_2_revealed, R.drawable.ornate_stop_25_3_revealed, R.drawable.ornate_stop_25_4_revealed};

    /**
     * Add an image into a string
     *
     * @param string - the text string
     * @param img - the image number
     * @param city - the city name to display in the popup
     *
     *        DETAILS FOR REVEALED TOKENS:
     * @param player
     * @param numRevealed
     * @return - the ouputed CharSeq containing all this data
     */
    private CharSequence addImageAsterisk(String string, Integer img, String city, Integer player, Integer numRevealed) {

        ImageSpan imageSpan;
        if (numRevealed > 0)  {
            imageSpan = new ImageSpan(this, REVEALED_DRAWABLE[img-1], ImageSpan.ALIGN_BOTTOM);
        } else {
            imageSpan = new ImageSpan(this, DRAWABLE[img-1], ImageSpan.ALIGN_BOTTOM);
        }

        final Context context = this;
        final SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, string.length()-1, string.length(), 0);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                AlertDialogPopup a = new AlertDialogPopup();

                // get the number of revealed tokens
                ArrayList<Token> revealedTokens = new ArrayList<>();
                for (Token t : pTokens[player].get(city)) {
                    if (t.isRevealed()) {
                        revealedTokens.add(t);
                    }
                }

                String text = img + " " + city + " Tokens " /*+ numRevealed + " revealed"*/;

                a.setupBuilder(null, "Location", DRAWABLE[img-1], text, Token.getImages(Token.getType(city)), context, revealedTokens, pTokens[player]);

                widget.invalidate();

                // on dialog dismiss check if anything has changed
                a.alertDialog.setOnDismissListener(dialog -> {
                    if (a.allTokens != null) {
                        Log.d("ALERT", "UPDATE");
                        pTokens[player] = a.allTokens;
                    }
                });

            }
        },string.length()-1, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // this will add a clickable span and on click will delete the span and text
        return spannableString;
    }

    /**
     * Method to kill the initial popup dialogue
     */
    private void kill() {
        playerDialog.cancel();
        playerDialog.dismiss();

        finish();

        // return home
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }
}
