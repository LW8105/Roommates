package group2.roommates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RoommatezActivity extends ActionBarActivity {

    ArrayList<Roommate> roommateArray = new ArrayList<>();
    ImageView roommateAvatar;
    Button addRoommate;
    LayoutInflater inflater;
    ExpenseActivity t;
    ImageView roommateImageView;
    int id = 0;

    ListAdapter adapter;
    ListView roommateListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommatez);
        setTitle("Roommatez");

        inflater = LayoutInflater.from(this);
        addRoommate = (Button) findViewById(R.id.addRoommateButton);

        addRoommate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog();
            }
        });

        roommateImageView = (ImageView) findViewById(R.id.roommateImageView);


        Drawable bartImage = getResources().getDrawable(R.drawable.bart);
        Drawable lisaImage = getResources().getDrawable(R.drawable.lisa);
        Drawable maggieImage = getResources().getDrawable(R.drawable.maggie);

        Roommate bart = new Roommate(17, "Bart", bartImage);
        Roommate lisa = new Roommate(11, "Lisa", lisaImage);
        Roommate maggie = new Roommate(3, "Maggie", maggieImage);

        roommateArray.add(bart);
        roommateArray.add(lisa);
        roommateArray.add(maggie);

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

    public void buildAlertDialog() {

        final View ADD_ROOMMATE_VIEW = inflater.inflate(R.layout.roommate_dialog, null);
        final EditText NAME_INPUT = (EditText) ADD_ROOMMATE_VIEW.findViewById(R.id.editRoommateName);
        roommateImageView = (ImageView) findViewById(R.id.roommateImageView);
        final Drawable bartImage = getResources().getDrawable(R.drawable.bart);

        adapter = new RoommateAdapter(this, R.layout.roommate_row, roommateArray);
        roommateListView = (ListView) findViewById(R.id.roommateListView);

        NAME_INPUT.setText("", TextView.BufferType.EDITABLE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add New Roommate");
        dialogBuilder.setView(ADD_ROOMMATE_VIEW);

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListAdapter adapter = new RoommateAdapter(RoommatezActivity.this, R.layout.roommate_row, roommateArray);
                ListView roommateListView = (ListView) findViewById(R.id.roommateListView);

                Roommate newRooomate = new Roommate(generateid(),
                        NAME_INPUT.getText().toString(), bartImage);

                roommateArray.add(newRooomate);

                roommateListView.setAdapter(adapter);
                registerForContextMenu(roommateListView);

            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            ExifInterface exif;
            try {

                exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                matrix.postRotate(correctOrientation(orientation));
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true); //Make sure the image is oriented correctly

                Bitmap resizedAvatar = scaleBitmap(rotatedBitmap, 150, 150); //Scale down the picture to fit the ImageView while maintaining aspect ratio
                //Bitmap resizedAvatar = Bitmap.createScaledBitmap(rotatedBitmap, 150, 150, false);
                //Log.w("path of image", picturePath + "");
                roommateImageView.setImageBitmap(resizedAvatar);

                roommateListView.setAdapter(adapter);

            } catch (Exception e) {
                e.toString();
            }


        }
    }

    public void avatarClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public Bitmap scaleBitmap(Bitmap originalBitmap, int width, int height) {// width and height parameters are the desired dimensions of the returned bitmap
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float originalWidth = originalBitmap.getWidth();
        float originalHeight = originalBitmap.getHeight();

        Canvas canvas = new Canvas(newBitmap);

        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalBitmap, transformation, paint);

        return newBitmap;
    }

    public int correctOrientation(int orientation) {//returns the number of degrees to rotate (clockwise) based on the dimension tag
        if (orientation == 6) {
            return 90;
        }
        if (orientation == 3) {
            return 180;
        }
        if (orientation == 8) {
            return 270;
        }
        return 0;
    }

    public int generateid() {
        id = id + 1;
        return id;
    }
}
