package group2.roommates;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "roommates_data.db";

    public static final String TABLE_EVENTS = "events"; //name of the table storing information for calendar events
    public static final String EVENT_ID = "_id"; //The following are columns in the table
    public static final String EVENT_TITLE = "title";
    public static final String EVENT_DESCRIPTION = "description";
    public static final String EVENT_DATE = "date";
    public static final String EVENT_AUTHOR = "author";

    public static final String TABLE_TODO = "todo"; //table storing items in the to-do list
    public static final String TODO_ID = "_id";
    public static final String TODO_DESCRIPTION = "description";
    public static final String TODO_AUTHOR = "author";
    public static final String TODO_CHECKED = "checked";

    public static final String TABLE_EXPENSES = "expenses"; //table storing expenses
    public static final String EXPENSE_ID = "_id";
    public static final String EXPENSE_AMOUNT = "amount";
    public static final String EXPENSE_DIVAMOUNT = "divided_amount";
    public static final String EXPENSE_DUEDATE = "due_date";
    public static final String EXPENSE_NAME = "name";
    public static final String EXPENSE_AUTHOR = "author";
    public static final String EXPENSE_NOTES = "notes";
    public static final String EXPENSE_PAID = "paid";

    public static final String TABLE_FEED = "feed"; //table storing FeedObjects
    public static final String FEED_ID = "_id";
    public static final String FEED_TYPE = "type";
    public static final String FEED_TITLE = "title";
    public static final String FEED_AUTHOR = "author";
    public static final String FEED_TIME = "time";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_events = "CREATE TABLE " + TABLE_EVENTS + "(" +
                EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EVENT_TITLE + " TEXT, " +
                EVENT_DESCRIPTION + " TEXT, " +
                EVENT_DATE + " INTEGER, " +
                EVENT_AUTHOR + " TEXT" +
                ");";

        String query_todo = "CREATE TABLE " + TABLE_TODO + "(" +
                TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODO_DESCRIPTION + " TEXT, " +
                TODO_AUTHOR + " TEXT, " +
                TODO_CHECKED + " INTEGER" +
                ");";

        String query_expenses = "CREATE TABLE " + TABLE_EXPENSES + "(" +
                EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EXPENSE_AMOUNT + " TEXT, " +
                EXPENSE_DIVAMOUNT + " REAL, " +
                EXPENSE_DUEDATE + " TEXT," +
                EXPENSE_NAME + " TEXT," +
                EXPENSE_AUTHOR + " TEXT," +
                EXPENSE_NOTES + " TEXT," +
                EXPENSE_PAID + " INTEGER" +
                ");";

        String query_feed = "CREATE TABLE " + TABLE_FEED + "(" +
                FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FEED_TYPE + " TEXT, " +
                FEED_TITLE + " TEXT, " +
                FEED_AUTHOR + " TEXT, " +
                FEED_TIME + " TEXT" +
                ");";

        db.execSQL(query_events);
        db.execSQL(query_todo);
        db.execSQL(query_expenses);
        db.execSQL(query_feed);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);
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

    public void addFeedObject(FeedObject feedObject){ //Add a new feed object row to the database
        ContentValues values = new ContentValues();
        //fill the columns with their corresponding values
        values.put(FEED_TYPE, feedObject.getType());
        values.put(FEED_TITLE, feedObject.getTitle());
        values.put(FEED_AUTHOR, feedObject.getAuthor());
        values.put(FEED_TIME, feedObject.getTimeCreated());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_FEED, null, values);
        db.close();
    }

    public void addToDoItem(ToDoItem toDoItem){ //Add a new to-do row to the database
        ContentValues values = new ContentValues();
        //fill the columns with their corresponding values
        values.put(TODO_DESCRIPTION, toDoItem.getDescription());
        values.put(TODO_AUTHOR, toDoItem.getAuthor());
        values.put(TODO_CHECKED, toDoItem.getChecked());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public void addExpense(ExpenseObject expenseObject){ //Add a new row to the expense table
        ContentValues values = new ContentValues();
        //fill the columns with their corresponding values
        values.put(EXPENSE_AMOUNT, expenseObject.getExpenseAmount());
        values.put(EXPENSE_DIVAMOUNT, expenseObject.getDividedAmount());
        values.put(EXPENSE_DUEDATE, expenseObject.getDueDate());
        values.put(EXPENSE_NAME, expenseObject.getExpenseName());
        values.put(EXPENSE_AUTHOR, expenseObject.getAuthor());
        values.put(EXPENSE_NOTES, expenseObject.getNotes());
        values.put(EXPENSE_PAID, expenseObject.isPaid());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public void deleteEvent(String title){ //Delete an event row in the database. (Need to update to use _id if possible)
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + EVENT_TITLE + "=\"" + title + "\";");
    }

    public void deleteExpense(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXPENSES + " WHERE " + EXPENSE_ID + "=\"" + id + "\";");

    }

    public String databaseToString(){
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

    public ArrayList<FeedObject> pullFeed (){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FEED + " WHERE 1";
        //Cursor points to a location in the results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst(); // move to the first row

        ArrayList<FeedObject> feedArray = new ArrayList<FeedObject>();

        while(!c.isAfterLast()){ //While there are still more entries (while the cursor is not after the last entry)
            //Loops through all rows in the table

            String pType = c.getString(c.getColumnIndex("type"));
            String pTitle = c.getString(c.getColumnIndex("title"));
            String pAuthor = c.getString(c.getColumnIndex("author"));
            String pTime = c.getString(c.getColumnIndex("time"));
            int pId = c.getInt(c.getColumnIndex("_id"));

            FeedObject pulledFeedObject = new FeedObject(pType, pTitle, pAuthor, pTime);
            pulledFeedObject.set_id(pId);
            feedArray.add(pulledFeedObject);

            c.moveToNext();
        }//end while
        db.close();

        return feedArray;
    }

    public ArrayList<ToDoItem> pullToDoList (){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TODO + " WHERE 1";
        //Cursor points to a location in the results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst(); // move to the first row

        ArrayList<ToDoItem> toDoArray = new ArrayList<ToDoItem>();

        while(!c.isAfterLast()){ //While there are still more entries (while the cursor is not after the last entry)
            //Loops through all rows in the table

            String pDescription = c.getString(c.getColumnIndex("description"));
            String pAuthor = c.getString(c.getColumnIndex("author"));
            int pChecked = c.getInt(c.getColumnIndex("checked"));
            int pId = c.getInt(c.getColumnIndex("_id"));

            ToDoItem pulledItem = new ToDoItem(pDescription, pAuthor, pChecked);
            pulledItem.set_id(pId);
            toDoArray.add(pulledItem);

            c.moveToNext();
        }//end while
        db.close();

        return toDoArray;
    }

    public ArrayList<ExpenseObject> pullExpenses (){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE 1";
        //Cursor points to a location in the results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst(); // move to the first row

        ArrayList<ExpenseObject> expenseArray = new ArrayList<ExpenseObject>();

        while(!c.isAfterLast()){ //While there are still more entries (while the cursor is not after the last entry)
            //Loops through all rows in the table

            int pId = c.getInt(c.getColumnIndex("_id"));
            String pAmount = c.getString(c.getColumnIndex("amount"));
            double pDivAmount = c.getDouble(c.getColumnIndex("divided_amount"));
            String pDueDate = c.getString(c.getColumnIndex("due_date"));
            String pName = c.getString(c.getColumnIndex("name"));
            String pAuthor = c.getString(c.getColumnIndex("author"));
            String pNotes = c.getString(c.getColumnIndex("notes"));
            int pPaid = c.getInt(c.getColumnIndex("paid"));
            boolean paidBoolean;
            if(pPaid == 0){
                paidBoolean = false;
            }else{
                paidBoolean = true;
            }

            ExpenseObject pulledExpense = new ExpenseObject(pId, pAmount, pDivAmount, pDueDate, pName, pAuthor, pNotes, paidBoolean);
            //pulledExpense.setId(pId);
            expenseArray.add(pulledExpense);

            c.moveToNext();
        }//end while
        db.close();

        return expenseArray;
    }// end of pullExpenses

}

































