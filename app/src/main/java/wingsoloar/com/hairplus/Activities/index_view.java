package wingsoloar.com.hairplus.Activities;

/**
 * Created by wingsolarxu on 2018/10/13.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import wingsoloar.com.hairplus.Activities.CutterDetail;
import wingsoloar.com.hairplus.Objects.CutterProfile;
import wingsoloar.com.hairplus.R;
import wingsoloar.com.hairplus.Threads.GetAllCutters;
import wingsoloar.com.hairplus.Threads.GetImage;

/**
 * Created by wingsolarxu on 2018/4/26.
 */

public class index_view extends Fragment {

    private View view;
    private ArrayList<String> avators;
    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private TextView textView_1;
    private TextView textView_2;
    private TextView textView_3;
    private MyAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout filter_bar;
    private LinearLayout divider;
    private RelativeLayout filter_1;
    private RelativeLayout filter_2;
    private RelativeLayout filter_3;
    private ImageView arrow1;
    private ImageView arrow2;
    private ImageView arrow3;
    private my_popupview popupwindow_1;
    private my_popupview popupwindow_2;
    private my_popupview popupwindow_3;
    private FrameLayout frameLayout;
    private ArrayList<CutterProfile> origin;
    private ArrayList<CutterProfile> cutterProfiles;
    private TextView rating;

