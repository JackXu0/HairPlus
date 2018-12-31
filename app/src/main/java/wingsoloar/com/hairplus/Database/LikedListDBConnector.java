package wingsoloar.com.hairplus.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsolarxu on 2018/11/2.
 */

public class LikedListDBConnector {
    private LikedListDBHelper helper;
    private Context context;
    private SQLiteDatabase db;

    public LikedListDBConnector(Context context){
        this.context=context;
        helper=new LikedListDBHelper(context);
    }

    public void add(int postID){
        db=null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into Liked (postID) values("+postID+")");

            db.setTransactionSuccessful();

        }catch (Exception e){
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public void delete(int postId){
        db=null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("DELETE FROM Liked WHERE postID = "+postId+"");

            db.setTransactionSuccessful();

        }catch (Exception e){
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }}

    public List<Integer> getLiked(){
        db = null;
        db = helper.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from Liked",null);
        List<Integer> Liked =new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
            Liked = new ArrayList<Integer>(cursor.getCount());

        for (int i=0;i<cursor.getCount();i++){
            int postID=cursor.getInt(1);

            Liked.add(postID);
            cursor.moveToNext();
        }

        return Liked;
    }

}
