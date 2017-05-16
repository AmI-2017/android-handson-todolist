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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import it.polito.elite.todolist.async_classes.AsyncInsertNewTask;
import it.polito.elite.todolist.R;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
    }

    //function executed when the "Insert" button is pressed in the "NewTaskActivity" activity
    public void insertNewTask(View view)
    {
        //get info from the widgets contained in the layout
        EditText taskDescriptionet = (EditText) this.findViewById(R.id.taskDescription);
        CheckBox urgentcb = (CheckBox) this.findViewById(R.id.urgentCheckBox);
        String description = taskDescriptionet.getText().toString();
        int urgent=0;
        if (urgentcb.isChecked())
            urgent=1;

        //launch the async process to perform the insert on the server
        AsyncInsertNewTask task = new AsyncInsertNewTask(this.getBaseContext(),this, description, urgent);
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

}
