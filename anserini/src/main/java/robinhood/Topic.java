package robinhood;

public class Topic {
	private String number;
	private String title;
	
	public Topic(String number, String title) {
		this.number = number;
		this.title = title;
	}
	
	public String getNumber() {
		return this.number;
	}
	public String getTitle() {
		return this.title;
	}
}
