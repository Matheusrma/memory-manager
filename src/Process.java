import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Process extends Thread{

	private Page[] m_pages;
	private int m_workingSet;
	private int m_pid;
	private int m_processAcessDelay;
	
	Map<Integer, Integer> m_frameTable;
	List<Integer> m_allocatedPages;
	
	private static int s_nextPid = 0;
	private static Integer s_lock = 0;
	
	public Process(final int pageCount, final int workingSet, final int processAcessDelay){
		m_pid = s_nextPid++;
		
		System.out.println("Thread " + m_pid + " created.");

		m_pages = new Page[pageCount];
		
		for (int i = 0; i < pageCount; ++i){
			m_pages[i] = new Page(m_pid);
		}
		
		m_workingSet = workingSet;
		m_processAcessDelay = processAcessDelay;
		
		m_allocatedPages = new ArrayList<Integer>();
		m_frameTable = new HashMap<Integer, Integer>();
	}
	
	public void run () {
		for (;;){
			
			synchronized (s_lock) {			
				System.out.println("Thread " + m_pid + " start allocating");
				allocateRandomPage();
				printFrameTable();
			    System.out.println();
			}
			
			try {
				sleep(m_processAcessDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deallocateAll(){
		m_allocatedPages.clear();
		m_frameTable.clear();
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
		
		System.out.println("Removing Page " + pageToDeallocateIndex);
		
		MemoryManager.getInstance().getMemory().deallocatePage(m_pages[pageToDeallocateIndex]);
		m_frameTable.remove(pageToDeallocateIndex);
		m_allocatedPages.remove(0);
	}

	@SuppressWarnings("rawtypes")
	private void printFrameTable(){
		System.out.println("FRAME TABLE");
		Iterator it = m_frameTable.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println("P:" + pairs.getKey() + " --> F:" + pairs.getValue());
	    }
	}

	public Integer getPid() {
		return m_pid;
	}
}
