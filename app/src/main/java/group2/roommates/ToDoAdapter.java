package group2.roommates;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDoItem> {
    private List<ToDoItem> thelist = null;

    public ToDoAdapter(Context context, int layoutId, List<ToDoItem> toDoArray) {
        super(context, layoutId, toDoArray);

        thelist = toDoArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.todo_row, parent, false);

        String author = thelist.get(position).getAuthor();
        String description = thelist.get(position).getDescription();
        boolean checked = thelist.get(position).isChecked();



        TextView authorText = (TextView) customView.findViewById(R.id.authorText);
        CheckBox descriptionText = (CheckBox) customView.findViewById(R.id.checkBox);


        authorText.setText("added by " + author);
        descriptionText.setText(description);
        descriptionText.setChecked(checked);

        return customView;

    }
}


