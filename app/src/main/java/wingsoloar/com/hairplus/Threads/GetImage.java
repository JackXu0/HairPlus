package wingsoloar.com.hairplus.Threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetImage extends Thread {

    private Handler handler;
    private String filename;
    private int id;
    private int tag;

    public GetImage(Handler handler, String filename, int id, int tag){
        this.handler=handler;
        this.filename = filename;
        this.id=id;
        this.tag= tag;
    }

    @Override
    public void run() {

        final Message msg = new Message();
        msg.what=2;


        FormBody requestBodyBuilder1=new FormBody.Builder()
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://120.79.237.66:8000/image/images/"+filename).build();
        Log.e("path","http://10.0.2.2:8000/image/images/"+filename);

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
                Log.e("eee","1");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    msg.obj=bitmap;
                    Bundle b =new Bundle();
                    b.putInt("response_code",1);
                    b.putInt("id",id);
                    b.putString("filename",filename);
                    b.putInt("tag", tag);
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
