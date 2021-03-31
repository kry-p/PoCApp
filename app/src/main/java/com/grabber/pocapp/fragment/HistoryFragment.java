package com.grabber.pocapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.grabber.pocapp.R;
import com.grabber.pocapp.adapter.CurrentMonthAdapter;
import com.grabber.pocapp.database.AppDatabase;
import com.grabber.pocapp.database.pojo.CategoryProp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {

    private AppDatabase db;
    private CurrentMonthAdapter categoryAdapter;
    private EditText edtYear, edtMonth;
    private Button btnSearch;
    private List<CategoryProp> categoryProps;
    private int year, month;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        // initialize db
        db = AppDatabase.getDatabase(getActivity());

        // initialize view
        RecyclerView currentMonthView = v.findViewById(R.id.current_month_view);
        edtYear = v.findViewById(R.id.txtYear);
        edtMonth = v.findViewById(R.id.txtMonth);
        btnSearch = v.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);

        // initialize layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // initialize category view
        currentMonthView.setHasFixedSize(true);
        currentMonthView.setLayoutManager(layoutManager);
        categoryAdapter = new CurrentMonthAdapter(db);
        currentMonthView.setAdapter(categoryAdapter);

        return v;
    }

    // DB 에서 값을 받아온 후 Adapter 에 업데이트
    public void getUpdate() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Runnable getProps = () -> categoryProps = db.propDao().getAllCategoryDuration(year, month);
        Future<?> future = executor.submit(getProps);

        try {
            future.get();
            categoryAdapter.setItem(categoryProps);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    // 정보를 가져올 연월 값을 읽은 후 getUpdate() 호출
    public void getData() {
        try {
            year = Integer.parseInt(edtYear.getText().toString());
            month = Integer.parseInt(edtMonth.getText().toString());

            getUpdate();
        } catch (Exception e) {
            Log.e("Parser", "Date parsing error");
        }
    }

    // 요소 클릭 시 Listener
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            getData();
        }
    }
}
