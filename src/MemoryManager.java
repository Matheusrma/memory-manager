
public class MemoryManager extends Thread {

	private Memory m_memory;
	private int m_processCount;
	
	private static MemoryManager m_instance;
	
	private MemoryManager(final int processCount){
		m_memory = new Memory(64);
		m_processCount = processCount;
	}
	
	public static MemoryManager getInstance(){
		if (m_instance == null){
			m_instance = new MemoryManager(2);
		}
		return m_instance;
	}
	
	public Memory getMemory(){
		return m_memory;
	}
	
	public void run () {
		for (int i = 0;i < m_processCount;++i){
						
			Process process = new Process(50, 4);
			process.start();
			
			try {
				sleep(3 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
