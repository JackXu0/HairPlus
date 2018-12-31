package wingsoloar.com.hairplus.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import wingsoloar.com.hairplus.Threads.GetAllPosts;
import wingsoloar.com.hairplus.Threads.GetImage;
import wingsoloar.com.hairplus.Threads.Login;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wingsolarxu on 2018/10/13.
 */

public class Filter_view extends Fragment {

    private View view;
    public static ArrayList<Post> original;
    public static ArrayList<Post> posts;

    private FloatingActionButton add_button;

    public static MyAdapter mAdapter;
    private FliterButtonAdapter filterAdapter1;
    private FliterButtonAdapter filterAdapter2;
    private FliterButtonAdapter filterAdapter3;

    private RecyclerView mRecyclerView;
    private RecyclerView filterRecycleView1;
    private RecyclerView filterRecycleView2;
    private RecyclerView filterRecycleView3;

    private StaggeredGridLayoutManager mLayoutManager;
    private LinearLayoutManager linearLayoutManager1;
    private LinearLayoutManager linearLayoutManager2;
    private LinearLayoutManager linearLayoutManager3;

    private ArrayList<String> choices1;
    private ArrayList<String> choices2;
    private ArrayList<String> choices3;
    private ArrayList<Integer> likedList;

    public static boolean onInitial;
    private SharedPreferences preferences;

    private static String[] selected = {"全部","全部","全部"};

    private Context context=getActivity();
    private static Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.filter_view_main, container, false);
        activity=getActivity();

        add_button = view.findViewById(R.id.add_button);

        onInitial = true;

        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        linearLayoutManager1 =new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager2 =new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager3 =new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);


        original = new ArrayList<>();
        posts = new ArrayList<>();
        choices1 = new ArrayList<>();
        choices2 = new ArrayList<>();
        choices3 = new ArrayList<>();
        likedList = new ArrayList<>();

//        new GetAllPosts(myHandler).start();

        choices1.add("全部");
        choices1.add("日式");
        choices1.add("美式");
        choices1.add("韩式");

        choices2.add("全部");
        choices2.add("男士");
        choices2.add("女士");

        choices3.add("全部");
        choices3.add("剪客");
        choices3.add("设计师");

        mAdapter = new MyAdapter(posts,likedList);
        filterAdapter1 = new FliterButtonAdapter(choices1,selected,1);
        filterAdapter2 = new FliterButtonAdapter(choices2, selected,2);
        filterAdapter3 = new FliterButtonAdapter(choices3,selected,3);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.filter_listview);
        filterRecycleView1 = view.findViewById(R.id.line1);
        filterRecycleView2 = view.findViewById(R.id.line2);
        filterRecycleView3 = view.findViewById(R.id.line3);


        mRecyclerView.setLayoutManager(mLayoutManager);
        filterRecycleView1.setLayoutManager(linearLayoutManager1);
        filterRecycleView2.setLayoutManager(linearLayoutManager2);
        filterRecycleView3.setLayoutManager(linearLayoutManager3);


        mRecyclerView.setAdapter(mAdapter);
        filterRecycleView1.setAdapter(filterAdapter1);
        filterRecycleView2.setAdapter(filterAdapter2);
        filterRecycleView3.setAdapter(filterAdapter3);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preferences=activity.getSharedPreferences("user_info",MODE_PRIVATE);
                String name=preferences.getString("name","");
                String password = preferences.getString("password","");

                Log.e("name",name);
                if(name.equals("")){
                    Log.e("111","111");
                    Intent intent = new Intent(activity,LoginActivity.class);
                    startActivity(intent);
                }

                Intent intent = new Intent(activity, PublishPost.class);
                activity.startActivity(intent);
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        Log.e("111","onresume");

        if(onInitial){
            selected[0]="全部";
            selected[1]="全部";
            selected[2]="全部";

            filterAdapter1.notifyDataSetChanged();
            filterAdapter2.notifyDataSetChanged();
            filterAdapter3.notifyDataSetChanged();
        }




        preferences=activity.getSharedPreferences("user_info",MODE_PRIVATE);
        String name=preferences.getString("name","");
        String password = preferences.getString("password","");

        Log.e("name",name);
        if(name.equals("")){
            Log.e("111","111");
            Intent intent = new Intent(activity,LoginActivity.class);
            startActivity(intent);
        }else{
            Log.e("222","222");
            new Login(getContext(),myHandler,name,password,0,0,null).start();
        }

        new Login(getContext(),myHandler,name,password,0,0,null).start();


