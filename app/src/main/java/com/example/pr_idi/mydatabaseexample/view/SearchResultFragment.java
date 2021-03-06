package com.example.pr_idi.mydatabaseexample.view;

import android.os.Bundle;
import android.view.View;

import com.example.pr_idi.mydatabaseexample.model.Film;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchResultFragment extends BasicFragment {
    private static final String ARG_PARAM1 = "param1";

    public SearchResultFragment() {

    }

    public static SearchResultFragment newInstance(String param1) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getFeedsList() {
        // Filter the elements by the given query
        feedsList.clear();
        List<Film> allFilms = db.getAllFilms();
        for (int i = 0; i < allFilms.size(); i++) {
            Film current = allFilms.get(i);
            if (current.getTitle().toLowerCase().contains(mParam1.toLowerCase())) {
                feedsList.add(current);
            } else if (current.getDirector().toLowerCase().contains(mParam1.toLowerCase())) {
                feedsList.add(current);
            } else if (current.getProtagonist().toLowerCase().contains(mParam1.toLowerCase())) {
                feedsList.add(current);
            }
        }
        // Show if no results are found
        if(feedsList.size() == 0) {
            noResultsFound.setText("No results found for " + mParam1 + ".");
            noResultsFound.setVisibility(View.VISIBLE);
        }

        // Sort by year
        Collections.sort(feedsList, new Comparator<Film>() {
            @Override
            public int compare(Film f1, Film f2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return f1.getYear()-f2.getYear();
            }
        });
    }
}
