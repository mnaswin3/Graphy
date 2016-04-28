package com.example.aswin.grphy;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class FragmentThree extends Fragment {

    PieChart chart_pie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_three, container,
                false);

        chart_pie = (PieChart)rootView.findViewById(R.id.chart_pie);
        chart_pie.setDescription("Pie Chart");
        chart_pie.setNoDataTextDescription("No Data Available");
        chart_pie.setBackgroundColor(getResources().getColor(R.color.lightgrey));
        chart_pie.setHoleColorTransparent(true);

        PopulatingPieChart();

        return rootView;
    }

    private void PopulatingPieChart() {



        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

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

        PieDataSet dataset = new PieDataSet(entries,"Values");
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData data = new PieData(labels,dataset);
        chart_pie.setData(data);
        chart_pie.animateXY(2000,2000);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){
                chart_pie.animateXY(2000,2000);
            }else{
                chart_pie.animateY(2000);
            }
        }
    }

}
