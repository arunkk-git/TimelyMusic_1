package tech.sree.com.timelymusic_1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ananth on 4/28/2016.
 */
public class DL {
    public  static void t(Context context,String msg){
        Toast.makeText( context,msg,Toast.LENGTH_LONG).show();
    }
    public static void p(String msg){
        Log.v("ARUN", msg);
    }
}
