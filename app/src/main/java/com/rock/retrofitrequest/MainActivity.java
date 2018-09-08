package com.rock.retrofitrequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rock.retrofitrequest.https.HttpUtil;
import com.rock.retrofitrequest.https.RetrofitUtil;
import com.rock.retrofitrequest.https.RxSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;

/**
 * 这是一个使用 rxjava+ retrofit + mvc 模式的网络框架
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.txt_content)
    TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        findViewById(R.id.btn_click).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Flowable<String> flowable = RetrofitUtil.getInstance().getServiceWithStr(this).getSearchSkillsType();

        HttpUtil.getInstance().toSubscribeNoGson(this, flowable, new RxSubscriber<String>() {
            @Override
            public void _onNext(String s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        JSONObject object = new JSONObject(s);
                        txtContent.setText(s.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void _onError(String msg) {

            }
        });
    }
}
