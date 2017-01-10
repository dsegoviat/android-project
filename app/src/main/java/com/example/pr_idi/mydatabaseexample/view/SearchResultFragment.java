package com.example.pr_idi.mydatabaseexample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.*;

import java.util.ArrayList;
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
            if(allFilms.get(i).getTitle().toLowerCase().contains(mParam1.toLowerCase())) {
                feedsList.add(allFilms.get(i));
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
