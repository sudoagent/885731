package com.sudoinc.cricketscorer;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.sudoinc.cricketscorer.commons.Fragment.ScoreCardFragment;
import com.sudoinc.cricketscorer.R;

public class ScoreSheetActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Scorecard";
    private static final String TAG_STRIKER = "Change Striker";
    private static final String TAG_NON_STRIKER = "Change Non-Striker";
    private static final String TAG_BOWLER = "Change Bowler";
    private static final String TAG_BATT_SC = "Batting Scorecard";
    private static final String TAG_BOWL_SC = "Bowling Scorecard";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private String matchID;
    private String TeamAID;
    private String TeamBID;
    private String MatchType;
    private String tossWinnerID;
    private String tossDecision;
    private String selectedOvers;
    private String ResumeOrNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_sheet);
        toolbar = findViewById(R.id.toolbar_score_sheet);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = findViewById(R.id.nav_drawerlayout_score_sheet);
        navigationView = findViewById(R.id.nav_viewer_score_sheet);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.nav_header_textView);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.score_sheet_menu_array);

        // load nav menu header data
        txtName.setText(R.string.nav_title);
//        txtName.setTextColor(R.color.color_on_primary);
//        txtName.setTextSize(getResources().get);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        Bundle thisArguments = getIntent().getExtras();

        this.matchID = thisArguments.getString("matchID");
        this.TeamAID = thisArguments.getString("selectedMyTeamID");
        this.TeamBID = thisArguments.getString("selectedOppTeamID");
        this.MatchType = thisArguments.getString("selectedMatchType");
        this.tossWinnerID = thisArguments.getString("tossWinnerID");
        this.tossDecision = thisArguments.getString("tossDecision");
        this.selectedOvers = thisArguments.getString("selectedOvers");
        this.ResumeOrNew = thisArguments.getString("ResumeOrNew");
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                Bundle args = new Bundle();
                args.putString("matchID", matchID);
                args.putString("selectedMyTeamID", TeamAID);
                args.putString("selectedOppTeamID", TeamBID);
                args.putString("selectedMatchType", MatchType);
                args.putString("tossWinnerID", tossWinnerID);
                args.putString("tossDecision", tossDecision);
                args.putString("selectedOvers", selectedOvers);
                args.putString("ResumeOrNew", ResumeOrNew);

                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.homeFrame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                ScoreCardFragment homeFragment = new ScoreCardFragment();
                return homeFragment;
                //TODO: add other Fragments
//            case 1:
//                // photos
//                PhotosFragment photosFragment = new PhotosFragment();
//                return photosFragment;
//            case 2:
//                // movies fragment
//                MoviesFragment moviesFragment = new MoviesFragment();
//                return moviesFragment;
//            case 3:
//                // notifications fragment
//                NotificationsFragment notificationsFragment = new NotificationsFragment();
//                return notificationsFragment;
//
//            case 4:
//                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new ScoreCardFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.mainScorecard:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        Toast.makeText(getApplicationContext(), "Clicked on mainScorecard!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.editStriker:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_STRIKER;
                        Toast.makeText(getApplicationContext(), "Clicked on editStriker!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.editNonStriker:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_NON_STRIKER;
                        Toast.makeText(getApplicationContext(), "Clicked on editNonStriker!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.editBowler:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BOWLER;
                        Toast.makeText(getApplicationContext(), "Clicked on editBowler!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.viewBattScoreCard:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_BATT_SC;
                        Toast.makeText(getApplicationContext(), "Clicked on BattScorecard!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.viewBowlScoreCard:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_BOWL_SC;
                        Toast.makeText(getApplicationContext(), "Clicked on BowlScoreCard!", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        navItemIndex = 0;
                        Toast.makeText(getApplicationContext(), "Clicked on mainScorecard!", Toast.LENGTH_LONG).show();
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }
}