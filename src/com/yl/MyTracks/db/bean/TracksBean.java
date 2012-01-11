package com.yl.MyTracks.db.bean;

public class TracksBean {
	private int id;
	private int row_id;
	private String name;//名称
	private String desc;
	private String distance;//距离
	private String tracked_time;
	private String locate_count;
	private String created_at;//创建时间
	private String updated_at;//更新时间
	private String avg_speed;//平均速度
	private String max_speed;//最大速度
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRow_id() {
		return row_id;
	}
	public void setRow_id(int row_id) {
		this.row_id = row_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTracked_time() {
		return tracked_time;
	}
	public void setTracked_time(String tracked_time) {
		this.tracked_time = tracked_time;
	}
	public String getLocate_count() {
		return locate_count;
	}
	public void setLocate_count(String locate_count) {
		this.locate_count = locate_count;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getAvg_speed() {
		return avg_speed;
	}
	public void setAvg_speed(String avg_speed) {
		this.avg_speed = avg_speed;
	}
	public String getMax_speed() {
		return max_speed;
	}
	public void setMax_speed(String max_speed) {
		this.max_speed = max_speed;
	}
	
}
