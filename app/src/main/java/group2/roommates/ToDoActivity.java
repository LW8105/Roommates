package group2.roommates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class ToDoActivity extends ActionBarActivity {

    public ArrayList<ToDoItem> toDoArray = new ArrayList<>();
    private AlertDialog.Builder dialogBuilder;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        setTitle("To-Do List");

        dbHandler = new DBHandler(this, null, null, 1); //SQLite DB handler
        toDoArray = dbHandler.pullToDoList(); //fill toDoArray with items from the db
        Collections.reverse(toDoArray);

        ListAdapter adapter = new ToDoAdapter(this, R.layout.todo_row, toDoArray);
        ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
        toDoListView.setAdapter(adapter);
    }

    public void addItemClick(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View addToDoView = inflater.inflate(R.layout.todo_dialog, null);
        final EditText descriptionInput = (EditText) addToDoView.findViewById(R.id.editDescription);

        descriptionInput.setText("", TextView.BufferType.EDITABLE);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add To-Do Item");
        dialogBuilder.setView(addToDoView);
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ToDoItem newItem = new ToDoItem(descriptionInput.getText().toString(), LoginActivity.getUserName(), 0);
                dbHandler.addToDoItem(newItem); //add new item to the DB
                toDoArray = dbHandler.pullToDoList(); //repopulate the array with the updated data
                Collections.reverse(toDoArray);

                ListAdapter adapter = new ToDoAdapter(ToDoActivity.this, R.layout.todo_row, toDoArray);
                ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
                toDoListView.setAdapter(adapter);

                //Create a corresponding feedObject and add it to the database
                Long tsLong = System.currentTimeMillis(); //need to know what time the event was added by a user
                java.util.Date time = new java.util.Date(tsLong);
                String timeString = time.toString(); // dow mon dd hh:mm:ss zzz yyyy
                FeedObject newFeedObject = new FeedObject("item to the to-do list", newItem.getDescription(), LoginActivity.getUserName(), timeString);
                dbHandler.addFeedObject(newFeedObject);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();
    }

    public void checkBoxClick (View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHandler = new DBHandler(this, null, null, 1); //SQLite DB handler
        toDoArray = dbHandler.pullToDoList(); //fill toDoArray with items from the db
        Collections.reverse(toDoArray);

        ListAdapter adapter = new ToDoAdapter(this, R.layout.todo_row, toDoArray);
        ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
        toDoListView.setAdapter(adapter);
    }
}
