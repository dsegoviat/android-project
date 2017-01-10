package com.example.pr_idi.mydatabaseexample.model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.example.pr_idi.mydatabaseexample.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {
    private List<Film> filmList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<Film> filmList) {
        this.filmList = filmList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_results_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Film film = filmList.get(i);

        //Setting text view title
        String title = (film.getTitle() == null) ? "" : film.getTitle();
        int year = film.getYear();
        String director = (film.getDirector() == null) ? "" : film.getDirector();
        String protagonist = (film.getProtagonist() == null) ? "" : film.getProtagonist();
        String country = (film.getCountry() == null) ? "" : film.getCountry();
        int rating = film.getCritics_rate();

        customViewHolder.filmTitle.setText(Html.fromHtml(title + " (" + year+ ")"));
        customViewHolder.filmDirector.setText(Html.fromHtml(director));
        customViewHolder.filmProtagonista.setText(Html.fromHtml(protagonist));
        customViewHolder.filmPais.setText(Html.fromHtml(country));
        customViewHolder.filmPuntuacio.setText(Html.fromHtml(String.valueOf(rating) + "/5"));
    }

    @Override
    public int getItemCount() {
        return (null != filmList ? filmList.size() : 0);
    }

    public void refreshFilmsList(List<Film> l) {
        filmList = l;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView filmTitle;
        protected TextView filmDirector;
        protected TextView filmProtagonista;
        protected TextView filmPais;
        protected TextView filmPuntuacio;

        public CustomViewHolder(View view) {
            super(view);
            this.filmTitle = (TextView) view.findViewById(R.id.title);
            this.filmDirector = (TextView) view.findViewById(R.id.director);
            this.filmProtagonista = (TextView) view.findViewById(R.id.protagonista);
            this.filmPais = (TextView) view.findViewById(R.id.pais);
            this.filmPuntuacio = (TextView) view.findViewById(R.id.puntuacio);
        }
    }

    public void onItemRemove(final int position, final RecyclerView recyclerView, final FilmData db, final Film film) {
        Snackbar snackbar = Snackbar
                .make(recyclerView, "Film " + film.getTitle() + " removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Film newFilm = db.createFilm(film.getTitle(), film.getDirector(), film.getCountry(),
                                film.getYear(), film.getProtagonist(), film.getCritics_rate());
                        filmList.add(position, newFilm);
                        notifyItemInserted(position);
                        recyclerView.scrollToPosition(position);
                        Log.d("Inserted film ", newFilm.getTitle() + ". id: " + Long.toString(newFilm.getId()));
                    }
                });
        snackbar.show();
        notifyItemRemoved(position);
    }
}
