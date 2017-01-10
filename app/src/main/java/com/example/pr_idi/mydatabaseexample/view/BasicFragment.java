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
import com.example.pr_idi.mydatabaseexample.model.Film;
import com.example.pr_idi.mydatabaseexample.model.FilmComparatorTitle;
import com.example.pr_idi.mydatabaseexample.model.FilmData;
import com.example.pr_idi.mydatabaseexample.model.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    protected String mParam1;

    protected List<Film> feedsList;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter adapter;
    protected ProgressBar mProgressBar;
    protected TextView noResultsFound;
    protected FilmData db;
    protected Context parentActivity;


    public BasicFragment() {

    }

//    public static BasicFragment newInstance(String param1) {
//        BasicFragment fragment = new BasicFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        db = FilmData.getInstance();
        feedsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        parentActivity = this.getActivity();
        super.onViewCreated(view, savedInstanceState);

        if (view != null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.basic_recycler);
            noResultsFound = (TextView) view.findViewById(R.id.no_results_found);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            getFeedsList();

            // Set Recycler View's adapter
            final LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this.getActivity());
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new RecyclerViewAdapter(parentActivity, feedsList);
            mRecyclerView.setAdapter(adapter);

            // Hide progressbar once films are loaded
            mProgressBar.setVisibility(View.GONE);

            // Define event for swiping a film (deleting it)
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    //Remove swiped item from list and notify the RecyclerView
                    int position = viewHolder.getLayoutPosition();
                    Film film = feedsList.get(position);
                    feedsList.remove(position);
                    db.deleteFilm(film);
                    adapter.onItemRemove(position, mRecyclerView, db, film);
                    //getFeedsList();
                    adapter.refreshFilmsList(feedsList);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
    }

    protected void getFeedsList() {
        // Filter the elements by the given query
        feedsList.clear();
        feedsList.addAll(db.getAllFilms());
        // Sort by year
        Collections.sort(feedsList, new FilmComparatorTitle());
    }

    @Override
    public void onStop() {
        noResultsFound.setVisibility(View.GONE);
        super.onStop();
    }

}
