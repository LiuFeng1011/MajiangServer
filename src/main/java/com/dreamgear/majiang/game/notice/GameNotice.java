package com.dreamgear.majiang.game.notice;

import java.util.ArrayList;
import java.util.List;
/**
 * 公告
 * 
 * @author liufeng
 *
 */
public class GameNotice{
	long id;
	int noticeType = 0;	/*Y	公告类型：1-滚屏公告
							当取值为0时，表示删除当前唯一编号的所有记录。*/
	String noticeTitle  = "";	/*Y	公告标题*/
	String noticeContent  = "";	/*Y	公告内容*/
	long startTime = 0;	/*Y	公告开始时间*/
	long endTime =0;	/*Y	公告结束时间。当取值为null时表示永不过期*/
	int timeDelay =0;	/*Y	显示间隔时间。单位：秒*/
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}
	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getTimeDelay() {
		return timeDelay;
	}
	public void setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
	}

	
	
}
