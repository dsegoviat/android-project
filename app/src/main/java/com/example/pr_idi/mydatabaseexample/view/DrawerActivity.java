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
import com.example.pr_idi.mydatabaseexample.model.FilmData;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private NavigationView mNavigationView;
    private static int mCurrentActivity = 0;
    private FilmData filmData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Film Manager");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);


        /** COMMENT LATER JUST TESTING **/
        // FOR NAVIGATION VIEW ITEM TEXT COLOR
//        int[][] state = new int[][] {
//                new int[] {-android.R.attr.state_enabled}, // disabled
//                new int[] {android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_checked}, // unchecked
//                new int[] { android.R.attr.state_pressed}  // pressed
//        };
//
//        int[] textColor = new int[] {
//                Color.BLACK,
//                Color.BLACK,
//                Color.BLACK,
//                Color.BLACK
//        };
//
//        ColorStateList csl = new ColorStateList(state, textColor);
//
//
//    // FOR NAVIGATION VIEW ITEM ICON COLOR
//        int[][] states = new int[][] {
//                new int[] {-android.R.attr.state_enabled}, // disabled
//                new int[] {android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_checked}, // unchecked
//                new int[] { android.R.attr.state_pressed}  // pressed
//        };
//
//        int[] iconColor = new int[] {
//                Color.BLACK,
//                Color.BLACK,
//                Color.BLACK,
//                Color.BLACK
//        };
//
//        ColorStateList csl2 = new ColorStateList(states, iconColor);
//
//        mNavigationView.setItemTextColor(csl);
//        mNavigationView.setItemIconTintList(csl2);
        /** TEST FINISH **/

        mNavigationView.getMenu().getItem(mCurrentActivity).setChecked(true);

        filmData = FilmData.getInstance();
        filmData.init(this);
        filmData.open();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            SearchResultFragment srf = new SearchResultFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, srf).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
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
//            uncheckAll();
//            mNavigationView.getMenu().getItem(0).setChecked(true);
            mCurrentActivity = 0;
        } else if (id == R.id.nav_watchlist) {
            TestFragment st = new TestFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_fragment, st).commit();

            mCurrentActivity = 1;
        } else if (id == R.id.nav_favs) {
            // type here

            mCurrentActivity = 2;
        } else if (id == R.id.nav_help) {
            // type here

            mCurrentActivity = 3;

        } else if (id == R.id.nav_about) {
            // type here

            mCurrentActivity = 4;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
