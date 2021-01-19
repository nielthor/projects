package ser321.assign3.tnielse3.server;

import java.io.*;
import java.util.*;
import java.net.URL;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.rmi.*;
import java.rmi.server.*;
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
 * Purpose: MediaLibraryImpl is the implementing class for library interface
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version March 2020
 */
public class MediaLibraryImpl extends UnicastRemoteObject implements MediaLibrary{

   private Hashtable<String,MusicAlbum> aLib;
   private static final String fileName="music.json";
   private FileWriter writer;
   private File file;

   public MediaLibraryImpl () throws RemoteException{
      this.aLib = new Hashtable<String,MusicAlbum>();
     try{
         InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
         if(is==null){
            is = new FileInputStream(new File(this.fileName));
         }
         JSONObject media = new JSONObject(new JSONTokener(is));
         Iterator<String> it = media.keys();
         while (it.hasNext()){
            String mediaTitle = it.next();
            JSONObject aMed = media.optJSONObject(mediaTitle);
            if (aMed != null){
              //System.out.println(aMed.toString());
               MusicAlbum md = new MusicAlbum(aMed);
               aLib.put(mediaTitle, md);
            }
         }
      }catch (Exception ex){
         System.out.println("Exception reading "+fileName+": "+ex.getMessage());
      }
   }

   public boolean addAlbum(MusicAlbum md) throws RemoteException{
      boolean result = false;
      System.out.println("Adding: "+md.albumName);
      try{
         aLib.put(md.albumName,md);
         result = true;

      }catch(Exception ex){
         System.out.println("exception in add: "+ex.getMessage());
      }
      return result;
   }
   public boolean addTrack(Tracks aTrack) throws RemoteException{
     boolean result = false;
     Set<String> keys = aLib.keySet();
     for(String key : keys){
       if(aTrack.albumName.equals(key)){
         aLib.get(key).addTrack(aTrack);
         return true;
       }
     }
     MusicAlbum aAlbum = new MusicAlbum();
     aAlbum.setAlbumName(aTrack.albumName);
     aAlbum.setArtistGroup(aTrack.artistGroup);
     aAlbum.addTrack(aTrack);
     aLib.put(aTrack.albumName,aAlbum);
     return true;
   }


   public boolean removeAlbum(String mediaTitle) throws RemoteException{
      boolean result = false;
      System.out.println("Removing "+mediaTitle);
      try{
         aLib.remove(mediaTitle);
         result = true;
      }catch(Exception ex){
         System.out.println("exception in remove: "+ex.getMessage());
      }
      return result;
   }

   public boolean removeTrack(String aTitle) throws RemoteException{
     boolean result = false;
     try{
       MusicAlbum aAlbum = findAlbum(aTitle);
       aLib.get(aAlbum.albumName).removeTrack(aTitle);
       result = true;
     }catch(Exception ex){
        System.out.println("exception in get: "+ex.getMessage());
     }
     return result;
   }

   public MusicAlbum get(String mediaTitle) throws RemoteException{
      MusicAlbum result = null;
      try{
         result = aLib.get(mediaTitle);
      }catch(Exception ex){
         System.out.println("exception in get: "+ex.getMessage());
      }
      return result;
   }

   public String[] getTitles() throws RemoteException{
      String[] result = null;
      try{
         Set<String> vec = aLib.keySet();
         result = vec.toArray(new String[]{});

      }catch(Exception ex){
         System.out.println("exception in getTitles: "+ex.getMessage());
      }
      return result;
   }
   public MusicAlbum findAlbum(String aTitle) throws RemoteException{
     Set<String> keys = aLib.keySet();
     MusicAlbum aAlbum = new MusicAlbum();
     aAlbum.setAlbumName("-unknownAlbum-");
     for(String key: keys){
         if(aTitle.equalsIgnoreCase(key)){
           return aLib.get(key);
         }
         for(int i=0;i<aLib.get(key).tracks.size();i++){
           if(aTitle.equalsIgnoreCase(aLib.get(key).tracks.get(i).trackName)){
             return aLib.get(key);
           }
         }
     }
      return aAlbum;
   }

   public Tracks findTrack(String aTitle) throws RemoteException{
     Set<String> keys = aLib.keySet();
     Tracks aTrack = new Tracks();
     aTrack.setTrackName("-unknownTrack-");
     for(String key: keys){

       for(int i=0;i<aLib.get(key).tracks.size();i++){
         if(aTitle.equalsIgnoreCase(aLib.get(key).tracks.get(i).trackName)){
           return aLib.get(key).tracks.get(i);
         }
       }
     }
     return aTrack;
   }

   public void save() throws RemoteException{
     try{
       JSONObject jsonFile = new JSONObject();
       Set<String> keys = aLib.keySet();
       for(String key: keys){
         jsonFile.put(key, aLib.get(key).toJson());
       }
       //System.out.println(jsonFile.toString());
       writer = new FileWriter("music.json");
       writer.write(jsonFile.toString());
       writer.flush();
       writer.close();
     }catch(Exception ex){
        System.out.println("Exception in toJson: "+ex.getMessage());
     }
   }
   public void restore() throws RemoteException{
     aLib.clear();
    try{
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
        if(is==null){
           is = new FileInputStream(new File(this.fileName));
        }
        JSONObject media = new JSONObject(new JSONTokener(is));
        Iterator<String> it = media.keys();
        while (it.hasNext()){
           String mediaTitle = it.next();
           JSONObject aMed = media.optJSONObject(mediaTitle);
           if (aMed != null){
             //System.out.println(aMed.toString());
              MusicAlbum md = new MusicAlbum(aMed);
              aLib.put(mediaTitle, md);
           }
        }
     }catch (Exception ex){
        System.out.println("Exception reading "+fileName+": "+ex.getMessage());
     }

   }
   public static void main(String args[]) {
      try {
        System.out.println("in Server main");
         String hostId="localhost";
         String regPort="8080";
         if (args.length >= 2){
	          hostId=args[0];
            regPort=args[1];
         }
         //System.setSecurityManager(new RMISecurityManager()); // rmisecmgr deprecated
         MediaLibrary obj = new MediaLibraryImpl();
         //Naming.rebind("rmi://"+hostId+":"+regPort+"/MediaLibraryServer", obj);
         Naming.rebind("rmi://" + hostId + ":"+ regPort +"/MediaLibraryServer", obj);
         System.out.println("Server bound in registry as: "+
                            "rmi://"+hostId+":"+regPort+"/MediaLibraryServer");
      }catch (Exception e) {
        System.out.println("Failed");
      }
   }

}
