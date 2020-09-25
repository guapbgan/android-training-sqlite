package com.uuu;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uuu.util.DBHelper;

public class Add extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_add);
        // 取得按鈕的實例
        Button submitButton = findViewById(R.id.income_submit);
        // 設定按鈕按下的回應函數
        submitButton.setOnClickListener(view -> submitCallback());
    }

    private void submitCallback() {
        // 取得文字編輯視窗的實例
        EditText desc = findViewById(R.id.income_desc);
        // 取得該資料庫的實例
        DBHelper helper = new DBHelper(Add.this);

        // 取得可讀寫的資料庫
        try(SQLiteDatabase db = helper.getWritableDatabase()){
            ContentValues args = new ContentValues();
            // 將對應的欄位值填入
            args.put("INCOME_DESCRIPTION", desc.getText().toString());
            // 新增成功會將row id傳回
            long rowid = db.insert("INCOME_MAIN", null, args);
            // 使用一個Toast訊息確認資料有被新增
            Toast.makeText(Add.this, "One record inserted, id=" + rowid,
                    Toast.LENGTH_LONG).show();
        }

        // 結束目前的Activity，回到原本的Activity
        finish();
    }
}
