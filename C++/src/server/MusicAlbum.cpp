#include "MusicAlbum.hpp"
#include <iostream>
#include <stdlib.h>
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
 * Purpose: MediaDescription is a class whose properties describe a single
 * media work -- song or video/clip.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version April 2020
 */
MusicAlbum::MusicAlbum(){
   vector<Track> track;
   groupName = "";
   albumName = "";
   genre = "";
   summary = "";
   xlImg = "";
   duration = "";
}

MusicAlbum::MusicAlbum(string aGroupName, string aAlbumName,string aGenre,
                           string aSummary, string aXlImg, string aDuration) {
   vector<Track> track;
   groupName = aGroupName;
   albumName = aAlbumName;
   genre = aGenre;
   summary = aSummary;
   xlImg = aXlImg;
   duration = aDuration;
}

MusicAlbum::MusicAlbum(const Json::Value& jsonObj){
   Json::Value::Members mbr = jsonObj.getMemberNames();
   for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
      //cout << "Json by value" << " " << endl;

      Json::Value jsonM = jsonObj[*i];
      if(*i=="albumName"){
         albumName = jsonM.asString();
      }else if(*i=="groupName"){
         groupName = jsonM.asString();
      }else if(*i=="xlImgUrl"){
         xlImg = jsonM.asString();
      }else if(*i=="summary"){
         summary = jsonM.asString();
      }else if(*i=="duration"){
         duration = jsonM.asString();
      }else if(*i=="genre"){
         genre = jsonM.asString();
      }else if(*i=="tracks"){
          //cout << jsonObj << endl;
          const Json::Value& trk = jsonObj["tracks"];
          for(int j = 0; j < trk.size(); j++){
            track.push_back(Track(trk[j]["album"].asString(), trk[j]["rankOrder"].asString(),
                                    trk[j]["artist"].asString(), trk[j]["track"].asString(),
                                    trk[j]["duration"].asString()));
            //cout << trk[j]["album"].asString()<< " ,";
            //cout << trk[j]["artist"].asString()<< " ,";
            //[cout << trk[j]["duration"].asString()<< " ,";
            //cout << trk[j]["rankOrder"].asString()<< " ,";
            //cout << trk[j]["track"].asString()<< endl;
          }
//"album" : "Believe",
//"artist" : "Cher",
//"duration" : "239",
//"rankOrder" : "1",
//"track" : "Believe"
      }
    }
}

MusicAlbum::MusicAlbum(string jsonString){
   Json::Reader reader;
   Json::Value root;
   bool parseSuccess = reader.parse(jsonString,root,false);
   if(parseSuccess){
      cout << "successful parse" << endl;
      Json::Value::Members mbr = root.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
         //cout << *i << " " << endl;
         Json::Value jsonM = root[*i];
         if(*i=="albumName"){
            albumName = jsonM.asString();
         }else if(*i=="groupName"){
            groupName = jsonM.asString();
         }else if(*i=="xlImgUrl"){
            albumName = jsonM.asString();
         }else if(*i=="summary"){
            summary = jsonM.asString();
         }else if(*i=="duration"){
            duration = jsonM.asString();
         }else if(*i=="genre"){
            genre = jsonM.asString();
         }
      }
   }else{
      cout << "Album constructor parse error with input: " << jsonString
           << endl;
   }
}

MusicAlbum::~MusicAlbum() {
   //cout << "MediaDescription destructor.\n";
   track.clear();
   groupName = "";
   albumName = "";
   genre = "";
   summary = "";
   xlImg = "";
   duration = "";
}

string MusicAlbum::toJsonString(){
   string ret = "{}";
   Json::Value jsonLib;
   jsonLib["albumName"] = albumName;
   jsonLib["groupName"] = groupName;
   jsonLib["xlImgUrl"] = xlImg;
   jsonLib["genre"] = genre;
   jsonLib["summary"] = summary;
   jsonLib["duration"] = duration;
   ret = jsonLib.toStyledString();
   return ret;
}

Json::Value MusicAlbum::toJson(){
   //string ret = "{}";
   Json::Value jsonLib;
   Json::Value trkA;
   jsonLib["albumName"] = albumName;
   jsonLib["groupName"] = groupName;
   jsonLib["xlImgUrl"] = xlImg;
   jsonLib["genre"] = genre;
   jsonLib["summary"] = summary;
   jsonLib["duration"] = duration;
   for(auto tracks : track){
     Json::Value trk;
     trk["album"] = tracks.albumName;
     trk["rankOrder"] = tracks.rankOrder;
     trk["artist"] = tracks.artistGroup;
     trk["track"] = tracks.trackName;
     trk["duration"] = tracks.duration;
     trkA.append(Json::Value(trk));
   }
   jsonLib["tracks"] = trkA;
   return jsonLib;
}

void MusicAlbum::setValues(string aGroupName, string aAlbumName,string aGenre,
                           string aSummary, string aXlImg, string aDuration) {
   groupName = aGroupName;
   albumName = aAlbumName;
   genre = aGenre;
   summary = aSummary;
   xlImg = aXlImg;
   duration = aDuration;
}

void MusicAlbum::print(){
   cout << "duration " << duration << " author " << groupName << " album " << albumName
        << " genre " << genre << " summary " << summary << "\n";
}

void MusicAlbum::Add(string aAlbumName, string aRankOrder,string aArtistGroup,
                                       string aTrackName, string aDuration){
   track.push_back(Track(aAlbumName, aRankOrder, aArtistGroup, aTrackName, aDuration));
}

void MusicAlbum::Remove(string aTrackName){
  cout << "Removing: " << aTrackName << " From: " << albumName <<endl;
    for(int i = 0; i < track.size(); i++){
      if(track.at(i).trackName == aTrackName){
        cout << "Removing Meow " << track.at(i).trackName << endl;
        track.erase(track.begin() + i);
      }
    }
}

int MusicAlbum::getTrackIndex(string aTrackName){
  int count = 0;
  for(auto tracks : track){
    if(tracks.trackName == aTrackName){
      return count;
    }
    count++;
  }
  return -1;
}
