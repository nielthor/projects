package ser321.assign6.tnielse3.server;
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
 * Purpose: MediaLibrary defines the interface for music library operations
 * media work -- song.
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Thor Nielsen
 * @version March 2020
 */
public interface MediaLibrary {
   public boolean addAlbum(MusicAlbum aAlbum);
   public boolean addTrack(Tracks aTrack);
   public boolean removeTrack(String mediaTitle);
   public boolean removeAlbum(String medialTitle);

   public MusicAlbum findAlbum(String aTitle);
   public Tracks findTrack(String aTrack);

   public boolean save();
   public boolean restore();
   public String[] getTitles();
}
