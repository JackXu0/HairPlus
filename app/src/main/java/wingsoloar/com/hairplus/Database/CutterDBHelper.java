package wingsoloar.com.hairplus.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class CutterDBHelper extends SQLiteOpenHelper {
    public CutterDBHelper(Context context){super(context,"cutter.db",null,1);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table Cutters (id INTEGER PRIMARY KEY AUTOINCREMENT, shopID int, name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Cutters");
        onCreate(sqLiteDatabase);
    }

}
