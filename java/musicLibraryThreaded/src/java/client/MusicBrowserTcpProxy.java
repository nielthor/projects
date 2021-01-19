package ser321.assign6.tnielse3.client;

import ser321.assign6.tnielse3.server.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

import java.nio.file.Paths;
import java.nio.charset.Charset;
import javax.sound.sampled.*;
import java.beans.*;


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
  * The student collection client proxy implements the server methods
  * by marshalling/unmarshalling parameters and results and using a TCP
  * connection to request the method be executed on the server.
  * Byte arrays are used for communication to support multiple langs.
  *
  * @author Thor Nielsen
  * @version April 2020
  */

  public class MusicBrowserTcpProxy extends Object implements MediaLibrary {

     private static final boolean debugOn = true;
     private static final int buffSize = 32768;
     private static int id = 0;
     private String host;
     private int port;

     public MusicBrowserTcpProxy (String host, int port){
        this.host = host;
        this.port = port;
     }

     private void debug(String message) {
        if (debugOn)
           System.out.println("debug: "+message);
     }

     public String callMethod(String method, Object[] params){
        JSONObject theCall = new JSONObject();
        String ret = "{}";
        try{
           debug("Request is: "+theCall.toString());
           theCall.put("method",method);
           theCall.put("id",id);
           theCall.put("jsonrpc","2.0");
           ArrayList<Object> al = new ArrayList();
           for (int i=0; i<params.length; i++){
              al.add(params[i]);
           }
           JSONArray paramsJson = new JSONArray(al);
           theCall.put("params",paramsJson);
           Socket sock = new Socket(host,port);
           OutputStream os = sock.getOutputStream();
           InputStream is = sock.getInputStream();
           int numBytesReceived;
           int bufLen = 32768;//was 1024
           String strToSend = theCall.toString();
           byte bytesReceived[] = new byte[buffSize];
           byte bytesToSend[] = strToSend.getBytes();
           os.write(bytesToSend,0,bytesToSend.length);
           numBytesReceived = is.read(bytesReceived,0,bufLen);
           ret = new String(bytesReceived,0,numBytesReceived);
           debug("callMethod received from server: "+ret);
           os.close();
           is.close();
           sock.close();
        }catch(Exception ex){
           System.out.println("exception in callMethod: "+ex.getMessage());
        }
        return ret;
     }
     //add a Album
     public boolean addAlbum(MusicAlbum aAlbum) {
        boolean ret = false;
        String result = callMethod("addAlbum", new Object[]{aAlbum.toJson()});
        JSONObject res = new JSONObject(result);
        ret = res.optBoolean("result",false);
        return ret;
     }

     public boolean addTrack(Tracks aTrack) {
        boolean ret = false;
        String result = callMethod("addTrack", new Object[]{aTrack.toJson()});
        JSONObject res = new JSONObject(result);
        ret = res.optBoolean("result",false);
        return ret;
     }

     public boolean removeTrack(String mediaTitle) {
        boolean ret = false;
        String result = callMethod("removeTrack", new Object[]{mediaTitle});
        JSONObject res = new JSONObject(result);
        ret = res.optBoolean("result",false);
        return ret;
     }

     public boolean removeAlbum(String mediaTitle) {
        boolean ret = false;
        String result = callMethod("removeAlbum", new Object[]{mediaTitle});
        JSONObject res = new JSONObject(result);
        ret = res.optBoolean("result",false);
        return ret;
     }

     public MusicAlbum findAlbum(String aTitle) {
        MusicAlbum ret = new MusicAlbum();
        String result = callMethod("findAlbum", new Object[]{aTitle});
        JSONObject res = new JSONObject(result);
        JSONObject albumJson = res.optJSONObject("result");
        ret = new MusicAlbum(albumJson);
        return ret;
     }

     public Tracks findTrack(String aTitle) {
        Tracks ret = new Tracks();
        String result = callMethod("findTrack", new Object[]{aTitle});
        JSONObject res = new JSONObject(result);
        JSONObject trackJson = res.optJSONObject("result");
        ret = new Tracks(trackJson);
        return ret;
     }

     public boolean save() {
        boolean ret = false;
        String result = callMethod("save", new Object[]{});
        JSONObject res = new JSONObject(result);
        ret = res.optBoolean("result", false);
        return ret;
     }

     public boolean restore() {
        boolean ret = false;
        String result = callMethod("restore", new Object[]{});
        JSONObject res = new JSONObject(result);
        ret = res.optBoolean("result", false);
        return ret;
     }

     public String[] getTitles() {
        String[] ret = new String[]{};
        String result = callMethod("getTitles", new Object[0]);
        debug("result of getTitles is: "+result);
        JSONObject res = new JSONObject(result);
        JSONArray titlesJson = res.optJSONArray("result");
        ret = new String[titlesJson.length()];
        for (int i=0; i<titlesJson.length(); i++){
           ret[i] = titlesJson.optString(i,"unknown");
        }
        return ret;
     }
  }
