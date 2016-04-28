package com.example.aswin.grphy;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener{

    EditText url;
    Button begin;
    ListView home;
    String website;

    Integer[] imageLeft = {
            R.drawable.url,
            R.drawable.url1,
            R.drawable.url2,
            R.drawable.url3,
            R.drawable.url4,
            R.drawable.url5,
            R.drawable.url6
    };

    Integer[] imageRight = {
            R.drawable.arrow_dark
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialize();

        PopulatingListView();
    }

    private void Initialize() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        url = (EditText)findViewById(R.id.et_home_url);
        begin = (Button)findViewById(R.id.b_home_begin);
        home = (ListView)findViewById(R.id.lv_home);
        begin.setOnClickListener(this);

        DbHelper worker = new DbHelper(this);
        SQLiteDatabase db = worker.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT count(*) FROM " + DbHelper.TABLE_LIST_ACTIVITY,null);
        c.moveToFirst();
        int flag = c.getInt(0);
        if (flag < 1){
            AddEntryListActivity("Small Dataset", "http://androiddream.esy.es/Default_Small.json");
            AddEntryListActivity("Medium Dataset", "http://androiddream.esy.es/Default_Medium.json");
            AddEntryListActivity("Large Dataset", "http://androiddream.esy.es/Default_Large.json");
        }

        ClearGraphTable();
    }

    private void ClearGraphTable() {

        DbHelper worker = new DbHelper(this);
        SQLiteDatabase db = worker.getWritableDatabase();
        db.execSQL("delete from "+ DbHelper.TABLE_GRAPH_ACTIVITY);
    }


    private void PopulatingListView() {

        final ArrayList<String> url_tag = new ArrayList<>();
        final ArrayList<String> url_value = new ArrayList<>();

        DbHelper worker = new DbHelper(this);
        SQLiteDatabase db = worker.getWritableDatabase();
        final Cursor c = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_LIST_ACTIVITY, null);
        if (c.moveToLast()) {
            do {
                url_tag.add(c.getString(1));
                url_value.add(c.getString(2));
            } while (c.moveToPrevious());
        }
        c.close();

        CustomListView adapter = new CustomListView(this,url_tag,url_value,imageLeft,imageRight);
        home.setAdapter(adapter);

        home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                website = url_value.get(position);
                BeginExecutionAfterNetworkCheck(website);
            }
        });

        home.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setIcon(R.drawable.action_delete_black);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String delete_url = url_tag.get(pos);

                        DbHelper worker = new DbHelper(Home.this);
                        SQLiteDatabase db = worker.getWritableDatabase();
                        db.execSQL("DELETE FROM " + DbHelper.TABLE_LIST_ACTIVITY + " WHERE "
                                + DbHelper.URL_TAG_NAME + " = " + "'" + delete_url + "'");

                        PopulatingListView();
                        Toast.makeText(Home.this, "Deletion Completed!", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), "Saved By The Bell!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                return true;
            }
        });
    }

    private String CheckNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return "true";
        } else {
            return "false";
        }
    }

    private void BeginExecutionAfterNetworkCheck(String website) {
        if (CheckNetwork().equals("true")) {
            ClearGraphTable();
            new BackgroundTask().execute(website);
        } else {
            Toast.makeText(Home.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_home_begin:

                website = url.getText().toString();
                BeginExecutionAfterNetworkCheck(website);
                break;
        }
    }



    private class BackgroundTask extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... params) {
        try {
            return DownloadData(params[0]);
        } catch (IOException e) {
            return "Unable to retrieve data";
            }
        }

        protected void onPostExecute(String s) {

            ExecuteJsonParsing(s);
            Intent i1 = new Intent(Home.this,TabActivity.class);
            startActivity(i1);
        }
    }

    private String DownloadData(String param) throws IOException {

        InputStream is = null;
        URL url = new URL(param);
        HttpURLConnection client = (HttpURLConnection) url.openConnection();

        try {
            client.setRequestMethod("GET");
            client.connect();
            is = client.getInputStream();
            String buffered = StreamToString(is);
            return buffered;
        } finally {
            is.close();
            client.disconnect();
        }
    }

    private String StreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void ExecuteJsonParsing(String s) {

        try {
            JSONObject response;
            response = new JSONObject(s);
            JSONArray array = response.getJSONArray("values");
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject temp = array.getJSONObject(i);
                String xaxis = temp.getString("xaxis");
                String yaxis = temp.getString("yaxis");

                AddEntryGraphActivity(xaxis, yaxis);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private long AddEntryGraphActivity(String xaxis, String yaxis) {

        DbHelper worker = new DbHelper(this);
        SQLiteDatabase db = worker.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.XAXIS, xaxis);
        values.put(DbHelper.YAXIS, yaxis);
        long count = db.insert(DbHelper.TABLE_GRAPH_ACTIVITY, null, values);
        db.close();

        return count;
    }

    private long AddEntryListActivity(String tag, String value) {

        DbHelper worker = new DbHelper(this);
        SQLiteDatabase db = worker.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.URL_TAG_NAME, tag);
        values.put(DbHelper.URL_VALUE, value);
        long count = db.insert(DbHelper.TABLE_LIST_ACTIVITY, null, values);
        db.close();

        return count;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String title,message;
        int icon;

        switch (item.getItemId()) {

            case R.id.action_add:

                Intent intent = new Intent(Home.this,CreateNew.class);
                startActivity(intent);
                return true;

            case R.id.action_delete:

                title = "Confirm Execution";
                message = "Long Press On ListView Item To Delete The Url.";
                icon = R.drawable.action_delete_black;
                ShowDialogueBox(title, message, icon);
                return true;

            case R.id.action_refresh_home:

                PopulatingListView();
                return true;

            case R.id.action_about_us_home:

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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
