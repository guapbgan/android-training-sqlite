package com.uuu;

import com.uuu.util.DBHelper;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class SqliteExerciseActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private String[] m_incomeDescArray;
    private Long[] m_idArray; // 儲存ListView中每一筆資料的ID
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取得這個ListActivity的ListView
        ListView lv = getListView();
        // 取得一個LayoutInflater的實例
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE); //getLayoutInflater()也可以

        // 從XML生成標題的View
        View headerView = inflater.inflate(R.layout.income_header, findViewById(R.id.income_header));
        // 從XML生成標尾的View
        View footerView = inflater.inflate(R.layout.income_footer, findViewById(R.id.income_footer));
        lv.addHeaderView(headerView);
        lv.addFooterView(footerView);
        reloadDb();

        // 從 header中取得Button的實例
        // 增加收入Button的回應函式
        Button incomeAddButton = headerView.findViewById(R.id.income_add_button);
        incomeAddButton.setOnClickListener(view -> {
            // Add是新增收入的Activity
            Intent intent = new Intent(SqliteExerciseActivity.this, Add.class);
            startActivity(intent);

        });

        m_delButton =  headerView.findViewById(R.id.del_button);
        getListView().setOnItemClickListener(this);
        // 刪除和修改都一併處理


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadDb();
    }

    private void reloadDb() {
        // 在每次頁面重匯時重新讀取資料庫
        readFromSQLite();
        // 並且更新資料來源
        setListAdapter(new ArrayAdapter<String>(this, R.layout.income_item,
                m_incomeDescArray));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, String.format("posi: %d, index: %d",i,l), Toast.LENGTH_LONG).show();
        this.processRecordAt(l);
        // 從資料庫中重新讀取內容
        readFromSQLite();
        reloadDb();

    }
    /**
     * 從 INCOME_MAIN表格中將資料讀出，存入m_incomeDescArray中作為Adapter的來源
     */
    private void readFromSQLite() {
        DBHelper helper = new DBHelper(this);
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            List<String> incomeStringList = new ArrayList<>();
            List<Long> idList = new ArrayList<>();
            try(Cursor cursor = db.query("INCOME_MAIN", new String[] { "_ID",
                    "INCOME_DESCRIPTION" }, null, null, null, null, null)){
                if (cursor.moveToFirst()) {
                    do {
                        long id = cursor.getLong(0);
                        String incomeString = cursor.getString(1);
                        incomeStringList.add(incomeString);
                        idList.add(id);
                    } while (cursor.moveToNext());
                }
            }
            // 設定內容的字串陣列
            m_incomeDescArray = incomeStringList.toArray(new String[] {});
            // 設定索引的長整數陣列
            m_idArray = idList.toArray(new Long[] {});
        }



    }
    private ToggleButton m_delButton;
    /**
     * 點擊INCOME_MAIN中第index筆資料
     * 如果是刪除模式則直接刪除
     * 如果是修改模式就設定參數進Bundle並且進入Modify.java並且
     * @param index
     */
    private void processRecordAt(long index) {
        if (index == -1) {
//            Toast.makeText(this,"index==-1?", Toast.LENGTH_LONG).show();
            return;
        }

        // 取得被點擊的位置
        long rawId = m_idArray[(int) index];
        String whereClause = String.format("_ID = %1$d", rawId);
        if (m_delButton.isChecked()) {
            // 如果是刪除模式
            // 就進行資料庫的刪除
            try(DBHelper helper = new DBHelper(this)){
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("INCOME_MAIN", whereClause, null);
            }
        } else {
            // 如果是修改模式
            // 就跳入修改的畫面
            String origString = m_incomeDescArray[(int) index];
            Bundle bundle = new Bundle();
            bundle.putString("whereClause", whereClause);
            bundle.putString("origString", origString);
            Intent intent = new Intent(this, Mod.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}