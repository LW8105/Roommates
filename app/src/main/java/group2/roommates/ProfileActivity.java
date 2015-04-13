package group2.roommates;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileActivity extends ActionBarActivity {

    private ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("My Profile");
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nameTextView.setText(LoginActivity.getUserName());
    }

    public void avatarClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
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
                avatarImageView.setImageBitmap(resizedAvatar);
            } catch (Exception e) {
                e.toString();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
