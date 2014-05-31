import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Memory {

	private Frame[] m_frames;
	private List<Frame> m_freeFrames;
	private Map<Page, Frame> m_frameTable;
	
	public Memory(final int frameCount){
		m_frames = new Frame[frameCount];
		m_freeFrames = new ArrayList<Frame>();
		m_frameTable = new HashMap<Page, Frame>();
		
		for (int i = 0; i < frameCount; ++i){
			m_frames[i] = new Frame(i);
			m_freeFrames.add(m_frames[i]);
		}
	}
	
	public int allocatePage(Page page){
		//TODO: If there are no free frames, remove an entire process
		
		Frame choosenFrame = m_freeFrames.get(0);
		m_freeFrames.remove(0);
		
		choosenFrame.setPage(page);
		m_frameTable.put(page, choosenFrame);
		return choosenFrame.getAddress();
	}
	
	public void deallocatePage(Page page){
		m_freeFrames.add(m_frameTable.get(page));
		m_frameTable.remove(page);
	}

	public void print() {
		System.out.println("Memory has " + m_freeFrames.size() + " free frames");
	}
}
