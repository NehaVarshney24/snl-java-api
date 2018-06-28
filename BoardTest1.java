package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.qainfotech.tap.training.snl.api.Board;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.*;


public class BoardTest1 {

	Board board ;
    JSONArray namePlayer;
    JSONObject playerUuid;
    JSONObject playerPosition;


    @BeforeClass
	public void before() throws FileNotFoundException, UnsupportedEncodingException, IOException
	{
		board = new Board();
	}
	
	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
    public void maxPlayerPossible() throws Exception
	{  
		   namePlayer =  board.registerPlayer("Neha");
           namePlayer =  board.registerPlayer("Abhi");
           namePlayer =  board.registerPlayer("Shreya");
           namePlayer =  board.registerPlayer("Anu");
           namePlayer =  board.registerPlayer("abc");
    }
	
	@Test(expectedExceptions = PlayerExistsException.class )
	public void nameAlreadyExists() throws Exception
	{
		Board newName=new Board();
		for (int i = 0; i <namePlayer.length(); i++) {
			newName.registerPlayer("Anu");
        }
	}
	
	@Test
	public void extractingUuidFromJsonArray() throws Exception
	{
		Board newUuid =new Board();
		namePlayer = newUuid.registerPlayer("Abhi");
		namePlayer = newUuid.registerPlayer("Neha");
		namePlayer = newUuid.registerPlayer("Abc");
		for(int i=0;i<namePlayer.length();i++) {
			playerUuid = namePlayer.getJSONObject(i); //.get("uuid");
			System.out.println("--------------UUID---------------");
			System.out.println(playerUuid.get("uuid"));
		}
		
	}
	
	@Test
	public void playerWithInitialPosition_extractingPositionFromJsonArray() throws Exception
	{
		Board playerPos =new Board();
		namePlayer = playerPos.registerPlayer("Abhi");
		namePlayer = playerPos.registerPlayer("Neha");
		namePlayer = playerPos.registerPlayer("Abc");
		for(int i=0;i<namePlayer.length();i++) 
		{
			playerPosition = namePlayer.getJSONObject(i);//.get("uuid");
			int position = (int) playerPosition.get("position");
			Assert.assertEquals(0,position);
		}
		
	}
	
	@Test//(expectedExceptions=NoUserWithSuchUUIDException.class)
	public void deletePlayerIfUuidMatches() throws Exception
	{
		Board newUuid =new Board();
		namePlayer = newUuid.registerPlayer("Abhi");
		namePlayer = newUuid.registerPlayer("Shreya");
        namePlayer = newUuid.registerPlayer("Anu");
        namePlayer = newUuid.registerPlayer("abc");
		playerUuid = namePlayer.getJSONObject(0);
		
		UUID uuid = (UUID) newUuid.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		newUuid.deletePlayer(uuid);

//		UUID uid=UUID.fromString((String) playerUuid.get("uuid"));
//		newUuid.deletePlayer(uid);  
  	}
	
	@Test
	public void rollDieForFirstTime() throws Exception
	{
		Board newUuid =new Board();
		namePlayer = newUuid.registerPlayer("Neha");
		namePlayer = newUuid.registerPlayer("Abhi");
		playerUuid = namePlayer.getJSONObject(0);
		
		UUID uid=UUID.fromString((String) playerUuid.get("uuid"));
		JSONObject rollObject = newUuid.rollDice(uid);
		Object finalPosition = ((JSONObject) newUuid.getData().getJSONArray("players").get(0)).getInt("position");
		Integer str= (Integer) rollObject.get("dice");
		System.out.println(rollObject);
		System.out.println(str);
		System.out.println(playerUuid.get("uuid"));
		System.out.println("Final position "+finalPosition); 
		Assert.assertNotEquals(0,finalPosition);
		
	}
	
	
/**	Test case to check whether user has started a game 
 *  Only run when user has started the game
 * 
 @Test(expectedExceptions=GameInProgressException.class,dependsOnMethods= {"deletePlayerIfUuidMatches"})
	public void gameInProgressPlayerWithoutInitialPosition() throws Exception
	{
		Board newUuid =new Board();
		namePlayer = newUuid.registerPlayer("Abc");
		for(int i=0;i<namePlayer.length();i++) 
		{	playerPosition = namePlayer.getJSONObject(i);//.get("uuid");
			int position = (int) playerPosition.get("position");
			System.out.println(position); 
			Assert.assertNotEquals(0,position);
		}
	}*/
}
