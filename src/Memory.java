import java.util.ArrayList;
import java.util.List;


public class Memory {

	private Frame[] m_frames;
	private List<Frame> m_freeFrames;
	
	public Memory(final int frameCount){
		m_frames = new Frame[frameCount];
		m_freeFrames = new ArrayList<Frame>();
		
		for (int i = 0; i < frameCount; ++i){
			m_frames[i] = new Frame(i);
			m_freeFrames.add(m_frames[i]);
		}
	}
	
	public int allocatePage(Page page){
		Frame choosenFrame = m_freeFrames.get(0);
		m_freeFrames.remove(0);
		
		choosenFrame.setPage(page);
		return choosenFrame.getAddress();
	}
}
