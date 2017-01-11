package com.example.pr_idi.mydatabaseexample.model;


import java.util.ArrayList;
import java.util.List;

public class WatchlistFilms {

    private static WatchlistFilms instance = null;
    private List<Film> savedFilms;

    protected WatchlistFilms() {
        savedFilms = new ArrayList<>();
    }

    public static WatchlistFilms getInstance() {
        if (instance == null)
            instance = new WatchlistFilms();
        return instance;
    }

    public List<Film> getSavedFilms() {
        return savedFilms;
    }

    public void addToWatchlist(Film film) {
        savedFilms.add(film);
    }

    public void removeFromWatchlist(Film film) {
        for (Film f : savedFilms) {
            if (f.getId() == film.getId())
                savedFilms.remove(film);
        }
    }

    public boolean isInWatchlist(Film film) {
        for (Film f : savedFilms) {
            if (f.getId() == film.getId())
                return true;
        }
        return false;
    }
}
