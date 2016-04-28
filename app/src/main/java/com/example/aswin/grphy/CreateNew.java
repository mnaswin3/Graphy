package com.example.aswin.grphy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateNew extends AppCompatActivity implements View.OnClickListener{

    EditText url_tag,url_value;
    Button create;
    String tag,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);

        Initialize();
    }

    private void Initialize() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.up_arrow_black);
        }

        url_tag = (EditText)findViewById(R.id.et_url_tag);
        url_value = (EditText)findViewById(R.id.et_url_value);
        create = (Button)findViewById(R.id.b_create);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_create:
                tag = url_tag.getText().toString();
                value = url_value.getText().toString();
                AddEntryListActivity(tag,value);
                break;
        }
    }

    private long AddEntryListActivity(String tag, String value) {

        DbHelper worker = new DbHelper(this);
        SQLiteDatabase db = worker.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.URL_TAG_NAME, tag);
        values.put(DbHelper.URL_VALUE, value);
        long count = db.insert(DbHelper.TABLE_LIST_ACTIVITY, null, values);

        Intent intent = new Intent(CreateNew.this,Home.class);
        startActivity(intent);
        return count;
    }

    public void onBackPressed() {
        Intent intent = new Intent(CreateNew.this,Home.class);
        startActivity(intent);
    }
}
