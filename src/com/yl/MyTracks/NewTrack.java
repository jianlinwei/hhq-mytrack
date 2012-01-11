package com.yl.MyTracks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

import com.yl.MyTracks.db.TrackDbAdapter;

public class NewTrack extends Activity {
    private static final String TAG = "NewTrack";
	private Button button_new;
	private EditText field_new_name;
	private EditText field_new_desc;

	private TrackDbAdapter mDbHelper;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.new_track);
    	setTitle(R.string.menu_new);
		findViews();
		setListensers();
		
		mDbHelper = new TrackDbAdapter(this);
		mDbHelper.open();
    }
    /**
     * 关闭操作
     */
    protected void onStop(){
		super.onStop();
		mDbHelper.close();
	}
    
    private void findViews() {
		field_new_name = (EditText) findViewById(R.id.new_name);
		field_new_desc = (EditText) findViewById(R.id.new_desc);
		button_new = (Button) findViewById(R.id.new_submit);
	}
    
    private void setListensers() {
		button_new.setOnClickListener(new_track);
	}
    /**
     * 新建按键点击事件处理函数
     */
    private Button.OnClickListener new_track = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				String name = (field_new_name.getText().toString());
				String desc = (field_new_desc.getText().toString());
				if (name.equals("")) {
					Toast.makeText(NewTrack.this,
							getString(R.string.new_name_null),
							Toast.LENGTH_SHORT).show();
				} else {
					// 调用存储接口保存到数据库并启动service
					Long row_id = mDbHelper.createTrack(name, desc);
					Log.d(TAG, "row_id="+row_id);

					Intent intent = new Intent();
					intent.setClass(NewTrack.this, ShowTrack.class);
					intent.putExtra(TrackDbAdapter.KEY_ROWID, row_id);
					intent.putExtra(TrackDbAdapter.NAME, name);
					intent.putExtra(TrackDbAdapter.DESC, desc);
					
					startActivity(intent);
				}
			} catch (Exception err) {
				Toast.makeText(NewTrack.this, getString(R.string.new_fail),
						Toast.LENGTH_SHORT).show();
			}
		}
	};
}
