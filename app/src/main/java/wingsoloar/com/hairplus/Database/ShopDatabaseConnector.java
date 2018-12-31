package wingsoloar.com.hairplus.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wingsoloar.com.hairplus.Objects.Shop;

import static android.content.ContentValues.TAG;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class ShopDatabaseConnector {

    private ShopDBHelper helper;
    private Context context;
    private SQLiteDatabase db;

    public ShopDatabaseConnector(Context context){
        this.context=context;
        helper=new ShopDBHelper(context);
    }

    public void initial(){
        db=null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into Shops (name) values('顶尖造型文萃店')");
            db.execSQL("insert into Shops (name) values('纯剪造型文萃店')");
            db.execSQL("insert into Shops (name) values('纯剪造型文星店')");
            db.execSQL("insert into Shops (name) values('三千丝')");

            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public boolean isEmpty(){
        db = null;
        db = helper.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from Shops",null);

        return cursor==null||cursor.getCount()==0;
    }

    public List<Shop> getShops(){
        db = null;
        db = helper.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from Shops",null);
        List<Shop> shops =new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
            shops = new ArrayList<Shop>(cursor.getCount());

        for (int i=0;i<cursor.getCount();i++){
            int id=cursor.getInt(0);
            String name=cursor.getString(1);

            Shop shop = new Shop(id,name);
            shops.add(shop);
            cursor.moveToNext();
        }

        return shops;
    }

}
