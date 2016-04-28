package com.example.aswin.grphy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TabActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Initialize();
    }

    private void Initialize() {

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new FragmentOne(),"Line");
        viewPagerAdapter.addFragments(new FragmentTwo(),"Bar");
        viewPagerAdapter.addFragments(new FragmentThree(), "Pie");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.line_chart_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.bar_chart_black);
        tabLayout.getTabAt(2).setIcon(R.drawable.pie_chart_black);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_tabactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String title,message;
        int icon;

        switch (item.getItemId()) {

            case R.id.action_about_us_tabactivity:

                title = "About Us?";
                message = "Some Other Time Maybe.";
                icon = R.drawable.action_about_us_black;
                ShowDialogueBox(title,message,icon);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowDialogueBox(String title, String message, int icon) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TabActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(icon);

        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public void onBackPressed() {
        Intent intent = new Intent(TabActivity.this,Home.class);
        startActivity(intent);
    }
}
