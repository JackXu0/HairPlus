package wingsoloar.com.hairplus.Activities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;

import wingsoloar.com.hairplus.R;


public class MainActivity extends FragmentActivity {

    private TextView mTextMessage;
    private Fragment index_page;
    private Fragment filter_page;
    private Fragment my_page;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private int page=0;

    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private File outputImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_index:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.frame,index_page);
                    ft.commit();
                    page=0;
                    return true;
                case R.id.navigation_dashboard:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.frame,filter_page);
                    ft.commit();
                    page=1;
                    return true;
                case R.id.navigation_my:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.frame,my_page);
                    ft.commit();
                    page=2;
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        fm = getFragmentManager();
        ft = fm.beginTransaction();

        index_page=new index_view();
        filter_page = new Filter_view();
        my_page = new User_view();
        ft.replace(R.id.frame,index_page);
        ft.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);






//        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//
//
//
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                outputImage = new File(getContext().getApplicationContext().getExternalCacheDir(), "output_image.jpg");
//
//                try {
//                    if (outputImage.exists()) {
//                        outputImage.delete();
//                    }
//                    outputImage.createNewFile();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (Build.VERSION.SDK_INT >= 24) {
//                    imageUri = FileProvider.getUriForFile(getContext().getApplicationContext(),"wingsoloar.com.hairplus.fileprovider", outputImage);
//
//                } else {
//                    imageUri = Uri.fromFile(outputImage);
//                }
//
//                //启动相机程序
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                getActivity().startActivityForResult(intent, TAKE_PHOTO);
//            }
//        });
//
//        @Override
//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            switch (requestCode) {
//                case TAKE_PHOTO:
//                    if (resultCode == RESULT_OK) {
//                        try {
//                            Bitmap bm = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
//                            FileOutputStream b=null;
//                            try {
//                                b = new FileOutputStream(outputImage);
//                                bm.compress(Bitmap.CompressFormat.JPEG, 25, b);// 把数据写入文件
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } finally {
//                                try {
//                                    b.flush();
//                                    b.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            Log.e(outputImage.length()+"","map1");
//                            image.setImageBitmap(bm);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        my_page.onActivityResult(requestCode, resultCode, data);
    }


}
