package com.example.pr_idi.mydatabaseexample.model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.view.DrawerActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {
    private List<Film> filmList;
    private Context mContext;
    private boolean showWatchlistBtn;

    public RecyclerViewAdapter(Context context, List<Film> filmList, boolean swb) {
        this.filmList = filmList;
        this.mContext = context;
        this.showWatchlistBtn = swb;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_results_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
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
        customViewHolder.filmPuntuacio.setVisibility(View.VISIBLE);
        switch(rating) {
            case 0:
                customViewHolder.filmPuntuacio.setVisibility(View.GONE);
            case 1:
                customViewHolder.filmPuntuacio.setImageDrawable(mContext.getResources().getDrawable(R.drawable.r1star, mContext.getApplicationContext().getTheme()));
                break;
            case 2:
                customViewHolder.filmPuntuacio.setImageDrawable(mContext.getResources().getDrawable(R.drawable.r2star, mContext.getApplicationContext().getTheme()));
                break;
            case 3:
                customViewHolder.filmPuntuacio.setImageDrawable(mContext.getResources().getDrawable(R.drawable.r3star, mContext.getApplicationContext().getTheme()));
                break;
            case 4:
                customViewHolder.filmPuntuacio.setImageDrawable(mContext.getResources().getDrawable(R.drawable.r4star, mContext.getApplicationContext().getTheme()));
                break;
            case 5:
                customViewHolder.filmPuntuacio.setImageDrawable(mContext.getResources().getDrawable(R.drawable.r5star, mContext.getApplicationContext().getTheme()));
                break;
        }

        if(showWatchlistBtn) {
            if(((DrawerActivity) mContext).isInWatchlist(film)) {
                customViewHolder.addToWatchlist.setImageDrawable(mContext.getResources().getDrawable(R.drawable.watchlist_remove, mContext.getApplicationContext().getTheme()));
            }
            customViewHolder.addToWatchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((DrawerActivity) mContext).isInWatchlist(film)) {
                        ((DrawerActivity) mContext).removeFromWatchlist(film);

                        Toast toast = Toast.makeText(mContext, "Film removed from watchlist", Toast.LENGTH_SHORT);
                        toast.show();

                        customViewHolder.addToWatchlist.setImageDrawable(mContext.getResources().getDrawable(R.drawable.watchlist_add, mContext.getApplicationContext().getTheme()));
                    } else {
                        ((DrawerActivity) mContext).addToWatchlist(film);

                        Toast toast = Toast.makeText(mContext, "Film added to watchlist", Toast.LENGTH_SHORT);
                        toast.show();

                        customViewHolder.addToWatchlist.setImageDrawable(mContext.getResources().getDrawable(R.drawable.watchlist_remove, mContext.getApplicationContext().getTheme()));
                    }
                }
            });
        } else {
            customViewHolder.addToWatchlist.setVisibility(View.GONE);
        }

        customViewHolder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerActivity d = (DrawerActivity) mContext;
                d.taskRatingPopup(film);
            }
        });
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
        protected ImageView filmPuntuacio;
        protected ImageButton addToWatchlist;
        protected ImageView starButton;

        public CustomViewHolder(View view) {
            super(view);
            this.filmTitle = (TextView) view.findViewById(R.id.title);
            this.filmDirector = (TextView) view.findViewById(R.id.director);
            this.filmProtagonista = (TextView) view.findViewById(R.id.protagonista);
            this.filmPais = (TextView) view.findViewById(R.id.pais);
            this.filmPuntuacio = (ImageView) view.findViewById(R.id.puntuacio);
            this.addToWatchlist = (ImageButton) view.findViewById(R.id.add_watchlist);
            this.starButton = (ImageView) view.findViewById(R.id.rate_star2);
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

    public void onRateChanged() {
        filmList.clear();
        filmList.addAll(FilmData.getInstance().getAllFilms());
        notifyDataSetChanged();
    }


}
