package com.example.testproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AlertDialogPopup {


    /**
     *  Popup message for describing your city
     */
    AlertDialog alertDialog = null;

    // structure to store the id's of the image views to manipulate
    static final int[] ALERTIMAGEIDS = {R.id.dialog_imageview, R.id.dialog_imageview2, R.id.dialog_imageview3, R.id.dialog_imageview4};

    /**
     * Creates an alert with the given parameters
     *
     * @param name
     * @param drawable
     * @param c
     * @return
     */
    public void setupBuilder(ArrayList<Clue> clues, String name, int drawable, String string, Integer[] images, Context c) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        if (clues != null) {
            ListAdapter adapter = createAdapter(clues, c);
            alertDialogBuilder.setAdapter(adapter, (dialog, which) -> clues.get(which).setVisible(true));
        }
        if (string != null) {
            alertDialogBuilder.setMessage(string);
        }
        if (images != null) {
            LayoutInflater factory = LayoutInflater.from(c);
            final View view = factory.inflate(R.layout.sample, null);
            alertDialogBuilder.setView(view);
            // set the images
            for (int i = 0; i < 4; i++) {
                // get the next imageview
                ImageView iv = view.findViewById(ALERTIMAGEIDS[i]);
                if (images[i] == null) {
                    iv.setImageDrawable(null);
                } else {
                    iv.setImageResource(images[i]);
                }
            }
        }
        alertDialogBuilder.setIcon(drawable);
        alertDialogBuilder.setTitle(name);
        alertDialogBuilder.setNegativeButton("ok", (dialogInterface, i) -> {
            alertDialog.cancel();
            alertDialog.dismiss();
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * creates an adapter for a list of clues
     *
     * @param clues
     * @param context
     * @return
     */
    public ListAdapter createAdapter(ArrayList<Clue> clues, Context context) {

        ListAdapter adapter = new ArrayAdapter<Clue>(
                context,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                clues)
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(clues.get(position).getIcon(), 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int)(5 * context.getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };

        return adapter;
    }
}
