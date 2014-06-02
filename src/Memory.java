import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Memory {
	private Integer m_lock = new Integer(0);
	
	private Frame[] m_frames;
	private List<Frame> m_freeFrames;
	private Map<Page, Frame> m_frameTable;
	private Set<Integer> m_processPidSet;
	private int m_workingSet;
	
	public Memory(final int frameCount, int processWorkingSet){
		m_workingSet = processWorkingSet;
		m_frames = new Frame[frameCount];
		m_freeFrames = new ArrayList<Frame>();
		m_frameTable = new HashMap<Page, Frame>();
		m_processPidSet = new LinkedHashSet<Integer>();
		
		for (int i = 0; i < frameCount; ++i){
			m_frames[i] = new Frame(i);
			m_freeFrames.add(m_frames[i]);
		}
	}
	
	public int allocatePage(Process process, Page page){
		synchronized (m_lock) {
			print();
			
			if (process.getAllocatedPages().size() >= m_workingSet) {
				// page fault!
				process.deallocatePage();
			}
			
			if (m_freeFrames.isEmpty()){
				removeProcess(m_processPidSet.iterator().next());
				m_processPidSet.remove(0);
			}
			
			Frame choosenFrame = m_freeFrames.get(0);
			m_freeFrames.remove(0);
			
			m_processPidSet.add(page.getOwnerPid());
			
			m_frameTable.put(page, choosenFrame);

			return choosenFrame.getAddress();
		}
	}
	
	public void deallocatePage(Page page){
		synchronized (m_lock) {
			m_freeFrames.add(m_frameTable.get(page));
			m_frameTable.remove(page);
		}
	}

	private void print() {
		System.out.println("Memory has " + m_freeFrames.size() + " free frames");
	}
	
	private void removeProcess(final int pid){
		List<Page> toRemovePages = getProcessPages(pid);
		
		System.out.println("SWAPPING OUT: " + toRemovePages.size() + " pages from Thread " + pid);
		
		for (int i = 0; i < toRemovePages.size(); ++i){
			deallocatePage(toRemovePages.get(i));			
		}
		
		Controller.getInstance().getProcess(pid).deallocateAll();
	}

	@SuppressWarnings("rawtypes")
	private List<Page> getProcessPages(final int pid) {
		List<Page> pages = new ArrayList<Page>();
		Iterator it = m_frameTable.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Page page = (Page)pairs.getKey();
			
			if (page.getOwnerPid() == pid){
				pages.add(page);
			}
		}
		return pages;
	}
}
