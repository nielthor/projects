#ifndef TRACK_HPP
#define TRACK_HPP
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
 * Purpose: Track is a class whose properties describe a single
 * media song.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version April 2020
 */
class Track {
protected:
public:
   string albumName;
   string rankOrder;
   string artistGroup;
   string trackName;
   string duration;

   Track();
   Track(string aAlbumName, string aRankOrder,string aArtistGroup,
                                          string aTrackName, string aDuration);
   ~Track();
   void setValues(string aAlbumName, string aRankOrder, string aArtistGroup,
                                            string aTrackName, string aDuration);


   void print();
};
#endif
