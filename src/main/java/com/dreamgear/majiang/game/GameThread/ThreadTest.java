package com.dreamgear.majiang.game.GameThread;

public class ThreadTest  implements ThreadWork{
	private String myname = "";
	private long sleepTime = 10;
	
	public void doWork() {
		// TODO Auto-generated method stub
		
		for(int i = 0 ; i < 10 ; i ++){
			System.out.print(myname + "\n");
			try {
				// 线程发送时，暂停sleepTime(ms)
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getMyname() {
		return myname;
	}

	public void setMyname(String myname) {
		this.myname = myname;
	}
	
	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public static void main(String[] args) throws Exception{
		ThreadTest t1 = new ThreadTest();
		t1.setMyname("thread 1");
		t1.setSleepTime(1000l);
		ThreadTest t2 = new ThreadTest();
		t2.setMyname("thread 2");
		t2.setSleepTime(100l);
		
		GameThread gt1 = new GameThread();
		gt1.setWork(t1);
		gt1.start();
		

		GameThread gt2 = new GameThread();
		gt2.setWork(t2);
		gt2.start();
		
		
	}

}
