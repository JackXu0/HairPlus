package wingsoloar.com.hairplus.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import wingsoloar.com.hairplus.R;

/**
 * Created by wingsolarxu on 2018/10/21.
 */

public class TestActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Button button;
    private Context mContext;
    private GridView gridView;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

//        button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openAlbum();
//            }
//        });

        mContext = this;
        gridView = (GridView) findViewById(R.id.gridView);
        mPicList.add("dafad");
        mPicList.add("dafad");
        initGridView();
    }

    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(mContext, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                if (position == parent.getChildCount() - 1) {
//                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
//                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
//                        //最多添加5张图片
//                        viewPluImg(position);
//                    } else {
//                        //添加凭证图片
//                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
//                    }
//                } else {
//                    viewPluImg(position);
//                }
//            }
//        });
    }



    public class GridViewAdapter extends android.widget.BaseAdapter {

        private Context mContext;
        private List<String> mList;
        private LayoutInflater inflater;

        public GridViewAdapter(Context mContext, List<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            //return mList.size() + 1;//因为最后多了一个添加图片的ImageView
            int count = mList == null ? 1 : mList.size() + 1;
            if (count > 7) {
                return mList.size();
            } else {
                return count;
            }
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.test_layout_child, parent,false);
            ImageView iv = convertView.findViewById(R.id.image);
            if (position < mList.size()) {
                iv.setImageResource(R.drawable.designer_work1);

            } else {
                iv.setImageResource(R.mipmap.ic_launcher);//最后一个显示加号图片
            }
            return convertView;
        }
    }

}
