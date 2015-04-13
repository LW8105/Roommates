package group2.roommates;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseActivity extends ActionBarActivity {

    private ArrayList<ExpenseObject> expenseArray = new ArrayList<>();
    final Calendar MY_CALENDAR = Calendar.getInstance();
    EditText DATE_INPUT;
    String dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setTitle("Expenses");

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
        final double AMOUNT;

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);

//        final Spinner descriptionSpinner = (Spinner) findViewById(R.id.editDescription);
//        final ArrayAdapter<CharSequence> descriptionAdapter = ArrayAdapter.createFromResource(this,
//                R.array.description_array, android.R.layout.simple_spinner_item);
//        descriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        descriptionSpinner.setAdapter(descriptionAdapter);


        final View ADD_EXPENSE_VIEW = inflater.inflate(R.layout.expense_dialog, null);

        final EditText NAME_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editDescription);

//        final String NAME_INPUT = descriptionSpinner.getSelectedItem().toString();

        DATE_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editDueDate);
        final EditText AMOUNT_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editAmount);
        final EditText DESCRIPTION_INPUT = (EditText) ADD_EXPENSE_VIEW.findViewById(R.id.editNotes);



        NAME_INPUT.setText("", TextView.BufferType.EDITABLE);
        AMOUNT_INPUT.setText("", TextView.BufferType.EDITABLE);
        DESCRIPTION_INPUT.setText("", TextView.BufferType.EDITABLE);
        DATE_INPUT.setText("", TextView.BufferType.EDITABLE);



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add New Expense");
        dialogBuilder.setView(ADD_EXPENSE_VIEW);
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExpenseObject newExpense = new ExpenseObject(0, "$" + AMOUNT_INPUT.getText().toString() + " ", dueDate,
                                NAME_INPUT.getText().toString(), LoginActivity.getUserName(), DESCRIPTION_INPUT.getText().toString(), false);
                        expenseArray.add(newExpense);

                        ListAdapter adapter = new ExpenseAdapter(ExpenseActivity.this, R.layout.expense_row, expenseArray);
                        ListView expenseListView = (ListView) findViewById(R.id.expenseListView);
                        expenseListView.setAdapter(adapter);

                        expenseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                Log.v("Long Click", "position: " + position);




                                return true;
                            }
                        });

                    }

                }

        );

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
        String dateFormat = "mm/dd/yy";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);

        DATE_INPUT.setText(df.format(MY_CALENDAR.getTime()));

        if (DATE_INPUT != null)
            dueDate = df.format(MY_CALENDAR.getTime());
    }

}
