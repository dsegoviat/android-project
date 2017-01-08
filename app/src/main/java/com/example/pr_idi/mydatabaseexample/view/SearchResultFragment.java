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
import java.util.List;

public class SearchResultFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private List<Film> feedsList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView noResultsFound;
    private Button newFilmBtn;
    private FilmData db;
    private Context parentActivity;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        db = FilmData.getInstance();
        db.init(this.getActivity());
        db.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        parentActivity = this.getActivity();

        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            noResultsFound = (TextView) view.findViewById(R.id.no_results_found);
            newFilmBtn = (Button) view.findViewById(R.id.new_film_button);

            getFeedsList();

            // Set Recycler View's adapter
            final LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this.getActivity());
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new RecyclerViewAdapter(parentActivity, feedsList);
            mRecyclerView.setAdapter(adapter);

            // Hide progressbar once films are loaded
            progressBar.setVisibility(View.GONE);

            // Define event for swiping a film (deleting it)
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    //Remove swiped item from list and notify the RecyclerView
                    db.deleteFilm(feedsList.get(viewHolder.getLayoutPosition()));
                    getFeedsList();
                    adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                    adapter.refreshFilmsList(feedsList);

                    Toast toast = Toast.makeText(parentActivity, "Deleted item", Toast.LENGTH_LONG);
                    toast.show();
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

            // New film button event
            newFilmBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ((DrawerActivity)getActivity()).createNewFilm();
                }
            });
        }
    }

    private void getFeedsList() {
        // Filter the elements by the given query
        feedsList = new ArrayList<>();
        List<Film> allFilms = db.getAllFilms();
        for (int i = 0; i < allFilms.size(); i++) {
            if(allFilms.get(i).getTitle().toLowerCase().contains(mParam1.toLowerCase())) {
                feedsList.add(allFilms.get(i));
            }
        }
        // Show if no results are found
        if(feedsList.size() == 0) noResultsFound.setVisibility(View.VISIBLE);
    }
}
