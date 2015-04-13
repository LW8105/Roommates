package group2.roommates;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.content.Intent;
import android.widget.EditText;


public class LoginActivity extends ActionBarActivity {

    static private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public static String getUserName() {
        return userName;
    }

    public void logInClick(View view) {
        Intent i = new Intent(this, HomeActivity.class);
        EditText nameField = (EditText) findViewById(R.id.nameField);
        userName = nameField.getText().toString();

        //String userMessage = inputText.getText().toString();
        //i.putExtra("userMessage", userMessage);

        startActivity(i);
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
