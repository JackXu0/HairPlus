package wingsoloar.com.hairplus.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import wingsoloar.com.hairplus.Objects.Post;
import wingsoloar.com.hairplus.R;
import wingsoloar.com.hairplus.Threads.Login;
import wingsoloar.com.hairplus.Threads.UploadImage;

import static wingsoloar.com.hairplus.Activities.Filter_view.onInitial;
import static wingsoloar.com.hairplus.Activities.Filter_view.posts;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class PublishPost extends Activity {

    private View view;
    private TextView upload;
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private File outputImage;
    private GridView gridView;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private ArrayList<File> imageFiles;
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private TextView cancel;
    private TextView publish;
    private EditText content;
//    private RecyclerView recyclerView;
//    public static MyRecycleViewAdapter myAdapter;
    private LinearLayoutManager mLayoutManager;
//    public static String[] texts = new String[1];
    public static int shopID=0;
    private int uploadCounter=0;
    private ArrayList<Integer> imageIDs;
    private SharedPreferences preferences;
    private RatingBar ratingBar;
    private int ratingbar_value=100;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView tagView;

    private TagAdapter tagAdapter;
    private ArrayList<String> options;
    private ArrayList<Boolean> isSelected;
    private RelativeLayout barber_choser;
    public static TextView barber_name_tv;
    public static String barber_name;
    public static String shop_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_post_main);

        imageFiles = new ArrayList<>();
        options = new ArrayList<>();
        isSelected = new ArrayList<>();

        upload = findViewById(R.id.text);
        gridView = findViewById(R.id.gridview);

        cancel = findViewById(R.id.cancel);
        publish = findViewById(R.id.publish);
//        recyclerView = findViewById(R.id.recycleview);
        content = findViewById(R.id.content);
        tagView = findViewById(R.id.tagView);
        barber_choser = findViewById(R.id.barber);
        barber_name_tv = findViewById(R.id.text);
        ratingBar = findViewById(R.id.rating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.e("rating",v+"");
                ratingbar_value = (int) (v*20);
            }
        });

        linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        options.add("日式");
        options.add("美式");
        options.add("韩式");
        options.add("男士");
        options.add("女士");

        isSelected.add(false);
        isSelected.add(false);
        isSelected.add(false);
        isSelected.add(false);
        isSelected.add(false);

//        texts[0] = "选择发型师";
//        texts[1] = "理发师";

        barber_choser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishPost.this,LocationSelect.class);
                startActivity(intent);
            }
        });

        tagAdapter = new TagAdapter(options,isSelected);

        tagView.setLayoutManager(linearLayoutManager);
        tagView.setAdapter(tagAdapter);


