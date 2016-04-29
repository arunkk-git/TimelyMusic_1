package tech.sree.com.timelymusic_1;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class defaultPlayList extends AppCompatActivity {
    TextView playlist;
    ListView listView ;
    HashMap<String,Cursor> defaultplayListInfo;
    StringBuffer buffer = new StringBuffer();
    ArrayList<String> paths =  new ArrayList<String>();
    final MediaPlayer player = new MediaPlayer();
    ArrayList<String> playList_lv ;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    int  index =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_play_list);
//        playlist = (TextView) findViewById(R.id.pList);
//        listView = (ListView) findViewById(R.id.listViewDefault);
        ExpandableListDataPump dataPump =  new ExpandableListDataPump(this);
        dataPump. getdefaultMusicPlayList();
//        ArrayAdapter adapter =  new ArrayAdapter(this,android.R.layout.simple_list_item_1,playList_lv);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv = (TextView) view;
//                DL.t(getApplicationContext(), tv.getText().toString());
//            }
//        });

      //  DL.p("defaultplayListInfo.toString = " + defaultplayListInfo.toString());

/*-----------------------------------------*/

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        expandableListDetail = dataPump.getData();

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle,
                expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                DL.t(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.");
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                DL.t(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.");
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                DL.t(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition));
                return false;
            }
        });
    }

        /*-----------------------------------------*/
/*

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
        playList_lv = new ArrayList<String>();

        for(int i = 0; i <playListCursor.getCount() ; i++)
        {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("name"));


            DL.p("> " + i + "  : " + playListName);
            playList_lv.add(playListName);
            buffer.append("\n > " + i + "  : " + playListName);
        }

        // Play the first song from the first playlist.
        playListCursor.moveToFirst();
        for(int i = 0; i <playListCursor.getCount() ; i++) {
            playListCursor.moveToPosition(i);

            // final long playlistID = playListCursor.getLong(playListCursor.getColumnIndex(idKey));
            this.playTrackFromPlaylist(playListCursor.getLong(playListCursor.getColumnIndex(idKey)));

        }
//        playlist.setText("\n Default PlayList in phone:  " + buffer);

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
//             paths =  new ArrayList<String>();
//             player = new MediaPlayer();
            buffer.append("List of  Songs : \n ");
            do {

                int  duration=(Integer.parseInt(tracks.getString(tracks.getColumnIndex(duriationKey))))/1000;
                int min = duration / 60 ;
                int sec = duration % 60 ;
                final String dataPath = tracks.getString(tracks.getColumnIndex(dataKey));
                buffer.append(" >> \n " + tracks.getString(tracks.getColumnIndex(titleKey)) +
                        "  [ " + min + " : " + sec + " ]\n path : " + dataPath);
                // playAudio(dataPath);
                paths.add(dataPath);
            }while (tracks.moveToNext());

            // final MediaPlayer player = new MediaPlayer();
            playAudio(paths.get(0),player);
         //  tracks.close();
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
*/
}
