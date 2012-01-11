package com.yl.MyTracks.db;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.Cursor;
import android.util.Log;
/**
 * 位置信息表模型，封装对轨迹表的操作
 * @author hhq163
 *
 */
public class LocateDbAdapter extends DbAdapter {
	private static final String TAG = "LocateDbAdapter";
	
	public static final String TABLE_NAME = "locates";
	public static final String ID = "_id";
	public static final String TRACKID = "track_id";
	public static final String LON = "longitude";
	public static final String LAT = "latitude";	
	public static final String ALT = "altitude";	
	public static final String CREATED = "created_at";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;

	public LocateDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	/**
	 * 返回一个LocateDbAdapter实例
	 * @return　Ｏbject
	 * @throws SQLException
	 */
	public LocateDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	/**
	 * 关闭数据库连接
	 */
	public void close() {
		mDbHelper.close();
	}
	/**
	 * 查询一条记录
	 * @param rowId
	 * @return Object
	 * @throws SQLException
	 */
	public Cursor getLocate(long rowId) throws SQLException {
		Cursor mCursor =
		mDb.query(true, TABLE_NAME, new String[] { ID, LON,
				LAT, ALT, CREATED }, ID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	/**
	 * 插入一条记录
	 * @param track_id 轨迹ID
	 * @param longitude 维度
	 * @param latitude 经度
	 * @param altitude 偏差
	 * @return
	 */
	public long createLocate(int track_id, Double longitude, Double latitude ,Double altitude) {
		Log.d(TAG, "createLocate.");
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "-" +calendar.get(Calendar.MONTH) + "-" +  calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"  + calendar.get(Calendar.SECOND);
		ContentValues initialValues = new ContentValues();
		initialValues.put(TRACKID, track_id);		
		initialValues.put(LON, longitude);
		initialValues.put(LAT, latitude);
		initialValues.put(ALT, altitude);
		initialValues.put(CREATED, created);
		return mDb.insert(TABLE_NAME, null, initialValues);
	}
	
	/**
	 * 删除记录
	 * @param rowId
	 * @return true/false
	 */
	public boolean deleteLocate(long rowId) {
		return mDb.delete(TABLE_NAME, ID + "=" + rowId, null) > 0;
	}
	/**
	 * 获取某一轨迹的所有的位置记录
	 * @param trackId 轨迹ID
	 * @return
	 */
	public Cursor getTrackAllLocates(int trackId) {
		return mDb.query(TABLE_NAME, new String[] { ID,TRACKID, LON,
				LAT, ALT,CREATED }, "track_id=?", new String[] {String.valueOf(trackId)}, null, null, "created_at asc");
	}
}
