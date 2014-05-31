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
	private int m_processAcessDelay;
	
	Map<Integer, Integer> m_frameTable;
	List<Integer> m_allocatedPages;
	
	private static int s_id = 0;
	private static Integer s_lock = 0;
	
	public Process(final int pageCount, final int workingSet, final int processAcessDelay){
		m_pages = new Page[pageCount];
		m_workingSet = workingSet;
		m_processAcessDelay = processAcessDelay;
		
		m_allocatedPages = new ArrayList<Integer>();
		m_frameTable = new HashMap<Integer, Integer>();
		
		m_id = s_id++;
	}
	
	public void run () {
		for (;;){
			
			synchronized (s_lock) {				
				allocateRandomPage();
				printFrameTable();
			}
			
			try {
				sleep(m_processAcessDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void allocateRandomPage() {
		if (m_allocatedPages.size() >= m_workingSet){
			deallocatePage();
		}
		
		Random rnd = new Random();
		int choosenPageIndex = rnd.nextInt(m_pages.length);
		
		while(m_allocatedPages.contains(choosenPageIndex))
			choosenPageIndex = rnd.nextInt(m_pages.length);
		
		int usedFrameIndex = MemoryManager.getInstance().getMemory().allocatePage(m_pages[choosenPageIndex]);
		
		m_frameTable.put(choosenPageIndex, usedFrameIndex);
		m_allocatedPages.add(choosenPageIndex);
	}
	
	private void deallocatePage() {
		Integer pageToDeallocateIndex = m_allocatedPages.get(0);
		
		MemoryManager.getInstance().getMemory().deallocatePage(m_pages[pageToDeallocateIndex]);
		m_frameTable.remove(pageToDeallocateIndex);
		m_allocatedPages.remove(0);
	}

	@SuppressWarnings("rawtypes")
	private void printFrameTable(){
		System.out.println("Thread " + m_id);
		Iterator it = m_frameTable.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + "-->" + pairs.getValue());
	    }
	}
}
