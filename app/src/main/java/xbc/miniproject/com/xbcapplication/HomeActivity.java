package xbc.miniproject.com.xbcapplication;

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
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import xbc.miniproject.com.xbcapplication.adapter.HomeMenuAdapter;
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
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListView expListView;
    private HomeMenuAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setPopupTheme(R.style.PopupMenu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        headerNamaUser = findViewById(R.id.headerNamaUser);
        headerNamaUser.setText(SessionManager.getUsername(context));

        setTitle("XBC MOBILE APPS");
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_all_menu, homeFragment, "XBC MOBILE APPS");
        fragmentTransaction.commit();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView = (ExpandableListView) findViewById(R.id.leftDrawer);
        prepareListData(listDataHeader, listDataChild);
        listAdapter = new HomeMenuAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (groupPosition < 4) {
                    if (groupPosition == 0) {
                        setActionBarTitle("XBC MOBILE APPS");
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, homeFragment, "XBC MOBILE APPS");
                        fragmentTransaction.commit();
                    } else if (groupPosition == 1) {
                        setActionBarTitle("Biodata");
                        BiodataFragment biodataFragment = new BiodataFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, biodataFragment, "Biodata");
                        fragmentTransaction.commit();
                    } else if (groupPosition == 2) {
                        setActionBarTitle("Trainer");
                        TrainerFragment trainerFragment = new TrainerFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, trainerFragment, "Trainer");
                        fragmentTransaction.commit();
                    } else if (groupPosition == 3) {
                        setActionBarTitle("Technology");
                        TechnologyFragment technologyFragment = new TechnologyFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, technologyFragment, "Technology");
                        fragmentTransaction.commit();
                    }
                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groupPosition == 4) {
                    if (childPosition == 0) {
                        setActionBarTitle("Batch");
                        BatchFragment batchFragment = new BatchFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, batchFragment, "Batch");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        setActionBarTitle("Class");
                        ClassFragment classFragment = new ClassFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, classFragment, "Class");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

                if (groupPosition == 5) {
                    if (childPosition == 0) {
                        Toast.makeText(context, "Menu Filtering Saat Ini Belum Tersedia", Toast.LENGTH_SHORT).show();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        Toast.makeText(context, "Menu MiniProject Saat Ini Belum Tersedia", Toast.LENGTH_SHORT).show();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 2) {
                        Toast.makeText(context, "Menu Custom Saat Ini Belum Tersedia", Toast.LENGTH_SHORT).show();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

                if (groupPosition == 6) {
                    if (childPosition == 0) {
                        setActionBarTitle("Feedback");
                        FeedbackFragment feedbackFragment = new FeedbackFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, feedbackFragment, "Feedback");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        setActionBarTitle("Idle News");
                        IdleNewsFragment idleNewsFragment = new IdleNewsFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, idleNewsFragment, "Idle News");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 2) {
                        setActionBarTitle("Testimony");
                        TestimonyFragment testimonyFragment = new TestimonyFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, testimonyFragment, "Testimony");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

                if (groupPosition == 7) {
                    if (childPosition == 0) {
                        setActionBarTitle("Monitoring");
                        MonitoringFragment monitoringFragment = new MonitoringFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, monitoringFragment, "Monitoring");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        setActionBarTitle("Assignment");
                        AssignmentFragment assignmentFragment = new AssignmentFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, assignmentFragment, "Assignment");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

                if (groupPosition == 8) {
                    if (childPosition == 0) {
                        setActionBarTitle("User");
                        UserFragment userFragment = new UserFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_all_menu, userFragment, "User");
                        fragmentTransaction.commit();
                        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

                return false;
            }
        });

    }

    private void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //deteksi klik burger icon
        if (id == android.R.id.home) {
            //slide navigation drawer
            drawerLayout.openDrawer(Gravity.START);
        } else if (id == R.id.homeOptionLogout) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constanta.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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

    private void prepareListData(List<String> listDataHeader, Map<String,
            List<String>> listDataChild) {

//         Adding child data
        listDataHeader.add("Home");
        listDataHeader.add("Biodata");
        listDataHeader.add("Trainer");
        listDataHeader.add("Technology");
        listDataHeader.add("Bootcamp");
        listDataHeader.add("Assessment");
        listDataHeader.add("Portal");
        listDataHeader.add("Idle");
        listDataHeader.add("Setting");

        List<String> home = new ArrayList<>();
        List<String> biodata = new ArrayList<>();
        List<String> trainer = new ArrayList<>();
        List<String> technology = new ArrayList<>();

        // Adding child data
        List<String> bootcamp = new ArrayList<>();
        bootcamp.add("Batch");
        bootcamp.add("Class");

        List<String> assessment = new ArrayList<>();
        assessment.add("Filtering");
        assessment.add("Mini Project");
        assessment.add("Custom");

        List<String> portal = new ArrayList<>();
        portal.add("Feedback");
        portal.add("Idle News");
        portal.add("\"Kata\" Idle");

        List<String> idle = new ArrayList<>();
        idle.add("Monitoring");
        idle.add("Assignment");

        List<String> setting = new ArrayList<>();
        setting.add("User");

        listDataChild.put(listDataHeader.get(0), home);
        listDataChild.put(listDataHeader.get(1), biodata);
        listDataChild.put(listDataHeader.get(2), trainer);
        listDataChild.put(listDataHeader.get(3), technology);
        listDataChild.put(listDataHeader.get(4), bootcamp);
        listDataChild.put(listDataHeader.get(5), assessment);
        listDataChild.put(listDataHeader.get(6), portal);
        listDataChild.put(listDataHeader.get(7), idle);
        listDataChild.put(listDataHeader.get(8), setting);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}