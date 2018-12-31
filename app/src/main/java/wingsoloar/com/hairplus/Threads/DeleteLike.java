package wingsoloar.com.hairplus.Threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wingsolarxu on 2018/11/20.
 */

public class DeleteLike extends  Thread {
    private Handler handler;
    private int id;
    private String username;
    private int postId;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public DeleteLike(Handler handler, String username, int postId){
        this.handler=handler;
        this.username = username;
        this.postId = postId;
    }

    @Override
    public void run() {

        final Message msg = new Message();
        msg.what=7;

        FormBody requestBodyBuilder=new FormBody.Builder()
                .add("username",username)
                .add("id",postId+"")
                .build();

        Request.Builder requestBuilder=new Request.Builder();
        Request request1 = requestBuilder.url("http://120.79.237.66:8000/deleteLike")
                .post(requestBodyBuilder).build();

        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request1);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Bundle b =new Bundle();
                b.putInt("response_code",0);
                msg.setData(b);
                handler.sendMessage(msg);
                Log.e("eee","1");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    String res_string=response.body().string();
                    msg.obj=res_string;
                    Log.e("bbbbbb","qq"+res_string);

                    Bundle b =new Bundle();
                    b.putInt("response_code",1);
                    b.putInt("id",id);
                    msg.setData(b);
                    handler.sendMessage(msg);

                    Log.e("eee","2");


                }else{
                    Bundle b =new Bundle();
                    b.putInt("response_code",2);
                    msg.setData(b);
                    handler.sendMessage(msg);

                    Log.e("eee","3");
                }


            }
        });
    }
}
