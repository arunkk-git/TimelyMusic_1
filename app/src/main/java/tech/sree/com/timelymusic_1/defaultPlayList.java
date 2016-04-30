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
        ExpandableListDataPump dataPump =  new ExpandableListDataPump(this);
        dataPump. getdefaultMusicPlayList();
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

}
