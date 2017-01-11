package com.example.pr_idi.mydatabaseexample.view;

import android.app.Dialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.model.Film;
import com.example.pr_idi.mydatabaseexample.model.FilmData;
import com.example.pr_idi.mydatabaseexample.model.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private NavigationView mNavigationView;
    private static int mCurrentActivity = 0;
    private static int mPrevActivity;
    private FilmData db;
    private SearchView searchView;
    private List<Film> watchlist = new ArrayList<>();
    private static boolean firstStart = true;
    private Dialog rankDialog;
    private RecyclerViewAdapter adapter;


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

        db = FilmData.getInstance();
        db.init(this);
        db.open();

        //TODO
        if(db.getAllFilms().size() == 0) {
            db.deleteAll();
            db.createFilm("AFilm", "Dir", "Sweden", 1999, "Brad Pitt", 4);
            db.createFilm("CFilm", "Dir", "Germany", 2002, "Nicholas Cage", 5);
            db.createFilm("BFilm", "Dir2", "United States", 2001, "Tom Hanks", 3);
        }
        //TODO
        if (firstStart) {
            checkMenuItem(0);
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
            navigateHome();
        } else if (id == R.id.nav_watchlist) {
            navigateWatchlist();
        } else if (id == R.id.nav_add) {
            createNewFilm();
        } else if (id == R.id.nav_help) {
            navigateHelp();
        } else if (id == R.id.nav_about) {
            navigateAbout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void navigateHome() {
        BasicFragment bf = new BasicFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_fragment, bf, "HOME").commit();
        mCurrentActivity = 0;
    }

    public void navigateWatchlist() {
        WatchlistFragment wf = new WatchlistFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_fragment, wf, "WATCH").commit();
        mCurrentActivity = 1;
    }

    public void createNewFilm() {
        mPrevActivity = mCurrentActivity;
        uncheckAll();
        mNavigationView.getMenu().getItem(2).setChecked(true);
        CreateItemFragment cif = CreateItemFragment.newInstance(mPrevActivity);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_fragment, cif, "CREATE").addToBackStack("home").commit();

        mCurrentActivity = 2;
    }

    public void navigateHelp() {
        HelpFragment hf = new HelpFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_fragment, hf, "HELP").commit();
        mCurrentActivity = 3;
    }

    public void navigateAbout() {
        AboutFragment af = new AboutFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_fragment, af, "ABOUT").commit();
        mCurrentActivity = 4;
    }

    public void checkMenuItem(int pos) {
        uncheckAll();
        mNavigationView.getMenu().getItem(pos).setChecked(true);
    }

    public void taskRatingPopup(final Film film) {
        Context mainScreen = this;
        rankDialog = new Dialog(mainScreen);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(film.getCritics_rate()); //Change this
//        Drawable drawable = ratingBar.getProgressDrawable();
//        drawable.setColorFilter(Color.parseColor("#f5f8a0"), PorterDuff.Mode.SRC_ATOP);
        TextView title = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        title.setText("Films name");
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stars = (int) ratingBar.getRating();
                Log.d("Stars", Integer.toString(stars));
                film.setCritics_rate(stars);
                db.deleteFilm(film);
                db.createFilm(film.getTitle(), film.getDirector(), film.getCountry(),
                        film.getYear(), film.getProtagonist(), film.getCritics_rate());
                rankDialog.dismiss();
                if (adapter != null) {
                    adapter.onRateChanged();
                }
            }
        });
        rankDialog.show();


    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
