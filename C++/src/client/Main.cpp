#include "MediaClientGui.cpp"
#include "../server/MusicLibrary.hpp"
#include "../server/Track.hpp"
#include "../server/MusicAlbum.hpp"
#include "MusicCollectionStub.h"

#include <FL/Fl.H>
#include <FL/Fl_Window.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Output.H>
#include <FL/Fl_Multiline_Input.H>
#include <FL/Fl_Check_Browser.H>
#include <stdio.h>
#include <iostream>
#include <chrono>
#include <ctime>
#include <stdlib.h>
#include <sstream>
#include <thread>
#include <json/json.h>

#include <curlpp/cURLpp.hpp>
#include <curl/curl.h>
#include <curlpp/Options.hpp>
#include <curlpp/Easy.hpp>
#include <curlpp/Exception.hpp>
#include <jsonrpccpp/client/connectors/httpclient.h>

using namespace std;
using namespace jsonrpc;

std::string cmd;
void run(){
   system(cmd.c_str());
}

/**
 * Copyright (c) 2020 Thor Nielsen,
 * Software Engineering,
 * Arizona State University at the Polytechnic campus
 * <p/>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation version 2
 * of the License.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but without any warranty or fitness for a particular purpose.
 * <p/>
 * Please review the GNU General Public License at:
 * http://www.gnu.org/licenses/gpl-2.0.html
 * see also: https://www.gnu.org/licenses/gpl-faq.html
 * so you are aware of the terms and your rights with regard to this software.
 * Or, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,USA
 * <p/>
 * Purpose: C++ FLTK client UI for Music management.
 * This class extends the Gui component class MediaClientGui and demonstrates
 * sample control functions that respond to button clicks and menu item selects.
 * This software is meant to run on Linux and MacOS using g++.
 * Students may use this source code as the basis for their solution to
 * the programming projects in Ser321 to create a distributed music browser.
 * <p/>
 * This example depends on the library curlpp to download a file or make
 * a URL request. This should be used in problem solutions to search
 * last.fm for album and track information. Download the library from:
 *   http://www.curlpp.org/
 * Extract the archive and from a terminal change to the directory created.
 * Execute the following commands looking for errors. Note the following steps
 * require that libcurl first be installed, if its not already present.
 *   cmake ./
 *   make
 *   sudo make install
 *   sudo ldconfig
 * To see the make options available do: make help
 * you may want to note the files that are installed by make install since there
 * does not appear to be any "uninstall" target in the makefile.
 *
 * To use this library, the compile and link must reference the appropriate
 * libraries:
 * g++ -std=c++14 MusicBrowser.cpp -o exampleCurlPP -lstdc++ -ljsoncpp -lcurlpp -lcurl
 * If the includes and libraries are not installed in a location known to g++, then
 * the command will need -I and -L options which point g++ to the include and
 * library files respectively.
 *
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @file    MusicMain.cpp
 * @date    April, 2020
 **/
class MediaClient : public MediaClientGui {

public:

   std::string userId;
   std::string lastfmkey;
   std::thread * playThread;
   MusicLibrary * library;
   //string host;
   string hostId;

/** ClickedX is one of the callbacks for GUI controls.
    * Callbacks need to be static functions. But, static functions
    * cannot directly access instance data. This program uses "userdata"
    * to get around that by passing the instance to the callback
    * function. The callback then accesses whatever GUI control object
    * that it needs for implementing its functionality.
    */
   static void ClickedX(Fl_Widget * w, void * userdata) {
      std::cout << "You clicked Exit" << std::endl;
      exit(1);
   }

   /**
    * Static search button callback method.
    */
   static void SearchCallbackS(Fl_Widget*w, void*data) {
      MediaClient *o = (MediaClient*)data;
      cout << "Search Clicked. You asked for a last.fm search of album: " <<
         o->albSrchInput->value() << " by artist: " <<
         o->artSrchInput->value() << endl;
      try{
         std::string url = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&artist=Cher&album=The+Very+Best+Of+Cher&format=json&api_key=";
         url = url + o->lastfmkey;
         cout << "sending request url: " << url << endl;
         std::ostringstream os;
         curlpp::Easy myRequest;
         myRequest.setOpt(new curlpp::options::WriteStream(&os));
         //curlpp::options::Url myUrl(std::string(url));
         myRequest.setOpt(new curlpp::options::Url(url.c_str()));
         myRequest.perform();
         std::string aString = os.str();
         std::cout << aString << std::endl;

      }catch ( curlpp::LogicError & e ) {
         std::cout << e.what() << std::endl;
      }
      catch ( curlpp::RuntimeError & e ) {
         std::cout << e.what() << std::endl;
      }
   }

