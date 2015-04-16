package group2.roommates;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RoommateAdapter extends ArrayAdapter<Roommate>{

    private List<Roommate> roommateList = null;

    public RoommateAdapter(Context context, int layoutId, List<Roommate> roommateArray) {
        super(context, layoutId, roommateArray);

        roommateList = roommateArray;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.roommate_row, parent, false);

        String roommateName = roommateList.get(position).getName();
        Drawable avatar = roommateList.get(position).getAvatar();

        TextView roommateNameText = (TextView) customView.findViewById(R.id.roommateName);
        ImageView roommateAvatar = (ImageView) customView.findViewById(R.id.roommateAvatar);

        roommateNameText.setText(roommateName);
        roommateAvatar.setImageDrawable(avatar);

        return customView;
    }

}
