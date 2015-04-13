package group2.roommates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "roommates_data.db";

    public static final String TABLE_EVENTS = "events"; //name of the table storing information for calendar events
    public static final String EVENT_ID = "_id"; //The following are columns in the table
    public static final String EVENT_TITLE = "title";
    public static final String EVENT_DESCRIPTION = "description";
    public static final String EVENT_DATE = "date";
    public static final String EVENT_AUTHOR = "author";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_EVENTS + "(" +
                EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EVENT_TITLE + " TEXT, " +
                EVENT_DESCRIPTION + " TEXT, " +
                EVENT_DATE + " INTEGER, " +
                EVENT_AUTHOR + " TEXT" +
                ");";

            db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(CalendarEvent calendarEvent){ //Add a new event row to the database
        ContentValues values = new ContentValues();
        //fill the columns with their corresponding values
        values.put(EVENT_TITLE, calendarEvent.getTitle());
        values.put(EVENT_DESCRIPTION, calendarEvent.getDescription());
        values.put(EVENT_DATE, calendarEvent.getDate());
        values.put(EVENT_AUTHOR, calendarEvent.getAuthor());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public void deleteEvent(String title){ //Delete an event row in the database
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + EVENT_TITLE + "=\"" + title + "\";");
    }

    public String databaseToString(){ //not used
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE 1";

        //Cursor points to a location in the results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst(); // move to the first row

        while(!c.isAfterLast()){ //While there are still more entries (while the cursor is not after the last entry)
            //Loops through all rows in the table
            if(c.getString(c.getColumnIndex("title"))!= null){
                dbString += c.getString(c.getColumnIndex("title"));
                dbString += "\n";
            }
            c.moveToNext();
        }//end while
        db.close();
        return dbString;
    }

    public ArrayList<CalendarEvent> pullEvents (){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE 1";
        //Cursor points to a location in the results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst(); // move to the first row

        ArrayList<CalendarEvent> allEventsArray = new ArrayList<CalendarEvent>();

        while(!c.isAfterLast()){ //While there are still more entries (while the cursor is not after the last entry)
            //Loops through all rows in the table

            String pTitle = c.getString(c.getColumnIndex("title"));
            String pDescription = c.getString(c.getColumnIndex("description"));
            int pDate = c.getInt(c.getColumnIndex("date"));
            String pAuthor = c.getString(c.getColumnIndex("author"));
            int pId = c.getInt(c.getColumnIndex("_id"));

            CalendarEvent pulledEvent = new CalendarEvent(pDate, pTitle, pDescription, pAuthor);
            pulledEvent.set_id(pId);
            allEventsArray.add(pulledEvent);

            c.moveToNext();
        }//end while
        db.close();

        return allEventsArray;
    }// end of pullEvents method


}
















