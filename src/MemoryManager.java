import java.util.HashMap;
import java.util.Map;


public class MemoryManager extends Thread {

	private static final int BETWEEN_PROCESS_TIME = 500;
	private static final int BETWEEN_PROCESS_REQUESTS_TIME = 500;
	
	private static final int MEMORY_FRAME_COUNT = 64;

	private static final int MAX_PROCESS_COUNT = 20;

	private static final int PROCESS_PAGE_COUNT = 50;
	private static final int PROCESS_WORKING_SET = 4;
	
	private Memory m_memory;
	private Map<Integer, Process> m_processPool;
	private int m_processCountMax;
	
	private static MemoryManager m_instance;
	
	private MemoryManager(final int processCount){
		m_memory = new Memory(MEMORY_FRAME_COUNT);
		m_processCountMax = processCount;
		m_processPool = new HashMap<Integer, Process>();
	}
	
	public static MemoryManager getInstance(){
		if (m_instance == null){
			m_instance = new MemoryManager(MAX_PROCESS_COUNT);
		}
		return m_instance;
	}
	
	public Memory getMemory(){
		return m_memory;
	}
	
	public Process getProcess(final int pid){
		return m_processPool.get(pid);
	}
	
	public void run () {
		int processCount = 0;
		for (;;){
				
			if (processCount < m_processCountMax){				
				Process process = new Process(PROCESS_PAGE_COUNT, PROCESS_WORKING_SET,BETWEEN_PROCESS_REQUESTS_TIME);
				process.start();
				m_processPool.put(process.getPid(), process);
				processCount++;
			}
			
			try {
				sleep(BETWEEN_PROCESS_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
