package rik.shared;

import java.util.List;


public interface Rpc {
	public LeaderBoardRep getLeaderBoard(String redditname);
	public LeaderBoardRep getDailyLeaderBoard(String redditname);
	public String buy(String rid,BRep ridBought);
	public String sell(String rid,BRep ridSold);
	
	/**
	 * Get the recent users who have posted to reddit on that reddit as a list
	 * 
	 * @param rid
	 * @return
	 */
	public BRep[] getToBuyList(String redditname,String rid);
	public BRep[] getBoughtList(String rid);
	public String[] getMySubreddits(String rid, String modhash);
	BRep getById(String id);
	public void setMessage(String rid,String text);

}