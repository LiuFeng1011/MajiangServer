package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.rank.RankGiftData;

public class RankGiftDataMapper implements RowMapper< RankGiftData>{

	public RankGiftData mapRow(ResultSet rs, int rowNum) throws SQLException {

		RankGiftData rgd = new RankGiftData();
		rgd.setId(rs.getLong("id"));
		rgd.setRank_min(rs.getInt("rank_min"));
		rgd.setRank_max(rs.getInt("rank_max"));
		rgd.setCount(rs.getInt("count"));
		return rgd;
		
	}
}
