package com.example.pr_idi.mydatabaseexample.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.Film;
import com.example.pr_idi.mydatabaseexample.model.FilmData;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private NavigationView mNavigationView;
    private static int mCurrentActivity = 0;
    private FilmData filmData;
    private SearchView searchView;
    private List<Film> watchlist = new ArrayList<>();
    private static boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Film Finder");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().getItem(mCurrentActivity).setChecked(true);

        filmData = FilmData.getInstance();
        filmData.init(this);
        filmData.open();

        //TODO
        if(filmData.getAllFilms().size() == 0) {
            filmData.deleteAll();
            filmData.createFilm("AFilm", "Dir", "Sweden", 1999, "Brad Pitt", 4);
            filmData.createFilm("CFilm", "Dir", "Germany", 2002, "Nicholas Cage", 5);
            filmData.createFilm("BFilm", "Dir2", "United States", 2001, "Tom Hanks", 3);
        }
        //TODO
        if (firstStart) {
            BasicFragment bf = new BasicFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, bf).commit();
        }
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            SearchResultFragment srf = SearchResultFragment.newInstance(query);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, srf).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uncheckAll() {
        Menu m = mNavigationView.getMenu();
        for (int i = 0; i < m.size(); ++i) {
            m.getItem(i).setChecked(false);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        uncheckAll();
        item.setChecked(true);
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            BasicFragment bf = new BasicFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, bf).commit();
            mCurrentActivity = 0;
        } else if (id == R.id.nav_watchlist) {
            WatchlistFragment wf = new WatchlistFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, wf).commit();

            mCurrentActivity = 1;
        } else if (id == R.id.nav_add) {
            mNavigationView.getMenu().getItem(mCurrentActivity).setChecked(true);
            createNewFilm();
        } else if (id == R.id.nav_help) {
            HelpFragment hf = new HelpFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, hf).commit();
            mCurrentActivity = 3;

        } else if (id == R.id.nav_about) {
            // type here
            AboutFragment af = new AboutFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, af).commit();
            mCurrentActivity = 4;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createNewFilm() {
        CreateItemFragment cif = new CreateItemFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_fragment, cif).commit();
    }

    public List<Film> getWatchlist() {
        return watchlist;
    }

    public List<Film> addToWatchlist(Film film) {
        watchlist.add(film);
        return watchlist;
    }

    public List<Film> removeFromWatchlist(Film film) {
        for (int i = 0; i < watchlist.size(); i++) {
            if(watchlist.get(i).getId() == film.getId()) {
                watchlist.remove(i);
            }
        }
        return watchlist;
    }

}
