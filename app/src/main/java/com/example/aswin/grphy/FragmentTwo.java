package com.example.aswin.grphy;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class FragmentTwo extends Fragment {

    BarChart chart_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_two, container,
                false);

        chart_bar = (BarChart)rootView.findViewById(R.id.chart_bar);
        chart_bar.setDescription("Bar Chart");
        chart_bar.setNoDataTextDescription("No Data Available");
        chart_bar.setBackgroundColor(getResources().getColor(R.color.greenish_white));
        chart_bar.setDrawGridBackground(false);

        PopulatingBarChart();

        return rootView;
    }

    private void PopulatingBarChart() {

        final ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();

        DbHelper worker = new DbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = worker.getWritableDatabase();
        final Cursor c = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_GRAPH_ACTIVITY, null);
        if (c.moveToLast()) {
            int i = 0;
            do {
                entries.add(new BarEntry(Float.parseFloat(c.getString(2)),i));
                labels.add(c.getString(1));
                i++;
            } while (c.moveToPrevious());
        }
        c.close();

        BarDataSet dataset;
        dataset = new BarDataSet(entries,"Values");
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        BarData data = new BarData(labels,dataset);
        chart_bar.setData(data);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser) {
                chart_bar.animateY(2000);
            }
        }
    }

}