   // Static menu callback method
   static void TreeCallbackS(Fl_Widget*w, void*data) {
      MediaClient *o = (MediaClient*)data;
      o->TreeCallback(); //call the instance callback method
   }

   /**
    * TreeCallback is a callback for tree selections, deselections, expand or
    * collapse.
    */
   void TreeCallback() {
     HttpClient httpclient(hostId.c_str());
     //library = new mediaclientstub(httpclient);
      //HttpClient httpclient("http://127.0.0.1:8080");
      MusicCollectionStub sc(httpclient);
      // Find item that was clicked
      Fl_Tree_Item *item = (Fl_Tree_Item*)tree->item_clicked();
      cout << "Tree callback. Item selected: ";
      if ( item ) {
         cout << item->label();
      } else {
         cout << "none";
      }
      cout << endl;
      std::string aStr("unknown");
      std::string aTitle(item->label());
      switch ( tree->callback_reason() ) {  // reason callback was invoked
      case       FL_TREE_REASON_NONE: {aStr = "none"; break;}
      case     FL_TREE_REASON_OPENED: {aStr = "opened";break;}
      case     FL_TREE_REASON_CLOSED: {aStr = "closed"; break;}
      case   FL_TREE_REASON_SELECTED: {
         aStr = "selected";
         //MusicAlbum md;
         //if(library){
        //    cout << "trying to get: " << item->label() << endl;
        //    md = library->getAlbum(aTitle);
        //    cout << md.albumName << endl;
        // }else{
        //    cout << "library entry not found" << endl;
        //    break;
        // }
        //HttpClient httpclient("http://127.0.0.1:8080");
        //MusicCollectionStub sc(httpclient);
        Json::Value jsonTrack;
        Json::Value jsonAlbum;
        try{
          //cout << "IN TREE CALL BACK" << endl;
          jsonTrack = sc.getTrack(aTitle);
          jsonAlbum = sc.getAlbum(aTitle);
          //cout << "IN TREE CALL BACK - jsonloaded" << endl;
          //cout << jsonTrack << endl;
        }catch(exception& e){
          cout << e.what() << endl;
          return;
        }

         //cout << "media: "<< md.summary << " " << md.albumName << " "
          //    << md.groupName << " " << md.genre << " " << md.xlImg
            //  << endl;
              if(jsonTrack["album"].asString() != "jsonNullValue"){
                trackInput->value(jsonTrack["track"].asString().c_str());
                timeInput->value(jsonTrack["duration"].asString().c_str());
                rankInput->value(jsonTrack["rankOrder"].asString().c_str());
                authorInput->value(jsonTrack["artist"].asString().c_str());
                albumInput->value(jsonTrack["album"].asString().c_str());
                //genreChoice->value(md.genre.c_str());  need to be a array
              }else if(jsonTrack["albumName"].asString()  != "jsonNullValue"){
                /*try{
                  jsonAlbum = sc.getAlbum(aTitle);
                  cout << "IN TREE CALL BACK" << endl;
                  cout << jsonTrack << endl;
                }catch(exception& e){
                  cout << e.what() << endl;
                  return;
                }*/
                 summaryMLI->value(jsonAlbum["summary"].asString().c_str());
                 trackInput->value("");
                 albumInput->value(jsonAlbum["albumName"].asString().c_str());
                 authorInput->value(jsonAlbum["groupName"].asString().c_str());
                 timeInput->value(jsonAlbum["duration"].asString().c_str());
                 rankInput->value("");
                 //genreChoice->value(md.genre.c_str());  need to be a array
                 //genreChoice->value(md.typeExt.c_str()=="mp3"?0:1);
              }
         break;
      }
      case FL_TREE_REASON_DESELECTED: {aStr = "deselected"; break;}
      default: {break;}
      }
      cout << "Callback reason: " << aStr.c_str() << endl;
   }

