package com.example.pr_idi.mydatabaseexample.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.*;

import java.util.Collections;
import java.util.List;

public class SearchResultFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private List<Film> feedsList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private ProgressBar progressBar;
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
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        db = FilmData.getInstance();

        feedsList = db.getAllFilms();

        adapter = new RecyclerViewAdapter(SearchResultFragment.this, feedsList);
        mRecyclerView.setAdapter(adapter);
    }*/

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
            db = FilmData.getInstance();
            feedsList = db.getAllFilms();
            final LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this.getActivity());
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new RecyclerViewAdapter(this.getActivity(), feedsList);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
