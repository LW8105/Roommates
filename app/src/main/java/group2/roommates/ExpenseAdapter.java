package group2.roommates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ExpenseAdapter extends ArrayAdapter<ExpenseObject> {
    private List<ExpenseObject> expenseList = null;

    public ExpenseAdapter(Context context, int layoutId, List<ExpenseObject> expenseArray) {
        super(context, layoutId, expenseArray);

        expenseList = expenseArray;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.expense_row, parent, false);

        String expenseName = expenseList.get(position).getExpenseName();
        String dueDate = expenseList.get(position).getDueDate();
        String expenseAmount = expenseList.get(position).getExpenseAmount();
        boolean paid = expenseList.get(position).isPaid();
        String author = expenseList.get(position).getAuthor();
        String notes = expenseList.get(position).getNotes();


        TextView expenseNameText = (TextView) customView.findViewById(R.id.expenseNameText);
        TextView dueDateText = (TextView) customView.findViewById(R.id.dueDateText);
        TextView expenseAmountText = (TextView) customView.findViewById(R.id.expenseAmountText);
        TextView paidStatusText = (TextView) customView.findViewById(R.id.expenseStatusText);
        TextView authorText = (TextView) customView.findViewById(R.id.authorText);
        TextView notesText = (TextView) customView.findViewById(R.id.expenseNotesText);

        expenseNameText.setText(expenseName);
        dueDateText.setText(dueDate);
        expenseAmountText.setText(expenseAmount);
        authorText.setText("Added by " + author);
        notesText.setText(notes);

        if (paid)
            paidStatusText.setText("Paid");
        else
            paidStatusText.setText("Not Paid");

        return customView;
    }


}
