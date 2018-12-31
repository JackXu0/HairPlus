package wingsoloar.com.hairplus.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import wingsoloar.com.hairplus.Activities.Personal_information_activity;
import wingsoloar.com.hairplus.Activities.PostDetail;
import wingsoloar.com.hairplus.Database.LikedListDBConnector;
import wingsoloar.com.hairplus.Objects.Post;
import wingsoloar.com.hairplus.R;
import wingsoloar.com.hairplus.Threads.GetImage;
import wingsoloar.com.hairplus.Threads.GetLikedList;
import wingsoloar.com.hairplus.Threads.GetOnePost;
import wingsoloar.com.hairplus.Threads.Login;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wingsolarxu on 2018/10/13.
 */

public class User_view extends Fragment {


    private View view;
    private ViewPager vp;
    private int width = 160;
    private TextView tv;
    private RelativeLayout rl_progress;
    private ArrayList<RecyclerView> recyclerViews;
    private NestedScrollView scrollingView;
    private VPAdapter vpa;
    private ArrayList<Post> posts1;
    private ArrayList<Post> posts2;
    private MyAdapter adapter1;
    private MyAdapter adapter2;
    private RelativeLayout edit_button;
    private StaggeredGridLayoutManager mLayoutManager1;
    private StaggeredGridLayoutManager mLayoutManager2;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private ImageView avator;
    private ImageView background;
    private TextView mine;
    private TextView collect;
    private int pageID = 0;
    private static boolean set1 = false;
    private static boolean set2 = false;
    private SharedPreferences preferences;
    private TextView user_name;

    private static boolean OnInitial1 ;
    private Activity activity;
    private ArrayList<Integer> likedList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_view, container, false);

        activity = getActivity();
        OnInitial1 = true;

        recyclerViews = new ArrayList<>();
        posts1 = new ArrayList<>();
        posts2 = new ArrayList<>();
        likedList = new ArrayList<>();

//        connector= new LikedListDBConnector(activity);

        vp = (ViewPager) view.findViewById(R.id.viewpager);
        rl_progress = view.findViewById(R.id.rl_progress);
        scrollingView = view.findViewById(R.id.scrollview);
        edit_button = view.findViewById(R.id.edit_button);
        user_name = view.findViewById(R.id.user_name);
        avator = view.findViewById(R.id.avator);
        background = view.findViewById(R.id.background_image);
        mine = view.findViewById(R.id.mine);
        collect = view.findViewById(R.id.collect);

        adapter1 = new MyAdapter(posts1,likedList);
        adapter2 = new MyAdapter(posts2,likedList);


        mLayoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        recyclerView1 = new RecyclerView(activity);
        recyclerView2 = new RecyclerView(activity);

        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView1.setAdapter(adapter1);

        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setAdapter(adapter2);

        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);

        recyclerViews.add(recyclerView1);
        recyclerViews.add(recyclerView2);

        recyclerView1.setFocusable(false);
        recyclerView2.setFocusable(false);


        vpa = new VPAdapter((ArrayList<RecyclerView>) recyclerViews);
        vp.setAdapter(vpa);
        vp.setOffscreenPageLimit(0);

        tv = new TextView(activity);
        tv.setWidth(width);
        tv.setHeight(10);
        tv.setBackgroundColor(Color.parseColor("#0000AA"));

        rl_progress.addView(tv);

        preferences=activity.getSharedPreferences("user_info",MODE_PRIVATE);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                }else{

                    openAlbum();
                }
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,Personal_information_activity.class);
                activity.startActivity(intent);
            }
        });


        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                pageID = arg0;
//                new GetLikedList(myHandler, preferences.getString("name","")).start();

                String name=preferences.getString("name","");
                String password = preferences.getString("password","");

                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();