    private Context context=getActivity();
    private static Activity activity;
    private ViewGroup container;
    private LayoutInflater inflater;
    private ArrayList<String> options1;
    private ArrayList<String> options2;
    private ArrayList<String> options3;
    private String[] selected = {"全部","全部","全部"};
    private int counter = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.index_view_main, container, false);
        activity=getActivity();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.container=container;
        this.inflater = inflater;

        counter = 0;

        filter_bar = view.findViewById(R.id.filter_bar);
        filter_1 = view.findViewById(R.id.filter_1);
        filter_2 = view.findViewById(R.id.filter_2);
        filter_3 = view.findViewById(R.id.filter_3);
        frameLayout = view.findViewById(R.id.frag);
        divider = view.findViewById(R.id.divider);
        textView_1 = view.findViewById(R.id.textview_1);
        textView_2 = view.findViewById(R.id.textview_2);
        textView_3 = view.findViewById(R.id.textview_3);
        rating = view.findViewById(R.id.rating);
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        options1 = new ArrayList<>();
        options2 = new ArrayList<>();
        options3 = new ArrayList<>();
        cutterProfiles = new ArrayList<>();
        origin = new ArrayList<>();

        options1.add("全部");
        options1.add("可爱");
        options1.add("前卫");
        options1.add("甜美");
        options1.add("摩登");

        options3.add("全部");
        options3.add("50元以下");
        options3.add("50-150元");
        options3.add("150-500元");
        options3.add("500-1000元");
        options3.add("1000元以上");

        options2.add("全部");
        options2.add("洗护");
        options2.add("剪发");
        options2.add("烫发");
        options2.add("染发");


        popupwindow_1 = new my_popupview(activity,options1,1);
        popupwindow_2 = new my_popupview(activity,options2,2);
        popupwindow_3 = new my_popupview(activity,options3,3);

        mAdapter = new MyAdapter(cutterProfiles);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.index_listview);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        filter_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView_1.setTextColor(Color.parseColor("#9921AAAA"));
                arrow1.setImageResource(R.drawable.arrow_up);

                popupwindow_1.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        dismissAnimator().start();
                        arrow1.setImageResource(R.drawable.arrow_down);
                        resetOption(0);
                    }
                });

                if(popupwindow_2.isShowing()){
                    popupwindow_2.dismiss();
                }
                if(popupwindow_3.isShowing()){
                    popupwindow_3.dismiss();
                }

                if (!popupwindow_1.isShowing()) {

                    popupwindow_1.showAsDropDown(divider, 0, 0);
                    showAnimator().start();
                    Log.e("iiiiiiii","11");

                } else {
                    popupwindow_1.dismiss();
                    dismissAnimator().start();
                }




            }
        });

        filter_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(popupwindow_1.isShowing()){
                    popupwindow_1.dismiss();

                }
                if(popupwindow_3.isShowing()){
                    popupwindow_3.dismiss();
                }
                if (!popupwindow_2.isShowing()) {
                    popupwindow_2.showAsDropDown(divider, 0, 0);
                    showAnimator().start();
                    textView_2.setTextColor(Color.parseColor("#9921AAAA"));
                    arrow2.setImageResource(R.drawable.arrow_up);

                } else {
                    popupwindow_2.dismiss();
//                    dismissAnimator().start();
                }


                popupwindow_2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        arrow2.setImageResource(R.drawable.arrow_down);
                        resetOption(0);
                        dismissAnimator().start();
                    }
                });
            }
        });
        filter_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(popupwindow_1.isShowing()){
                    popupwindow_1.dismiss();

                }
                if(popupwindow_2.isShowing()){
                    popupwindow_2.dismiss();
                }
                if (!popupwindow_3.isShowing()) {
                    popupwindow_3.showAsDropDown(divider, 0, 0);
                    showAnimator().start();
                    textView_3.setTextColor(Color.parseColor("#9921AAAA"));
                    arrow3.setImageResource(R.drawable.arrow_up);

                } else {
                    popupwindow_3.dismiss();
//                    dismissAnimator().start();
                }


                popupwindow_3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        arrow3.setImageResource(R.drawable.arrow_down);

                        resetOption(0);
                        dismissAnimator().start();
                    }
                });
            }
        });


        resetOption(0);

        frameLayout.getForeground().setAlpha( 0);

        new GetAllCutters(myHandler).start();


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

        resetOption(0);
    }

    private void openAnimation(){
        //设置展开的基准位置,从顶部开始展开(默认是中心位置展开收缩)
        final View view2= inflater.inflate(R.layout.index_view_main, container, false);
        view2.setPivotY(0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(filter_bar,"scaleY",0f,1f);
        scaleY.setDuration(300);
        view2.setVisibility(View.VISIBLE);
        scaleY.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override public void onAnimationEnd(Animator animation) {
//                if(viewMask!=null){
//                    viewMask.setVisibility(View.VISIBLE);
//                }

                view2.setVisibility(View.VISIBLE);



            }

            @Override public void onAnimationCancel(Animator animation) {

            }

            @Override public void onAnimationRepeat(Animator animation) {

            }
        });
        scaleY.start();
    }

    public class my_popupview extends PopupWindow {

        private View conentView;
        private Context context;
        private ArrayList<String> options;
        private int column;


        public my_popupview(final Context context, ArrayList<String> options, int column) {
            super(context);
            this.context = context;
            this.options = options;
            this.column = column;
            this.initPopupWindow();

        }

        private void initPopupWindow() {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popupview_main, null);
            this.setContentView(conentView);
            this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            this.setFocusable(false);
            this.setTouchable(true);
            this.setOutsideTouchable(true);
            this.update();
            ColorDrawable dw = new ColorDrawable(0000000000);
            this.setBackgroundDrawable(dw);
            this.setAnimationStyle(R.style.AnimationPreview);
            RecyclerView recyclerView = conentView.findViewById(R.id.recycleview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
            OptionAdapter adapter = new OptionAdapter(options, selected,column);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);


        }
    }
    class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

        private ArrayList<String> options;
        private String[] selected;
        private int column;

        public OptionAdapter(@Nullable ArrayList<String> options,
                             String[] selected,
                             int column) {

            this.options = options;
            this.selected = selected;
            this.column=column;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popupview_child, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new OptionAdapter.ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.text.setText(options.get(position));

            switch(column){
                case 1:
                    if(options.get(position).equals(selected[0])){
                        holder.checkbox.setImageResource(R.drawable.check_button_blue);
                    }else{
                        holder.checkbox.setImageDrawable(null);
                    }
                    break;
                case 2:
                    if(options.get(position).equals(selected[1])){
                        holder.checkbox.setImageResource(R.drawable.check_button_blue);
                    }else{
                        holder.checkbox.setImageDrawable(null);
                    }
                    break;

                case 3:
                    if(options.get(position).equals(selected[2])){
                        holder.checkbox.setImageResource(R.drawable.check_button_blue);
                    }else{
                        holder.checkbox.setImageDrawable(null);
                    }
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selected[column-1]=options.get(position);
                    Log.e("option now",options.get(position));
                    notifyDataSetChanged();
                    resetOption(1);


                    if(popupwindow_1.isShowing()){
                        popupwindow_1.dismiss();
                    }

                    if(popupwindow_2.isShowing()){
                        popupwindow_2.dismiss();
                    }

                    if(popupwindow_3.isShowing()){
                        popupwindow_3.dismiss();
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
            ImageView checkbox;
            TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                checkbox = itemView.findViewById(R.id.checkbox);
                text= itemView.findViewById(R.id.text);
            }
        }
    }

    private void resetOption(int mode){
        textView_1.setText(selected[0]);
        textView_2.setText(selected[1]);
        textView_3.setText(selected[2]);

        cutterProfiles.clear();

        if(selected[0].equals("全部")){
            textView_1.setText("发型风格");
            if(mode==0){
                textView_1.setTextColor(Color.parseColor("#000000"));
            }else{
                textView_1.setTextColor(Color.parseColor("#9921AAAA"));

            }

            for(int i=0;i<origin.size();i++){
                cutterProfiles.add(origin.get(i));
            }

            mAdapter.notifyDataSetChanged();

        }else{
            textView_1.setTextColor(Color.parseColor("#9921AAAA"));
            arrow1.setImageResource(R.drawable.arrow_dow2);
            for(int i=0;i<origin.size();i++){
                if(origin.get(i).getStyle().contains(selected[0])){
                    cutterProfiles.add(origin.get(i));
                }
            }



        }

        if(selected[1].equals("全部")){
            textView_2.setText("洗剪烫染");
            if(mode==0){
                textView_2.setTextColor(Color.parseColor("#000000"));
            }else{
                textView_2.setTextColor(Color.parseColor("#9921AAAA"));

            }
        }else{
            textView_2.setTextColor(Color.parseColor("#9921AAAA"));
            arrow2.setImageResource(R.drawable.arrow_dow2);

            ArrayList<CutterProfile> temp = new ArrayList<>();

            for(int i=0;i<cutterProfiles.size();i++){
                temp.add(cutterProfiles.get(i));
            }

            cutterProfiles.clear();

            for(int i=0;i<temp.size();i++){
                if(temp.get(i).getSkills().contains(selected[1])){
                    cutterProfiles.add(temp.get(i));
                }
            }
        }

        if(selected[2].equals("全部")){
            textView_3.setText("价格区间");
            if(mode==0){
                textView_3.setTextColor(Color.parseColor("#000000"));
            }else{
                textView_3.setTextColor(Color.parseColor("#9921AAAA"));

            }
        }else{

            ArrayList<CutterProfile> temp = new ArrayList<>();

            for(int i=0;i<cutterProfiles.size();i++){
                temp.add(cutterProfiles.get(i));
            }

            cutterProfiles.clear();

            for(int i=0;i<temp.size();i++){
                switch (selected[2]){
                    case "50元以下":
                        if(temp.get(i).getMaxPrice()<=50){
                            cutterProfiles.add(temp.get(i));
                        }
                        break;
                    case "50-150元":
                        if((temp.get(i).getMaxPrice()<=150&&temp.get(i).getMaxPrice()>50)||(temp.get(i).getMinPrice()>50&&temp.get(i).getMinPrice()<=150)){
                            cutterProfiles.add(temp.get(i));
                        }
                        break;
                    case "150-500元":
                        if((temp.get(i).getMaxPrice()<=500&&temp.get(i).getMaxPrice()>150)||(temp.get(i).getMinPrice()>150&&temp.get(i).getMinPrice()<=500)){
                            cutterProfiles.add(temp.get(i));
                        }
                        break;
                    case "500-1000元":
                        if((temp.get(i).getMaxPrice()<=1000&&temp.get(i).getMaxPrice()>500)||(temp.get(i).getMinPrice()>500&&temp.get(i).getMinPrice()<=1000)){
                            cutterProfiles.add(temp.get(i));
                        }
                        break;
                    case "1000元以上":
                        if(temp.get(i).getMinPrice()>1000){
                            cutterProfiles.add(temp.get(i));
                        }
                        break;
                }
            }


            textView_3.setTextColor(Color.parseColor("#9921AAAA"));
            arrow3.setImageResource(R.drawable.arrow_dow2);

        }

        mAdapter.notifyDataSetChanged();

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<CutterProfile> cutterProfiles;

        public MyAdapter(@Nullable ArrayList<CutterProfile> cutterProfiles) {

            this.cutterProfiles = cutterProfiles;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_view_child, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new MyAdapter.ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {


            holder.name.setText(cutterProfiles.get(position).getName());
            holder.price.setText(cutterProfiles.get(position).getStyle());
            holder.rating1.setText(""+cutterProfiles.get(position).getRating());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CutterDetail.class);
                    intent.putExtra("cutter_name", cutterProfiles.get(position).getName());
                    intent.putExtra("cutter_experience", cutterProfiles.get(position).getExperience());
                    intent.putExtra("cutter_tel",cutterProfiles.get(position).getTel());
                    intent.putExtra("cutter_location",cutterProfiles.get(position).getLocation());
                    intent.putExtra("minPrice",cutterProfiles.get(position).getMinPrice());
                    intent.putExtra("maxPrice",cutterProfiles.get(position).getMaxPrice());
                    intent.putExtra("cutter_introduction",cutterProfiles.get(position).getIntroduction());
                    intent.putExtra("cutter_rating",cutterProfiles.get(position).getRating());
                    intent.putExtra("cutter_avator",cutterProfiles.get(position).getAvator());
                    getActivity().startActivity(intent);
                }
            });



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {


                    Log.e("filenameiiiiii",cutterProfiles.get(position).getAvator());
                    Drawable d = Drawable.createFromPath("storage/emulated/0/"+cutterProfiles.get(position).getAvator());

                    if(d!=null){
                        holder.avator.setImageDrawable(d);
                    }


                for(int i=0;i<cutterProfiles.get(position).getImageNames().length;i++){
                    if(i>=3){
                        return;
                    }

                    if(cutterProfiles.get(position).getImageNames()[i]!=null){
                        Drawable dd = Drawable.createFromPath("storage/emulated/0/"+cutterProfiles.get(position).getImageNames()[i]);
//                Bitmap b = BitmapFactory.decodeByteArray(posts.get(position).getImages()[0], 0, posts.get(position).getImageNames()[0]);
                        if(dd!=null){
                            switch (i){
                                case 0:
                                    holder.image1.setImageDrawable(dd);
                                    break;
                                case 1:
                                    holder.image2.setImageDrawable(dd);
                                    break;
                                case 2:
                                    holder.image3.setImageDrawable(dd);
                                    break;
                            }
                        }

                    }



                }


            }

