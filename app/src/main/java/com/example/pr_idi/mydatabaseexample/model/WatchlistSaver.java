package com.example.pr_idi.mydatabaseexample.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class WatchlistSaver {

    private static WatchlistSaver instance = null;
    private final static String FILE_NAME = "watchl.txt";
    private Context context;
    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    private FilmData db;

    public WatchlistSaver() {

    }

    public static WatchlistSaver getInstance() {
        if (instance == null) {
            instance = new WatchlistSaver();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        db = FilmData.getInstance();
    }

    public List<Film> readWatchlist() {
        List<Long> ids = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    ids.add(Long.parseLong(receiveString));
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        List<Film> result = new ArrayList<>();
        List<Film> allFilms = db.getAllFilms();
        for (Long l : ids) {
            for (Film f : allFilms) {
                if (f.getId() == l) {
                    result.add(f);
                }
            }
        }

        return result;
    }

    public void writeWatchlist(List<Film> savedFilms) {
        Log.d("I WILL", "WRITE");
        //TODO write
        String data = "";
        for (Film f : savedFilms) {
            data += Long.toString(f.getId()) + System.getProperty("line.separator");
        }
        Log.d("IM GONNA WRITE THIS", data);

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }
}