//        new GetLikedList(myHandler, "ElfinHouse").start();
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

            holder.name.setText(posts.get(position).getUserName());
            holder.content.setText(posts.get(position).getContent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, PostDetail.class);
                    if (likedList.contains(posts.get(position).getId())){
                        posts.get(position).setLiked(true);
                    }else{
                        posts.get(position).setLiked(false);
                    }
                    intent.putExtra("post", posts.get(position));

                    activity.startActivity(intent);
                }
            });

            if (likedList.contains(posts.get(position).getId())){
                holder.collect_button.setImageResource(R.drawable.collected);
            }else {
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
                        new Login(getContext(),myHandler,name,password,2,posts.get(position).getId(),null).start();
//                        new DeleteLike(myHandler,"ElfinHouse",posts.get(position).getId()).start();
//                        holder.collect_button.setImageResource(R.drawable.uncollected);
//                        likedList.remove((Integer)posts.get(position).getId() );
                    }
                    else {
                        new Login(getContext(),myHandler,name,password,1,posts.get(position).getId(),null).start();
//                        new AddLike(myHandler,"ElfinHouse",posts.get(position).getId()).start();
//                        holder.collect_button.setImageResource(R.drawable.collected);
//                        likedList.add((Integer)posts.get(position).getId());

                    }
