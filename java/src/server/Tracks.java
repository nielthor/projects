package ser321.assign3.tnielse3.server;

import java.io.Serializable;

public class Tracks extends Object implements java.io.Serializable{
	public String trackName, artistGroup, rankOrder, albumName, duration;

	public Tracks(){
		this.setTrackName(null);
		this.setArtistGroup(null);
		this.setDuration(null);
		this.setRankOrder(null);
		this.setAlbumName(null);

	}
	public Tracks(String trackName, String artistGroup, String duration, String rankOrder, String albumName)
	{
		this.setTrackName(trackName);
		this.setArtistGroup(artistGroup);
		this.setDuration(duration);
		this.setRankOrder(rankOrder);
		this.setAlbumName(albumName);
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
   	public String toString()
   	{
        	return getTrackName() + "\n"+getArtistGroup()+ "\n"+getRankOrder()+ "\n"+ getDuration()+ "\n"+getDuration();
   	}
}
