package ser321.assign3.tnielse3.server;
import java.rmi.*;
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
public interface MediaLibrary extends Remote{
   public boolean addAlbum(MusicAlbum aAlbum) throws RemoteException;
   public boolean addTrack(Tracks aTrack) throws RemoteException;
   public boolean removeTrack(String mediaTitle) throws RemoteException;
   public boolean removeAlbum(String medialTitle) throws RemoteException;

   public MusicAlbum get(String mediaTitle) throws RemoteException;
   public MusicAlbum findAlbum(String aTitle) throws RemoteException;
   public Tracks findTrack(String aTrack) throws RemoteException;

   public void save() throws RemoteException;
   public void restore() throws RemoteException;
   public String[] getTitles() throws RemoteException;
}