//            holder.title.setText(titles.get(position));

        }


        @Override
        public int getItemCount() {
            return cutterProfiles == null ? 0 : cutterProfiles.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            ImageView image1;
            ImageView image2;
            ImageView image3;
            ImageView avator;
            TextView name;
            TextView price;
            TextView rating1;



            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                avator = itemView.findViewById(R.id.avator);
                image1=itemView.findViewById(R.id.designer_work1);
                image2=itemView.findViewById(R.id.designer_work2);
                image3=itemView.findViewById(R.id.designer_work3);
                name=itemView.findViewById(R.id.user_name);
                price = itemView.findViewById(R.id.price_right);
                rating1 =itemView.findViewById(R.id.rating);
            }
        }
    }



    //used for dismissing filter popup window
    private ValueAnimator dismissAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                frameLayout.getForeground().setAlpha( 0);
            }
        });

        animator.setDuration((long) 0.5);

        return animator;
    }

    //used to show filter popup window
    private ValueAnimator showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.7f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                frameLayout.getForeground().setAlpha( 120);
            }
        });

        animator.setDuration((long) 0.5);
        return animator;
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            final Bundle b;
            int code;
            switch (msg.what){

                case 1:
                    b=msg.getData();
                    code=b.getInt("response_code");

                    cutterProfiles.clear();
                    origin.clear();

                    switch(code){
                        case 0:
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity,"Please Check Internet Access!",Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                        case 1:

                            String jsonData = msg.obj.toString();

                            Log.e("dfaf",jsonData.replace("\"",""));

                            try {

                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONArray orgArray = jsonObject.getJSONArray("cutters");

                                for (int j = 0; j < orgArray.length(); j++) {
                                    JSONObject org_Array = orgArray.getJSONObject(j);

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
                                    String user_avator = "";
                                    String[] images = org_Array.getString("images").replace("[","").replace("]","").replace("\"","").split(",");

                                    CutterProfile profile;

//                                    counter = counter+images.length;

                                    new GetImage(myHandler, org_Array.getString("avator_address").split("/")[3], j,1).start();

                                    if(org_Array.getString("images")=="[]"){
                                        profile = new CutterProfile(id,shopName,user_name, new String[0],user_avator,style,skills,rating,minPrice,maxPrice,experience,tel,location,introduction);
                                    }else {

                                        for (int i = 0; i < images.length; i++) {
                                            new GetImage(myHandler, images[i].split("/")[3], j,2).start();
                                            Log.e("size2", images[i].split("/")[3]);
                                        }
//                                        new GetImage(myHandler, images[0].split("/")[3], j,2).start();
                                        profile = new CutterProfile(id,shopName,user_name, new String[images.length],user_avator,style,skills,rating,minPrice,maxPrice,experience,tel,location,introduction);
                                    }

                                    cutterProfiles.add(profile);
                                    origin.add(profile);
                                }

//                                mAdapter.notifyDataSetChanged();
                                resetOption(0);


                            } catch (Exception e) {

                                e.printStackTrace();
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

                    break;

                case 2:
                    b=msg.getData();
                    code=b.getInt("response_code");

                    switch(code){
                        case 0:
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity,"Please Check Internet Access!",Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                        case 1:
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
                                            MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                                                    imagefile.getAbsolutePath(), b.getString("filename"), null);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        Log.e("filenamefffff",b.getString("filename"));

                                        if(b.getInt("tag")==1){
                                            cutterProfiles.get(b.getInt("id")).setAvator(b.getString("filename"));
                                            origin.get(b.getInt("id")).setAvator(b.getString("filename"));
                                            Log.e("filenamerrrr", b.getString("filename"));
                                        }else{
                                            Log.e("addImageName",b.getString("filename"));
                                            cutterProfiles.get(b.getInt("id")).addImageName(b.getString("filename"));
                                            origin.get(b.getInt("id")).addImageName(b.getString("filename"));
                                        }

                                        counter = counter+1;

                                        Log.e("counter","o"+counter);

//                                if(counter==2){
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Log.e("dfsa","dfaf");
//                                            mAdapter.notifyDataSetChanged();
//                                            counter=0;
//                                        }
//                                    });
//                                }

                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("dfsa","dfaf");
                                                mAdapter.notifyDataSetChanged();
                                                counter=0;
                                            }
                                        });


                                        Log.e("3","contact1");
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }
                                }
                            }).start();



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

                    break;

            }
        }
    };
}

