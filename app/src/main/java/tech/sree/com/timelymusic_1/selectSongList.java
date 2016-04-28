package tech.sree.com.timelymusic_1;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class selectSongList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> songs = new ArrayList<String>();
        songs = getListofMusicFiles();
        ArrayAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songs);
        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);
        ArrayList<String> selectedSongs =  new ArrayList<String>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DL.t(getApplicationContext()," "+position);
            }
        });
    }
    public ArrayList<String>  getListofMusicFiles(){
        ArrayList<String> songs;
        Uri uri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String projection[]={android.provider.MediaStore.Audio.Media.DATA,
                android.provider.MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION
        };
        Cursor cursor=getContentResolver().query(uri, projection, null, null, null);
        songs=new ArrayList<String>();
        while (cursor.moveToNext()) {

            int  duration=(Integer.parseInt(cursor.getString(2)))/1000;
            int min = duration / 60 ;
            int sec = duration % 60 ;

            songs.add(cursor.getString(1)+"  [ "+min+" : "+sec+" ]");
        }
        return songs;
    }
}
