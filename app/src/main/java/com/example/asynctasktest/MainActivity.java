package com.example.asynctasktest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkManager mWorkManager = WorkManager.getInstance();
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(CallbackWorker.class).build();
        Log.i(TAG, "Enqueue the work ...");
        mWorkManager.enqueue(mRequest);


    }

    private class GetUrlOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                doGetRequest("https://www.google.com");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "Complete");
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class GetUrlOperationEn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                doGetRequestEn("https://www.google.com");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "Complete");
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }



    void doGetRequestEn(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.i(TAG, "onFailure");

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i(TAG, "onResponse callback is !!! " + response);

            }
        });
    }


    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        Log.i(TAG, "Get response is " + response);
        return response.body().string();
    }

}

