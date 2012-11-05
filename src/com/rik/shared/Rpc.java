package com.rik.shared;

import java.util.List;


public interface Rpc {
	public BRep[] getLeaderBoard(String redditname);
	public String buy(String rid,BRep ridBought);
	public String sell(String rid,BRep ridSold);
	
	/**
	 * Get the recent users who have posted to reddit on that reddit as a list
	 * 
	 * @param rid
	 * @return
	 */
	public BRep[] getToBuyList(String redditname);
	public BRep[] getBoughtList(String rid);
	
}