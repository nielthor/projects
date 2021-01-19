package ser321.assign3.tnielse3.server;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.*;
	/**
	 * Copyright 2020 Thor Nielsen,
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 * http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 *
	 * Purpose: MediaAlbum is a class whose properties describe a single
	 * media work -- song or video/clip.
	 * Ser321 Principles of Distributed Software Systems
	 * see http://pooh.poly.asu.edu/Ser321
	 * @author Thor Nielsen tnielse3@asu.edu
	 * @version February 2020
	 */
public class MusicAlbum extends Object implements java.io.Serializable{


	   public String albumName;
	   public String groupName;
	   public String  xlImgUrl;
	   public String runTime;
	   public String summary;
	   public ArrayList<Tracks> tracks;
	   public ArrayList<String> genre;
	   public int runTimeInt;


		 public MusicAlbum(){
			 this.albumName = "";
			 this.groupName = "";
			 this.xlImgUrl = "";
			 this.runTime = "";
			 this.summary = "";
			 this.tracks = new ArrayList<Tracks>();
			 this.genre = new ArrayList<String>();
			 this.runTimeInt = 0;
		 }
		 public MusicAlbum(String albumName, String groupName, String xlImgUrl,
	                            String summary, ArrayList<String> genre, ArrayList<Tracks> tracks){
	      this.albumName = albumName;
	      this.groupName = groupName;
	      this.xlImgUrl = xlImgUrl;
	      this.summary = summary;
	      this.genre = genre;
	      this.tracks = tracks;
	   }

	   public MusicAlbum(String jsonString){
	      //this(new JSONObject(jsonString));
    	  try{

    		  	JSONObject media = new JSONObject(jsonString);
    		  	JSONObject albObj = media.getJSONObject("album");
    		  	this.albumName = albObj.getString("name"); //--------------------Album Name
    		  	this.groupName = albObj.getString("artist");//--------------------Group Name
    		  	//System.out.println("the title is: "+albObj.getString("name"));
    		  	//System.out.println("the title is: "+albObj.getString("artist"));

    		  	//Find and set XL image
    		  	JSONArray imgArr = albObj.getJSONArray("image");
    		  	for(int i=0; i<imgArr.length(); i++)
    		  	{
    		  		JSONObject inObj = imgArr.getJSONObject(i);
    			  	if(inObj.getString("size").equalsIgnoreCase("extralarge")){
    			  		this.xlImgUrl = inObj.getString("#text");
    			  		//System.out.println("url of png is: "+inObj.getString("#text"));//------Image URL
    			}}

    		  	//Created Track Class  - Track Name, Duration, Rank Order, and GroupName
			this.tracks = new ArrayList<Tracks>();
    		  	JSONObject trk = albObj.getJSONObject("tracks");
    		  	JSONArray trkArr = trk.getJSONArray("track");
    		  	for(int i = 0; i < trkArr.length(); i++)
    		  	{
    		  		Tracks aTrk = new Tracks();
    		  		JSONObject trkObj = trkArr.getJSONObject(i);
							aTrk.setAlbumName(this.albumName);
    		  		aTrk.setTrackName(trkObj.getString("name")); //--------------- Track Name

							//need to format to time
							aTrk.setDuration(formatTime(trkObj.getInt("duration")));//--------------Duration
							runTimeInt += trkObj.getInt("duration");
							JSONObject rnkObj = trkObj.getJSONObject("@attr");
    		  		aTrk.setRankOrder(rnkObj.getString("rank"));//-----------------Rank Order

    		  		JSONObject artObj = trkObj.getJSONObject("artist");
    		  		aTrk.setArtistGroup(artObj.getString("name"));//---------------Group Name
							//System.out.println("HERE " + aTrk.toString());

    		  		this.tracks.add(aTrk);
    		  	}
						runTime = formatTime(runTimeInt);

    		  	//Album Duration
						/*
    		  	for(int i = 0; i < tracks.size(); i++)
    		  	{
    		  		runTimeInt += tracks.get(i).getDuration();
    		  	}
		    		int hour, min, sec;
						hour = runTimeInt/3600;
						min = (runTimeInt%3600)/60;
						sec = (runTimeInt%3600) % 60;
		    		runTime = String.format("%02d:%02d:%02d", hour, min,sec);
						*/

    		  	//Create Tag Array List
			genre = new ArrayList<String>();
    		  	JSONObject tags = albObj.getJSONObject("tags");
    		  	JSONArray tagArr = tags.getJSONArray("tag");
    		  	for(int i = 0; i < tagArr.length(); i++)
    		  	{
    		  		JSONObject tagObj = tagArr.getJSONObject(i);
    		  		genre.add(tagObj.getString("name"));
    		  	}

    		  	//Get Summary
    		  	JSONObject smry = albObj.getJSONObject("wiki");
    		  	this.summary = smry.getString("summary");

    		  	}catch (Exception ex){
    		  		System.out.println("exception : "+ex.getMessage());
    		  	}
	   }
		 private String formatTime(int value){
			 int hour, min, sec;
			 hour = runTimeInt/3600;
			 min = (runTimeInt%3600)/60;
			 sec = (runTimeInt%3600) % 60;
			 return	String.format("%02d:%02d:%02d", hour, min,sec);
		 }
	   //From my Music.json file
	   public MusicAlbum(JSONObject jsonObj){
	      try{
	    	 this.albumName = jsonObj.getString("albumName");
	    	 this.groupName = jsonObj.getString("groupName");
	    	 this.xlImgUrl = jsonObj.getString("xlImgUrl");
	    	 this.summary = jsonObj.getString("summary");
				 this.runTime = jsonObj.getString("duration");

		//Add Genres
		genre = new ArrayList<String>();
		JSONArray genreArr = jsonObj.getJSONArray("genre");
		for(int i = 0; i< genreArr.length(); i++){
			this.genre.add(genreArr.getString(i));
		}
		//Add tracks
		tracks = new ArrayList<Tracks>();
		JSONArray trkArr = jsonObj.getJSONArray("tracks");
		for(int i =0; i < trkArr.length(); i++){
			Tracks trk = new Tracks();
			JSONObject trkObj = trkArr.getJSONObject(i);
			trk.setTrackName(trkObj.getString("track"));
			trk.setArtistGroup(trkObj.getString("artist"));
			trk.setDuration(trkObj.getString("duration"));
			trk.setRankOrder(trkObj.getString("rankOrder"));
			trk.setAlbumName(this.albumName);
			this.tracks.add(trk);

		}
		//genre = jsonObj.optJSONArray("genre");
	         //System.out.println("constructed "+this.toJsonString()+" from json");
	      }catch(Exception ex){
	         System.out.println("Exception in MediaDescription(JSONObject): "+ex.getMessage());
	      }
	   }

