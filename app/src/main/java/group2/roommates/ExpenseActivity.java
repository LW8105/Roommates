package group2.roommates;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseActivity extends ActionBarActivity {

    private ArrayList<ExpenseObject> expenseArray = new ArrayList<>();
    private ArrayAdapter<CharSequence> spinnerAdapter;
    final Calendar MY_CALENDAR = Calendar.getInstance();
    EditText DATE_INPUT;
    String dueDate;
    Spinner categorySpinner;
    double roommateCount;
    DecimalFormat df = new DecimalFormat("#.##");
    ExpenseObject object;
    DBHandler dbHandler;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setTitle("Expenses");

        roommateCount = 3.0;

        dbHandler = new DBHandler(this, null, null, 1); //SQLite DB handler
        expenseArray = dbHandler.pullExpenses(); //fill expense array with expense objects from the db

        ListAdapter adapter = new ExpenseAdapter(this, R.layout.expense_row, expenseArray);
        ListView expenseListView = (ListView) findViewById(R.id.expenseListView);

        expenseListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expense, menu);
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


    public void addExpenseClick(View view) throws ParseException {
        LayoutInflater inflater = LayoutInflater.from(this);

        final View ADD_EXPENSE_VIEW = inflater.inflate(R.layout.expense_dialog, null);

        DATE_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editDueDate);
        final EditText AMOUNT_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editAmount);
        final EditText DESCRIPTION_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editNotes);


        AMOUNT_INPUT.setText("", TextView.BufferType.EDITABLE);
        DESCRIPTION_INPUT.setText("", TextView.BufferType.EDITABLE);
        DATE_INPUT.setText("", TextView.BufferType.EDITABLE);

        spinnerAdapter = ArrayAdapter.createFromResource(ExpenseActivity.this,
                R.array.description_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add New Expense");
        dialogBuilder.setView(ADD_EXPENSE_VIEW);

        categorySpinner = (Spinner) ADD_EXPENSE_VIEW.findViewById(R.id.categorySpinner);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                double divide = Double.parseDouble(AMOUNT_INPUT.getText().toString()) / roommateCount;

                ExpenseObject newExpense = new ExpenseObject(generateid(), AMOUNT_INPUT.getText().toString(), divide, dueDate,
                       categorySpinner.getSelectedItem().toString(), LoginActivity.getUserName(), DESCRIPTION_INPUT.getText().toString(), false);
                //expenseArray.add(newExpense);
                dbHandler.addExpense(newExpense); //add new expense to the database
                expenseArray = dbHandler.pullExpenses(); //update expense array with new data

                //Create a corresponding feedObject and add it to the database
                Long tsLong = System.currentTimeMillis(); //need to know what time the event was added by a user
                java.util.Date time = new java.util.Date(tsLong);
                String timeString = time.toString(); // dow mon dd hh:mm:ss zzz yyyy
                FeedObject newFeedObject = new FeedObject("expense", newExpense.getExpenseName(), LoginActivity.getUserName(), timeString);
                dbHandler.addFeedObject(newFeedObject);

                ListAdapter adapter = new ExpenseAdapter(ExpenseActivity.this, R.layout.expense_row, expenseArray);
                ListView expenseListView = (ListView) findViewById(R.id.expenseListView);

                expenseListView.setAdapter(adapter);
                registerForContextMenu(expenseListView);
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();

        final DatePickerDialog.OnDateSetListener dueDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                MY_CALENDAR.set(Calendar.YEAR, year);
                MY_CALENDAR.set(Calendar.MONTH, monthOfYear);
                MY_CALENDAR.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        DATE_INPUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExpenseActivity.this, dueDatePicker, MY_CALENDAR.get(Calendar.YEAR),
                        MY_CALENDAR.get(Calendar.MONTH), MY_CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);

        DATE_INPUT.setText(df.format(MY_CALENDAR.getTime()));

        if (DATE_INPUT != null)
            dueDate = df.format(MY_CALENDAR.getTime());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.expenseListView) {
            ListView expenseListView = (ListView) v;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            object = (ExpenseObject) expenseListView.getItemAtPosition(info.position);

            menu.setHeaderTitle(object.getExpenseName());


            String[] menuOptions = getResources().getStringArray(R.array.menu_options_array);           // populates context menu with
            for (int i = 0; i < menuOptions.length; i++)                                                // edit, delete, and edit paid/unpaid
                menu.add(Menu.NONE, i, i, menuOptions[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        switch (menuItemIndex) {
            case 0:
                Log.i("Menu option selected", "edit");
                LayoutInflater inflater = LayoutInflater.from(this);
                editAlertDialog(inflater, object);
                break;

            case 1:
                Log.i("Menu option selected", "delete");
                dbHandler.deleteExpense(object.getid());
                expenseArray = dbHandler.pullExpenses();

                ListView expenseListView = (ListView) findViewById(R.id.expenseListView);
                ListAdapter adapter = new ExpenseAdapter(ExpenseActivity.this, R.layout.expense_row, expenseArray);
                expenseListView.setAdapter(adapter);
                break;

            case 2:
                adapter = new ExpenseAdapter(ExpenseActivity.this, R.layout.expense_row, expenseArray);
                expenseListView = (ListView) findViewById(R.id.expenseListView);

                object.setPaid(!object.isPaid());
                expenseListView.setAdapter(adapter);
                break;
        }

        return true;
    }

    public void editAlertDialog(LayoutInflater inflater, final ExpenseObject object) {               // builds dialog to edit item
        final View ADD_EXPENSE_VIEW = inflater.inflate(R.layout.expense_dialog, null);

        DATE_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editDueDate);
        final EditText AMOUNT_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editAmount);
        final EditText DESCRIPTION_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editNotes);


        AMOUNT_INPUT.setText(object.getExpenseAmount(), TextView.BufferType.EDITABLE);
        DESCRIPTION_INPUT.setText(object.getNotes(), TextView.BufferType.EDITABLE);
        DATE_INPUT.setText(object.getDueDate(), TextView.BufferType.EDITABLE);

        spinnerAdapter = ArrayAdapter.createFromResource(ExpenseActivity.this,
                R.array.description_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add New Expense");
        dialogBuilder.setView(ADD_EXPENSE_VIEW);

        categorySpinner = (Spinner) ADD_EXPENSE_VIEW.findViewById(R.id.categorySpinner);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                double divide = Double.parseDouble(AMOUNT_INPUT.getText().toString()) / roommateCount;
                DecimalFormat df = new DecimalFormat("#.00");

                object.setExpenseName(categorySpinner.getSelectedItem().toString());
                object.setExpenseAmount(AMOUNT_INPUT.getText().toString());
                object.setDividedAmount(Double.valueOf(df.format(divide)));
                object.setDueDate(DATE_INPUT.getText().toString());
                object.setNotes(DESCRIPTION_INPUT.getText().toString());

                ListAdapter adapter = new ExpenseAdapter(ExpenseActivity.this, R.layout.expense_row, expenseArray);
                ListView expenseListView = (ListView) findViewById(R.id.expenseListView);

                expenseListView.setAdapter(adapter);
                registerForContextMenu(expenseListView);
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();

        final DatePickerDialog.OnDateSetListener dueDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                MY_CALENDAR.set(Calendar.YEAR, year);
                MY_CALENDAR.set(Calendar.MONTH, monthOfYear);
                MY_CALENDAR.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        DATE_INPUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExpenseActivity.this, dueDatePicker, MY_CALENDAR.get(Calendar.YEAR),
                        MY_CALENDAR.get(Calendar.MONTH), MY_CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void deleteExpenseObject(ExpenseObject id) {

    }

    public int generateid() {
        id = id + 1;
        return id;
    }
}
