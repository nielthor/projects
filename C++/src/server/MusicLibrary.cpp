#include "MusicLibrary.hpp"
#include <iostream>
#include <stdlib.h>
#include <cmath>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;
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
 * Purpose: MediaLibrary is a class defining the interface between clients
 * and the server. The server implementation of MediaLibrary
 * provides storage for description of multiple media works -- song or video/clip.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version April 2020
 */

MusicLibrary::MusicLibrary(){
   initLibraryFromJsonFile("albums.json");
}

MusicLibrary::~MusicLibrary() {
   //cout << "MediaLibrary destructor.\n";
   media.clear();
}

bool MusicLibrary::initLibraryFromJsonFile(string jsonFileName){
   bool ret = false;
   Json::Reader reader;
   Json::Value root;
   std::ifstream json(jsonFileName.c_str(), std::ifstream::binary);
   bool parseSuccess = reader.parse(json,root,false);
   cout << "Media library parseSuccess: " << parseSuccess << endl;
   if(parseSuccess){
      Json::Value::Members mbr = root.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
         //cout << *i << " " << endl;
         Json::Value jsonMedia = root[*i];
         MusicAlbum * aDesc = new MusicAlbum(jsonMedia);
         media[*i] = *aDesc;
         cout << "adding ";
         aDesc->print();
      }
      ret = true;
   }
   return ret;
}

bool MusicLibrary::toJsonFile(string jsonFileName){
   bool ret = false;
   Json::Value jsonLib;
   for(std::map<string,MusicAlbum>::iterator i = media.begin();
                                                         i!= media.end(); i++){
      string key = i->first;
      cout << key << " " << endl;
      MusicAlbum aMedia = media[key];
      Json::Value jsonMedia = aMedia.toJson();
      jsonLib[key] = jsonMedia;

   }
   Json::StyledStreamWriter ssw("  ");
   std::ofstream jsonOutFile(jsonFileName.c_str(), std::ofstream::binary);
   ssw.write(jsonOutFile, jsonLib);
   return true;
}

MusicAlbum MusicLibrary::get(string aAlbumName){
   //cout << "get: " << aTitle << endl;
   MusicAlbum aMedia = media[aAlbumName];
   return aMedia;
}

vector<string> MusicLibrary::getTitles(){
   //cout << "getTitles: " << endl;
   vector<string> myVec;
   for(map<string,MusicAlbum>::iterator it = media.begin();
                                              it != media.end();++it){
      myVec.push_back(it->first);
      //cout << it->first << "\n";
   }
   return myVec;
}

MusicAlbum MusicLibrary::getAlbum(string aTrackName){

  for(const auto &album : media){
    for(auto tracks : album.second.track){
      if(tracks.trackName == aTrackName || tracks.albumName == aTrackName){
        return album.second;
      }
    }
    if(album.first == aTrackName){
      return album.second;
    }
  }
  MusicAlbum *tmp = new MusicAlbum();
  return *tmp;
}
bool MusicLibrary::removeTrack(string aTrackName){
  for(auto &album : media){
    for(auto tracks : album.second.track){
      if(tracks.trackName == aTrackName){
        album.second.Remove(aTrackName);
      }
    }
  }
  return true;
}
bool MusicLibrary::removeAlbum(string aAlbumName){
  media.erase(aAlbumName);
  return true;
}
bool MusicLibrary::Restore(){
  media.clear();
  initLibraryFromJsonFile("albums.json");
  return true;
}
void MusicLibrary::addAlbum(string aGroupName, string aAlbumName,string aGenre,
                              string aSummary, string aXlImg, string aDuration){
  //start
  media.insert(std::pair<std::string, MusicAlbum>
    (aAlbumName, MusicAlbum(aGroupName, aAlbumName, aGenre, aSummary, aXlImg, aDuration)));
}
void MusicLibrary::addTrack(string aAlbumName, string aRankOrder,string aArtistGroup,
                                    string aTrackName, string aDuration){
  //start
  for(auto &album : media){
      if(album.first == aAlbumName){
        album.second.Add(aAlbumName, aRankOrder,aArtistGroup,aTrackName, aDuration);
      }
  }
}
void MusicLibrary::save(){
  for(auto &album : media){
    //cout << album.second.toJsonString() << endl;
  }
}
//assign5 addtions
Json::Value MusicLibrary::getLibrary(){
  Json::Value jsonLib;
  for(const auto &album : media){
    if(album.second.track.size() == 0){
      jsonLib[album.first].append("jsonNullValue");
    }
    for(auto tracks : album.second.track){
      jsonLib[tracks.albumName].append(tracks.trackName);
    }
  }
  //cout << jsonLib << endl;
  return jsonLib;
}
Json::Value MusicLibrary::getAlbumJson(string aTitle){
  Json::Value jsonLib;
  //cout << aTitle << " IN getAlbumJson" << endl;
for(auto album : media){
    if(album.first == aTitle){

        jsonLib["albumName"] = album.second.albumName;
        jsonLib["groupName"] = album.second.groupName;
        jsonLib["xlImgUrl"] = album.second.xlImg;
        jsonLib["genre"] = album.second.genre;
        jsonLib["summary"] = album.second.summary;
        jsonLib["duration"] = album.second.duration;
        return jsonLib;
    }
}
jsonLib["albumName"] = "jsonNullValue";
return jsonLib;

}
Json::Value MusicLibrary::getTrackJson(string aTitle){
  Json::Value jsonLib;
  for(const auto &album : media){
    for(auto tracks : album.second.track){
      if(tracks.trackName == aTitle){
        jsonLib["album"] = tracks.albumName;
        jsonLib["rankOrder"] = tracks.rankOrder;
        jsonLib["artist"] = tracks.artistGroup;
        jsonLib["track"] = tracks.trackName;
        jsonLib["duration"] = tracks.duration;
        return jsonLib;
      }
    }
  }
  jsonLib["album"] = "jsonNullValue";
  return jsonLib;
}
bool MusicLibrary::addAlbum(Json::Value album){
  string aGroupName = album["groupName"].asString();
  string aAlbumName = album["albumName"].asString();
  string aGenre = album["genre"].asString();
  string aSummary = album["summary"].asString();
  string aXlImg = album["xlImgUrl"].asString();
  string aDuration = album["duration"].asString();

  media.insert(std::pair<std::string, MusicAlbum>
    (aAlbumName, MusicAlbum(aGroupName, aAlbumName, aGenre, aSummary, aXlImg, aDuration)));
  //cout << aGroupName << " " << aAlbumName << " " <<  aGenre << " " <<  aSummary << " " <<  aXlImg << " " <<  aDuration << endl;
  return true;
}

bool MusicLibrary::addTrack(Json::Value track){
  for(auto &album : media){
      string aAlbumName = track["album"].asString();
      string aArtistGroup = track["artist"].asString();
      string aDuration = track["duration"].asString();
      string aRankOrder = track["rankOrder"].asString();
      string aTrackName = track["track"].asString();

      if(album.first == aAlbumName){
        album.second.Add(aAlbumName, aRankOrder,aArtistGroup,aTrackName, aDuration);
        return true;
      }
    }
    return false;
  }
