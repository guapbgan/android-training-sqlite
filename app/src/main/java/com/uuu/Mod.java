package com.uuu;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.uuu.util.DBHelper;

public class Mod extends Activity {
    private String m_whereClause; // 更新資料庫的SQL子句
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 從Bundle中得到初始值和Where子句
        setContentView(R.layout.income_add);
        Bundle bundle = getIntent().getExtras();
        String origString = bundle.getString("origString");
        m_whereClause = bundle.getString("whereClause");
        // 設定EditText的初始值
        EditText editText = findViewById(R.id.income_desc);
        editText.setText(origString);
        // 設定Button的標題與聆聽器
        Button submitButton = findViewById(R.id.income_submit);
        submitButton.setText(R.string.income_mod);
        submitButton.setOnClickListener(view -> modifyCallback());
    }

    private void modifyCallback() {
        EditText desc = findViewById(R.id.income_desc);
        // 進行資料庫現有欄位的更新
        DBHelper helper = new DBHelper(this);
        try(SQLiteDatabase db = helper.getWritableDatabase()){
            ContentValues args = new ContentValues();
            // 由使用者介面讀取
            args.put("INCOME_DESCRIPTION", desc.getText().toString());
            // 進行資料庫的更新
            db.update("INCOME_MAIN", args, m_whereClause, null);
        }
        finish();
    }
}
