package group2.roommates;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RoommatezActivity extends ActionBarActivity {

    ArrayList<Roommate> roommateArray = new ArrayList<>();
    ImageView roommateAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommatez);
        setTitle("Roommatez");

        Drawable bartImage = getResources().getDrawable(R.drawable.bart);
        Drawable lisaImage = getResources().getDrawable(R.drawable.lisa);

        Roommate bart = new Roommate(17, "Bart", bartImage);
        Roommate lisa = new Roommate(11, "Lisa", lisaImage);

        roommateArray.add(bart);
        roommateArray.add(lisa);

        ListAdapter adapter = new RoommateAdapter(this, R.layout.roommate_row, roommateArray);
        ListView roommateListView = (ListView) findViewById(R.id.roommateListView);

        roommateListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roommatez, menu);
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
