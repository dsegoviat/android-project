package com.example.pr_idi.mydatabaseexample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.*;

public class CreateItemFragment extends Fragment {
    private Context parentActivity;
    private FilmData db;
    private EditText titleInput;
    private EditText countryInput;
    private EditText yearInput;
    private EditText directorInput;
    private EditText protagonistInput;
    private SeekBar ratingBar;
    private TextView ratingText;
    private Button sendButton;

    //BIG TODO: onBackPresed close fragment

    public CreateItemFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FilmData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        parentActivity = this.getActivity();

        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            titleInput = (EditText) view.findViewById(R.id.titol);
            countryInput = (EditText) view.findViewById(R.id.pais);
            yearInput = (EditText) view.findViewById(R.id.any);
            directorInput = (EditText) view.findViewById(R.id.director);
            protagonistInput = (EditText) view.findViewById(R.id.protagonista);
            ratingBar = (SeekBar) view.findViewById(R.id.puntuacio);
            ratingText = (TextView) view.findViewById(R.id.text_puntuacio);
            sendButton = (Button) view.findViewById(R.id.send_button);

            ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    ratingText.setText("Puntuació: " + String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(titleInput.getText() == null || titleInput.getText().toString().equals("") ||
                       directorInput.getText() == null || directorInput.getText().toString().equals("") ||
                       countryInput.getText() == null || countryInput.getText().toString().equals("") ||
                       yearInput.getText() == null || yearInput.getText().toString().equals("") ||
                       protagonistInput.getText() == null || protagonistInput.getText().toString().equals("")) {

                        Toast toast = Toast.makeText(parentActivity, "Missing field, please fill it first", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        db.createFilm(  titleInput.getText().toString(),
                                directorInput.getText().toString(),
                                countryInput.getText().toString(),
                                Integer.parseInt(yearInput.getText().toString()),
                                protagonistInput.getText().toString(),
                                ratingBar.getProgress());

                        Toast toast = Toast.makeText(parentActivity, "Film added successfully", Toast.LENGTH_SHORT);
                        toast.show();

                        ((DrawerActivity)getActivity()).navigateHome();
                    }
                }
            });
        }
    }
}
