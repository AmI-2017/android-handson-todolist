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
import java.util.ArrayList;
import java.util.List;

import it.polito.elite.todolist.activities.MainActivity;
import it.polito.elite.todolist.custom_objects.Task;
import it.polito.elite.todolist.GetTaskListFromJSON;


public class AsyncGetTaskList extends AsyncTask<String, Integer, List<Task>> {

    //context and activity of the activity that called the service
    Context mContext;
    MainActivity mActivity;

    ProgressDialog barPD;
    public AsyncGetTaskList(Context context, Activity activity)
    {
        this.mContext = context;
        this.mActivity = (MainActivity) activity;

        barPD = new ProgressDialog(this.mContext);
    }
    protected List<Task> doInBackground(String... urls) {

        List<Task> tasksList = new ArrayList<Task>();
        try {

            //open an HTTP connection and send request
            HttpURLConnection conn;
            URL urlObj = new URL("http://10.0.2.2:5000/api/v1.0/tasks");

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.connect();

            //get response and convert it to string
            InputStream in = new BufferedInputStream(conn.getInputStream());

            //convert the inputStream obtained from the server in a List of task (ArrayList<Task>)
            tasksList = GetTaskListFromJSON.readJsonStream(in);
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tasksList;
    }

    protected void onPreExecute() {
        try {
            barPD = barPD.show(this.mActivity, "Loading", "Please wait");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void onPostExecute(List<Task> result) {
        try {
            mActivity.populateListBackground(result);
            barPD.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
