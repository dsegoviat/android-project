package com.example.pr_idi.mydatabaseexample.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private FilmData db;

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

        //TODO REMOVE
        db.deleteAll();
        db.createFilm("AFilm", "Dir");
        db.createFilm("CFilm", "Dir");
        db.createFilm("BFilm", "Dir2");
        //TODO REMOVE
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            noResultsFound = (TextView) view.findViewById(R.id.no_results_found);

            feedsList = new ArrayList<>();
            List<Film> allFilms = db.getAllFilms();
            for (int i = 0; i < allFilms.size(); i++) {
                if(allFilms.get(i).getTitle().toLowerCase().contains(mParam1.toLowerCase())) {
                    feedsList.add(allFilms.get(i));
                }
            }
            if(feedsList.size() == 0) noResultsFound.setVisibility(View.VISIBLE);

            final LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this.getActivity());
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new RecyclerViewAdapter(this.getActivity(), feedsList);
            mRecyclerView.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        }
    }
}
