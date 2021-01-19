#include "Track.hpp"
#include <iostream>
#include <stdlib.h>

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
 * Purpose: Track is a class whose properties describe a single
 * media song.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version January 2020
 */
Track::Track(){
  albumName = "";
  rankOrder = "";
  artistGroup = "";
  trackName = "";
  duration = "";
}

Track::Track(string aAlbumName, string aRankOrder,string aArtistGroup,
                                       string aTrackName, string aDuration) {
  albumName = aAlbumName;
  rankOrder = aRankOrder;
  artistGroup = aArtistGroup;
  trackName = aTrackName;
  duration = aDuration;
}

Track::~Track() {
   //cout << "MediaDescription destructor.\n";
   albumName = "";
   rankOrder = "";
   artistGroup = "";
   trackName = "";
   duration = "";
}

void Track::setValues(string aAlbumName, string aRankOrder, string aArtistGroup,
                                         string aTrackName, string aDuration) {
  albumName = aAlbumName;
  rankOrder = aRankOrder;
  artistGroup = aArtistGroup;
  trackName = aTrackName;
  duration = aDuration;
}

void Track::print(){
   cout << "media " << albumName << " author " << artistGroup
        << " Track "<< rankOrder<< ": " << trackName << " duration "
        << duration << "\n";
}
