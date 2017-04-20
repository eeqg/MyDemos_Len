package com.example.mydemos_len.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class TestBean1 {
	@SerializedName("data")
	public DataInfo dataInfo;
	
	public ArrayList<ItemInfo> list;
	
	public String page;
	
	public String size;
	
	public String pageCount;
	
	@SerializedName("total")
	public String totalRecord;
	
	public class DataInfo {
		public String id;
		public String name;
		public String keywords;
		public String cover;
		
		@Override
		public String toString() {
			return String.format("DataInfo: id = %s" +
					"\n name = %s \n keyword = %s", id, name, keywords);
		}
	}
	
	public class ItemInfo {
		public String id;
		public String title;
		public String descript;
		public String cover;
		@SerializedName("adddate")
		public String date;
		
		@Override
		public String toString() {
			return String.format("DataInfo: id = %s" +
					"\n title = %s \n description = %s \n date = %s", id, title, descript, date);
		}
	}
}
