package tech.sree.com.timelymusic_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void processButton(View V){
        Intent intent=null;
        switch (V.getId()) {
            case R.id.songSelect:
                DL.t(this,"songSelect");
                  intent =  new Intent(getApplicationContext(),selectSongList.class);
                startActivity(intent);
                break;
            case R.id.setTime:
                DL.t(this, "setTime");
                break;
            case R.id.done:
                DL.t(this,"Done");
                break;
            case R.id.playlist:
                DL.t(this,"playlist");
                  intent =  new Intent(getApplicationContext(),defaultPlayList.class);
                startActivity(intent);
                break;
            default:
                DL.t(this,"NOTHING ???");
        }

    }
}