//                if(!(pageID==1&&OnInitial1)){
//                    new Login(getContext(),myHandler,name,password,0,0,null).start();
//                }


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                int left = (width + 120) * arg0 + (int) ((width + 120) * arg1) + width - 60;
                RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                rllp.leftMargin = left;
                tv.setLayoutParams(rllp);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });

        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(0,false);
            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1,false);
            }
        });


        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        Log.e("111", "onresume");


        String background_path=preferences.getString("background_path","");
        String avator_path = preferences.getString("avator_path","");

        if(avator_path.length()>1){
            displayImageAvator(avator_path);
        }

        if(background_path.length()>1){
            displayImageBackground(background_path);
        }

        String name=preferences.getString("name","");
        String password = preferences.getString("password","");

        user_name.setText(name);


        Log.e("name",name);
        if(name.equals("")){
            Log.e("111","111");
            Intent intent = new Intent(activity,LoginActivity.class);
            startActivity(intent);
        }else{
            Log.e("222","222");
            if(OnInitial1){
                new Login(getContext(),myHandler,name,password,0,0,null).start();
            }
        }

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<Post> posts;
        private int offset;
        private ArrayList<Integer> likelist;

        public MyAdapter(@Nullable ArrayList<Post> posts, ArrayList<Integer> likelist) {

            this.posts = posts;
            this.likelist = likelist;

        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_view_child, parent, false);
            // 实例化viewholder
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            // 绑定数据

            Log.e("dfafa", "sd " + position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                if (posts.get(position).getImageNames().length != 0) {

                    if(posts.get(position).getImageNames()[0]!=null){
                        Drawable d = Drawable.createFromPath("storage/emulated/0/"+posts.get(position).getImageNames()[0]);
                        if(d!=null){
                            holder.image.setImageDrawable(d);
                        }
                    }

                }

                if (posts.get(position).getUser_avator().length() > 7) {
                    Log.e("dsfafadfafadfafa", posts.get(position).getUser_avator());
                    Drawable d = Drawable.createFromPath("storage/emulated/0/" + posts.get(position).getUser_avator());
                    holder.avator.setImageDrawable(d);
                }


            }


