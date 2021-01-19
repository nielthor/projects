package ser321.assign6.tnielse3.server;

import java.io.Serializable;
import org.json.*;

public class Tracks {
	public String trackName, artistGroup, rankOrder, albumName, duration;

	public Tracks(){
		this.setTrackName("");
		this.setArtistGroup("");
		this.setDuration("");
		this.setRankOrder("");
		this.setAlbumName("");

	}
	public Tracks(String trackName, String artistGroup, String duration, String rankOrder, String albumName)
	{
		this.setTrackName(trackName);
		this.setArtistGroup(artistGroup);
		this.setDuration(duration);
		this.setRankOrder(rankOrder);
		this.setAlbumName(albumName);
	}

	public Tracks(JSONObject jsonStr){

		try{
			//JSONObject jo = new JSONObject(jsonStr);
			System.out.println(jsonStr.toString());
			this.setTrackName(jsonStr.getString("track"));
			this.setArtistGroup(jsonStr.getString("artist"));
			this.setDuration(jsonStr.getString("duration"));
			this.setRankOrder(jsonStr.getString("rankOrder"));
			this.setAlbumName(jsonStr.getString("album"));
		}catch(Exception e){
			System.out.println(this.getClass().getSimpleName() +
													" : error converting from JSON string");
		}
	}

	public String getAlbumName(){
		return albumName;
	}
	public void setAlbumName(String albumName){
		this.albumName = albumName;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getArtistGroup() {
		return artistGroup;
	}
	public void setArtistGroup(String artistGroup) {
		this.artistGroup = artistGroup;
	}
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getRankOrder() {
		return rankOrder;
	}
	public void setRankOrder(String rankOrder) {
		this.rankOrder = rankOrder;
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

	public JSONObject toJson(){
		 JSONObject obj = new JSONObject();
		 /*
		 this.setTrackName(jo.getString("track"));
		 this.setArtistGroup(jo.getString("artist"));
		 this.setDuration(jo.getString("duration"));
		 this.setRankOrder(jo.getString("rankOrder"));
		 this.setAlbumName(jo.getString("album"));
		 */
		 try{
				obj.put("track", this.getTrackName());
				obj.put("artist", this.getArtistGroup());
				obj.put("duration", this.getDuration());
				obj.put("rankOrder", this.getRankOrder());
				obj.put("album", this.getAlbumName());

		 }catch(Exception ex){
				System.out.println("Exception in toJson: "+ex.getMessage());
		 }
		 return obj;
	}
   	public String toString()
   	{
        	return getTrackName() + "\n"+getArtistGroup()+ "\n"+getRankOrder()+ "\n"+ getDuration()+ "\n"+getDuration();
   	}
}
