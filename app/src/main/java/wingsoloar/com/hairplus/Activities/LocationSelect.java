package wingsoloar.com.hairplus.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wingsoloar.com.hairplus.Database.ShopDatabaseConnector;
import wingsoloar.com.hairplus.Objects.Shop;
import wingsoloar.com.hairplus.R;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class LocationSelect extends Activity {

    private MyAdapter myAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> names;
    private List<Shop> shops;
    private ImageView back_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_select_main);

        ShopDatabaseConnector shopDatabaseConnector = new ShopDatabaseConnector(getApplicationContext());

        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(shopDatabaseConnector.isEmpty()){
            shopDatabaseConnector.initial();
        }

        names = new ArrayList<>();
        shops = shopDatabaseConnector.getShops();

        Log.e("shopsize", ""+shops.size());

        for(int i=0;i<shops.size();i++){
            names.add(shops.get(i).getName());
        }

        myAdapter = new MyAdapter(names);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView = (RecyclerView) findViewById(R.id.locations);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(myAdapter);


    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> names;

        public MyAdapter(@Nullable ArrayList<String> names) {

            this.names = names;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_select_child, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据


            holder.name_text.setText(names.get(position));

            holder.name_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PublishPost.shop_name = names.get(position);
                    PublishPost.shopID = position+1;
                    Intent intent = new Intent(LocationSelect.this, CutterSelect.class);
                    intent.putExtra("shopName",names.get(position));
                    startActivity(intent);
                    finish();
                }
            });


        }


        @Override
        public int getItemCount() {
            return names == null ? 0 : names.size();
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

}