   // Static menu callback method
   static void Menu_ClickedS(Fl_Widget*w, void*data) {
      MediaClient *o = (MediaClient*)data;
      o->Menu_Clicked(); //call the instance callback method
   }

   // Menu selection instance method that has ccess to instance vars.
   void Menu_Clicked() {
      //HttpClient httpclient("http://127.0.0.1:8080");
      HttpClient httpclient(hostId.c_str());
      MusicCollectionStub sc(httpclient);

      char picked[80];
      menubar->item_pathname(picked, sizeof(picked)-1);
      string selectPath(picked);
      cout << "Selected Menu Path: " << selectPath << endl;
      int select = genreChoice->value();
      cout << "Selected genre: " << ((select==0)?"rock":"blues") << endl;
      // Handle menu selections
      if(selectPath.compare("File/Save")==0){
         sc.save("albums.json");
      }else if(selectPath.compare("File/Restore")==0){
         sc.restore();
         buildTree();
      }else if(selectPath.compare("File/Tree Refresh")==0){
         buildTree();
      }else if(selectPath.compare("File/Exit")==0){
         if(playThread && playThread->joinable()){
            playThread->join();
         }
         exit(0);
      }else if(selectPath.compare("Album/Add")==0){
         cout << "Reading Album input: " << albumInput->value()  << endl;

          Json::Value aAlbum;
          aAlbum["albumName"] = albumInput->value();
          aAlbum["groupName"] = authorInput->value();
          aAlbum["xlImgUrl"] = "xlImg";
          aAlbum["genre"] = "ROCK";
          aAlbum["summary"] = summaryMLI->value();
          aAlbum["duration"] = timeInput->value();
          sc.addAlbum(aAlbum);
          buildTree();

      }else if(selectPath.compare("Album/Remove")==0){
         Fl_Tree_Item *item = (Fl_Tree_Item*)tree->item_clicked();
         std::string aTitle(item->label());
         sc.removeAlbum(aTitle);
         buildTree();
      }else if(selectPath.compare("Track/Add")==0){

         Json::Value aTrack;
         aTrack["album"] = albumInput->value();
         aTrack["rankOrder"] = rankInput->value();
         aTrack["artist"] = authorInput->value();
         aTrack["track"] = trackInput->value();
         aTrack["duration"] = timeInput->value();
         sc.addTrack(aTrack);
         buildTree();

      }else if(selectPath.compare("Track/Remove")==0){
         Fl_Tree_Item *item = (Fl_Tree_Item*)tree->item_clicked();
         std::string aTitle(item->label());
         sc.removeTrack(aTitle);
         buildTree();
      }else if(selectPath.compare("Track/Play")==0){
         std::string unameres = exec("uname");
         std::string pwdPath = exec("pwd");
         pwdPath = pwdPath.substr(0,pwdPath.length()-1);
         std::cout << "OS type is: " << unameres << " curr.dir is: "
                   << pwdPath << std::endl;
         // This path is only valid on linux so we will have to check ostype
         std::stringstream streamLinux;
         streamLinux << "/usr/bin/vlc "
                     << pwdPath << "/MediaFiles/" << trackInput->value()
                     << ".mp3" ;
         std::string aStr("Linux");
         std::stringstream streamMac;
         streamMac << "/Applications/VLC.app/Contents/MacOS/VLC "
                   << pwdPath << "/MediaFiles/" << trackInput->value()
                   << ".mp3" ;
         cout << "mac command: " << streamMac.str() << endl;
         cout << "linux command: " << streamLinux.str() << endl;
         // start vlc to play the media file
         //limit the comparison to the length of Linux to remove new line char
         if(unameres.compare(0,aStr.length(),aStr)==0){
            string argLinux(streamLinux.str());
            cmd = argLinux;
            playThread = new std::thread(run);
         }else{
            string arg(streamMac.str());
            cmd = arg;
            playThread = new std::thread(run);
         }
      }
   }

