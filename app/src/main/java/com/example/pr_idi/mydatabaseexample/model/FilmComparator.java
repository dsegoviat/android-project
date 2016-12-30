package com.example.pr_idi.mydatabaseexample.model;

import java.util.Comparator;

/**
 * @author dsegoviat
 */
public class FilmComparator implements Comparator {

    @Override
    public int compare(Object left, Object right) {
        Film leftFilm = (Film) left;
        Film rightFilm = (Film) right;
        String filmTitleLeft = leftFilm.getTitle();
        String filmTitleRight = rightFilm.getTitle();
        return filmTitleLeft.compareTo(filmTitleRight);
    }
}
