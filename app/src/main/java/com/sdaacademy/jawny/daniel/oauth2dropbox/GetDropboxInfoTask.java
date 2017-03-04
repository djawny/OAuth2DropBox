package com.sdaacademy.jawny.daniel.oauth2dropbox;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetDropboxInfoTask extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private boolean success;

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        try {
            response = sentRequest(params[0]);
            success = true;
        } catch (IOException e) {
            if (mainActivity != null) {
                mainActivity.showError("Blad połączenia");
                success = false;
            }
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mainActivity != null && success) {
            mainActivity.displayResponse(s);
        }
    }

    private String sentRequest(String token) throws IOException {
        MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.dropboxapi.com/2/users/get_current_account";
        String json = "null";

        RequestBody body = RequestBody.create(jsonMediaType, json);
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
