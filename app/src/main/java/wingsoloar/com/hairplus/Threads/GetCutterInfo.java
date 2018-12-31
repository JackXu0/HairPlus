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
 * Created by wingsolarxu on 2018/12/21.
 */

public class GetCutterInfo extends Thread {

    private Handler handler;
    private String cutter_name;

    public GetCutterInfo(Handler handler, String cutter_name){
        this.handler=handler;
        this.cutter_name = cutter_name;
    }

    @Override
    public void run() {

        final Message msg = new Message();
        msg.what=20;

        FormBody requestBodyBuilder1=new FormBody.Builder()
                .add("cutter_name",cutter_name)
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://120.79.237.66:8000/getOneCutter").post(requestBodyBuilder1).build();

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
