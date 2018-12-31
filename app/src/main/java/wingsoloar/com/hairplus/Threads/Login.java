package wingsoloar.com.hairplus.Threads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wingsolarxu on 2018/11/21.
 */

public class Login extends Thread {
    private Handler handler;
    private String username;
    private String password;
    private int function;
    private int postId;
    private JSONObject post;
    private SharedPreferences preferences;
    private Context context;

    public Login(Context context, Handler handler, String username, String password, int function, int postId, JSONObject post){
        this.context = context;
        this.handler=handler;
        this.username = username;
        this.password = password;
        this.function = function;
        this.postId = postId;
        this.post = post;
    }

    @Override
    public void run() {

        final Message msg = new Message();
        msg.what=13;

        FormBody requestBodyBuilder=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        Request.Builder requestBuilder=new Request.Builder();
        Request request1 = requestBuilder.url("http://120.79.237.66:8000/login")
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

                    if(res_string.equals("Login")){

                        preferences=context.getSharedPreferences("user_info",MODE_PRIVATE);;
                        final SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("name",username);
                        editor.putString("password",password);
                        editor.commit();
                        switch(function){
                            case 0:
                                new GetLikedList(handler, username).start();
                                break;
                            case 1:
                                new AddLike(handler,username,postId).start();
                                break;
                            case 2:
                                new DeleteLike(handler,username,postId).start();
                                break;
                            case 3:
                                new GetAllMyPosts(handler,username).start();
                                break;
                            case 4:
                                new UploadPost(handler,post).start();
                                break;
                        }
                    }
                    Log.e("bbbbbb",res_string);
                    Bundle b =new Bundle();
                    b.putInt("response_code",1);
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
