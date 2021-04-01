package com.grabber.pocapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grabber.pocapp.R;
import com.grabber.pocapp.database.AppDatabase;
import com.grabber.pocapp.database.pojo.CategoryProp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// 금월의 자산 정보를 표시하기 위한 RecyclerView Adapter
public class CurrentMonthAdapter extends RecyclerView.Adapter<CurrentMonthAdapter.ViewHolder> {

    private List<CategoryProp> items = new ArrayList<>();
    private List<CategoryProp> temp;
    private Context mContext;
    private AppDatabase db;

    public CurrentMonthAdapter(AppDatabase db) {
        this.db = db;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<CategoryProp> getItems() {
        return items;
    }

    public void setItem(List<CategoryProp> data) {
        items = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrentMonthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        mContext = parent.getContext();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentMonthAdapter.ViewHolder holder, int position) {
        holder.onBind(items.get(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvAmount;
        private Button btnDelete;
        private int index;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.rv_title);
            tvAmount = itemView.findViewById(R.id.rv_amount);
            btnDelete = itemView.findViewById(R.id.rv_delete);

            // Delete button listener
            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();

                if(position!=RecyclerView.NO_POSITION){
                    CategoryProp item = items.get(position);
                    String currentCategory = item.getCategory();
                    int year = item.getYear();
                    int month = item.getMonth();

                    ExecutorService executor = Executors.newSingleThreadExecutor();

                    Runnable getProps = () -> {
                        db.propDao().delete(currentCategory, year, month);
                        temp = db.propDao().getAllCategoryDuration(year, month);
                    };
                    Future<?> future = executor.submit(getProps);

                    try {
                        future.get();
                        setItem(temp);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                    }

                }
            });
        }

        public void onBind(CategoryProp categoryProp, int position) {
            index = position;
            tvTitle.setText(categoryProp.getCategory());
            // 금액은 표시 시 포맷팅부터 처리
            tvAmount.setText(String.format(Locale.KOREAN,"%,d원",(int)categoryProp.getAmount()));
        }
    }

}
