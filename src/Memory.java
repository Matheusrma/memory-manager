import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Memory {
	private Integer m_lock = new Integer(0);
	
	private Frame[] m_frames;
	private List<Frame> m_freeFrames;
	private Map<Page, Frame> m_frameTable;
	private List<Integer> m_processPidList;
	
	public Memory(final int frameCount){
		m_frames = new Frame[frameCount];
		m_freeFrames = new ArrayList<Frame>();
		m_frameTable = new HashMap<Page, Frame>();
		m_processPidList = new ArrayList<Integer>();
		
		for (int i = 0; i < frameCount; ++i){
			m_frames[i] = new Frame(i);
			m_freeFrames.add(m_frames[i]);
		}
	}
	
	public int allocatePage(Page page){
		synchronized (m_lock) {					
			if (m_freeFrames.size() == 0){
				removeProcess(m_processPidList.get(0));
				m_processPidList.remove(0);
			}
			
			Frame choosenFrame = m_freeFrames.get(0);
			m_freeFrames.remove(0);
			
			if (!m_processPidList.contains(page.getOwnerPid())){
				m_processPidList.add(page.getOwnerPid());
			}
			
			m_frameTable.put(page, choosenFrame);
			
			print();

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
	
	@SuppressWarnings("rawtypes")
	private void removeProcess(final int pid){
		System.out.println("REMOVING Thread " + pid);
		List<Page> toRemovePages = new ArrayList<Page>();
		Iterator it = m_frameTable.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Page page = (Page)pairs.getKey();
			
			if (page.getOwnerPid() == pid){
				toRemovePages.add(page);
			}
		}
		
		System.out.println("Removed " + toRemovePages.size() + " pages.");
		
		for (int i = 0; i < toRemovePages.size(); ++i){
			m_freeFrames.add(m_frameTable.get(toRemovePages.get(i)));
			m_frameTable.remove(toRemovePages.get(i));
			MemoryManager.getInstance().getProcess(pid).deallocateAll();
		}
	}
}
