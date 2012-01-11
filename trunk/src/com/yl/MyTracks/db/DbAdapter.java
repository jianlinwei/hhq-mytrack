package com.yl.MyTracks.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 数据库操作公共类
 * @author hhq163
 *
 */
public class DbAdapter {
	private static final String TAG = "DbAdapter";
	private static final String DATABASE_NAME = "MyTracks.db";
	private static final int DATABASE_VERSION = 1;
	
	public class DatabaseHelper extends SQLiteOpenHelper{
		public DatabaseHelper(Context context){
			super (context,DATABASE_NAME,null,DATABASE_VERSION);
		}
		/**
		 * 完成表创建工作
		 */
		public void onCreate(SQLiteDatabase db){
			//创建轨迹表
			String tracks_sql = "CREATE TABLE " +TrackDbAdapter.TABLE_NAME + " ("
					+ TrackDbAdapter.ID	+ " INTEGER primary key autoincrement, " 
					+ TrackDbAdapter.NAME	+ " text not null, " 
					+ TrackDbAdapter.DESC + " text ," 
					+ TrackDbAdapter.DIST + " LONG ," 
					+ TrackDbAdapter.TRACKEDTIME + " LONG ," 
					+ TrackDbAdapter.LOCATE_COUNT + " INTEGER, " 
					+ TrackDbAdapter.CREATED + " text, " 
					+ TrackDbAdapter.AVGSPEED + " LONG, " 
					+ TrackDbAdapter.MAXSPEED + " LONG ," 
					+ TrackDbAdapter.UPDATED + " text " 
					+ ");";
			Log.d(TAG, tracks_sql);
			db.execSQL(tracks_sql);
			
			//创建位置信息表
			String locates_sql = "CREATE TABLE " + LocateDbAdapter.TABLE_NAME + " (" 
					+ LocateDbAdapter.ID	+ " INTEGER primary key autoincrement, " 
					+ LocateDbAdapter.TRACKID	+ " INTEGER not null, " 
					+ LocateDbAdapter.LON + " DOUBLE ," 
					+ LocateDbAdapter.LAT + " DOUBLE ," 
					+ LocateDbAdapter.ALT + " DOUBLE ," 
					+ LocateDbAdapter.CREATED + " text " 
					+ ");";	
			Log.d(TAG, locates_sql);
			db.execSQL(locates_sql);
		}
		/**
		 * 完成数据库升级
		 */
		public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
			db.execSQL("DROP TABLE IF EXISTS" + LocateDbAdapter.TABLE_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS" + TrackDbAdapter.TABLE_NAME + ";");
			onCreate(db);
		}
	}
}
