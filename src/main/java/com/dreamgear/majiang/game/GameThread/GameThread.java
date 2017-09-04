package com.dreamgear.majiang.game.GameThread;

public class GameThread  extends Thread {
	ThreadWork work;

	@Override
	public void run() {
		work.doWork();
	}

	public ThreadWork getWork() {
		return work;
	}

	public void setWork(ThreadWork work) {
		this.work = work;
	}

	
}
