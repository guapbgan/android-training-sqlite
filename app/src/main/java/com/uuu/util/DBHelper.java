package com.uuu.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME="db.sqlite";
	private Context m_context;
	// 新增表格時的DDL
	private static final String INCOME_CREATE_DDL =
			"CREATE TABLE INCOME_MAIN ("
					+ "_ID INTEGER PRIMARY KEY,"
					+ "INCOME_DESCRIPTION TEXT);";
	// 刪除表格時的DDL
	private static final String INCOME_DELETE_DDL =
			"DROP TABLE IF EXISTS INCOME_MAIN;";
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		m_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		Toast.makeText(m_context, "DB Create", Toast.LENGTH_LONG).show();
		arg0.execSQL(INCOME_CREATE_DDL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		Toast.makeText(m_context, "DB upgrade", Toast.LENGTH_LONG).show();
		arg0.execSQL(INCOME_DELETE_DDL);
		arg0.execSQL(INCOME_CREATE_DDL);
	}

}
