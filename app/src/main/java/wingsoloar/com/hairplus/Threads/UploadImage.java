package wingsoloar.com.hairplus.Threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadImage extends  Thread {

    private Handler handler;
    private File file;
    private int id;

    public UploadImage(Handler handler, File file){
        this.handler=handler;
        this.file = file;
    }

    @Override
    public void run() {

        final Message msg = new Message();
        msg.what=3;

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imagefile",file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));

        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder=new Request.Builder();
        Request request1 = requestBuilder.url("http://120.79.237.66:8000/addImage")
                .post(requestBody).build();

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
//                    InputStream inputStream = response.body().byteStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                    msg.obj=bitmap;
                    String res_string=response.body().string();

                    Bundle b =new Bundle();
                    b.putInt("response_code",1);
                    b.putInt("id",id);
                    b.putInt("imageID", Integer.parseInt(res_string));
                    msg.setData(b);
                    handler.sendMessage(msg);

                    Log.e("eeeeee",res_string);


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
