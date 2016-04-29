package tech.sree.com.timelymusic_1;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    Context context;
    int index = 0 ;
    ArrayList<String> playList;
    final MediaPlayer player = new MediaPlayer();
    HashMap<String, List<String>> child_list =new HashMap<String,List<String>>();
    List<String> paths = new ArrayList<String>();
    ExpandableListDataPump(Context context){
        this.context = context;

    }
    public  HashMap<String, List<String>> getData() {

        return child_list;
    }
    public void   getdefaultMusicPlayList(){

        Uri tempPlaylistURI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        final String idKey = MediaStore.Audio.Playlists._ID;
        final String nameKey = MediaStore.Audio.Playlists.NAME;
        final String[] proj = { idKey, nameKey };

        Cursor playListCursor= context.getContentResolver().query(tempPlaylistURI, proj, null, null, null);

        if(playListCursor == null){
            DL.p("Not having any Playlist on phone --------------");
            return;//don't have list on phone
        }
        String playListName = null;

        DL.p(">>>>>>>  CREATING AND DISPLAYING LIST OF ALL CREATED PLAYLIST  <<<<<<");
        playList = new ArrayList<String>();

        for(int i = 0; i <playListCursor.getCount() ; i++)
        {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("name"));
            DL.p("> " + i + "  playListName : " + playListName);
            playList.add(playListName);
        }


        playListCursor.moveToFirst();
        for(int i = 0; i <playListCursor.getCount() ; i++) {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("name"));
            this.playTrackFromPlaylist(playListCursor.getLong(playListCursor.getColumnIndex(idKey)),playListName);
        }

        if(playListCursor != null)
            playListCursor.close();

    }

    public void playTrackFromPlaylist(final long playListID, String playListName) {
        final ContentResolver resolver = context.getContentResolver();
        final Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListID);
        final String dataKey = MediaStore.Audio.Media.DATA;
        final String titleKey = android.provider.MediaStore.Audio.Media.TITLE;
        final String duriationKey =  MediaStore.Audio.Media.DURATION;

//  android.provider.MediaStore.Audio.Media.DATA,android.provider.MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DURATION
        String projection[]={dataKey,titleKey,duriationKey};

        Cursor tracks = resolver.query(uri, projection, null, null, null);
        if (tracks != null) {
            int count = tracks.getCount();
            DL.t(context, "Numeber of Songs in playList " + count);
            DL.p("Numeber of Songs in playList " + count);
            tracks.moveToFirst();
            String songInfo ;
            List<String> childList = new ArrayList<String>();

            String player= ""+playList.get(0);
            do {

                int  duration=(Integer.parseInt(tracks.getString(tracks.getColumnIndex(duriationKey))))/1000;
                int min = duration / 60 ;
                int sec = duration % 60 ;
                final String dataPath = tracks.getString(tracks.getColumnIndex(dataKey));
                songInfo = (" >> \n " + tracks.getString(tracks.getColumnIndex(titleKey)) +
                        "  [ " + min + " : " + sec + " ]\n path : " + dataPath);

                childList.add(songInfo);
            }while (tracks.moveToNext());
            DL.t(context,"adding into HashMap : "+playListName);
            DL.p("adding into HashMap : "+playListName);
            child_list.put(playListName, childList);


            // final MediaPlayer player = new MediaPlayer();
        //    playAudio(paths.get(0),player);
/*
            final int dataIndex = tracks.getColumnIndex(dataKey);
            final String dataPath = tracks.getString(dataIndex);
            playAudio(dataPath);
*/            tracks.close();
        }
    }

    public  void playAudio( String SongsList, final MediaPlayer player) {
        //final MediaPlayer player = new MediaPlayer();
        if (SongsList == null) {
            DL.p("Called playAudio with null data stream.");
            return;
        }

        try {

            player.setDataSource(SongsList);
            DL.p("inedx "+index+"  song : "+SongsList);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    DL.p("Song play Completed ");

                    player.reset();
                    playNextTrack();
                }
            });
        } catch (Exception e) {
            DL.p( "Failed to start MediaPlayer: " + e.getMessage());
            return;
        }
    }
    public    void playNextTrack(){
        index = (index >= paths.size()) ?  0: index ;
        playAudio(paths.get(++index), player);
    }


}
