package org.esupportail.lecture.test.drivers;

public class TestThread {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		FooThread thread = new FooThread();
		thread.start();
		try {
			thread.join(500);
			System.out.println("Finally : "+thread.getI());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        if (thread.isAlive()) {
    		thread.interrupt();
            throw new Exception("Interrupt Thread");
        }
		
	}

}
