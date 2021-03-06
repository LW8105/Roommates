package group2.roommates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class CalendarActivity extends ActionBarActivity {

    public ArrayList<CalendarEvent> allEventsArray = new ArrayList<CalendarEvent>();
    private AlertDialog.Builder dialogBuilder;
    SimpleDateFormat sdfConverter = new SimpleDateFormat("yyyyMMdd",Locale.US);
    GregorianCalendar gCalendar = new GregorianCalendar(TimeZone.getTimeZone("PST"));
    public ArrayList<CalendarEvent> dailyEventsArray = new ArrayList<CalendarEvent>();
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setTitle("Scheduled Events");

        dbHandler = new DBHandler(this, null, null, 1); //SQLite DB handler
        allEventsArray = dbHandler.pullEvents(); //fill allEventsArray with calendarEvent objects from the db

        final CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        gCalendar.setTimeInMillis(calendar.getDate());

        String dateString = ("Events for " + (gCalendar.get(Calendar.MONTH)+1) + "-" + gCalendar.get(Calendar.DAY_OF_MONTH) + "-" + gCalendar.get(Calendar.YEAR));
        TextView header = (TextView) findViewById(R.id.header);
        header.setText(dateString);

        int thisDay = Integer.parseInt(sdfConverter.format(gCalendar.getTime())); //convert current date to "yyyymmdd" format

        for(int i=0; i < allEventsArray.size(); i++){//fill the daily event array from the global event array
            if(allEventsArray.get(i).getDate() == thisDay){
                dailyEventsArray.add(allEventsArray.get(i));
            }
        }

        //Populate the ListView
        ListAdapter adapter = new EventAdapter(this, R.layout.custom_row, dailyEventsArray);
        ListView calendarList = (ListView)findViewById(R.id.calendarList);
        calendarList.setAdapter(adapter);

        calendar.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {//Runs whenever a new day is selected from the calendar
                gCalendar.setTimeInMillis(calendar.getDate());

                String dateString = ("Events for " + (gCalendar.get(Calendar.MONTH)+1) + "-" + gCalendar.get(Calendar.DAY_OF_MONTH) + "-" + gCalendar.get(Calendar.YEAR));
                TextView header = (TextView) findViewById(R.id.header);
                header.setText(dateString);

                int thisDay = Integer.parseInt(sdfConverter.format(gCalendar.getTime())); //convert selected date to "yyyymmdd" format
                dailyEventsArray.clear();

                for(int i=0; i < allEventsArray.size(); i++){//fill the daily event array from the global event array
                    if(allEventsArray.get(i).getDate() == thisDay){
                        dailyEventsArray.add(allEventsArray.get(i));
                    }
                }
                //Populate the ListView
                ListAdapter adapter = new EventAdapter(CalendarActivity.this, R.layout.custom_row, dailyEventsArray);
                ListView calendarList = (ListView)findViewById(R.id.calendarList);
                calendarList.setAdapter(adapter);

            } // end of day change method
        }); //end of date change listener

    }//end of onCreate()

    public void addEventClick (View view) { //addEvent Button click method
        LayoutInflater inflater = LayoutInflater.from(this);
        final View addEventView = inflater.inflate(R.layout.event_dialog, null);
        final EditText titleInput = (EditText) addEventView.findViewById(R.id.editTitle);
        final EditText descriptionInput = (EditText) addEventView.findViewById(R.id.editDescription);

        titleInput.setText("", TextView.BufferType.EDITABLE);
        descriptionInput.setText("", TextView.BufferType.EDITABLE);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add an Event");
        dialogBuilder.setView(addEventView);
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //Positive button click method
                int currentDay = Integer.parseInt(sdfConverter.format(gCalendar.getTime())); //convert selected date to "yyyymmdd" format
                CalendarEvent newEvent = new CalendarEvent(currentDay, titleInput.getText().toString(), descriptionInput.getText().toString(), LoginActivity.getUserName());

                // allEventsArray.add(newEvent);
                // dailyEventsArray.add(newEvent);

                dbHandler.addEvent(newEvent); //Add new event to the database
                allEventsArray = dbHandler.pullEvents(); // Update the allEventsArray
                dailyEventsArray.clear(); //Clear the daily array so it can be updated with the new item

                for(int i=0; i < allEventsArray.size(); i++){ //fill the daily event array from the global event array
                    if(allEventsArray.get(i).getDate() == currentDay){
                        dailyEventsArray.add(allEventsArray.get(i));
                    }
                }

                //Update the ListView to show the new event
                ListAdapter adapter = new EventAdapter(CalendarActivity.this, R.layout.custom_row, dailyEventsArray);
                ListView calendarList = (ListView)findViewById(R.id.calendarList);
                calendarList.setAdapter(adapter);

                //Create a corresponding feedObject and add it to the database
                Long tsLong = System.currentTimeMillis(); //need to know what time the event was added by a user
                java.util.Date time = new java.util.Date(tsLong);
                String timeString = time.toString(); // dow mon dd hh:mm:ss zzz yyyy
                FeedObject newFeedObject = new FeedObject("event", titleInput.getText().toString(), LoginActivity.getUserName(), timeString);
                dbHandler.addFeedObject(newFeedObject);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//Negative button click method

            }
        });
        dialogBuilder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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
}