//                    mAdapter.notifyDataSetChanged();

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


    class FliterButtonAdapter extends RecyclerView.Adapter<FliterButtonAdapter.ViewHolder> {

        private ArrayList<String> options;
        private String[] selected;
        private int line;

        public FliterButtonAdapter(@Nullable ArrayList<String> options, String[] selected, int line) {

            this.options = options;
            this.selected = selected;
            this.line = line;

        }

        @Override
        public FliterButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_button, parent, false);
            // 实例化viewholder
            FliterButtonAdapter.ViewHolder viewHolder = new FliterButtonAdapter.ViewHolder(v);

            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(FliterButtonAdapter.ViewHolder holder, final int position) {
            // 绑定数据

            holder.button.setText(options.get(position));

            switch(line){
                case 1:
                    if(options.get(position).equals(selected[0])){
                        holder.button.setBackgroundResource(R.drawable.filter_button_style);
                    }else{
                        holder.button.setBackground(null);
                    }
                    break;
                case 2:
                    if(options.get(position).equals(selected[1])){
                        holder.button.setBackgroundResource(R.drawable.filter_button_style);
                    }else{
                        holder.button.setBackground(null);
                    }
                    break;
                case 3:
                    if(options.get(position).equals(selected[2])){
                        holder.button.setBackgroundResource(R.drawable.filter_button_style);
                    }else{
                        holder.button.setBackground(null);
                    }
                    break;
            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected[line-1] = options.get(position);
//                    filterAdapter1 = new FliterButtonAdapter(choices1,selected,1);
//                    filterAdapter2 = new FliterButtonAdapter(choices2, selected,2);
//                    filterAdapter3 = new FliterButtonAdapter(choices3,selected,3);
                    filterAdapter1.notifyDataSetChanged();
                    filterAdapter2.notifyDataSetChanged();
                    filterAdapter3.notifyDataSetChanged();
//                    notifyDataSetChanged();
                    reset();
                    mAdapter.notifyDataSetChanged();

                }
            });

        }


        @Override
        public int getItemCount() {
            return options == null ? 0 : options.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            Button button;


            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                button = itemView.findViewById(R.id.filter_button);

            }
        }
    }

    private void reset(){
        posts.clear();

        Log.e("selected1",selected[0]);
        Log.e("selected2",selected[1]);
        Log.e("selected3",selected[2]);


        switch (selected[1]){
            case "全部":
                for(int i=0; i<original.size(); i++) {
                    posts.add(original.get(i));
                }
                break;

            default:
                for(int i=0; i<original.size(); i++) {
                    if(original.get(i).getTags().contains(selected[1]))
                        posts.add(original.get(i));
                }
                break;
        }


        switch (selected[0]){
            case "全部":

                break;

            default:

                int size = posts.size();
                for(int i=size-1; i>=0; i--) {
                    Log.e("tag",posts.get(i).getTags());
                    Log.e("ifContain",posts.get(i).getTags().contains(selected[0])+"");
                    if(!posts.get(i).getTags().contains(selected[0]))
                        posts.remove(posts.get(i));
                }
                break;
        }

        switch (selected[2]){

            case "全部":


                break;
            case "剪客":

                int size = posts.size();
                for(int i=size-1; i>=0; i--) {
                    if(posts.get(i).is_tony_post()==true)
                        posts.remove(posts.get(i));
                }
                break;
            case "发型师":
                size = posts.size();
                for(int i=size-1; i>=0; i--) {
                    if(posts.get(i).is_tony_post()==false)
                        posts.remove(posts.get(i));
                }
                break;
        }

    }

    private Handler myHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(final Message msg) {
            final Bundle b;
            int code;
            b=msg.getData();
            code=b.getInt("response_code");
            switch (code){
                case 0:
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"Please Check Internet Access!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case 1:
                    switch(msg.what){

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

                        //loign
                        case 13:
                            String res = msg.obj.toString();

                            Log.e("rrrrrrrrrr",res);
                            if(res.equals("Login")){

                            }else if(res.equals("Not Login")){
                                Intent intent = new Intent(activity,LoginActivity.class);
                                activity.startActivity(intent);
                            }
                            break;

                        //get likelist
                        case 10:
                            likedList.clear();
                            String likelist = (String) msg.obj;
                            Log.e("likelist+++++",likelist);
                            String[] likelist_list = likelist.split(",");

                            if(likelist.contains(",")){
                                for(int i=0;i<likelist_list.length;i++){
                                    likedList.add(Integer.parseInt(likelist_list[i]));
                                }
                            }



                            Log.e("ffffffff","f"+onInitial);
                            if(onInitial){
                                onInitial = false;
                                new GetAllPosts(myHandler).start();
                            }else{
                                Log.e("mmmmmmmm","ds");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                            break;

                        //get all posts
                        case 1:

                            original.clear();
                            posts.clear();

                            String jsonData = msg.obj.toString();

                            if(jsonData.equals("Not Login")){
                                Intent intent = new Intent(activity,LoginActivity.class);
                                activity.startActivity(intent);
                                break;
                            }

                            Log.e("dfaf",jsonData.replace("\"",""));

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

                                    Log.e("testImages",org_Array.getString("images"));
                                    String[] images = org_Array.getString("images").replace("[","").replace("]","").replace("\"","").split(",");

                                    for(int i=0;i<images.length;i++){
                                        if(images[i].length()>3)
                                            images[i] = images[i].split("/")[3];

                                    }

                                    Log.e(cutter, ""+images.length);

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

                                    original.add(post);
                                    posts.add(post);

                                }

                                mAdapter.notifyDataSetChanged();

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            break;

                        //get image
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
                                            MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                                                    imagefile.getAbsolutePath(), b.getString("filename"), null);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        if (b.getInt("tag") == 1) {
                                            original.get(b.getInt("id")).setUser_avator(b.getString("filename"));
                                            posts.get(b.getInt("id")).setUser_avator(b.getString("filename"));
                                            Log.e("filename", b.getString("filename"));
                                        } else {
                                            Log.e("kkkkk", "id=" + b.getInt("id") + " postID=" + original.get(b.getInt("id")).getId() + " size=" + original.get(b.getInt("id")).getImageNames().length);
                                            original.get(b.getInt("id")).addImageName(b.getString("filename"));
                                            posts.set(b.getInt("id"), original.get(b.getInt("id")));

                                        }

                                        activity.runOnUiThread(new Runnable() {
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"Server Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;


            }
        }
    };
}
