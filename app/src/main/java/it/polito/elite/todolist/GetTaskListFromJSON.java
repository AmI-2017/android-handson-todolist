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
package it.polito.elite.todolist;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.polito.elite.todolist.custom_objects.Task;

/*
    This class implement all the methods needed to transform the InputStream obtained from the server into a ArrayList<Task> object
    More info about how the methods work can be found at: https://developer.android.com/reference/android/util/JsonReader.html
 */

public class GetTaskListFromJSON {

    public static List<Task> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readTasksArray(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Task> readTasksArray(JsonReader reader) throws IOException {
        List<Task> tasks = new ArrayList<Task>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("tasks")  && reader.peek() != JsonToken.NULL) {

                tasks = getTasks(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return tasks;
    }

    public static List<Task> getTasks(JsonReader reader) throws IOException
    {
        List<Task> tasks = new ArrayList<Task>();

        reader.beginArray();
        while (reader.hasNext()) {
            tasks.add(readSingleTask(reader));
        }
        reader.endArray();
        return tasks;
    }

    public static Task readSingleTask(JsonReader reader) throws IOException {

        Task singleTask = new Task();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("description")) {
                singleTask.setDescription(reader.nextString());
            } else if (name.equals("id")) {
                singleTask.setId(reader.nextInt());
            } else if (name.equals("urgent")) {
                singleTask.setUrgent(reader.nextInt());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return singleTask;
    }
}