//        myAdapter = new MyRecycleViewAdapter(texts);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(myAdapter);

        Log.e("create","vv"+getIntent().getIntExtra("shopID",0));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCounter = 0;
                imageIDs = new ArrayList<Integer>(imageFiles.size());

                if(imageFiles.size()==0){
                    Toast.makeText(getApplicationContext(),"You Must Include a Image",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(content.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"You Must Write Something",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(barber_name_tv.getText().toString().equals("选择发型师")){
                    Toast.makeText(getApplicationContext(),"You Must Choose a Barber",Toast.LENGTH_SHORT).show();
                    return;
                }



                for (int i=0;i<imageFiles.size();i++){
                    new UploadImage(myHandler,imageFiles.get(i)).start();
                }



            }
        });

        bitmaps = new ArrayList<>();

        mGridViewAddImgAdapter = new GridViewAdapter(getApplicationContext(), bitmaps);
        gridView.setAdapter(mGridViewAddImgAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("imgefilesize",imageFiles.size()+"");
                if (i == adapterView.getChildCount() - 1) {

                    if(imageFiles.size()<9){
                        takephoto();
                    }else{
                        Toast.makeText(PublishPost.this, "最多可选9张图片",Toast.LENGTH_SHORT).show();
                    }
                }}
        });

        ActivityCompat.requestPermissions(PublishPost.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }

    @Override
    protected void onResume() {
        super.onResume();

        preferences=getSharedPreferences("user_info",MODE_PRIVATE);
        String name=preferences.getString("name","");
        String password = preferences.getString("password","");


        Log.e("name",name);
        if(name.equals("")){
            Log.e("111","111");
            Intent intent = new Intent(PublishPost.this,LoginActivity.class);
            startActivity(intent);
        }else{
            Log.e("222","222");
            new Login(PublishPost.this,myHandler,name,password,0,0,null).start();
        }

    }

    private void takephoto(){
        outputImage = new File(Environment.getExternalStorageDirectory(), "output_image"+(int)(Math.random()*100000)+".jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(PublishPost.this, "wingsoloar.com.hairplus.fileprovider", outputImage);

        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        FileOutputStream b=null;
//                        File file = new File(new URI(imageUri.toString()));
                        try {
                            b = new FileOutputStream(outputImage);
                            bm.compress(Bitmap.CompressFormat.JPEG, 25, b);// 把数据写入文件
                            bm = imageCrop(bm);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                b.flush();
                                b.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e(outputImage.length()+"","map1");
//                        image.setImageBitmap(bm);
//                        new UploadImage(myHandler,outputImage).start();
                        mPicList.add(outputImage.getName());
                        Log.e("mPicList", outputImage.getName());
                        imageFiles.add(outputImage);
                        bitmaps.add(bm);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGridViewAddImgAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            default:
                break;
        }
    }
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            Bundle b;
            int code;
            switch (msg.what){

                case 3:
                    b=msg.getData();
                    code=b.getInt("response_code");

                    switch(code){
                        case 0:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PublishPost.this,"Please Check Internet Access!",Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                        case 1:

                            int id = b.getInt("imageID");
                            imageIDs.add(id);
                            uploadCounter++;

                            String tag="";

                            for(int i =0; i <options.size();i++){
                                if(isSelected.get(i)==true)
                                    tag = tag + options.get(i) + ";";
                            }

                            if (tag.contains(";"))
                                tag = tag.substring(0,tag.length()-1);

                            if(uploadCounter==imageFiles.size()){

                                preferences=getSharedPreferences("user_info",MODE_PRIVATE);
                                String name=preferences.getString("name","");
                                String password = preferences.getString("password","");

                                JSONObject objData = new JSONObject();
                                JSONArray jsonArray = new JSONArray();
                                try {
                                    objData.put("username",name);
                                    objData.put("shop", shop_name);
                                    objData.put("cutter", barber_name);
                                    objData.put("content", content.getText());
                                    objData.put("price",23);
                                    objData.put("tag",tag);
                                    jsonArray.put(imageIDs);
                                    objData.put("imageIDs",jsonArray);
                                    objData.put("rating",ratingbar_value);

                                    Log.e("dfafa", objData.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                                new Login(getApplicationContext(),myHandler,name,password,4,0,objData).start();
//                                new UploadPost(myHandler, objData).start();
                            }

                            String[] s = new String[mPicList.size()];
                            for(int i=0; i<mPicList.size();i++){
                                s[i] = mPicList.get(i);
                            }

//                            Post post =new Post(id,"ElfinHouse","dfas",content.getText().toString(),barber_name,shop_name,23, s,false,tag,false);
//
//                            posts.add(post);
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
////                                    Filter_view.mAdapter.notifyDataSetChanged();
//                                }
//                            });

                            Log.e("dfa","success"+id);
                            break;
                        case 2:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PublishPost.this,"Server Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }

                    break;

                //upload a post
                case 4:
                    b=msg.getData();
                    code=b.getInt("response_code");

                    onInitial = true;

                    finish();

                    switch(code){
                        case 0:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PublishPost.this,"Please Check Internet Access!",Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                        case 1:

                            Log.e("dfa","success111");
                            break;
                        case 2:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PublishPost.this,"Server Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }

                    break;



            }
        }
    };

    public class GridViewAdapter extends android.widget.BaseAdapter {

        private Context mContext;
        private ArrayList<Bitmap> bitmaps;
        private LayoutInflater inflater;

        public GridViewAdapter(Context mContext, ArrayList<Bitmap> bitmaps) {
            this.mContext = mContext;
            this.bitmaps = bitmaps;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            //return mList.size() + 1;//因为最后多了一个添加图片的ImageView
            return bitmaps == null ? 1 : bitmaps.size() + 1;

        }

        @Override
        public Object getItem(int position) {
            return bitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.test_layout_child, parent,false);
            ImageView iv = convertView.findViewById(R.id.image);
            if (position < bitmaps.size()) {
                Log.e("dafaf","size:"+getCount()+" index: "+(getCount()-position-2));
                iv.setImageBitmap(bitmaps.get(getCount()-position-2));

            } else {
                iv.setImageResource(R.drawable.add_button);//最后一个显示加号图片
            }
            return convertView;
        }
    }

//    class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {
//
//        private String[] texts;
//
//        public MyRecycleViewAdapter(@Nullable String[] texts) {
//
//            this.texts = texts;
//
//        }
//
//        @Override
//        public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            // 实例化展示的view
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.publish_post_child, parent, false);
//            // 实例化viewholder
//            MyRecycleViewAdapter.ViewHolder viewHolder = new MyRecycleViewAdapter.ViewHolder(v);
//
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(MyRecycleViewAdapter.ViewHolder holder, final int position) {
//            // 绑定数据
//            holder.name_text.setText(texts[position]);
//
//            if(position == 0){
//                holder.icon.setImageResource(R.drawable.location_icon);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(PublishPost.this, LocationSelect.class);
//                        startActivity(intent);
//                    }
//                });
//            }else{
//                holder.icon.setImageResource(R.drawable.cutter_icon);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(shopID==0){
//                            Toast.makeText(PublishPost.this,"请先选择理发店",Toast.LENGTH_SHORT).show();
//                        }else{
////                            Intent intent = new Intent(PublishPost.this, CutterSelect.class);
////                            intent.putExtra("shopID",shopID);
////                            startActivity(intent);
//                        }
//
//                    }
//                });
//            }
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return 2;
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//
//            View itemView;
//            TextView name_text;
//            ImageView icon;
//
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                this.itemView=itemView;
//                name_text = itemView.findViewById(R.id.text);
//                icon = itemView.findViewById(R.id.icon);
//
//            }
//        }
//    }

    class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

        private ArrayList<String> options;
        private ArrayList<Boolean> isSelected;

        public TagAdapter(@Nullable ArrayList<String> options, ArrayList<Boolean> isSelected) {

            this.options = options;
            this.isSelected = isSelected;

        }

        @Override
        public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_button, parent, false);
            // 实例化viewholder
            TagAdapter.ViewHolder viewHolder = new TagAdapter.ViewHolder(v);

            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final TagAdapter.ViewHolder holder, final int position) {
            // 绑定数据

            holder.button.setText(options.get(position));
            if(isSelected.get(position)==false){
                holder.button.setBackgroundResource(R.drawable.filter_button_style);
                holder.button.setTextColor(Color.BLACK);
            }else{
                holder.button.setBackgroundResource(R.drawable.tag_selected_style);
                holder.button.setTextColor(Color.WHITE);

            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSelected.set(position,!isSelected.get(position));
                    if(isSelected.get(position)==false){
                        holder.button.setBackgroundResource(R.drawable.filter_button_style);
                        holder.button.setTextColor(Color.BLACK);

                    }else{
                        holder.button.setBackgroundResource(R.drawable.tag_selected_style);
                        holder.button.setTextColor(Color.WHITE);

                    }
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

    public static Bitmap imageCrop(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int width = w > h ? h : w;

        int retX = (w - width) / 2;
        int retY = (h - width) / 2;

        return Bitmap.createBitmap(bitmap, retX, retY, width, width, null, false);
    }
}
