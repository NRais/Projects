package com.example.testproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

public class GameActivity extends AppCompatActivity {


    AlertDialog inputDialog = null;
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

            // generate consumer object that can be passed to the popup method
            Consumer<EditText> generatorFunction = t -> fireGeneratorDialogue(t);
            Consumer<EditText> playerNumberFunction = t -> firePlayerCountDialogue(t);

            // display popups to get user input
            //inputDialog = popup("Enter a game number", generatorFunction);
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

        /*TESTING
        HashMap<Boolean[], Integer> map = new HashMap<>();

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
     * On enter or click do the loading stuff for the inputDialogue
     *
     * @param inputField
     */
    private void fireGeneratorDialogue(EditText inputField) {
        String number = inputField.getText().toString();

        // if they enter nothing generate something for them
        if (number.length() == 0) {
            Log.d("LOG", "0 Length");
            generatedNumber = generateNumber();

            // hide it :)
            inputDialog.cancel();
            inputDialog.dismiss();
        }
        // otherwise check the thing is a number
        else if (number.matches("-?\\d+")) {

            try {
                generatedNumber = Integer.parseInt(number);

                // hide it :)
                inputDialog.cancel();
                inputDialog.dismiss();

            } catch (Exception e) {
                Log.e("ERROR", "failed to load what they input");
            }
        }
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
     * Currently randomly assign tokens, TODO give out tokens based upon patterns, and generatedNumber
     */
    private void giveOutTokens(Boolean[] gameTerrain) {

        // populate location tokens
        for (String s : Token.CITIES) {
            ArrayList<Token> list = new ArrayList<>();
            Boolean[] types = Token.getTerrain(Token.getType(s));

            // if the terrain and the cities type are the same then we throw a check mark token
            for (int i = 0; i < 4; i++) {
                list.add(new Token(s, types[i] == gameTerrain[i]));
            }

            locationTokens.put(s, list);
        }

        // for every player
        /*for (int i = 0; i < playerNumber; i++) {
            // give up to ten tokens
            for (int j = 0; j < r.nextInt(5) + 2; j++) {

                String city = Token.CITIES[r.nextInt(Token.CITIES.length-1)];

                // if there are tokens left in the city
                if (locationTokens.get(city).size() > 0) {
                    ArrayList<Token> tokens = new ArrayList<>();

                    // check if there are already tokens of this type for the player
                    if (pTokens[i].containsKey(city)) {
                        for (Token t : pTokens[i].get(city)) {
                            // if so record them too
                            tokens.add(t);
                        }
                    }

                    // add the new token
                    Token t = new Token(city, r.nextBoolean());
                    tokens.add(t);

                    pTokens[i].put(city, tokens);

                    // remove a token from the city list, TODO remove the right token rather than any token
                    ArrayList<Token> cT = locationTokens.get(city);
                    cT.remove(0);

                    locationTokens.put(city, cT);
                }
            }
        }*/
    }

    /**
     * Setup the buttons and stuff for the players and their cities
     */
    private void drawPlayers() {

        LinearLayout[] spots = new LinearLayout[] { findViewById(R.id.t2_l), findViewById(R.id.t2_r), findViewById(R.id.t4_l), findViewById(R.id.t4_r), findViewById(R.id.clueRow)};

        // for each active player draw their stuff
        for (int i = 0; i < playerNumber; i++) {

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
            if (i%2 == 0) {
                tv.setText("   Clues: " + pClues[i].size() + "  ");
            }
            tv.setTextColor(Color.WHITE);
            tv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            // for each city in the players token collection
            for (ArrayList<Token> t : pTokens[i].values()) {
                // name of the city
                String name = "";
                if (!t.isEmpty()) {name = t.get(0).getCity();}

                // append facedown icon and number
                tv.append(addImageAsterisk(" ", t.size(), name));
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
            if (i%2 == 0) {
                linearLayout.addView(btn);
                linearLayout.addView(tv);
            } else {
                tv.append("Clues: " + pClues[i].size() + "   ");
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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static RelativeLayout.LayoutParams defaultRLP() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        return lp;
    }


    public static final Integer[] DRAWABLE = new Integer[] {R.drawable.ornate_stop_25, R.drawable.ornate_stop_25_2, R.drawable.ornate_stop_25_3, R.drawable.ornate_stop_25_4};
    /**
     * Add an image into a string
     *
     * @param string - the text string
     * @param img - the image number
     * @param name - the city name to display in the popup
     * @return - the ouputed CharSeq containing all this data
     */
    private CharSequence addImageAsterisk(String string, Integer img, String name) {
        final Context context = this;
        ImageSpan imageSpan = new ImageSpan(this, DRAWABLE[img-1], ImageSpan.ALIGN_BOTTOM);
        final SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, string.length()-1, string.length(), 0);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                AlertDialogPopup a = new AlertDialogPopup();
                a.setupBuilder(null, "Location", DRAWABLE[img-1], img + " " + name + " tokens", Token.getImages(Token.getType(name)), context);

                widget.invalidate();
            }
        },string.length()-1, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // this will add a clickable span and on click will delete the span and text
        return spannableString;
    }

    /**
     * Method to kill the initial popup dialogue
     */
    private void kill() {
        inputDialog.cancel();
        inputDialog.dismiss();

        finish();

        // return home
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }
}
