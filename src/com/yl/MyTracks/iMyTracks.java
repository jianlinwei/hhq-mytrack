package com.yl.MyTracks;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;

import com.yl.MyTracks.db.TrackDbAdapter;
import com.yl.MyTracks.adapter.TrackListAdapter;
import com.yl.MyTracks.db.LocateDbAdapter;
import com.yl.MyTracks.db.bean.TracksBean;

public class iMyTracks extends Activity {
	public final String TAG = "iMyTracks";
	
	private TrackDbAdapter mDbHelper;
	private Cursor mTrackCursor;
	private List<TracksBean> trackInfoList = null;
	private ListView trackListView;
	private TrackListAdapter trackListAdapter;
	
	//定义菜单需要的常量
	private static final int MENU_NEW = Menu.FIRST+1;
	private static final int MENU_CON = MENU_NEW+1;
	private static final int MENU_SETTING = MENU_CON+1;
	private static final int MENU_HELPS = MENU_SETTING+1;
	private static final int MENU_EXIT = MENU_HELPS+1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(R.string.app_title);
        
        trackListView = (ListView)findViewById(R.id.track_list);
        trackListView.setOnItemClickListener(trackListViewClickListener);
        
        mDbHelper = new TrackDbAdapter(this);
        mDbHelper.open();
        renderListView();
    }
    /**
     * 关闭操作
     */
    protected void onStop(){
		super.onStop();
		Log.d(TAG, "onStop");
//		mDbHelper.close();
	}
  	/**
  	 * 取DB中记录，显示在列表中
  	 */
  	private void renderListView() {
//		Log.d(TAG, "renderListView.");
		trackInfoList = mDbHelper.getAllTracks();
//		Log.d(TAG, trackInfoList.toString());
//		
		TrackListAdapter trackListAdapter = new TrackListAdapter(iMyTracks.this,trackInfoList);

		trackListView.setAdapter(trackListAdapter);
//		Log.d(TAG, "ccc");
	}
  	
  	/**
  	 * 回调函数
  	 */
  	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		Log.d(TAG, "onActivityResult.");	
		super.onActivityResult(requestCode, resultCode, intent);
		renderListView();
	}
  	// 初始化菜单,按Menu键系统会调用onCreateOptionsMenu
 	@Override
 	public boolean onCreateOptionsMenu(Menu menu) {
 		Log.d(TAG, "onCreateOptionsMenu");

 		super.onCreateOptionsMenu(menu);

 		menu.add(0, MENU_NEW, 0, R.string.menu_new).setIcon(
 				R.drawable.new_track).setAlphabeticShortcut('N');
 		menu.add(0, MENU_CON, 0, R.string.menu_con).setIcon(
 				R.drawable.con_track).setAlphabeticShortcut('C');
 		menu.add(0, MENU_SETTING, 0, R.string.menu_setting).setIcon(
 				R.drawable.setting).setAlphabeticShortcut('S');
 		menu.add(0, MENU_HELPS, 0, R.string.menu_helps).setIcon(
 				R.drawable.helps).setAlphabeticShortcut('H');
 		menu.add(0, MENU_EXIT, 0, R.string.menu_exit).setIcon(
 				R.drawable.exit).setAlphabeticShortcut('E');
 		return true;
 	}
 	// 当一个菜单被选中的时的操作
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
 		Intent intent = new Intent();
 		switch (item.getItemId()) {
 		case MENU_NEW:
 			intent.setClass(iMyTracks.this, NewTrack.class);
 			startActivity(intent);
 			return true;
 		case MENU_CON:
 			//TODO: 继续跟踪选择的记录
 			conTrackService();
 			return true;
 		case MENU_SETTING:
 			intent.setClass(iMyTracks.this, Setting.class);
 			startActivity(intent);
 			return true;
 		case MENU_HELPS:
 			intent.setClass(iMyTracks.this, Helps.class);
 			startActivity(intent);
 			return true;
 		case MENU_EXIT:
 		    finish();
 		   System.exit(0);
 			break;
 		}
 		return true;
 	}
 	/**
 	 * 列表项点击事件
 	 * @author hhq
 	 */
 	private OnItemClickListener trackListViewClickListener = new OnItemClickListener(){
 		public void onItemClick(AdapterView<?> parent, View view, int position,long id){
 			Intent intent = new Intent(iMyTracks.this, ShowTrack.class);
 			TracksBean tracksBean = (TracksBean)view.getTag();
 			
 			intent.putExtra(TrackDbAdapter.KEY_ROWID,tracksBean.getId());
 			intent.putExtra(TrackDbAdapter.NAME, tracksBean.getName());
 			intent.putExtra(TrackDbAdapter.DESC, tracksBean.getDesc());
 			startActivity(intent);
 		}
 	};
 	
 	/**
 	 * 继续跟踪选择的记录
 	 */
 	private void conTrackService() {
		Intent i = new Intent("com.yl.MyTracks.iMyTracks.START_TRACK_SERVICE");
//		Long track_id = getListView().getSelectedItemId();
//		i.putExtra(LocateDbAdapter.TRACKID, track_id.intValue());
//        startService(i);		
	}
 	/**
 	 * 退出程序
 	 */
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			new AlertDialog.Builder(this).setTitle("提示").setCancelable(false)// 设置不能通过“后退”按钮关闭对话框
					.setMessage("确定退出客户端?").setPositiveButton("确认",new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									finish();
									System.exit(0);
								}
							}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();// 显示对话框
			return true;
		}
		return false;
	}
 	
}