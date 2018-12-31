package wingsoloar.com.hairplus.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class ShopDBHelper extends SQLiteOpenHelper {

    public ShopDBHelper(Context context){super(context,"shop.db",null,1);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table Shops (id INTEGER PRIMARY KEY AUTOINCREMENT, name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Shops");
        onCreate(sqLiteDatabase);
    }
}
