package com.daviddev16.core;

public class TimeCounterWatcher implements Runnable {

	private long totalTimeSpent = 0;
	private boolean state = false;
	private final Thread thread;
	
	public TimeCounterWatcher() {
		thread = new Thread(this);
	}
		
	@Override
	public void run() {
		try {
			final long startTime = System.currentTimeMillis();
			while (state) {
				totalTimeSpent = System.currentTimeMillis() - startTime;
				tick(totalTimeSpent);
				Thread.sleep(1L);
			}
		} catch (InterruptedException e) {/*ignore*/}
	}
	
	public void tick(long currentTimeSpent) {}
	
	public void start() { 
		state = true;
		thread.start();
	}

	public void stop() {
		state = false;
		thread.interrupt();
	}

}
