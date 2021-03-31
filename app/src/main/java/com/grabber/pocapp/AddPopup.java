package com.grabber.pocapp;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.grabber.pocapp.database.AppDatabase;
import com.grabber.pocapp.database.Prop;

import java.text.DecimalFormat;

// 팝업 요소를 표시하기 위한 액티비티
public class AddPopup extends Activity {

    private AppDatabase db;
    private TextView tvYear, tvMonth;
    private EditText edtCategory, edtAmount, edtYear, edtMonth;
    private Button btnInsert, btnClose;
    private String result;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_add);

        // Local methods
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        getWindow().getAttributes().width = (int) (dm.widthPixels * 0.9);

        // Watches change of amount value
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)) {
                    result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",", "")));
                    edtAmount.setText(result);
                    edtAmount.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        // Local variables and instantiation of global variables
        db = AppDatabase.getDatabase(this);

        // UI components
        edtYear = findViewById(R.id.TxtInputYear);
        edtMonth = findViewById(R.id.TxtInputMonth);
        edtCategory = findViewById(R.id.TxtInputType);
        edtAmount = findViewById(R.id.TxtInputAmount);
        btnInsert = findViewById(R.id.BtnInsert);
        btnClose = findViewById(R.id.BtnClose);

        // Listeners
        btnInsert.setOnClickListener(buttonInsertListener);
        btnClose.setOnClickListener(buttonCloseListener);
        edtAmount.addTextChangedListener(watcher);
    }

    // 추가 버튼을 누르면 Thread 를 이용하여 DB에 데이터를 삽입 후 Activity 를 닫음
    View.OnClickListener buttonInsertListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String amountStr = edtAmount.getText().toString().replaceAll("[^0-9]", "");

            // 잘못된 값이 있는지 체크하는 로직
            try{
                int month = Integer.parseInt(edtMonth.getText().toString());

                if(month>12 || month<1){
                    Toast.makeText(AddPopup.this, "1월~12월 사이로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch(Exception e){
                Toast.makeText(AddPopup.this, "잘못된 값입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (edtCategory.getText().toString().isEmpty()
                    || edtYear.getText().toString().isEmpty() ||
                    edtMonth.getText().toString().isEmpty() || edtAmount.getText().toString().isEmpty()) {
                Toast.makeText(AddPopup.this, "비거나 잘못된 값이 있습니다. 확인 후 다시 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                long amount;
                amount = Long.parseLong(amountStr);
                new Thread(() -> {
                    Prop prop = new Prop(
                            edtCategory.getText().toString(),
                            Integer.parseInt(edtYear.getText().toString()),
                            Integer.parseInt(edtMonth.getText().toString()),
                            amount);
                    db.propDao().insert(prop);
                }).start();
                finish();
            }
        }
    };

    // 닫기 버튼을 누르면 종료
    View.OnClickListener buttonCloseListener = v -> finish();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    // 닫을 때의 동작
    public void onClose(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        finish();
    }
}