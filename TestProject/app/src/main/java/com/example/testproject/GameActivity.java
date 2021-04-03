package com.example.testproject;

import android.app.AlertDialog;
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

import java.util.ArrayList;
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
    ArrayList<Object>[] pClues;

    /**
     * Currently randomly assign tokens, TODO give out tokens based upon patterns, and generatedNumber
     */
    private void giveOutTokens() {
        Random r = new Random();

        // populate location tokens
        for (String s : Token.CITIES) {
            ArrayList<Token> list = new ArrayList<>();
            list.add(new Token(s, r.nextBoolean()));
            list.add(new Token(s, r.nextBoolean()));
            list.add(new Token(s, r.nextBoolean()));
            list.add(new Token(s, r.nextBoolean()));

            locationTokens.put(s, list);
        }

        // for every player
        for (int i = 0; i < playerNumber; i++) {
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // load data from intent
        Bundle bundleOfTokens = getIntent().getBundleExtra("BUNDLE");

        // IF WE RECEIVED DATA load it
        if (bundleOfTokens != null) {

            pTokens = (HashMap<String, ArrayList<Token>>[]) bundleOfTokens.getSerializable("PLAYER_TOKENS");
            locationTokens = (HashMap<String, ArrayList<Token>>) bundleOfTokens.getSerializable("CITY_TOKENS");
            pClues = new ArrayList[playerNumber];

            // initialize
            for (int i = 0; i < playerNumber; i++) {
                pClues[i] = new ArrayList<>();
            }

            Log.d("OPEN BUNDLE ", "" + pClues);

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
        pClues = new ArrayList[playerNumber];
        pTokens = new HashMap[playerNumber];

        // initialize
        for (int i = 0; i < playerNumber; i++) {
            pTokens[i] = new HashMap<>();
            pClues[i] = new ArrayList<>();
        }

        createGame(generatedNumber);

        // TODO currently giving out tokens to test
        giveOutTokens();

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

                init();
            }
        }
    }

    /**
     * TODO return number in the space of our game
     *
     * @return
     */
    private Integer generateNumber() {
        return 5;
    }


    /**
     *  TODO generate the game
     */
    private void createGame(Integer gameNumber) {

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
                    args.putSerializable("PLAYER_TOKENS", pTokens);
                    args.putSerializable("CITY_TOKENS", locationTokens);
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
                popupMessage(drawable[img-1], img + " " + name + " Tokens");
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
