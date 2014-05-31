
public class Process extends Thread{

	private Pages[] m_pages;
	private int m_workingSet;
	private int m_id;
	
	private static int s_id = 0;
	
	public Process(final int pageCount, final int workingSet){
		m_pages = new Pages[pageCount];
		m_workingSet = workingSet;
		m_id = s_id++;
	}
	
	public void run () {
		for (;;){
			System.out.println("Thread " + m_id);
			
			try {
				sleep(3 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
