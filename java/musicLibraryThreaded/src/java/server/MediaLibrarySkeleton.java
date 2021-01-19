package ser321.assign6.tnielse3.server;

import java.net.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

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
 * A class for client-server connections with a threaded server.
 * The student collection server creates a server socket.
 * When a client request arrives, which should be a JsonRPC request, a new
 * thread is created to service the call and create the appropriate response.
 * Byte arrays are used for communication to support multiple langs.
 *
 * @author Thor Nielsen
 * @version April 2020
 */
public class MediaLibrarySkeleton extends Object {

   private static final boolean debugOn = true;
   MediaLibrary mediaLib;

   public MediaLibrarySkeleton (MediaLibrary mediaLib){
      this.mediaLib = mediaLib;
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   public String callMethod(String request){
      JSONObject result = new JSONObject();
      try{
         JSONObject theCall = new JSONObject(request);
         debug("Request is: "+theCall.toString());
         String method = theCall.getString("method");
         int id = theCall.getInt("id");
         JSONArray params = null;
         if(!theCall.isNull("params")){
            params = theCall.getJSONArray("params");
         }
         result.put("id",id);
         result.put("jsonrpc","2.0");
         //add album
         if(method.equals("addAlbum")){
            JSONObject albumJson = params.getJSONObject(0);
            MusicAlbum albumToAdd = new MusicAlbum(albumJson);
            debug("adding album: "+albumToAdd.toJsonString());
            mediaLib.addAlbum(albumToAdd);
            result.put("result",true);
         }
         //add track
         else if(method.equals("addTrack")){
            JSONObject trackJson = params.getJSONObject(0);
            Tracks trackToAdd = new Tracks(trackJson);
            debug("adding track: "+ trackToAdd.toJsonString());
            mediaLib.addTrack(trackToAdd);
            result.put("result",true);
         }
         //remove album
         else if(method.equals("removeAlbum")){
            String aTitle = params.getString(0);
            debug("removing album named "+ aTitle);
            mediaLib.removeAlbum(aTitle);
            result.put("result",true);
         }
         //remove track
         else if(method.equals("removeTrack")){
            String aTitle = params.getString(0);
            debug("removing track named "+ aTitle);
            mediaLib.removeTrack(aTitle);
            result.put("result",true);
         }
         //get album
         else if(method.equals("findAlbum")){
            String aTitle = params.getString(0);
            MusicAlbum aAlbum = mediaLib.findAlbum(aTitle);
            JSONObject albumJson = aAlbum.toJson();
            debug("found album : "+ albumJson.toString());
            result.put("result",albumJson);
         }
         //get track
         else if(method.equals("findTrack")){
           String aTitle = params.getString(0);
           Tracks aTrack = mediaLib.findTrack(aTitle);
           JSONObject trackJson = aTrack.toJson();
           debug("found track : "+ trackJson.toString());
           result.put("result",trackJson);
         }
         //Save library
         else if(method.equals("save")){
            debug("saving to json file");
            boolean saved = mediaLib.save();
            result.put("result",saved);
         }
         //restore library
         else if(method.equals("restore")){
           debug("restored from json file");
           boolean restored = mediaLib.restore();
           result.put("result",restored);
         }
         // get album titles
         else if(method.equals("getTitles")){
            String[] names = mediaLib.getTitles();
            JSONArray resArr = new JSONArray();
            for (int i=0; i<names.length; i++){
               resArr.put(names[i]);
            }
            debug("getTitles request found: "+resArr.toString());
            result.put("result",resArr);
         }else{
            debug("Unable to match method: "+method+". Returning 0.");
            result.put("result",0.0);
         }
      }catch(Exception ex){
         System.out.println("exception in callMethod: "+ex.getMessage());
      }
      return result.toString();
   }
}
