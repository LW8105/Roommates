package group2.roommates;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity {

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String[] mDrawerItems;
    public ArrayList<FeedObject> feedArray = new ArrayList<FeedObject>();
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        dbHandler = new DBHandler(this, null, null, 1); //SQLite DB handler
        feedArray = dbHandler.pullFeed(); //fill allEventsArray with calendarEvent objects from the db

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.drawerList);
        mDrawerItems = getResources().getStringArray(R.array.drawer_list);

        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDrawerItems));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                if(position == 0) {
                    Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(i);
                }
                if(position == 1){
                    Intent i = new Intent(HomeActivity.this, CalendarActivity.class);
                    startActivity(i);
                }
                if(position == 2){
                    Intent i = new Intent(HomeActivity.this, ToDoActivity.class);
                    startActivity(i);
                }
                if(position == 3){
                    Intent i = new Intent(HomeActivity.this, ExpenseActivity.class);
                    startActivity(i);
//                    Toast.makeText(HomeActivity.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }//end of onItemClick function
        });//end of listener declaration

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View v){
                super.onDrawerOpened(v);
                invalidateOptionsMenu();
                syncState();
            }
        };//end of toggle declaration

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        //Populate the ListView
        ListAdapter adapter = new FeedAdapter(this, R.layout.feed_row, feedArray);
        ListView feedListView = (ListView)findViewById(R.id.feedListView);
        feedListView.setAdapter(adapter);

    }//end of onCreate

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:{
                if(mDrawerLayout.isDrawerOpen(mDrawerList)){
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHandler = new DBHandler(this, null, null, 1); //SQLite DB handler
        feedArray = dbHandler.pullFeed(); //fill allEventsArray with calendarEvent objects from the db
        //Populate the ListView
        ListAdapter adapter = new FeedAdapter(this, R.layout.feed_row, feedArray);
        ListView feedListView = (ListView)findViewById(R.id.feedListView);
        feedListView.setAdapter(adapter);
    }

}
