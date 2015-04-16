package group2.roommates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter<FeedObject> {

    private List<FeedObject> thelist = null;

    public FeedAdapter(Context context, int layoutId, List<FeedObject> feedArray) {
        super(context, layoutId, feedArray);

        thelist = feedArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.feed_row, parent, false);

        //String type = thelist.get(position).getType();
        //String title = thelist.get(position).getTitle();
        //String author = thelist.get(position).getAuthor();
        //String timeCreated = thelist.get(position).getTimeCreated();

        TextView feedDescription = (TextView) customView.findViewById(R.id.feedDescription);
        TextView feedTime = (TextView) customView.findViewById(R.id.feedTime);

        feedDescription.setText(thelist.get(position).toString());
        feedTime.setText(thelist.get(position).getTimeCreated());

        return customView;

    }
}
