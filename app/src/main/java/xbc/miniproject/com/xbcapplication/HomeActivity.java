package xbc.miniproject.com.xbcapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import xbc.miniproject.com.xbcapplication.fragment.AssignmentFragment;
import xbc.miniproject.com.xbcapplication.fragment.BatchFragment;
import xbc.miniproject.com.xbcapplication.fragment.BiodataFragment;
import xbc.miniproject.com.xbcapplication.fragment.ClassFragment;
import xbc.miniproject.com.xbcapplication.fragment.FeedbackFragment;
import xbc.miniproject.com.xbcapplication.fragment.HomeFragment;
import xbc.miniproject.com.xbcapplication.fragment.IdleNewsFragment;
import xbc.miniproject.com.xbcapplication.fragment.MonitoringFragment;
import xbc.miniproject.com.xbcapplication.fragment.TechnologyFragment;
import xbc.miniproject.com.xbcapplication.fragment.TestimonyFragment;
import xbc.miniproject.com.xbcapplication.fragment.TrainerFragment;
import xbc.miniproject.com.xbcapplication.fragment.UserFragment;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView headerNamaUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setPopupTheme(R.style.PopupMenu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        headerNamaUser = headerView.findViewById(R.id.headerNamaUser);
        headerNamaUser.setText(SessionManager.getUsername(context));

        setTitle("XBC MOBILE APPS");
        HomeFragment homeFragment= new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_all_menu, homeFragment,"XBC MOBILE APPS");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Bagian Menu Yang Terkoneksi dengan Fragment
        int id = menuItem.getItemId();

        //Menu MonitoringBiodata
        if (id == R.id.menuBiodata) {
            setActionBarTitle("Biodata");
            BiodataFragment biodataFragment = new BiodataFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, biodataFragment, "MonitoringBiodata");
            fragmentTransaction.commit();
        }
        //Menu Trainer
        else if (id == R.id.menuTrainer) {
            setActionBarTitle("Trainer");
            TrainerFragment trainerFragment = new TrainerFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, trainerFragment, "Trainer");
            fragmentTransaction.commit();

        }
        //Menu Technologi
        else if (id == R.id.menuTechnology) {
            setActionBarTitle("Technology");
            TechnologyFragment technologyFragment = new TechnologyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, technologyFragment, "Technology");
            fragmentTransaction.commit();
        }
        //Menu Bootcamp
        else if (id == R.id.menuBatch) {
            setActionBarTitle("Batch");
            BatchFragment batchFragment = new BatchFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, batchFragment, "Batch");
            fragmentTransaction.commit();
        } else if (id == R.id.menuClass) {
            setActionBarTitle("Class");
            ClassFragment classFragment = new ClassFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, classFragment, "Class");
            fragmentTransaction.commit();
        }
        //Menu Assestment
        else if (id == R.id.menuFiltering) {
            Toast.makeText(context, "Menu Filtering Saat Ini Belum Tersedia", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menuMiniProject) {
            Toast.makeText(context, "Menu MiniProject Saat Ini Belum Tersedia", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menuCustom) {
            Toast.makeText(context, "Menu Custom Saat Ini Belum Tersedia", Toast.LENGTH_SHORT).show();
        }

        //Menu Portal
        else if (id == R.id.menuFeedback) {
            setActionBarTitle("Feedback");
            FeedbackFragment feedbackFragment = new FeedbackFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, feedbackFragment, "Feedback");
            fragmentTransaction.commit();
        } else if (id == R.id.menuIdleNews) {
            setActionBarTitle("Idle News");
            IdleNewsFragment idleNewsFragment = new IdleNewsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, idleNewsFragment, "Idle News");
            fragmentTransaction.commit();
        } else if (id == R.id.menuKataIdle) {
            setActionBarTitle("Testimony");
            TestimonyFragment testimonyFragment = new TestimonyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, testimonyFragment, "Testimony");
            fragmentTransaction.commit();
        }
        //Menu Idle
        else if (id == R.id.menuMonitoring) {
            setActionBarTitle("Monitoring");
            MonitoringFragment monitoringFragment = new MonitoringFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, monitoringFragment, "Monitoring");
            fragmentTransaction.commit();

        } else if (id == R.id.menuAssignment) {
            setActionBarTitle("Assignment");
            AssignmentFragment assignmentFragment = new AssignmentFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, assignmentFragment,"Assignment");
            fragmentTransaction.commit();
        }
        else if(id == R.id.menuUser){
            setActionBarTitle("User");
            UserFragment userFragment =  new UserFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, userFragment, "User");
            fragmentTransaction.commit();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //deteksi klik burger icon
        if (id == android.R.id.home) {
            //slide navigation drawer
            drawerLayout.openDrawer(Gravity.LEFT);
        } else if(id == R.id.homeOptionLogout){
            SharedPreferences sharedPreferences = getSharedPreferences(Constanta.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(context,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if(id == R.id.homeOptionHome){
            setActionBarTitle("XBC MOBILE APPS");
            HomeFragment homeFragment= new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_all_menu, homeFragment,"XBC MOBILE APPS");
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //jika yes
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //jika no
                        dialog.cancel();
                    }
                })
                .setCancelable(false);

        AlertDialog showAlert = alert.create();
        showAlert.show();
    }
}