import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Process extends Thread{

	private Page[] m_pages;
	private int m_workingSet;
	private int m_id;

	Map<Integer, Integer> m_frameTable;
	List<Integer> m_allocatedPages;
	
	private static int s_id = 0;
	
	public Process(final int pageCount, final int workingSet){
		m_pages = new Page[pageCount];
		m_workingSet = workingSet;
		
		m_allocatedPages = new ArrayList<Integer>();
		m_frameTable = new HashMap<Integer, Integer>();
		
		m_id = s_id++;
	}
	
	public void run () {
		for (;;){
			
			allocateRandomPage();
			printFrameTable();
			
			try {
				sleep(3 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void printFrameTable(){
		System.out.println("Thread " + m_id);
		Iterator it = m_frameTable.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + "-->" + pairs.getValue());
	    }
	}

	private void allocateRandomPage() {
		Random rnd = new Random();
		
		int choosenPage = rnd.nextInt(m_pages.length);;
		
		while(m_allocatedPages.contains(choosenPage))
			choosenPage = rnd.nextInt(m_pages.length);
		
		int usedFrame = MemoryManager.getInstance().getMemory().allocatePage(m_pages[choosenPage]);
		
		m_allocatedPages.add(choosenPage);
		m_frameTable.put(choosenPage, usedFrame);
	}
}
