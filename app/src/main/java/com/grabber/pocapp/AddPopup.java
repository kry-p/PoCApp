package com.grabber.pocapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.grabber.pocapp.database.AppDatabase;
import com.grabber.pocapp.database.Prop;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPopup extends Activity {

    private AppDatabase db;
    private TextView tvDate, tvTime;
    private EditText edtCategory, edtAmount;
    private Button btnInsert,btnClose;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private Date date;
    private Calendar cal;
    private SimpleDateFormat time;
    private String result;
    private boolean isFromAdapter;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_add);

        // Local methods
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;

        // Watches change of amount value
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    edtAmount.setText(result);
                    edtAmount.setSelection(result.length());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        };

        // Local variables and instantiation of global variables
        result="";
        cal=Calendar.getInstance();
        date=new Date();
        time=new SimpleDateFormat("yyyy년 MM월 d일 E요일 HH:mm", Locale.KOREAN);
        db = AppDatabase.getDatabase(this);
        cal=Calendar.getInstance();

        // UI components
        tvDate = findViewById(R.id.TxtInputDate);
        tvTime = findViewById(R.id.TxtInputTime);
        edtCategory = findViewById(R.id.TxtInputType);
        edtAmount = findViewById(R.id.TxtInputAmount);
        btnInsert=findViewById(R.id.BtnInsert);
        btnClose=findViewById(R.id.BtnClose);

        // Date default value
        tvDate.setText(time.format(date));

        // Listeners
        this.InitializeListener();
        tvTime.setOnClickListener(this::DateOnClickHandler);
        tvDate.setOnClickListener(this::DateOnClickHandler);
        btnInsert.setOnClickListener(buttonInsertListener);
        btnClose.setOnClickListener(buttonCloseListener);
        edtAmount.addTextChangedListener(watcher);
    }

    // 추가 버튼을 누르면 Thread를 이용하여 DB에 데이터를 삽입 후 Activity를 닫음
    View.OnClickListener buttonInsertListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String amountStr=edtAmount.getText().toString().replaceAll("[^0-9]","");
            java.sql.Date sqlDate=new java.sql.Date(date.getTime());

            if(edtCategory.getText().toString().isEmpty()
                    || tvDate.getText().toString().isEmpty() || edtAmount.getText().toString().isEmpty()){
                Toast.makeText(AddPopup.this, "비거나 잘못된 값이 있습니다. 확인 후 다시 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                long amount;
                amount=Long.parseLong(amountStr);
                new Thread(() -> {
                    Prop prop = new Prop(
                            edtCategory.getText().toString(),
                            sqlDate,
                            amount);
                    db.propDao().insert(prop);
                }).start();
                finish();
            }
        }
    };

    View.OnClickListener buttonCloseListener= v -> finish();

    public void InitializeListener() {
        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            TimePickerDialog timeDialog = new TimePickerDialog(AddPopup.this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
            timeDialog.show();
        };
        timeSetListener = (view, hourOfDay, minute) -> {
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            date=new Date(cal.getTimeInMillis());
            tvDate.setText(time.format(date));
            //tvTime.setText(hourOfDay+" : "+minute);
        };
    }

    public void DateOnClickHandler(View view) {
        DatePickerDialog dateDialog = new DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    public void TimeOnClickHandler(View view) {
        Calendar cal=Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        dialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    public void onClose(View v){
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        finish();
    }
}