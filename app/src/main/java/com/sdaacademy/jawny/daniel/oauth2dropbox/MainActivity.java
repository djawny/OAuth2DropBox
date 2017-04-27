package com.sdaacademy.jawny.daniel.oauth2dropbox;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.sdaacademy.jawny.daniel.oauth2dropbox.net.DropboxApi;

import org.fuckboilerplate.rx_social_connect.Response;
import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.json)
    TextView mJson;

    private GetDropboxInfoTask getDropboxInfoTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.logon)
    public void logon() {
        final String apiKey = "rclcpiz646gxgne";
        final String apiSecret = "otjgqyypmel561s";

        OAuth20Service service = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080")
                .build(DropboxApi.instance());

        RxSocialConnect.with(this, service)
                .subscribe(new Observer<Response<MainActivity, OAuth2AccessToken>>() {
                    @Override
                    public void onNext(Response<MainActivity, OAuth2AccessToken> tokenResponse) {
                        String token = tokenResponse.token().getAccessToken();
                        Log.i("TEST", "token: " + token);
                        getDropboxInfoTask = new GetDropboxInfoTask();
                        getDropboxInfoTask.setMainActivity(MainActivity.this);
                        getDropboxInfoTask.execute(token);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("TEST", e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    public void displayResponse(String text) {
        mJson.setText(text);
    }
}
