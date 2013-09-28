package models;

import java.util.*;

import play.modules.mongojack.MongoDB;
import org.mongojack.JacksonDBCollection;
import org.mongojack.Id;
import org.mongojack.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;
import org.mongojack.DBQuery.*;
import org.mongojack.DBQuery;

import javax.persistence.*;

public class Task {

    @Id
    @ObjectId
    public String id;

    public boolean isPrivate;

    public String label;

    private static JacksonDBCollection<Task, String> coll = MongoDB.getCollection("tasks", Task.class, String.class);

    public static List<Task> all() {
        return Task.coll.find().toArray();
    }

    public static void create(Task task) {
        Task.coll.save(task);
    }

    public static void delete(String id) {
        Task task = Task.coll.findOneById(id);
        if (task != null)
            // This method looks for the @ObjectId element. A query is required for removing based on other fields.
            Task.coll.removeById(id);
    }

    public static List<Task> search(String text) {
//        return Task.coll.find().is("label", text).toArray();
        return Task.coll.find().toArray();
    }

}