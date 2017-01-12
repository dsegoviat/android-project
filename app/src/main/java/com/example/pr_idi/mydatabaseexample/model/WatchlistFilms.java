package com.example.pr_idi.mydatabaseexample.model;


import java.util.ArrayList;
import java.util.List;

public class WatchlistFilms {

    private static WatchlistFilms instance = null;
    private List<Film> savedFilms;
    private WatchlistSaver watchSaver;

    protected WatchlistFilms() {
        savedFilms = new ArrayList<>();
        watchSaver = WatchlistSaver.getInstance();
    }

    public static WatchlistFilms getInstance() {
        if (instance == null)
            instance = new WatchlistFilms();
        return instance;
    }

    public void load() {
        savedFilms = watchSaver.readWatchlist();
    }

    public List<Film> getSavedFilms() {
        return savedFilms;
    }

    public void addToWatchlist(Film film) {
        savedFilms.add(film);
//        onWatchlistChanged();
    }

    public void removeFromWatchlist(Film film) {
//        int res = -1;
//        for (int i = 0; i < savedFilms.size(); ++i) {
//            if (savedFilms.get(i).getId() == film.getId())
//                res = i;
//        }
//        if (res >= 0) savedFilms.remove(res);
        savedFilms.remove(film);
//        onWatchlistChanged();
    }

    public boolean isInWatchlist(Film film) {
        for (Film f : savedFilms) {
            if (f.getId() == film.getId())
                return true;
        }
        return false;
    }

    public void onWatchlistChanged() {
        watchSaver.writeWatchlist(savedFilms);
    }
}
