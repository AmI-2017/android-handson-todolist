/*
Created on May 16, 2017
Copyright (c) 2017 Teodoro Montanaro

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License
@author: Teodoro Montanaro
*/

package it.polito.elite.todolist.async_classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

import it.polito.elite.todolist.activities.TaskDetailsActivity;


//AsyncTask that delete a task using REST API
public class AsyncDeleteTask extends AsyncTask<String, Integer, String> {

    //context and activity of the activity that called the service
    Context mContext;
    TaskDetailsActivity mActivity;
    ProgressDialog barPD;


    public AsyncDeleteTask(Context context, Activity activity)
    {
        this.mContext = context;
        this.mActivity = (TaskDetailsActivity) activity;

        barPD = new ProgressDialog(this.mContext);
    }

    //while data is acquired, we show a progress bar
    @Override
    protected void onPreExecute() {
        try {
            barPD = barPD.show(this.mActivity, "Loading", "Please wait");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            //open an HTTP connection and send request
            HttpURLConnection conn;
            URL urlObj = new URL(urls[0]);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(15000);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = "OK";
            } else {
                response = "PROBLEM";
            }
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //when a response is received from the server we check the result and then we close the activity coming back home or we show a message with the error
    @Override
    protected void onPostExecute(String response) {
        try {
            barPD.dismiss();
            if (response.equals("OK"))
            {
                mActivity.finish();
            }
            else
            {
                Toast.makeText(mContext, "A problem occured while performing action: try again!",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
