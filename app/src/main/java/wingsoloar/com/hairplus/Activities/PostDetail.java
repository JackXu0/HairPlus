package wingsoloar.com.hairplus.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import wingsoloar.com.hairplus.Threads.AddLike;
import wingsoloar.com.hairplus.Threads.DeleteLike;
import wingsoloar.com.hairplus.Threads.GetCutterInfo;
import wingsoloar.com.hairplus.Threads.GetImage;
import wingsoloar.com.hairplus.Threads.GetLikedList;

/**
 * Created by wingsolarxu on 2018/10/21.
 */

public class PostDetail extends Activity{

    private ViewPager vp;
    private VPAdapter vpa;
    private ImageView[] imageViews;
    private TextView name_text;
    private TextView content_text;
    private ImageView avator_view;
    private ImageView like_button;
    private String username;
    private String avator_path;
    private String content;
    private int price;
    private String cutter;
    private String shop;
    private Post post;
    private SharedPreferences preferences;
    private ImageView back_button;
    private Button find_button;

    private ArrayList<Integer> likedList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        likedList = new ArrayList<>();

        preferences=getSharedPreferences("user_info",MODE_PRIVATE);

        name_text = findViewById(R.id.user_name);
        content_text = findViewById(R.id.content);
        avator_view = findViewById(R.id.user_avator);
        like_button = findViewById(R.id.like_button);
        back_button = findViewById(R.id.back_button);
        find_button = findViewById(R.id.find_button);

        post = (Post) getIntent().getSerializableExtra("post");

        for (int i = 1; i < post.getImageNames().length; i++) {
            Drawable d = Drawable.createFromPath("storage/emulated/0/" + post.getImageNames()[i]);
            if (d==null){
                new GetImage(myHandler, post.getImageNames()[i], i,2).start();
            }
        }

        imageViews = new ImageView[post.getImageNames().length];

        username = post.getUserName();
        avator_path = post.getUser_avator();
        content = post.getContent();
        shop = post.getShop();
        cutter = post.getCutter();
        price = post.getPrice();

        name_text.setText(username);

        content_text.setText(content);

        Log.e("content111",content);
        if(avator_path.length()<5){

        }else{
            Drawable ava = Drawable.createFromPath("storage/emulated/0/"+avator_path);
            avator_view.setImageDrawable(ava);
        }

        if (post.isLiked()){
            like_button.setImageResource(R.drawable.collected);
        }else {
            like_button.setImageResource(R.drawable.uncollected);
        }

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.isLiked()){
                    post.setLiked(false);
                    new DeleteLike(myHandler,preferences.getString("name",""),post.getId()).start();
                    like_button.setImageResource(R.drawable.uncollected);
                }
                else {
                    post.setLiked(true);
                    new AddLike(myHandler,preferences.getString("name",""),post.getId()).start();
                    likedList.add(post.getId());
                    like_button.setImageResource(R.drawable.collected);

                }

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCutterInfo(myHandler,post.getCutter()).start();
            }
        });

        vp = findViewById(R.id.viewpager);
        vpa=new VPAdapter(post.getImageNames());
        vp.setAdapter(vpa);
        vp.setOffscreenPageLimit(0);
//        vp.setCurrentItem(0);

        new GetLikedList(myHandler, preferences.getString("name","")).start();

    }

    class VPAdapter extends PagerAdapter {
        private String[] names;

        public VPAdapter(String[] names) {
            super();
            this.names = names;
        }

        @Override
        public int getCount() {
            return names.length;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(getApplicationContext());
            Drawable d = Drawable.createFromPath("storage/emulated/0/"+names[position]);

            if(d != null){
                imageView = new ImageView(getApplicationContext());
                imageView.setImageDrawable(d);
                container.addView(imageView);
            }

            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setFocusable(false);

            Log.e("pos",position+"");
//
            ViewPager.LayoutParams params = (ViewPager.LayoutParams) imageView.getLayoutParams();

            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = 300;
            imageView.setLayoutParams(params);

            return imageView;
        }



        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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

                    switch (msg.what){
                        case 1:
                            Log.e("bbb222",msg.obj.toString());
                            final Bitmap bb = (Bitmap) msg.obj;

                            new Thread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("dfsa", "dfaf");
                                                vpa.notifyDataSetChanged();
                                            }
                                        });


                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                }
                            }).start();

                            break;

                        case 10:
                            String likelist = (String) msg.obj;
                            String[] likelist_list = likelist.split(",");

//                            for(int i=0;i<likelist_list.length;i++){
//                                if(post.getId()==Integer.parseInt(likelist_list[i])){
//                                    post.setLiked(true);
//                                    like_button.setImageResource(R.drawable.collected);
//                                }else{
//                                    post.setLiked(false);
//                                    like_button.setImageResource(R.drawable.uncollected);
//                                }
//                            }

                            break;

                        case 20:
                            String jsonData = msg.obj.toString();

                            try {

                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONArray orgArray = jsonObject.getJSONArray("cutter");

                                JSONObject org_Array = orgArray.getJSONObject(0);

                                int id = Integer.parseInt(org_Array.getString("id"));
                                String user_name = org_Array.getString("name");
                                String shopName = org_Array.getString("shopName");
                                String style = org_Array.getString("style");
                                String skills = org_Array.getString("skills");
                                int rating = org_Array.getInt("rating");
                                int maxPrice = org_Array.getInt("maxPrice");
                                int minPrice = org_Array.getInt("minPrice");
                                int experience = org_Array.getInt("experience");
                                String tel = org_Array.getString("tel");
                                String location = org_Array.getString("location");
                                String introduction = org_Array.getString("introduction");

                                Intent intent = new Intent(PostDetail.this, CutterDetail.class);
                                intent.putExtra("cutter_name", user_name);
                                intent.putExtra("cutter_experience", experience);
                                intent.putExtra("cutter_tel",tel);
                                intent.putExtra("cutter_location",location);
                                intent.putExtra("minPrice",minPrice);
                                intent.putExtra("maxPrice",maxPrice);
                                intent.putExtra("cutter_introduction",introduction);
                                intent.putExtra("cutter_rating",rating);
                                intent.putExtra("cutter_avator",org_Array.getString("avator_address").split("/")[3]);
                                startActivity(intent);



                            } catch (Exception e) {

                                e.printStackTrace();
                            }

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
