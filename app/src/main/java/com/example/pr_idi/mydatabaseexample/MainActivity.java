package com.example.pr_idi.mydatabaseexample;


import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private FilmData filmData;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private List<Film> mValues;
    private ArrayAdapter<Film> mAdapterList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBar mActionBar;

    private static final boolean DEBUG = true;

    private void updateList() {
        mValues.clear();
        mValues.addAll(filmData.getAllFilms());
        mAdapterList.notifyDataSetChanged();
    }

    private void addDrawerItems() {
        String[] drArray = { "Home" , "Help", "About" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mActionBar != null) mActionBar.setTitle("Menu");
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (mActionBar != null) mActionBar.setTitle(mActivityTitle);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        filmData = new FilmData(this);
        filmData.open();

        if (DEBUG) filmData.deleteAll();

        filmData.createFilm("Title1", "Dir1");
        filmData.createFilm("Title2", "Dir2");

        mValues = filmData.getAllFilms();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        mAdapterList = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mValues);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(mAdapterList);

        // set the ListView

        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        //set actions to the Navigation Drawer
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Home pressed",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Help pressed",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "About pressed",
                                Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        Button b = (Button) findViewById(R.id.but);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filmData.deleteAll();
                updateList();
                Log.d("MainActivity", "Everything was deleted");
            }
        });

        // action bar

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }

    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
//        @SuppressWarnings("unchecked")
//        ArrayAdapter<Film> adapter = (ArrayAdapter<Film>) getListAdapter();
//        Film film;
//        switch (view.getId()) {
//            case R.id.add:
//                String[] newFilm = new String[] { "Blade Runner", "Ridley Scott", "Rocky Horror Picture Show", "Jim Sharman", "The Godfather", "Francis Ford Coppola", "Toy Story", "John Lasseter" };
//                int nextInt = new Random().nextInt(4);
//                // save the new film to the database
//                film = filmData.createFilm(newFilm[nextInt*2], newFilm[nextInt*2 + 1]);
//                adapter.add(film);
//                break;
//            case R.id.delete:
//                if (getListAdapter().getCount() > 0) {
//                    film = (Film) getListAdapter().getItem(0);
//                    filmData.deleteFilm(film);
//                    adapter.remove(film);
//                }
//                break;
//        }
//
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        filmData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        filmData.close();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean def = super.onOptionsItemSelected(menuItem);
        return mDrawerToggle.onOptionsItemSelected(menuItem) || def;
    }

}