package ser321.assign3.tnielse3.client;

import ser321.assign2.lindquis.MediaLibraryGui;
import ser321.assign3.tnielse3.server.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import javax.sound.sampled.*;
import java.beans.*;
import java.net.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.lang.Runtime;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLConnection;
import java.time.Duration;
import java.io.FileWriter;
import java.rmi.*;

/**
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
 * Purpose: MusicBrowser instructor sample for solving Spring 2020 ser321 assignments.
 * This problem provides for browsing and managing information about
 * music albums. It uses a Swing JTree, and JTextField controls to
 * realize a GUI with a split pane. The left pane contains an expandable
 * JTree of the media library.
 * This program is a sample controller for the non-distributed version of
 * the system.
 * The right pane contains components that allow viewing, modifying and adding
 * albums, tracks, and corresponding files.
 * 9c5688b82801b6ee95720b7239ce5106
 *
 * @author Thor Nielsen
 * @version February 2020
 */
public class MusicBrowser extends MediaLibraryGui implements
                                                       TreeWillExpandListener,
     					               ActionListener,
					               TreeSelectionListener {

   private static final String pre = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&artist=";
   private static final boolean debugOn = true;
   private String url;
   private boolean stopPlaying;
   private boolean inSearch;
   private MediaLibrary library;
   private String lastFMKey;
   private MusicAlbum searchAlbum;
   private MediaLibrary server;

   public MusicBrowser(String author, String authorKey, String hostID, String regPort) {
      super(author);
      this.lastFMKey = authorKey;
      inSearch = false;

      stopPlaying = false;
      try{
        //library = new MediaLibrary();
        //MediaLibraryImpl server;
        System.out.println("Server Looking up: rmi://" + hostID + ":" + regPort + "/MediaLibraryServer");
        server = (MediaLibrary)Naming.lookup("rmi://" + hostID + ":" + regPort +"/MediaLibraryServer");

      }catch(Exception e){
        e.printStackTrace();

      }
      // register this object as an action listener for menu item clicks. This will cause
      // my actionPerformed method to be called every time the user selects a menuitem.
      for(int i=0; i<userMenuItems.length; i++){
         for(int j=0; j<userMenuItems[i].length; j++){
            userMenuItems[i][j].addActionListener(this);
         }
      }
      // register this object as an action listener for the Search button. This will cause
      // my actionPerformed method to be called every time the user clicks the Search button
      searchJButt.addActionListener(this);
      try{
         //tree.addTreeWillExpandListener(this);  // add if you want to get called with expansion/contract
         tree.addTreeSelectionListener(this);
         rebuildTree();
      }catch (Exception ex){
         JOptionPane.showMessageDialog(this,"Handling "+
                                       " constructor exception: " + ex.getMessage());
      }

      setVisible(true);
   }

   /**
    * A method to facilite printing debugging messages during development, but which can be
    * turned off as desired.
    *
    **/

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   /**
    * Create and initialize nodes in the JTree of the left pane.
    * buildInitialTree is called by MediaLibraryGui to initialize the JTree.
    * Classes that extend MediaLibraryGui should override this method to
    * perform initialization actions specific to the extended class.
    * The default functionality is to set base as the label of root.
    * In your solution, you will probably want to initialize by deserializing
    * your library and displaying the categories and subcategories in the
    * tree.
    * @param root Is the root node of the tree to be initialized.
    * @param base Is the string that is the root node of the tree.
    */
   public void buildInitialTree(DefaultMutableTreeNode root, String base){
      //set up the context and base name
      try{
         root.setUserObject(base);
      }catch (Exception ex){
         JOptionPane.showMessageDialog(this,"exception initial tree:"+ex);
         ex.printStackTrace();
      }
   }

   /**
    *
    * method to build the JTree of music shown in the left panel of the UI. The
    * field tree is a JTree as defined and initialized by MediaLibraryGui class.
    * it is defined to be protected so it can be accessed by extending classes.
    * This version of the method uses the music library to get the names of
    * tracks. Your solutions will need to replace this structure with one that
    * keeps information particular to both Album and Track (two classes Album.java,
    * and Track.java). Your music library will store and provide access to Album
    * and Track objects.
    * This method is provided to demonstrate one way to add nodes to a JTree based
    * on an underlying storage structure.
    * See also the methods clearTree, valueChanged, and getSubLabelled defined in this class.
    **/
   public void rebuildTree(){
      tree.removeTreeSelectionListener(this);
      //tree.removeTreeWillExpandListener(this);
      DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
      clearTree(root, model);
      DefaultMutableTreeNode musicNode = new DefaultMutableTreeNode("Music");
      model.insertNodeInto(musicNode, root, model.getChildCount(root));

	    // put nodes in the tree for all  registered with the library
      try{
        int listSize;
        if(inSearch){
          listSize = 1;
        }else{
          String[] ls = server.getTitles();
          listSize = ls.length;
        }
        String[] musicList = server.getTitles();
        //for (int i = 0; i<musicList.length; i++){
        for (int i = 0; i<listSize; i++){
          MusicAlbum aMD;
        if(inSearch){
          aMD = searchAlbum;
        }else{
          aMD = server.get(musicList[i]);
        }

         String aMTitle = aMD.albumName;

         debug("Adding song with title:"+aMD.albumName);
         for(int j = 0; j < aMD.tracks.size(); j++){
           aMTitle = aMD.tracks.get(j).trackName;
             System.out.println("HERHEHRE\n" + aMTitle + "HERHERHEHR\n");
           DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(aMTitle);
           DefaultMutableTreeNode subNode = getSubLabelled(musicNode,aMD.albumName);

         if(subNode!=null){ // if album subnode already exists
            debug("album exists: "+aMD.albumName);
            //subNode.add(toAdd);
            model.insertNodeInto(toAdd, subNode,
                                 model.getChildCount(subNode));
         }else{ // album node does not exist
            DefaultMutableTreeNode anAlbumNode =
               new DefaultMutableTreeNode(aMD.albumName);
            debug("no album, so add one with name: "+aMD.albumName);
            //root.add(aCatNode);
            model.insertNodeInto(anAlbumNode, musicNode,
                                 model.getChildCount(musicNode));
            DefaultMutableTreeNode aSubCatNode =
               new DefaultMutableTreeNode("aSubCat");
            debug("adding subcat labelled: "+"aSubCat");
            model.insertNodeInto(toAdd,anAlbumNode,
                                 model.getChildCount(anAlbumNode));
         }
      }
      // expand all the nodes in the JTree
      for(int r =0; r < tree.getRowCount(); r++){
         tree.expandRow(r);
      }
      tree.addTreeSelectionListener(this);
   }
    }catch(Exception e){
      e.printStackTrace();
    }
 }
//--------------------------------------------------------------------------
public void rebuildSearchTree(MusicAlbum aMD){
   tree.removeTreeSelectionListener(this);
   //tree.removeTreeWillExpandListener(this);
   DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
   DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
   clearTree(root, model);
   DefaultMutableTreeNode musicNode = new DefaultMutableTreeNode("Music");
   model.insertNodeInto(musicNode, root, model.getChildCount(root));

      String aMTitle = aMD.albumName;
      debug("Adding song with title:"+aMD.albumName);
      for(int j = 0; j < aMD.tracks.size(); j++){
        aMTitle = aMD.tracks.get(j).trackName;
          System.out.println("HERHEHRE\n" + aMTitle + "HERHERHEHR\n");
        DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(aMTitle);
        DefaultMutableTreeNode subNode = getSubLabelled(musicNode,aMD.albumName);

      if(subNode!=null){ // if album subnode already exists
         debug("album exists: "+aMD.albumName);
         //subNode.add(toAdd);
         model.insertNodeInto(toAdd, subNode,
                              model.getChildCount(subNode));
      }else{ // album node does not exist
         DefaultMutableTreeNode anAlbumNode =
            new DefaultMutableTreeNode(aMD.albumName);
         debug("no album, so add one with name: "+aMD.albumName);
         //root.add(aCatNode);
         model.insertNodeInto(anAlbumNode, musicNode,
                              model.getChildCount(musicNode));
         DefaultMutableTreeNode aSubCatNode =
            new DefaultMutableTreeNode("aSubCat");
         debug("adding subcat labelled: "+"aSubCat");
         model.insertNodeInto(toAdd,anAlbumNode,
                              model.getChildCount(anAlbumNode));
      }
   }
   // expand all the nodes in the JTree
   for(int r =0; r < tree.getRowCount(); r++){
      tree.expandRow(r);
   }
   tree.addTreeSelectionListener(this);
}
//--------------------------------------------------------------------------
   private void clearTree(DefaultMutableTreeNode root, DefaultTreeModel model){
      try{
         DefaultMutableTreeNode next = null;
         int subs = model.getChildCount(root);
         for(int k=subs-1; k>=0; k--){
            next = (DefaultMutableTreeNode)model.getChild(root,k);
            debug("removing node labelled:"+(String)next.getUserObject());
            model.removeNodeFromParent(next);
         }
      }catch (Exception ex) {
         System.out.println("Exception while trying to clear tree:");
         ex.printStackTrace();
      }
   }

   private DefaultMutableTreeNode getSubLabelled(DefaultMutableTreeNode root,
                                                 String label){
      DefaultMutableTreeNode ret = null;
      DefaultMutableTreeNode next = null;
      boolean found = false;
      for(Enumeration<TreeNode> e = root.children();
          e.hasMoreElements();){
         next = (DefaultMutableTreeNode)e.nextElement();
         debug("sub with label: "+(String)next.getUserObject());
         if (((String)next.getUserObject()).equals(label)){
            debug("found sub with label: "+label);
            found = true;
            break;
         }
      }
      if(found)
         ret = next;
      else
         ret = null;
      return (DefaultMutableTreeNode)ret;
   }

   public void treeWillCollapse(TreeExpansionEvent tee) {
      debug("In treeWillCollapse with path: "+tee.getPath());
      tree.setSelectionPath(tee.getPath());
   }

   public void treeWillExpand(TreeExpansionEvent tee) {
      debug("In treeWillExpand with path: "+tee.getPath());
      //DefaultMutableTreeNode dmtn =
      //    (DefaultMutableTreeNode)tee.getPath().getLastPathComponent();
      //System.out.println("will expand node: "+dmtn.getUserObject()+
      //		   " whose path is: "+tee.getPath());
   }

   public void valueChanged(TreeSelectionEvent e) {
      try{
         tree.removeTreeSelectionListener(this);
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            tree.getLastSelectedPathComponent();
         if(node!=null){
            String nodeLabel = (String)node.getUserObject();
            debug("In valueChanged. Selected node labelled: "+nodeLabel);
            MusicAlbum md;
            Tracks tk;
            if(inSearch){
              md = searchAlbum;
              tk = md.getTrack(nodeLabel);
            }else{
              md = server.findAlbum(nodeLabel);
              tk = server.findTrack(nodeLabel);
            }


            if(node.getChildCount()==0 &&
               (node != (DefaultMutableTreeNode)tree.getModel().getRoot())){

               trackJTF.setText(nodeLabel);
               authorJTF.setText(tk.artistGroup);
               albumJTF.setText(tk.albumName);
		           timeJTF.setText(tk.duration);
               rankJTF.setText(tk.rankOrder);
		           setAlbumImage(md.xlImgUrl);
		           summaryJTA.setText(md.summary);
             }
             else{
               trackJTF.setText("");
               rankJTF.setText("");
               authorJTF.setText(md.groupName);
               albumJTF.setText(md.albumName);
               timeJTF.setText(md.runTime);
               setAlbumImage(md.xlImgUrl);
               summaryJTA.setText(md.summary);
             }
          }
      }catch (Exception ex){
         ex.printStackTrace();
      }
      tree.addTreeSelectionListener(this);
   }

   public void actionPerformed(ActionEvent e){
      tree.removeTreeSelectionListener(this);
      try{
      if(e.getActionCommand().equals("Exit")) {
         System.exit(0);
      }else if(e.getActionCommand().equals("Save")) {
         server.save();
      }else if(e.getActionCommand().equals("Restore")) {
        inSearch = false;
        server.restore();
        rebuildTree();
      }else if(e.getActionCommand().equals("AlbumAdd")) {
        server.addAlbum(searchAlbum);
        rebuildTree();
      }else if(e.getActionCommand().equals("TrackAdd")) {
         int typeInd = genreJCB.getSelectedIndex();
         server.addTrack(new Tracks(trackJTF.getText().trim(), authorJTF.getText().trim(),
                            timeJTF.getText().trim(), rankJTF.getText().trim(),
                            albumJTF.getText().trim()));

      }else if(e.getActionCommand().equals("Search")) {
         String searchReqURL = pre+artistSearchJTF.getText().trim()+"&album="+albumSearchJTF.getText().trim()+
                               "&api_key="+lastFMKey+"&format=json";
          searchAlbum = new MusicAlbum(fetchURL(searchReqURL));
	        //library.add(searchAlbum);
          inSearch = true;
	        rebuildTree();

         //System.out.println("calling fetchAsyncURL with url: "+searchReqURL);
         fetchAsyncURL(searchReqURL);
      }else if(e.getActionCommand().equals("Tree Refresh")) {
         inSearch = false;
         rebuildTree();
      }else if(e.getActionCommand().equals("TrackRemove")) {
        if(inSearch){
          searchAlbum.removeTrack(trackJTF.getText().trim());
        }else{
          server.removeTrack(trackJTF.getText().trim());
        }
        rebuildTree();

      }else if(e.getActionCommand().equals("AlbumRemove")) {
        if(inSearch){
          inSearch = false;
        }else{
          server.removeAlbum(albumJTF.getText().trim());
        }
        rebuildTree();

      }else if(e.getActionCommand().equals("AlbumPlay") || e.getActionCommand().equals("TrackPlay")){
         try{
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                            tree.getLastSelectedPathComponent();
            if(node!=null){
               String nodeLabel = (String)node.getUserObject();
               MusicAlbum md = server.get(nodeLabel);
               //String fileName = md.fileName;
               //String path = "file://"+System.getProperty("user.dir")+
               //             "/MediaFiles/" + fileName;
               //this.playMedia(path, md.title);
            }
         }catch(Exception ex){
            System.out.println("Execption trying to play media:");
            ex.printStackTrace();
         }
      }
      tree.addTreeSelectionListener(this);
    }catch(Exception re){
       re.printStackTrace();
    }
   }

   /**
    *
    * A method to do asynchronous url request printing the result to System.out
    * @param aUrl the String indicating the query url for the lastFM api search
    *
    **/
   public void fetchAsyncURL(String aUrl){
      try{
         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(aUrl))
            .timeout(Duration.ofMinutes(1))
            .build();
         client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
      }catch(Exception ex){
         System.out.println("Exception in fetchAsyncUrl request: "+ex.getMessage());
      }
   }

   /**
    *
    * a method to make a web request. Note that this method will block execution
    * for up to 20 seconds while the request is being satisfied. Better to use a
    * non-blocking request.
    * @param aUrl the String indicating the query url for the lastFM api search
    * @return the String result of the http request.
    *
    **/
   public String fetchURL(String aUrl) {
      StringBuilder sb = new StringBuilder();
      URLConnection conn = null;
      InputStreamReader in = null;
      try {
         URL url = new URL(aUrl);
         conn = url.openConnection();
         if (conn != null)
            conn.setReadTimeout(20 * 1000); // timeout in 20 seconds
         if (conn != null && conn.getInputStream() != null) {
            in = new InputStreamReader(conn.getInputStream(),
                                       Charset.defaultCharset());
            BufferedReader br = new BufferedReader(in);
            if (br != null) {
               int ch;
               // read the next character until end of reader
               while ((ch = br.read()) != -1) {
                  sb.append((char)ch);
               }
               br.close();
            }
         }
         in.close();
      } catch (Exception ex) {
         System.out.println("Exception in url request:"+ ex.getMessage());
      }
      return sb.toString();
   }

   public boolean sezToStop(){
      return stopPlaying;
   }

   public static void main(String args[]) {

      String name = "tnielse3";
      String key = "9c5688b82801b6ee95720b7239ce5106";
      String hostId="localhost";
      String regPort="8080";
      if (args.length >= 3){
         //System.out.println("java -cp classes:lib/json.lib ser321.assign2.lindquist."+
         //                   "MediaLibraryApp \"Lindquist Music Library\" lastFM-Key");
         //name = args[0];
         key = args[2];
         hostId = args[0];
         regPort = args[1];

	       System.out.println("this is my key"+key);
      }
      try{
         //System.out.println("calling constructor name "+name);
         MusicBrowser mla = new MusicBrowser(name, key, hostId, regPort);
      }catch (Exception ex){
         ex.printStackTrace();
      }
   }
}
