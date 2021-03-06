package com.example.pr_idi.mydatabaseexample.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.Film;
import com.example.pr_idi.mydatabaseexample.model.FilmComparatorTitle;
import com.example.pr_idi.mydatabaseexample.model.FilmData;
import com.example.pr_idi.mydatabaseexample.model.RecyclerViewAdapter;

import org.w3c.dom.Text;

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
    protected FloatingActionButton mFAB;


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
            adapter = new RecyclerViewAdapter(parentActivity, feedsList, showAddToWatchlistBtn());
            DrawerActivity d = (DrawerActivity) getActivity();
            d.setAdapter(adapter);
            mRecyclerView.setAdapter(adapter);

            // Hide progressbar once films are loaded
            mProgressBar.setVisibility(View.GONE);

            // Hide add to watchlist button if we are in watchlist view


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

            mFAB = (FloatingActionButton) view.findViewById(R.id.fab);
            mFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DrawerActivity)getActivity()).createNewFilm();
                }
            });

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
        if(noResultsFound != null) noResultsFound.setVisibility(View.GONE);
        super.onStop();
    }

    protected boolean showAddToWatchlistBtn() {
        return true;
    }



}
