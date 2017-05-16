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

package it.polito.elite.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import it.polito.elite.todolist.async_classes.AsyncGetTaskList;
import it.polito.elite.todolist.R;
import it.polito.elite.todolist.custom_objects.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //run an async process to get the list of tasks
        AsyncGetTaskList task = new AsyncGetTaskList(this.getBaseContext(), this);
        task.execute("http://10.0.2.2:5000/api/v1.0/tasks");
        /*why we use 10.0.2.2 instead of 127.0.0.1:
        Each instance of the emulator runs behind a virtual router/firewall service that isolates it
        from our development machine's network interfaces and settings and from the internet.
        An emulated device can not see your development machine or other emulator instances on the network.
        Instead, it sees only that it is connected through Ethernet to a router/firewall.

        The virtual router for each instance manages the 10.0.2/24 network address space
        — all addresses managed by the router are in the form of 10.0.2.<xx>, where <xx> is a number.
        */

    }

    public void onResume()
    {
        //when the user come back to the main activity after using other activities, the list of tasks should be updated
        super.onResume();
        //run an async process to get the list of tasks
        AsyncGetTaskList task = new AsyncGetTaskList(this.getBaseContext(), this);
        task.execute("http://10.0.2.2:5000/api/v1.0/tasks");

    }


    //this method will be invoked when the async process finishes returning the acquired list of tasks
    public void populateListBackground(List<Task> tasksList) {
        ListView tasksListView = (ListView) findViewById(R.id.taskListView);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, tasksList);
        tasksListView.setAdapter(adapter);
                /*
                * It is possible to customize the Array adapter: more info can be found at the following links
                * 1. http://developer.android.com/reference/android/widget/ArrayAdapter.html
                * 2. TUTORIAL -> http://www.vogella.com/tutorials/AndroidListView/article.html
                */

        //when we click on an item the detail page will be open
        //another way of doing it: create a list with handled swipe actions:
        //more info:
        //1. https://github.com/baoyongzhang/SwipeMenuListView
        //2. http://www.tutecentral.com/android-swipe-listview/
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Task item = (Task) parent.getItemAtPosition(position);
                Intent intent = new Intent(parent.getContext(), TaskDetailsActivity.class);
                //pass the task Id as parameter to the view
                Bundle b = new Bundle();
                b.putInt("taskId", item.getId());
                intent.putExtras(b);

                startActivity(intent);
                //if you want to completely close the activity decomment the following line (if you press the back button it will close the app instead of coming back home)
                // finish();
            }

        });
    }

    //function executed when the "Insert a new task" button is pressed
    public void openNewTaskActivity(View view)
    {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }
}
