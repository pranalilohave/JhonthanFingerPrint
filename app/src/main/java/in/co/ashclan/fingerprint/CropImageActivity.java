package in.co.ashclan.fingerprint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.takusemba.cropme.CropView;
import com.takusemba.cropme.OnCropListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.co.ashclan.utils.Utility;

public class CropImageActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 0;
    Context mContext;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 2, SELECT_FILE = 1;
    private Bitmap bitmapImage;
    private File destination;
    private String imagePath;
    private CropView cropView;
    private String imageFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        mContext =this;
        cropView = findViewById(R.id.crop_view);
    }

    public void onClickSelectImage(View view) {
        selectImage();
    }

    public void onClickSaveImage(View view) {
        try {
            cropView.crop(new OnCropListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {

                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, bStream);
                    byte[] byteArray = bStream.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("bytesArray", byteArray);
                    intent.putExtra("ImagePath", getImagePath());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure() {

                }
            });
        }catch (Exception e){
            Toast.makeText(mContext,"selected image",Toast.LENGTH_LONG).show();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(mContext);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent() {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }
        Bitmap bitmap = null;
    private File createImageFile() throws IOException {
        {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            imageFilePath = image.getAbsolutePath();

            return image;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode==REQUEST_IMAGE)
                cropView.setUri(Uri.parse(imageFilePath));
        }
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                cropView.setUri(Uri.parse(imageFilePath));
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        bitmapImage = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String fileName=System.currentTimeMillis() + ".jpg";
        destination = new File(Environment.getExternalStorageDirectory(),
                fileName);
        //    setImagePath(fileName);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Toast.makeText(mContext,destination.getAbsolutePath(),Toast.LENGTH_LONG).show();
        //cropView.setUri(Uri.parse(destination.getAbsolutePath()));
        cropView.setBitmap(bitmapImage);
        //        imageViewFingerPrint2.setImageBitmap(bitmapImage);
        //     memberDetails.setPhotoLocalPath(BitMapToString(bitmapImage));
        setImagePath(destination.getAbsolutePath());
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        bitmapImage=null;
        String path ="";
        if (data != null) {
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                cursor.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //.makeText(mContext,path,Toast.LENGTH_LONG).show();
        cropView.setBitmap(bitmapImage);
        //imageViewFingerPrint2.setImageBitmap(bitmapImage);
        //   memberDetails.setPhotoLocalPath(BitMapToString(bitmapImage));
        setImagePath(path);
    }

    public void setImagePath(String path){
  //      editImage=true;
        imagePath=path;
    }

    public String getImagePath(){
    //    memberDetails.setPhotoLocalPath(imagePath);
        return imagePath;
    }

}
