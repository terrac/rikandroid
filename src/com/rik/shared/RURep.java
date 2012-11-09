package com.rik.shared;


public class RURep implements Comparable<RURep> {
	public String ruid;
	public String name;
	public int score;
	
	public RURep() {
		// TODO Auto-generated constructor stub
	}
	public RURep(String ruid, String name, int score) {
		super();
		this.ruid = ruid;
		this.name = name;
		this.score = score;
	}
	@Override
	public String toString() {
		return name + " " + score;
	}

	@Override
	public boolean equals(Object obj) {
		return ruid.equals(((RURep) obj).ruid);
	}

	@Override
	public int compareTo(RURep o) {
		// TODO Auto-generated method stub
		return score - o.score;
	}
}