package wingsoloar.com.hairplus.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import org.json.JSONArray;
import org.json.JSONObject;

import wingsoloar.com.hairplus.Activities.PublishPost;
import wingsoloar.com.hairplus.Objects.CutterProfile;
import wingsoloar.com.hairplus.R;
import wingsoloar.com.hairplus.Threads.GetAllCutters;
import wingsoloar.com.hairplus.Threads.GetImage;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class CutterSelect extends Activity {

    private MyAdapter myAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<CutterProfile> cutters;
    private String sName;
    private ImageView back_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutter_selection_main);

        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sName = getIntent().getStringExtra("shopName");

        cutters = new ArrayList<>();

        myAdapter = new MyAdapter(cutters);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView = (RecyclerView) findViewById(R.id.cutters);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(myAdapter);

        new GetAllCutters(myHandler).start();


    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<CutterProfile> cutterProfiles;

        public MyAdapter(@Nullable ArrayList<CutterProfile> cutterProfiles) {

            this.cutterProfiles = cutterProfiles;

        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutter_selection_child, parent, false);
            // 实例化viewholder
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            // 绑定数据
            holder.name_text.setText(cutterProfiles.get(position).getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublishPost.barber_name = cutterProfiles.get(position).getName();
                    PublishPost.barber_name_tv.setText(cutterProfiles.get(position).getName());
                    finish();
                }
            });
        }


        @Override
        public int getItemCount() {
            return cutterProfiles == null ? 0 : cutterProfiles.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            TextView name_text;


            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                name_text = itemView.findViewById(R.id.name);

            }
        }
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            final Bundle b;
            int code;
            b=msg.getData();
            code=b.getInt("response_code");
            switch (code){
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Please Check Internet Access!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case 1:
                    switch(msg.what){

                        case 1:
                            String jsonData = msg.obj.toString();

                            try {

                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONArray orgArray = jsonObject.getJSONArray("cutters");

                                Log.e("lllll","llllll");

                                for (int j = 0; j < orgArray.length(); j++) {
                                    JSONObject org_Array = orgArray.getJSONObject(j);

                                    if(sName.equals(org_Array.getString("shopName"))){
                                        int id = Integer.parseInt(org_Array.getString("id"));
                                        String user_name = org_Array.getString("name");
                                        String user_avator = "";
                                        int minPrice = org_Array.getInt("minPrice");
                                        int maxPrice = org_Array.getInt("maxPrice");
                                        int rate = org_Array.getInt("rating");
                                        String style = org_Array.getString("style");
                                        String skills = org_Array.getString("skills");
                                        String shopName = org_Array.getString("shopName");
                                        String[] images = org_Array.getString("images").replace("[","").replace("]","").replace("\"","").split(",");
                                        int experience = org_Array.getInt("experience");
                                        String tel = org_Array.getString("tel");
                                        String location = org_Array.getString("location");
                                        String introduction = org_Array.getString("introduction");
                                        for(int i=0;i<images.length;i++){
                                            images[i] = images[i].split("/")[3];
                                        }

                                        CutterProfile cutterProfile;

                                        Drawable d = Drawable.createFromPath("storage/emulated/0/" + org_Array.getString("avator_address").split("/")[3]);

                                        if (d==null){
                                            new GetImage(myHandler, org_Array.getString("avator_address").split("/")[3], j, 1).start();
                                        }


                                        if (org_Array.getString("images") == "[]") {

                                            cutterProfile = new CutterProfile(id, shopName,user_name , new String[0], org_Array.getString("avator_address").split("/")[3], style, skills, rate, minPrice,maxPrice, experience,tel,location,introduction);

                                        } else {
                                            Drawable dd = Drawable.createFromPath("storage/emulated/0/" + images[0]);

                                            if(dd==null){
                                                new GetImage(myHandler, images[0], j,2).start();
                                            }

                                            cutterProfile = new CutterProfile(id, shopName,user_name , images, org_Array.getString("avator_address").split("/")[3], style, skills, rate, minPrice,maxPrice,experience,tel,location,introduction);

                                        }

                                        cutters.add(cutterProfile);

                                        Log.e("ooooooo","llllll");


                                    }
                                }

                                myAdapter.notifyDataSetChanged();

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            Log.e("bbb222",msg.obj.toString());
                            final Bitmap bb = (Bitmap) msg.obj;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        final File imagefile = new File(Environment.getExternalStorageDirectory(), b.getString("filename"));

                                        try {
                                            if (imagefile.exists()) {
                                                imagefile.delete();
                                            }
                                            imagefile.createNewFile();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            FileOutputStream fos = new FileOutputStream(imagefile);
                                            Log.e("bbb111",bb.toString());

                                            bb.compress(Bitmap.CompressFormat.PNG, 10, fos);//压缩位图
                                            fos.flush();
                                            fos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        // 其次把文件插入到系统图库


                                        try {
                                            MediaStore.Images.Media.insertImage(getContentResolver(),
                                                    imagefile.getAbsolutePath(), b.getString("filename"), null);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        if (b.getInt("tag") == 1) {
                                            cutters.get(b.getInt("id")).setAvator(b.getString("filename"));
                                            Log.e("filename", b.getString("filename"));
                                        } else {
                                            cutters.get(b.getInt("id")).addImageName(b.getString("filename"));

                                        }

                                        myAdapter.notifyDataSetChanged();

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                }
                            }).start();


                            break;
                    }
                    break;
                case 2:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;


            }
        }
    };
}
