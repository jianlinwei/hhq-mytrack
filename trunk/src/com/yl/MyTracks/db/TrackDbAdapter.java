package com.yl.MyTracks.db;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.Cursor;
import android.util.Log;

import com.yl.MyTracks.db.bean.TracksBean;
/**
 * 轨迹表模型，封装对轨迹表的操作
 * @author hhq163
 *
 */
public class TrackDbAdapter extends DbAdapter{
	private static final String TAG = "TrackDbAdapter";
	public static final String TABLE_NAME = "tracks";
	public static final String ID = "_id";
	public static final String KEY_ROWID = "_id";
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String DIST = "distance";
	public static final String TRACKEDTIME = "tracked_time";
	public static final String LOCATE_COUNT = "locate_count";
	public static final String CREATED = "created_at";
	public static final String UPDATED = "updated_at";
	public static final String AVGSPEED = "avg_speed";
	public static final String MAXSPEED = "max_speed";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;
	
	public TrackDbAdapter(Context ctx){
		this.mCtx = ctx;
	}
	/**
	 * 返回一个TrackDbAdapter实例
	 * @return　Ｏbject
	 * @throws SQLException
	 */
	public TrackDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	/**
	 * 关闭数据库连接
	 */
	public void close(){
		mDbHelper.close();
	}
	/**
	 * 查询一条记录
	 * @param rowId
	 * @return Object
	 * @throws SQLException
	 */
	public TracksBean getTrack(long rowId) throws SQLException {
		TracksBean trackbean = new TracksBean();
		try {
			Cursor mCursor =mDb.query(true, TABLE_NAME, new String[] { KEY_ROWID, NAME,DESC, CREATED }, KEY_ROWID + "=" + rowId, null, null,null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				trackbean.setRow_id(mCursor.getInt(mCursor.getColumnIndex(KEY_ROWID)));
				trackbean.setName(mCursor.getString(mCursor.getColumnIndex(NAME)));
				trackbean.setDesc(mCursor.getString(mCursor.getColumnIndex(DESC)));
				trackbean.setCreated_at(mCursor.getString(mCursor.getColumnIndex(CREATED)));
			}
			if (mCursor != null) {
				mCursor.close();
			}
		} catch (Exception e){
			Log.d(TAG, e.getMessage());
		} finally{
			mDb.close();
		}
		
		return trackbean;
	}
	/**
	 * 插入一条记录
	 * @param name 轨迹名称
	 * @param desc　描述
	 * @return　long
	 */
	public long createTrack(String name, String desc) {
		Log.d(TAG, "createTrack.");
		Calendar calendar = Calendar.getInstance();//日历类
		String created = calendar.get(Calendar.YEAR) + "-" +calendar.get(Calendar.MONTH) + "-" +  calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"  + calendar.get(Calendar.SECOND);
		
		ContentValues initialValues = new ContentValues();//与Hashtable比较类似,负责存储一些名值对
		initialValues.put(NAME, name);
		initialValues.put(DESC, desc);
		initialValues.put(CREATED, created);
		initialValues.put(UPDATED, created);
		return mDb.insert(TABLE_NAME, null, initialValues);
	}
	/**
	 * 删除记录
	 * @param rowId
	 * @return true/false
	 */
	public boolean deleteTrack(long rowId) {
		return mDb.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
	}
	/**
	 * 获取所有的轨迹记录
	 * @return
	 */
	public List<TracksBean> getAllTracks() {
		List<TracksBean> trackInfoList= new ArrayList<TracksBean>();
		try {
			Cursor cursor = mDb.query(TABLE_NAME, new String[] { ID, NAME,DESC, CREATED }, null, null, null, null, "updated_at desc");
			while(cursor.moveToNext()){
				TracksBean trackbean = new TracksBean();
				trackbean.setId(cursor.getInt(cursor.getColumnIndex(ID)));
				trackbean.setName(cursor.getString(cursor.getColumnIndex(NAME)));
				trackbean.setDesc(cursor.getString(cursor.getColumnIndex(DESC)));
				trackbean.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED)));
				trackInfoList.add(trackbean);
			}
			if (cursor != null) {
				cursor.close();
			}
		} catch (Exception e){
			Log.d(TAG, e.getMessage());
		} finally{
			mDb.close();
		}
		
		return trackInfoList;
	}
	/**
	 * 更新一条轨迹记录
	 * @param rowId
	 * @param name　新名称
	 * @param desc　新描述
	 * @return true/false
	 */
	public boolean updateTrack(long rowId, String name, String desc) {
		Calendar calendar = Calendar.getInstance();
		String updated = calendar.get(Calendar.YEAR) + "-" +calendar.get(Calendar.MONTH) + "-" +  calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"  + calendar.get(Calendar.SECOND);
		
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		args.put(DESC, desc);
		args.put(UPDATED, updated);

		return mDb.update(TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
