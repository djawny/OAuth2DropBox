package com.sdaacademy.jawny.daniel.oauth2dropbox;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetGitHubInfoTask extends AsyncTask<String, Integer, String> {

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
        Request request = new Request.Builder()
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .url("https://api.dropboxapi.com/2/users/get_current_account")
                .build();
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute().body().string();
    }
}
