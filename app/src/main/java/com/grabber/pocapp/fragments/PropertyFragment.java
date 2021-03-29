package com.grabber.pocapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grabber.pocapp.AddPopup;
import com.grabber.pocapp.R;
import com.grabber.pocapp.database.AppDatabase;
import com.grabber.pocapp.database.CategoryProp;
import com.grabber.pocapp.database.Prop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropertyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropertyFragment extends Fragment implements View.OnClickListener{

    private AppDatabase db;

    private PieChart pieChart;
    private Button addPopup;
    private ArrayList<PieEntry> yValues;
    private PieData data;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public PropertyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PropoertyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PropertyFragment newInstance() {
        PropertyFragment fragment = new PropertyFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_property, container, false);

        // initialize db
        db = AppDatabase.getDatabase(getActivity());

        // add view
        pieChart = v.findViewById(R.id.pieChart);
        addPopup = v.findViewById(R.id.addPopup);

        // add listener
        addPopup.setOnClickListener(this);

        // set pie chart
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        draw();

        // for updating UI
        db.propDao().getAll().observe(getViewLifecycleOwner(), new Observer<List<Prop>>() {
            @Override
            public void onChanged(List<Prop> props) {
                draw();
            }
        });

        return v;
    }

    private void draw(){
        setData();
        PieDataSet dataSet = new PieDataSet(yValues, "종류");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        data = new PieData((dataSet));
        data.notifyDataChanged();
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);


        pieChart.notifyDataSetChanged();
        pieChart.setData(data);
    }

    private synchronized void setData(){
        new Thread(()->{
            yValues = new ArrayList<>();
            List<CategoryProp> temp=db.propDao().getAllCategory();

            for(CategoryProp item:temp){
                yValues.add(new PieEntry((float)(item.getAmount()), item.getCategory()));
            }
        }).start();
    }

    // Actions when clicking elements
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addPopup && getActivity()!=null) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AddPopup.class));
        }
    }
}