	   public String toJsonString(){
	      String ret = "{}";
	      try{
	         ret = this.toJson().toString(0);
	      }catch(Exception ex){
	         System.out.println("Exception in toJsonString: "+ex.getMessage());
	      }
	      return ret;
	   }

		 public void addTrack(Tracks aTrack){
			 this.tracks.add(aTrack);
		 }

		 public Tracks getTrack(String aTitle){
			 Tracks at = new Tracks();
			 for(int i = 0; i < tracks.size(); i++){
				 if(aTitle.equals(tracks.get(i).trackName)){
					 return tracks.get(i);
				 }
			 }
			 return at;
		 }

		 public void removeTrack(String aTitle){
			 for(int i = 0; i < tracks.size(); i++){
				 if(aTitle.equals(tracks.get(i).trackName)){
					 tracks.remove(i);
					 return;
				 }
			 }
		 }

		 public void setAlbumName(String aName){
			 this.albumName = aName;
		 }

		 public void setArtistGroup(String aGroup){
			 this.groupName = aGroup;
		 }

	   public JSONObject toJson(){
	      JSONObject obj = new JSONObject();
	      try{
	         obj.put("albumName", albumName);
	         obj.put("groupName", groupName);
	         obj.put("xlImgUrl", xlImgUrl);
	         obj.put("summary", summary);
	         obj.put("duration", runTime);
	         //JSONObject jsonTracks = new JSONObject();
	         JSONArray arrTracks = new JSONArray();

	         for(int i=0; i<tracks.size();i++)
	         {
	        	 JSONObject trkObj = new JSONObject();
	        	 trkObj.put("track", tracks.get(i).getTrackName());
	        	 trkObj.put("artist", tracks.get(i).getArtistGroup());
	        	 trkObj.put("duration", tracks.get(i).getDuration());
	        	 trkObj.put("rankOrder", tracks.get(i).getRankOrder());
						 trkObj.put("album", tracks.get(i).getAlbumName());
	        	 arrTracks.put(trkObj);

	         }
	         //jsonTracks.put("tracks", arrTracks);
	         obj.put("tracks", arrTracks);
	         JSONArray arrGenre = new JSONArray();
	         for(int i = 0; i<genre.size();i++)
	         {
	        	 arrGenre.put(genre.get(i));
	         }
	         obj.put("genre", arrGenre);
	      }catch(Exception ex){
	         System.out.println("Exception in toJson: "+ex.getMessage());
	      }
	      return obj;
	   }

}
