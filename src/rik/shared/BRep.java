package rik.shared;
public class BRep {
	public String rid;
	public String message;
	public int score;
	public String htmlString;

public BRep() {
}
	public BRep(String rid, String message, int score) {
		super();
		this.rid = rid;
		this.message = message;
		this.score = score;
	}


	@Override
	public String toString() {
		return message+" "+score;
	}
	
	public String toHtmlString() {
		if(htmlString != null){
			return htmlString;
		}
		String value = getColorCoded(score);
		return message +" "+ value;
	}
	public static String getColorCoded(int score) {
		String color = "green";
		if(score < 0){
			color = "red";
		}
		String value = "<font color="+color+">"+score+"</font>";
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		return rid.equals(((BRep)obj).rid);
	}
}