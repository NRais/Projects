package com.example.testproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    AlertDialog inputDialog = null;
    AlertDialog playerDialog = null;
    Integer playerNumber = 4;
    Integer generatedNumber = 0;


    HashMap<String, ArrayList<Token>> cityTokens = new HashMap<>();
    HashMap<String, ArrayList<Token>>[] pTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        drawOptions();
        LinearLayout lin = findViewById(R.id.linearPane);

        pTokens = new HashMap[playerNumber];

        // initialize
        for (int i = 0; i < playerNumber; i++) {
            pTokens[i] = new HashMap<>();
        }


        // generate consumer object that can be passed to the popup method
        Consumer<EditText> generatorFunction = t -> fireGeneratorDialogue(t);
        Consumer<EditText> playerNumberFunction = t -> firePlayerCountDialogue(t);

        // display popups to get user input
        //TODO inputDialog = popup("Enter a game number", generatorFunction);
        //TODO playerDialog = popup("Enter a player count", playerNumberFunction);

        generateLayout(lin);

        createGame(generatedNumber);
    }

    @Override
    public void onClick(View v) {

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

            //try {
                playerNumber = Integer.parseInt(number);

                // hide it :)
                playerDialog.cancel();
                playerDialog.dismiss();

           // } catch (Exception e) {
           //     Log.e("ERROR", "failed to load what they input for PLAYER NUMBER");
            //}
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
     * TODO setup the buttons and stuff for the cities
     *
     * @param lin
     */
    private void generateLayout(LinearLayout lin) {
        //set the properties for button
        Button btnTag = new Button(this);
        btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btnTag.setText("Button");

        //add button to the layout
        lin.addView(btnTag);
    }


    private void drawOptions() {

        LinearLayout spot1 = findViewById(R.id.t1_l);
        LinearLayout spot2 = findViewById(R.id.t1_r);
        TableRow row2 = findViewById(R.id.t2);
        TableRow row3 = findViewById(R.id.t3);
        TableRow row4 = findViewById(R.id.t4);

        //set the properties for button
        Button btnTag = new Button(this);
        btnTag.setText("Button");
        //set the properties for button
        Button btnTag2 = new Button(this);
        btnTag2.setText("Button 2");
        //set the properties for button
        Button btnTag3 = new Button(this);
        btnTag3.setText("Button 3");
        //set the properties for button
        Button btnTag4 = new Button(this);
        btnTag4.setText("Button 4");


        //RelativeLayout relativeLayout = new RelativeLayout(this);
        LinearLayout relativeLayout = new LinearLayout(this);
        // Defining the RelativeLayout layout parameters.
        // In this case I want to fill its parent
        /*RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);*/

        // Creating a new TextView
        Button btn = new Button(this);
        btn.setText("Buttonsss");

        TextView tv = new TextView(this);
        tv.setTextColor(Color.WHITE);
        tv.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        //SpannableStringBuilder ssb = new SpannableStringBuilder("   Clues : 2");
        //ssb.setSpan(new ImageSpan(this, R.drawable.amla_default, ImageSpan.ALIGN_CENTER), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.append(addImageAsterisk("   Clues : 2   "));
        tv.append(addImageAsterisk("   "));
        tv.append(addImageAsterisk("   "));

        // Defining the layout parameters of the TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp2.addRule(RelativeLayout.RIGHT_OF, btn.getId());

        // Setting the parameters on the TextView
        tv.setLayoutParams(lp2);
        btn.setLayoutParams(lp);

        // Adding the TextView to the RelativeLayout as a child
        relativeLayout.addView(btn);
        relativeLayout.addView(tv);

        //relativeLayout.setLayoutParams(rlp);

        spot1.addView(btnTag);
        spot2.addView(btnTag2);
        row3.addView(relativeLayout);
        row4.addView(btnTag4);
    }

    private CharSequence addImageAsterisk(String string) {
        ImageSpan imageSpan = new ImageSpan(this, R.drawable.amla_default, ImageSpan.ALIGN_CENTER);
        final SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, string.length()-1, string.length(), 0);
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
