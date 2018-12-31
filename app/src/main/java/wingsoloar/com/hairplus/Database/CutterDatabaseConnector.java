//package wingsoloar.com.hairplus.Database;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import wingsoloar.com.hairplus.Objects.CutterProfile;
//
//import static android.content.ContentValues.TAG;
//
///**
// * Created by wingsolarxu on 2018/10/22.
// */
//
//public class CutterDatabaseConnector {
//
//    private CutterDBHelper helper;
//    private Context context;
//    private SQLiteDatabase db;
//
//    public CutterDatabaseConnector(Context context){
//        this.context=context;
//        helper=new CutterDBHelper(context);
//    }
//
//    public void initial(){
//        db=null;
//        try {
//            db = helper.getWritableDatabase();
//            db.beginTransaction();
//            db.execSQL("insert into Cutters (shopID, name) values(1,'张麻子')");
//            db.execSQL("insert into Cutters (shopID, name) values(1,'王二蛋')");
//            db.execSQL("insert into Cutters (shopID, name) values(2,'赵老六')");
//            db.setTransactionSuccessful();
//
//        }catch (Exception e){
//            Log.e(TAG, "", e);
//        }finally {
//            if (db != null) {
//                db.endTransaction();
//                db.close();
//            }
//        }
//    }
//
//    public boolean isEmpty(){
//        db = null;
//        db = helper.getReadableDatabase();
//        Cursor  cursor = db.rawQuery("select * from Cutters",null);
//
//        return cursor==null||cursor.getCount()==0;
//    }
//
//
//    public List<CutterProfile> getCutters(int shopID){
//        db = null;
//        db = helper.getReadableDatabase();
//        Cursor  cursor = db.rawQuery("select * from Cutters where shopID="+shopID+"",null);
//        List<CutterProfile> cutters =new ArrayList<>();
//
//        if (cursor != null && cursor.moveToFirst())
//            cutters = new ArrayList<CutterProfile>(cursor.getCount());
//
//        for (int i=0;i<cursor.getCount();i++){
//            int id=cursor.getInt(0);
//            String name=cursor.getString(2);
//
//            CutterProfile cutter = new CutterProfile(id,shopID,name);
//            cutters.add(cutter);
//            cursor.moveToNext();
//        }
//
//        return cutters;
//    }
//}
