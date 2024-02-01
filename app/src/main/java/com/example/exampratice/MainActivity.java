package com.example.exampratice;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exampratice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
   // private ActivityMainBinding binding;
   DrawerLayout drawerLayout;
   BottomNavigationView bottomNavigationView;

    FrameLayout main_frame;

    NavigationView navigationView;
    private TextView drawerProfileName , drawerProfileText;

    private static final int FRA_HOME= 0;
    private static final int FRA_Leader= 1;
    private static final int FRA_Acc= 2;

    private int mCurrFra =FRA_HOME;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();


        // khong co getheaderView thi se khong nhan nav_drawer
        drawerProfileName= navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

            String name = DataBase.myProfile.getName();


       drawerProfileName.setText(DataBase.myProfile.getName().toString());

    drawerProfileText.setText(name.toUpperCase().substring(0,1)) ;


        setFragment(new CategoryFragment());

    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.bottomnavigation_home:
                    setFragment(new CategoryFragment());
                    return true;

                case R.id.bottomnavigation_leader:
                    //bottomNavigationView.setSelectedItemId(R.id.navLeader);
                    setFragment(new LeaderBoardFragment());
                    return true;

                case R.id.bottomnavigation_account:
                    setFragment(new AccountFragment());
                    return true;


            }

            return false;


        }
    };


    private void addEvents() {




    }




    private void addControls() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view);
        main_frame = findViewById(R.id.main_frame);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);



        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Categories");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // xem lai phan nay
        navigationView.setNavigationItemSelectedListener(this);

        replaceFraments(new CategoryFragment());

        navigationView.getMenu().findItem(R.id.nav_home1).setChecked(true);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.nav_home1)
        {
            if(mCurrFra!= FRA_HOME)
            {
                replaceFraments(new CategoryFragment());

                navigationView.getMenu().findItem(id).setChecked(true);


                mCurrFra = FRA_HOME;
            }
        }
        else if(id==R.id.nav_leaderboard1)
        {
            if(mCurrFra!= FRA_Leader)
            {
                replaceFraments(new LeaderBoardFragment());

                mCurrFra = FRA_Leader;
            }
        }
        else if(id==R.id.nav_account1)
        {
            if(mCurrFra!= FRA_Acc)
            {
                replaceFraments(new AccountFragment());

                mCurrFra = FRA_Acc;
            }
        }



        drawerLayout.closeDrawer(GravityCompat.START);


        return false;
    }




    private void setFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(),fragment);
        transaction.commit();
    }

    private void replaceFraments(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.isDrawerOpen(GravityCompat.END);
        }
        else
        {
            super.onBackPressed();
        }
    }



}