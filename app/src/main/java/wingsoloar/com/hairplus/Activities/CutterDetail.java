package wingsoloar.com.hairplus.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import wingsoloar.com.hairplus.Objects.Post;
import wingsoloar.com.hairplus.R;
import wingsoloar.com.hairplus.Threads.GetAllMyPosts;
import wingsoloar.com.hairplus.Threads.GetImage;
import wingsoloar.com.hairplus.Views.CircleImageView;

/**
 * Created by wingsolarxu on 2018/11/28.
 */

public class CutterDetail extends Activity {

    public RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Post> posts;
    private TextView name_tv;
    private TextView experiecne_tv;
    private TextView tel_tv;
    private TextView location_tv;
    private TextView price_tv;
    private TextView introdcution_tv;
    private TextView rating_tv;
    private CircleImageView avator_iv;
    private ImageView back_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutter_detail);

        String cutter_name = getIntent().getStringExtra("cutter_name");
        int cutter_experience = getIntent().getIntExtra("cutter_experience",0);
        String cutter_tel = getIntent().getStringExtra("cutter_tel");
        String cutter_location = getIntent().getStringExtra("cutter_location");
        int minPrice = getIntent().getIntExtra("minPrice",0);
        int maxPrice = getIntent().getIntExtra("maxPrice",0);
        String cutter_introduction = getIntent().getStringExtra("cutter_introduction");
        int cutter_rating = getIntent().getIntExtra("cutter_rating",0);
        String avator = getIntent().getStringExtra("cutter_avator");

        name_tv = findViewById(R.id.name_value);
        experiecne_tv = findViewById(R.id.age_value);
        tel_tv = findViewById(R.id.tele_value);
        location_tv = findViewById(R.id.district_value);
        price_tv = findViewById(R.id.price_value);
        introdcution_tv = findViewById(R.id.intro_value);
        rating_tv = findViewById(R.id.rating);
        avator_iv = findViewById(R.id.avator);
        back_button = findViewById(R.id.back_button);

        name_tv.setText(cutter_name);
        experiecne_tv.setText(cutter_experience+"");
        tel_tv.setText(cutter_tel);
        location_tv.setText(cutter_location);
        price_tv.setText(minPrice+" - "+maxPrice);
        introdcution_tv.setText(cutter_introduction);
        rating_tv.setText(cutter_rating+"");

        Drawable d = Drawable.createFromPath("storage/emulated/0/"+avator);

        if(d!=null){
            avator_iv.setImageDrawable(d);
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        posts = new ArrayList<>();

        Log.e("cutter_name", cutter_name);

        new GetAllMyPosts(myHandler,cutter_name).start();
//
//        posts.add(new Post());
//        posts.add(new Post());

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new MyAdapter(posts);
        recyclerView = findViewById(R.id.recycleview);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(mAdapter);



    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<Post> posts;

        public MyAdapter(@Nullable ArrayList<Post> posts) {

            this.posts = posts;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutter_detail_child, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new MyAdapter.ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {


            holder.title.setText(posts.get(position).getContent());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                for(int i=0;i<posts.get(position).getImageNames().length;i++){

                        Drawable dd = Drawable.createFromPath("storage/emulated/0/"+posts.get(position).getImageNames()[0]);
                        holder.image.setImageDrawable(dd);

                }

            }

            holder.title.setText(posts.get(position).getContent());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(CutterDetail.this, PostDetail.class);
//                    if (likedList.contains(posts.get(position).getId())){
//                        posts.get(position).setLiked(true);
//                    }else{
//                        posts.get(position).setLiked(false);
//                    }
//                    intent.putExtra("post", posts.get(position));
//                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return posts == null ? 0 : posts.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            ImageView image;
            TextView title;


            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                image = itemView.findViewById(R.id.image);
                title =itemView.findViewById(R.id.title);
            }
        }
    }

    private Handler myHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(final Message msg) {
            final Bundle b;
            int code;
            b = msg.getData();
            code = b.getInt("response_code");
            switch (code) {
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please Check Internet Access!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case 1:

                    Log.e("kkkk","kkkkkkk");
                    switch (msg.what) {

                        case 1:
                            posts.clear();
                            String jsonData = msg.obj.toString();

                            Log.e("dfaf", jsonData.replace("\"", ""));

                            try {

                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONArray orgArray = jsonObject.getJSONArray("posts");

                                for (int j = 0; j < orgArray.length(); j++) {
                                    JSONObject org_Array = orgArray.getJSONObject(j);

                                    int id = Integer.parseInt(org_Array.getString("id"));
                                    String user_name = org_Array.getString("user_name");
                                    String user_avator = "";
                                    String content = org_Array.getString("content");
                                    String cutter = org_Array.getString("cutter");
                                    String shop = org_Array.getString("shop");
                                    String tag = org_Array.getString("tag");
                                    boolean is_tony_post = org_Array.getBoolean("is_tony_post");
                                    int price = org_Array.getInt("price");
                                    String[] images = org_Array.getString("images").replace("[", "").replace("]", "").replace("\"", "").split(",");

                                    for (int i = 0; i < images.length; i++) {
                                        images[i] = images[i].split("/")[3];
                                    }

                                    Log.e("content22", content);

                                    Log.e("postID", "" + id);

                                    Log.e(cutter, "" + images.length);

                                    Post post;

                                    Drawable d = Drawable.createFromPath("storage/emulated/0/" + org_Array.getString("user_avator").split("/")[3]);

                                    if (d == null) {
                                        new GetImage(myHandler, org_Array.getString("user_avator").split("/")[3], j, 1).start();
                                    }


                                    if (org_Array.getString("images") == "[]") {
                                        post = new Post(id, user_name, org_Array.getString("user_avator").split("/")[3], content, cutter, shop, price, new String[0], false, tag, is_tony_post);
                                    } else {
                                        Drawable dd = Drawable.createFromPath("storage/emulated/0/" + images[0]);

                                        if (dd == null) {
                                            new GetImage(myHandler, images[0], j, 2).start();
                                        }


                                        post = new Post(id, user_name, user_avator, content, cutter, shop, price, images, false, tag, is_tony_post);

                                    }

                                    Log.e("iiiiiii", "llll");
                                    posts.add(post);
                                }

                                mAdapter.notifyDataSetChanged();

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            break;

                        case 2:

                            final Bitmap bb = (Bitmap) msg.obj;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        File imagefile = new File(Environment.getExternalStorageDirectory(), b.getString("filename"));

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
                                            posts.get(b.getInt("id")).setUser_avator(b.getString("filename"));
                                            Log.e("filename", b.getString("filename"));
                                        } else {
                                            Log.e("addImageName", b.getString("filename"));
                                            posts.get(b.getInt("id")).addImageName(b.getString("filename"));
                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("dfsa", "dfaf");
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        Log.e("3", "contact1");
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
                            Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
            }

        }
    };


}
