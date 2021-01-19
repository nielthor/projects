#ifndef MUSICALBUM_H
#define MUSICALBUM_H
#include "Track.hpp"
#include <string>
#include <cmath>
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
 * Purpose: Album is a class whose properties describe a single
 * media work -- song or video/clip.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version April 2020
 */
class MusicAlbum {
protected:
public:
   vector<Track> track;
   string groupName;
   string albumName;
   string genre;
   string summary;
   string xlImg;
   string duration;

   MusicAlbum();
   MusicAlbum(string aGroupName, string aAlbumName,string aGenre,
                              string aSummary, string aXlImg, string aDuration);
   MusicAlbum(const Json::Value& jsonObj);
   MusicAlbum(string jsonString);
   ~MusicAlbum();
   string toJsonString();
   Json::Value toJson();
   void fromJson(Json::Value json);
   void setValues(string aGroupName, string aAlbumName,string aGenre,
                              string aSummary, string aXlImg, string aDuration);
   void print();

   void Add(string aAlbumName, string aRankOrder,string aArtistGroup,
                                          string aTrackName, string aDuration);
   void Remove(string aTrackName);
   int getTrackIndex(string aTrackName);
};
#endif
