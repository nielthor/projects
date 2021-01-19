#include "MusicAlbum.hpp"
#include "MusicAlbum.hpp"
#include "Track.hpp"
#include <string>
#include <map>
#include <vector>
#include <json/json.h>

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
 * Purpose: MusicLibrary is a class defining the interface between clients
 * and the server. The server implementation of MusicLibrary
 * provides storage for description of multiple media works -- song or video/clip.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version April 2020
 */
class MusicLibrary {
protected:
   std::map<std::string, MusicAlbum> media;

public:
   MusicLibrary();
   ~MusicLibrary();

   bool initLibraryFromJsonFile(string jsonFileName);
   bool toJsonFile(string jsonFileName);
   string serviceInfo();
   MusicAlbum get(string aAlbumName);
   MusicAlbum getAlbum(string aTrackName);
   bool removeTrack(string aTrackName);
   bool removeAlbum(string aAlbumName);
   bool Restore();
   void addAlbum(string aGroupName, string aAlbumName,string aGenre,
                        string aSummary, string aXlImg, string aDuration);
   void addTrack(string aAlbumName, string aRankOrder,string aArtistGroup,
                                  string aTrackName, string aDuration);
   void save();
   std::vector<string> getTitles();

   Json::Value getLibrary(); //Need to create tree
   Json::Value getAlbumJson(string aTitle);//album Info
   Json::Value getTrackJson(string aTitle);//track info
   bool addAlbum(Json::Value album);//adds album
   bool addTrack(Json::Value track);//adds track
};
