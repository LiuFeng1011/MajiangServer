package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.rank.RankData;

public class RankDataMapper  implements RowMapper<RankData> {

	public RankData mapRow(ResultSet rs, int rowNum) throws SQLException {

		RankData rd = new RankData();
		rd.setId(rs.getLong("id"));
		rd.setUid(rs.getLong("uid"));
		rd.setScores(rs.getInt("scores"));
		return rd;
		
	}
}
