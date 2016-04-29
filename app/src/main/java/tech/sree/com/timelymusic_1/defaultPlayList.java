package tech.sree.com.timelymusic_1;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class defaultPlayList extends AppCompatActivity {
    TextView playlist;
    StringBuffer buffer = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_play_list);
        playlist = (TextView) findViewById(R.id.pList);
        getdefaultMusicPlayList();
    }

    public void getdefaultMusicPlayList(){

        Uri tempPlaylistURI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        final String idKey = MediaStore.Audio.Playlists._ID;
        final String nameKey = MediaStore.Audio.Playlists.NAME;
        final String[] proj = { idKey, nameKey };

        Cursor playListCursor= getContentResolver().query(tempPlaylistURI, proj, null, null, null);

        if(playListCursor == null){
            DL.p("Not having any Playlist on phone --------------");
            return;//don't have list on phone
        }
        String playListName = null;

        DL.p(">>>>>>>  CREATING AND DISPLAYING LIST OF ALL CREATED PLAYLIST  <<<<<<");

        for(int i = 0; i <playListCursor.getCount() ; i++)
        {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("name"));
            DL.p("> " + i + "  : " + playListName);
            buffer.append("\n > " + i + "  : " + playListName);
        }

        // Play the first song from the first playlist.
        playListCursor.moveToFirst();
        for(int i = 0; i <playListCursor.getCount() ; i++) {
            playListCursor.moveToPosition(i);

            // final long playlistID = playListCursor.getLong(playListCursor.getColumnIndex(idKey));
            this.playTrackFromPlaylist(playListCursor.getLong(playListCursor.getColumnIndex(idKey)));

        }
        playlist.setText("\n Default PlayList in phone:  " + buffer);

        if(playListCursor != null)
            playListCursor.close();

    }
    public void playTrackFromPlaylist(final long playListID) {
        final ContentResolver resolver = this.getContentResolver();
        final Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListID);
        final String dataKey = MediaStore.Audio.Media.DATA;
        final String titleKey = android.provider.MediaStore.Audio.Media.TITLE;
        final String duriationKey =  MediaStore.Audio.Media.DURATION;


        String projection[]={dataKey,titleKey,duriationKey
//                android.provider.MediaStore.Audio.Media.DATA,
//                android.provider.MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DURATION
        };
        //Cursor tracks = resolver.query(uri, new String[] { dataKey }, null, null, null);
        Cursor tracks = resolver.query(uri, projection, null, null, null);
        if (tracks != null) {
            int count = tracks.getCount();
            DL.t(getApplicationContext(), "Numeber of Songs in playList " + count);
            DL.p("Numeber of Songs in playList " + count);
            tracks.moveToFirst();
            buffer.append("List of  Songs : \n ");
            do {

                int  duration=(Integer.parseInt(tracks.getString(tracks.getColumnIndex(duriationKey))))/1000;
                int min = duration / 60 ;
                int sec = duration % 60 ;
                final String dataPath = tracks.getString(tracks.getColumnIndex(dataKey));
                buffer.append(" >> \n "+tracks.getString(tracks.getColumnIndex(titleKey))+
                        "  [ "+min+" : "+sec+" ]\n path : "+dataPath);
            }while (tracks.moveToNext());

/*
            final int dataIndex = tracks.getColumnIndex(dataKey);
            final String dataPath = tracks.getString(dataIndex);
            playAudio(dataPath);
            tracks.close();
*/        }
    }

    public static void playAudio(final String path) {
        final MediaPlayer player = new MediaPlayer();
        if (path == null) {
            DL.p( "Called playAudio with null data stream.");
            return;
        }
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
        } catch (Exception e) {
            DL.p( "Failed to start MediaPlayer: " + e.getMessage());
            return;
        }
    }
}
