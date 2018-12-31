package wingsoloar.com.hairplus.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wingsolarxu on 2018/11/2.
 */

public class LikedListDBHelper extends SQLiteOpenHelper {
    public LikedListDBHelper(Context context){super(context,"likedlist.db",null,1);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table Liked (id INTEGER PRIMARY KEY AUTOINCREMENT, postID int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Liked");
        onCreate(sqLiteDatabase);
    }}
