package com.example.pr_idi.mydatabaseexample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.Film;
import com.example.pr_idi.mydatabaseexample.model.RecyclerViewAdapter;

import java.util.List;

/**
 * Created by rogia on 09/01/17.
 */

public class WatchlistFragment extends BasicFragment {

    private List<Film> savedFilms;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private TextView noSavedFilms;
    private Context parentActivity;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    public static WatchlistFragment newInstance(String param1, String param2) {
        WatchlistFragment fragment = new WatchlistFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.watchlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        parentActivity = this.getActivity();

        if (view != null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            noSavedFilms = (TextView) view.findViewById(R.id.no_saved_films);

            savedFilms = ((DrawerActivity)getActivity()).getWatchlist();

            // Set Recycler View's adapter
            final LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this.getActivity());
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new RecyclerViewAdapter(parentActivity, savedFilms);
            mRecyclerView.setAdapter(adapter);

            // Define event for swiping a film (deleting it)
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    //Remove swiped item from list and notify the RecyclerView
                    ((DrawerActivity)getActivity()).removeFromWatchlist(savedFilms.get(viewHolder.getLayoutPosition()));
                    savedFilms = ((DrawerActivity)getActivity()).getWatchlist();
                    adapter.refreshFilmsList(savedFilms);

                    Toast toast = Toast.makeText(parentActivity, "Removed item from watchlist", Toast.LENGTH_SHORT);
                    toast.show();
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
    }
}
