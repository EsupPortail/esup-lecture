package org.esupportail.lecture.test.drivers;

public class FooThread extends Thread {
	private int i  = 1;

	@Override
	public void run() {
		while (true) {
//			try {
////				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			System.out.println(i++);
			if (interrupted()) {
				return;
			}
		}
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
}
