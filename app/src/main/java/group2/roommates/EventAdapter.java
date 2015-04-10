package group2.roommates;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<CalendarEvent>{

    private List<CalendarEvent> thelist = null;

    public EventAdapter(Context context, int layoutId, List<CalendarEvent> dailyList) {
        super(context, layoutId, dailyList);

        thelist = dailyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);


        //String title = getItem(position).getTitle();
       // String author = getItem(position).getAuthor();
        //String description = getItem(position).getDescription();

        String title = thelist.get(position).getTitle();
        String author = thelist.get(position).getAuthor();
        String description = thelist.get(position).getDescription();


        TextView titleText = (TextView) customView.findViewById(R.id.titleText);
        TextView authorText = (TextView) customView.findViewById(R.id.authorText);
        TextView descriptionText = (TextView) customView.findViewById(R.id.descriptionText);

        titleText.setText(title);
        authorText.setText("added by " + author);
        descriptionText.setText(description);

        return customView;

    }
}
