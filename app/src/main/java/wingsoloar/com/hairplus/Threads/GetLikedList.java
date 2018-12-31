package wingsoloar.com.hairplus.Threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wingsolarxu on 2018/11/20.
 */

public class GetLikedList extends Thread {
    private Handler handler;
    private String username;

    public GetLikedList(Handler handler, String username){
        this.handler=handler;
        this.username = username;
    }

    @Override
    public void run() {

        final Message msg = new Message();
        msg.what=10;

        FormBody requestBodyBuilder=new FormBody.Builder()
                .add("username",username)
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://120.79.237.66:8000/getLikedList").post(requestBodyBuilder).build();

        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Bundle b =new Bundle();
                b.putInt("response_code",0);
                msg.setData(b);
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res_string=response.body().string();
                    msg.obj=res_string;
                    Bundle b =new Bundle();
                    b.putInt("response_code",1);
                    msg.setData(b);
                    handler.sendMessage(msg);


                }else{
                    Bundle b =new Bundle();
                    b.putInt("response_code",2);
                    msg.setData(b);
                    handler.sendMessage(msg);
                }


            }
        });
    }
}
