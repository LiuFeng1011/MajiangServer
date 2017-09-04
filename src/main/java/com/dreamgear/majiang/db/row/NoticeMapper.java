package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.notice.GameNotice;


public class NoticeMapper  implements RowMapper<GameNotice>{

	public GameNotice mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		GameNotice notice = new GameNotice();
		
		notice.setId(rs.getLong("id"));
		notice.setNoticeType(rs.getInt("noticeType"));
		notice.setNoticeTitle(rs.getString("noticeTitle"));
		notice.setNoticeContent(rs.getString("noticeContent"));
		notice.setStartTime(rs.getLong("startTime"));
		notice.setEndTime(rs.getLong("endTime"));
		notice.setTimeDelay(rs.getInt("timeDelay"));
		
		
		return notice;
	}

}
