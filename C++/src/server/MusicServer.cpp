/**
 * Copyright 2020 Thor Nielsen
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
 * <p/>
 * Purpose: C++ class which serves as server for a collection of students. This
 * class is part of a student collection distributed application that uses JsonRPC.
 * Meant to run on OSX, Debian Linux, and Raspberry Pi Debian.
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version April 2020
 */

#include <jsonrpccpp/server.h>
#include <jsonrpccpp/server/connectors/httpserver.h>
#include <json/json.h>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <cstdlib>
#include <csignal>

#include "MusicServerStub.h"
#include "MusicLibrary.hpp"


using namespace jsonrpc;
using namespace std;

class MusicServer : public MusicServerStub {
public:
    MusicServer(AbstractServerConnector &connector, int port);
   virtual std::string serviceInfo();
   virtual bool initLibraryFromFile(); //create library from file
   virtual bool save(const std::string &aFile); //toJsonFile
   virtual bool restore(); //Restore
   virtual Json::Value getLibrary(); //Need to create tree
   virtual Json::Value getAlbum(const std::string &media);//album Info
   virtual Json::Value getTrack(const std::string &media);//track info
   virtual bool addAlbum(const Json::Value& album);//adds album
   virtual bool addTrack(const Json::Value& track);//adds track
   virtual bool removeAlbum(const std::string& aTitle);//remove album
   virtual bool removeTrack(const std::string& aTitle);//remove track

private:

   MusicLibrary* lib;
   int portNum;
};

MusicServer::MusicServer(AbstractServerConnector &connector, int port) : MusicServerStub(connector){
   lib = new MusicLibrary();
   //lib -> initLibraryFromJsonFile("albums.json");
   portNum = port;
   cout << "Port - " << portNum << endl;
}

// Connect
string MusicServer::serviceInfo(){
   std::string msg = "Music collection management service.";
   stringstream ss;
   ss << portNum;
   cout << "serviceInfo called. Returning: Music collection management service."
        << endl;
   return  msg.append(ss.str());
}

bool MusicServer::initLibraryFromFile() {
    cout << "restoring collection from albums.json" << endl;
    return true;
}
// geting library for tree
Json::Value MusicServer::getLibrary(){
    return lib->getLibrary();
}

// get album info
Json::Value MusicServer::getAlbum(const std::string& media) {
    cout << "getting album " << media << endl;
    return lib->getAlbumJson(media);
}

// get track info
Json::Value MusicServer::getTrack(const std::string& media) {
    cout << "getting track " << media << endl;
    return lib->getTrackJson(media);
}

// adding album to library
bool MusicServer::addAlbum(const Json::Value& album) {
    cout << "adding album " << album["album"].asString() << endl;
    return lib->addAlbum(album);
}

// ddding track to library
bool MusicServer::addTrack(const Json::Value& track) {
    cout << "adding track " << track["title"].asString() << endl;
    return lib->addTrack(track);
}

// Remove album
bool MusicServer::removeAlbum(const std::string& aTitle) {
    cout <<"removing album " << aTitle << endl;
    return lib->removeAlbum(aTitle);
}

// Remove Track
bool MusicServer::removeTrack(const std::string& aTitle){
    cout << "removing track " << aTitle << endl;
    return lib->removeTrack(aTitle);
}

// Save Library
bool MusicServer::save(const std::string& aFile){
    cout << "saving collection to albums.json" << endl;
    bool ret = lib->toJsonFile(aFile);
    return ret;
}

// Restore Library
bool MusicServer::restore(){
    cout << "restoring collection from albums.json" << endl;
    bool ret = lib->Restore();
    return ret;
}

void exiting(){
   std::cout << "Server has been terminated. Exiting normally" << endl;
   //ss.StopListening();
}

int main(int argc, char * argv[]) {

   int port = 8080;
   if(argc > 1){
      port = atoi(argv[1]);
      cout << "Server Port -> " << argv[1]<< endl;
   }

   HttpServer httpserver(port);
   MusicServer ss(httpserver, port);

   std::atexit(exiting);
   auto ex = [] (int i) {cout << "server terminating with signal " << i << endl;
                         // ss.StopListening();
                         exit(0);
                         //return 0;
                        };
   // ^C
   std::signal(SIGINT, ex);
   // abort()
   std::signal(SIGABRT, ex);
   // sent by kill command
   std::signal(SIGTERM, ex);
   // ^Z
   std::signal(SIGTSTP, ex);
   cout << "Music collection server listening on port " << port
      //<< " press return/enter to quit." << endl;
        << " use ps to get pid. To quit: kill -9 pid " << endl;
   ss.StartListening();
   while(true){
   }
   //int c = getchar();
   ss.StopListening();
   return 0;
}
