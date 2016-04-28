package com.example.aswin.grphy;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class FragmentOne extends Fragment {

    LineChart chart_line;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_one, container,
                false);

        chart_line = (LineChart)rootView.findViewById(R.id.chart_line);
        chart_line.setDescription("Line Chart");
        chart_line.setNoDataTextDescription("No Data Available");
        chart_line.setBackgroundColor(getResources().getColor(R.color.greenish_white));
        chart_line.setDrawGridBackground(false);

        PopulatingLineChart();

        return rootView;
    }

    private void PopulatingLineChart() {

        final ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();

        DbHelper worker = new DbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = worker.getWritableDatabase();
        final Cursor c = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_GRAPH_ACTIVITY, null);
        if (c.moveToLast()) {
            int i = 0;
            do {
                entries.add(new Entry(Float.parseFloat(c.getString(2)),i));
                labels.add(c.getString(1));
                i++;
            } while (c.moveToPrevious());
        }
        c.close();

        LineDataSet dataset = new LineDataSet(entries,"Values");
        LineData data = new LineData(labels,dataset);

        chart_line.setData(data);
        chart_line.animateX(2000);

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){
                chart_line.animateX(2000);
            }else {
                chart_line.animateY(2000);
            }
        }
    }

}
