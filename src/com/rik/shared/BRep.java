package com.rik.shared;
public class BRep {
	String rid;
	String message;
	int score;

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
	@Override
	public boolean equals(Object obj) {
		return rid.equals(((BRep)obj).rid);
	}
}