   /**
    * a static method to remove spaces, tabs, new lines and returns from the
    * begining or end of a string.
    */
   static std::string& trimMe (std::string& str) {
      // right trim
      while (str.length() > 0 && (str[str.length()-1] == ' '  ||
                                  str[str.length()-1] == '\t' ||
                                  str[str.length()-1] == '\n' ||
                                  str[str.length()-1] == '\r')){
         str.erase(str.length ()-1, 1);
      }
      // left trim
      while (str.length () > 0 && (str[0] == ' ' || str[0] == '\t')){
         str.erase(0, 1);
      }
      return str;
   }

   /**
    * a method to execute a command line command and to return
    * the resulting string.
    */
   std::string exec(const char* cmd) {
      FILE* pipe = popen(cmd, "r");
      if (!pipe) return "ERROR";
      char buffer[128];
      std::string result = "";
      while(!feof(pipe)) {
         if(fgets(buffer, 128, pipe) != NULL)
            result += buffer;
      }
      pclose(pipe);
      return result;
   }

   void buildTree(){
      //HttpClient httpclient("http://127.0.0.1:8080");
      HttpClient httpclient(hostId.c_str());
      MusicCollectionStub sc(httpclient);
      Json::Value jsonLibrary;
      Json::Value jsonTrack;

      cout << "server has titles " << hostId <<endl;

      tree->clear();
      try{
        jsonLibrary = sc.getLibrary();
        cout << "IN BUILD TREE" << endl;
      }catch(exception& e){
        cout << "IN TREE FAIL" << endl;
        cout << sc.getLibrary() << endl;
        cout << "sc^" << endl;
        cout << e.what() << endl;
        return;
      }
      //cout << jsonLibrary << endl;
      Json::Value::Members mbr = jsonLibrary.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
        cout << *i << endl;
        jsonTrack = jsonLibrary[*i];
        cout << "IN BUILD TREE - TRACK" << endl;
        cout << jsonTrack << endl;
         cout << " " << jsonLibrary[*i] << endl;
         //MusicAlbum md = library->get(result[i]);
         //cout  << md.albumName << " " << md.groupName
              //<< " " << md.genre << endl;
              if(jsonTrack[0] == "jsonNullValue"){
                std::stringstream ablStream;
                ablStream << "Music"
                             << "/"
                             << *i;
                tree->add(ablStream.str().c_str()); // adds songs
              }else{
         for(int j =0; j < jsonTrack.size(); j++){
           std::stringstream stream;
           stream << "Music"
                        << "/"
                        << *i
                        << "/" << jsonTrack[j].asString();
           tree->add(stream.str().c_str()); // adds songs
         }
      }}
      cout << endl;
      tree->redraw();
   }
   //MediaClient(const char * name = "Thor", const char * key = "myKey",
            //    const char * host = "http://127.0.0.1:8080") : MediaClientGui(name){
   MediaClient(const char * name, const char * key,
                const char * host) : MediaClientGui(name) {
      searchButt->callback(SearchCallbackS, (void*)this);
      menubar->callback(Menu_ClickedS, (void*)this);
      tree->callback(TreeCallbackS, (void*)this);
      callback(ClickedX);
      lastfmkey = key;
      userId = "Thor.Nielsen";
      hostId = host;
      //library = new MusicLibrary();
      buildTree();
   }
};

int main(int argc, char * argv[]) {
   //string host = "http://127.0.0.1:8080";
   std::string hostName = (argc>1)?argv[1]:"127.0.0.1";
   std::string portId = (argc>2)?argv[2]:"8080";
   std::string developer = (argc>3)?argv[3]:"Thor.Nielsen";
   std::string lastfmkey = (argc>4)?argv[4]:"lastfmkey";
   std::string windowTitle = developer + "'s Music Browser";
   std::string host = "http://" + hostName + ":"+portId;
   cout << host << endl;
   cout << "This is you host ID ^" << endl;
   //MediaClient cm(windowTitle.c_str(),lastfmkey.c_str());
   MediaClient cm(windowTitle.c_str(),lastfmkey.c_str(), host.c_str());
   return (Fl::run());
}
