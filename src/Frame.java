
public class Frame {
	private Page m_page;
	private int m_address;
	
	public Frame(final int address){
		m_address = address;
	}

	public int getAddress(){
		return m_address;
	}
	
	public Page getPage() {
		return m_page;
	}

	public void setPage(Page page) {
		m_page = page;
	}
}