//            holder.cutter.setText(posts.get(position).getCutter());
            holder.name.setText(posts.get(position).getUserName());
            holder.content.setText(posts.get(position).getContent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, PostDetail.class);
                    intent.putExtra("post", posts.get(position));
                    activity.startActivity(intent);
                }
            });

            if (likelist.contains(posts.get(position).getId())){
                posts.get(position).setLiked(true);
                holder.collect_button.setImageResource(R.drawable.collected);
            }else {
                posts.get(position).setLiked(false);
                holder.collect_button.setImageResource(R.drawable.uncollected);
            }
            holder.collect_button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {

                    preferences=activity.getSharedPreferences("user_info",MODE_PRIVATE);
                    String name=preferences.getString("name","");
                    String password = preferences.getString("password","");

                    if (likedList.contains(posts.get(position).getId())){
//                        posts.get(position).setLiked(false);
                        new Login(getContext(),myHandler,name,password,2,posts.get(position).getId(),null).start();
//                        new DeleteLike(myHandler,"ElfinHouse",posts.get(position).getId()).start();
//                        holder.collect_button.setImageResource(R.drawable.uncollected);
//                        likedList.remove((Integer)posts.get(position).getId() );
//                        Log.e(";;;;;;;","11111");
                    }
                    else {
//                        posts.get(position).setLiked(true);
                        new Login(getContext(),myHandler,name,password,1,posts.get(position).getId(),null).start();
//                        new AddLike(myHandler,"ElfinHouse",posts.get(position).getId()).start();
//                        holder.collect_button.setImageResource(R.drawable.collected);
//                        likedList.add((Integer)posts.get(position).getId());
//                        Log.e(";;;;;;;","22222");

                    }
//                    notifyDataSetChanged();

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
            ImageView avator;
            TextView name;
            TextView content;
            ImageView collect_button;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                avator = itemView.findViewById(R.id.user_avator);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.user_name);
                content = itemView.findViewById(R.id.content);
                collect_button=itemView.findViewById(R.id.collect_button);

            }
        }
    }


    class VPAdapter extends PagerAdapter {
        private ArrayList<RecyclerView> lists;

        public VPAdapter(ArrayList<RecyclerView> lists) {
            super();
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RecyclerView iv = lists.get(position);
//            if(position==0){
//                mAdapter adapter=new mAdapter(fill_in_listview(position));
//                iv.setAdapter(adapter);
//            }

            container.addView(iv);
            Log.e("pos", position + "");
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(lists.get(position));
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Please Check Internet Access!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case 1:
                    switch (msg.what) {

                        //add a like
                        case 6:
                            String name=preferences.getString("name","");
                            String password = preferences.getString("password","");
                            new Login(getContext(),myHandler,name,password,0,0,null).start();

                            break;

                        //delete a like
                        case 7:
                            name=preferences.getString("name","");
                            password = preferences.getString("password","");
                            new Login(getContext(),myHandler,name,password,0,0,null).start();

                            break;

                        //get all likelist
                        case 10:

                            likedList.clear();
                            String likelist = (String) msg.obj;
                            String[] likelist_list = likelist.split(",");

                            if(likelist.contains(",")){
                                for(int i=0;i<likelist_list.length;i++){
                                    likedList.add(Integer.parseInt(likelist_list[i]));
                                }
                            }

                            Log.e("likelistooo",likedList.size()+"");


//                            if(OnInitial1){
////                                OnInitial1 = false;
//
//
//
//                                posts2.clear();
//                                if(likedList.size()==0){
//                                    adapter2.notifyDataSetChanged();
//                                }else{
//                                    for (int i=0;i<likedList.size();i++){
//                                        new GetOnePost(myHandler,likedList.get(i)).start();
//                                        Log.e("pppppppp",""+likedList.get(i));
//                                    }
//                                }
////                                    new GetAllMyPosts(myHandler,"ElfinHouse").start();
//                            }else{
//                                if(pageID==0){
//
//
//                                }else{

                                    preferences=activity.getSharedPreferences("user_info",MODE_PRIVATE);
                                    name=preferences.getString("name","");
                                    password = preferences.getString("password","");

                                    Log.e("8888888",name);

                                    new Login(getContext(),myHandler,name,password,3,0,null).start();
                                    Log.e("mmmmmmmm","ds");
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter1.notifyDataSetChanged();
                                        }
                                    });

                                    posts2.clear();
                                    if(likedList.size()==0){
                                        adapter2.notifyDataSetChanged();
                                    }else{
                                        for (int i=0;i<likedList.size();i++){
                                            new GetOnePost(myHandler,likedList.get(i)).start();
                                            Log.e("pppppppp",""+likedList.get(i));
                                        }
//                                    }


//                                }
                            }


                            break;

                        //get all posts
                            case 1:

                                if (true) {
                                    posts1.clear();
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

                                            for(int i=0;i<images.length;i++){
                                                images[i] = images[i].split("/")[3];
                                            }

                                            Log.e("content22", content);

                                            Log.e("postID",""+id);

                                            Log.e(cutter, "" + images.length);

                                            Post post;

                                            Drawable d = Drawable.createFromPath("storage/emulated/0/" + org_Array.getString("user_avator").split("/")[3]);

                                            if (d==null){
                                                new GetImage(myHandler, org_Array.getString("user_avator").split("/")[3], j, 1).start();
                                            }


                                            if (org_Array.getString("images") == "[]") {
                                                post = new Post(id, user_name, org_Array.getString("user_avator").split("/")[3], content, cutter, shop, price, new String[0], false, tag, is_tony_post);
                                            } else {
                                                Drawable dd = Drawable.createFromPath("storage/emulated/0/" + images[0]);

                                                if(dd==null){
                                                    new GetImage(myHandler, images[0], j,2).start();
                                                }


                                                post = new Post(id,user_name,user_avator,content,cutter,shop,price, images,false,tag,is_tony_post);

                                            }

                                            Log.e("iiiiiii","llll");
                                            posts1.add(post);
                                        }

                                        adapter1.notifyDataSetChanged();

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                }

                                break;

                            //get one post
                            case 11:
                                String jsonData = msg.obj.toString();

                                Log.e("ooooooooo","o"+posts2.size());

                                try {

                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    JSONArray orgArray = jsonObject.getJSONArray("posts");
                                    Log.e("ooooooooo","k"+orgArray.length());


                                    JSONObject org_Array = orgArray.getJSONObject(0);

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


                                    for (int i=0;i<posts2.size();i++){
                                        if(posts2.get(i).getId()==id){
                                            break;
                                        }
                                    }
                                    for(int i=0;i<images.length;i++){
                                        images[i] = images[i].split("/")[3];
                                    }

                                    Log.e("content22", content);

                                    Log.e("postID",""+id);

                                    Log.e(cutter, "" + images.length);

                                    Post post;

                                    Drawable d = Drawable.createFromPath("storage/emulated/0/" + org_Array.getString("user_avator").split("/")[3]);

                                    if (d==null){
                                        new GetImage(myHandler, org_Array.getString("user_avator").split("/")[3], 0, 1).start();
                                    }


                                    if (org_Array.getString("images") == "[]") {
                                        post = new Post(id, user_name, org_Array.getString("user_avator").split("/")[3], content, cutter, shop, price, new String[0], false, tag, is_tony_post);
                                    } else {
                                        Drawable dd = Drawable.createFromPath("storage/emulated/0/" + images[0]);

                                        if(dd==null){
                                            new GetImage(myHandler, images[0], 0,2).start();
                                        }


                                        post = new Post(id,user_name,user_avator,content,cutter,shop,price, images,false,tag,is_tony_post);

                                    }

                                    Log.e("iiiiiii","llll");
                                    posts2.add(post);


                                    adapter2.notifyDataSetChanged();

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }


                                break;

                            //get image
                            case 2:

                                if (pageID == 0) {

                                    final Bitmap bb = (Bitmap) msg.obj;

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            set1 = true;
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
                                                    MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                                                            imagefile.getAbsolutePath(), b.getString("filename"), null);
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }

                                                if (b.getInt("tag") == 1) {
                                                    posts1.get(b.getInt("id")).setUser_avator(b.getString("filename"));
                                                    Log.e("filename", b.getString("filename"));
                                                } else {
                                                    Log.e("addImageName", b.getString("filename"));
                                                    posts1.get(b.getInt("id")).addImageName(b.getString("filename"));
                                                }

                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.e("dfsa", "dfaf");
                                                        adapter1.notifyDataSetChanged();
                                                    }
                                                });
                                                Log.e("3", "contact1");
                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();

                                } else if (pageID == 1) {

                                    final Bitmap bb = (Bitmap) msg.obj;

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            set2 = true;
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
                                                    MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                                                            imagefile.getAbsolutePath(), b.getString("filename"), null);
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }

                                                if (b.getInt("tag") == 1) {
                                                    posts2.get(b.getInt("id")).setUser_avator(b.getString("filename"));
                                                    Log.e("filename", b.getString("filename"));
                                                } else {
                                                    Log.e("addImageName", b.getString("filename"));
                                                    posts2.get(b.getInt("id")).addImageName(b.getString("filename"));
                                                }

                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.e("dfsa", "dfaf");
                                                        adapter2.notifyDataSetChanged();
                                                    }
                                                });
                                                Log.e("3", "contact1");
                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }

                                break;
                        }

                    break;

                case 2:

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;

            }


        }
    };

    public void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 2:if(resultCode==RESULT_OK){
                if(Build.VERSION.SDK_INT>=19){
                    handleImageOnKitKat(data);
                }else{
                    handleImageBeforeKitKat(data);
                }
            }break;
            default:;
        }
    }


    //save the path of new picture for avator and display the new image
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(activity,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString("background_path",imagePath);
        editor.commit();
        displayImageBackground(imagePath);
    }

    //save the path of new picture for avator and display the new image
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void handleImageBeforeKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        imagePath=getImagePath(uri,null);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString("background_path",imagePath);
        editor.commit();
        displayImageBackground(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String Path=null;
        Cursor cursor=activity.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                Path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return Path;
    }


    //resize the picture to get a square
    public static Bitmap imageCrop(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int width = w > h ? h : w;

        int retX = (w - width) / 2;
        int retY = (h - width) / 2;

        return Bitmap.createBitmap(bitmap, retX, retY, width, width, null, false);
    }



    private void displayImageAvator(String Path){
        Bitmap bm= BitmapFactory.decodeFile(Path);
        avator.setImageBitmap(bm);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayImageBackground(String Path){
        Bitmap bm= BitmapFactory.decodeFile(Path);
        Drawable drawable =new BitmapDrawable(bm);
        background.setImageDrawable(drawable);
    }

}
