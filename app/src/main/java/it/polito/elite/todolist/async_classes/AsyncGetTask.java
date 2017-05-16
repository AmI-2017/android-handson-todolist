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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.polito.elite.todolist.activities.TaskDetailsActivity;
import it.polito.elite.todolist.custom_objects.Task;

import static it.polito.elite.todolist.GetSingleTaskFromJson.readJsonStream;

public class AsyncGetTask extends AsyncTask<String, Integer, Task> {

    //context and activity of the activity that called the service
    Context mContext;
    TaskDetailsActivity mActivity;

    ProgressDialog barPD;
    public AsyncGetTask(Context context, Activity activity)
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
    protected Task doInBackground(String... urls) {
        Task returnedTask = new Task();
        try {

            //open an HTTP connection and send request
            HttpURLConnection conn;
            URL urlObj = new URL(urls[0]);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.connect();

            //get response and convert it to string
            InputStream in = new BufferedInputStream(conn.getInputStream());

            //convert the inputStream obtained from the server in a Task
            returnedTask = readJsonStream(in);
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnedTask;
    }

    @Override
    protected void onPostExecute(Task task) {
        try {
            mActivity.returnBackgroundMethod(task);
            barPD.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
