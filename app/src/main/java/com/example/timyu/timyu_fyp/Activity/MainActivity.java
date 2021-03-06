package com.example.timyu.timyu_fyp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.Class.SharedPreferencesEditor;
import com.example.timyu.timyu_fyp.Class.UserManager;
import com.example.timyu.timyu_fyp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView loginUser;
    TextView loginEmail;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //紅色制
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        loginUser = (TextView) header.findViewById(R.id.txtUser);
        loginEmail = (TextView) header.findViewById(R.id.txtEmail);
        SharedPreferencesEditor sharedPreferencesEditor = new SharedPreferencesEditor(this);
        UserManager.getInstance().setUser(this, sharedPreferencesEditor.getUser());
        setNavViewMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewMenu();

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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean isLogin = UserManager.getInstance().isLoggedIn();

        switch (id){
            case R.id.nav_login:

                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
            case R.id.nav_designPlan:

                Intent designPlan = new Intent(this, DesignPlanActivity.class);
                startActivity(designPlan);
                break;
            case R.id.nav_slideshow:
                if (isLogin) {
                    Intent planList = new Intent(this, UserPlanListActivity.class);
                    startActivity(planList);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please Login")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(login);
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    builder.create().show();
                }
                break;
            case R.id.nav_manage:

                if (isLogin) {
                    Intent recommendForm = new Intent(this, RecommendFormActivity.class);
                    startActivity(recommendForm);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please Login")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(login);
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    builder.create().show();
                }
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;

        }
        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setNavViewMenu() {
        boolean isLogin = UserManager.getInstance().isLoggedIn();


        if (isLogin) {
            loginUser.setText("User Name : " + UserManager.getInstance().getUser().getName());
            loginEmail.setText("Email : " + UserManager.getInstance().getUser().getEmail());
        }
    }
}
