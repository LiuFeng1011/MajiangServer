package com.dreamgear.majiang.game.game;

public class GameResult {
	public int badPlayer = -1;//点炮的玩家
	public int winPlayer;//胜利的玩家
	public int[] playerScores;
	public int winType;
	public String brand;//胡牌
	public int getBadPlayer() {
		return badPlayer;
	}

	public void setBadPlayer(int badPlayer) {
		this.badPlayer = badPlayer;
	}

	public int getWinPlayer() {
		return winPlayer;
	}

	public void setWinPlayer(int winPlayer) {
		this.winPlayer = winPlayer;
	}
	
}
