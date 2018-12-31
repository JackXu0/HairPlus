package wingsoloar.com.hairplus.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wingsoloar.com.hairplus.JellyInterpolator;
import wingsoloar.com.hairplus.R;
import wingsoloar.com.hairplus.Threads.Login;

/**
 * Created by wingsolarxu on 2018/11/21.
 */

public class LoginActivity extends Activity{

    private Button mBtnLogin;
    private View progress;
    private View mInputLayout;
    private ImageView back_button;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private EditText name_input;
    private EditText password_input;
    private AnimatorSet set;
    PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
            0.5f, 1f);
    PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
            0.5f, 1f);


    private ObjectAnimator animator3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);

        initView();
    }

    private void initView() {
        mBtnLogin = (Button) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        back_button = findViewById(R.id.back_button);
        name_input = findViewById(R.id.name_input);
        password_input = findViewById(R.id.password_input);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();

                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                mInputLayout.setVisibility(View.INVISIBLE);

                progress.setVisibility(View.VISIBLE);

                new Login(getApplicationContext(),myHandler,name_input.getText().toString(),password_input.getText().toString(),0,0,null).start();
            }
        });
    }

    private void inputAnimator(final View view, float w, float h) {

        set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);

        set.start();

        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
//                mInputLayout.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mInputLayout.setVisibility(View.VISIBLE);
                mName.setVisibility(View.VISIBLE);
                mPsw.setVisibility(View.VISIBLE);

            }
        });

    }

    private void progressAnimator(final View view) {

        animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);

        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            final Bundle b;
            int code;
            b = msg.getData();
            code = b.getInt("response_code");
            switch (code) {
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Please Check Internet Access!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case 1:

                    switch (msg.what) {
                        case 13:
                            Log.e("44444","ffff");
                            String result = msg.obj.toString();
                            if(result.equals("Login")){
                                finish();
                                return;
                            }else{
                                Log.e("55555","ffff");
                                name_input.setText("");
                                password_input.setText("");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progress.setVisibility(View.GONE);
                                mInputLayout.setVisibility(View.VISIBLE);
                                mPsw.setVisibility(View.VISIBLE);
                                mName.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(),"Username or Password incorrect!",Toast.LENGTH_SHORT).show();
//                                animator3.end();
                                Log.e("66666","ffff");

                            }


                            break;

                    }

                    break;

                case 2:

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;

            }


        }
    };


}
