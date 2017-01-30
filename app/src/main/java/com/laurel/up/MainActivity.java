package com.laurel.up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by SAIRAM on 1/10/2017.
 */

public class MainActivity extends Activity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;

    private static final int MY_INTENT_CLICK = 302;
    static String picturePath;
    Spinner complaintTypeId;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    public static final int REQUEST_CODE = 1;
    ImageView imageView;
    Button upload, selectimage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        complaintTypeId = (Spinner) findViewById(R.id.ctype);
        selectimage = (Button) findViewById(R.id.select);
        // getFromSdcard();

    }


    public void select(View view) {

/*
      File file = new File(android.os.Environment.getExternalStorageDirectory(), "/gStorage/snapshot");
        Intent intent = new Intent();


           Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                   + "/gStorage/snapshot");
           intent.setDataAndType(uri, "text/csv");

        intent.setAction(Intent.ACTION_GET_CONTENT);*/
//
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MY_INTENT_CLICK);
        // startActivityForResult(Intent.createChooser(intent, "Select File"), MY_INTENT_CLICK);

    }

    static String destinationImagePath;
    // String mSelectedImagePath;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            try {
                Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                Log.d("Picture Path", picturePath);
//                imageView.setVisibility(View.VISIBLE);
//                upload.setVisibility(View.VISIBLE);



                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            } catch (Exception e) {
                Log.e("Path Error", e.toString());
            }
        }
    }

    int[] i = {0, 53, 52, 56, 78, 65, 58, 61, 133, 59, 60, 74, 24, 29, 64, 89, 90, 79, 35, 72, 77, 91, 63, 57, 49, 140, 51, 54, 30, 48, 62, 50, 81, 28, 70, 73, 80, 69, 42, 134, 68, 76, 67};
    int complaintId;

    public void upload(View view) {
        if (complaintTypeId.getSelectedItemPosition() > 0) {
            complaintId = i[complaintTypeId.getSelectedItemPosition()];
            Log.e("complaintId", complaintId + "");
            String mSelectedImagePath = picturePath;
            System.out.println("mSelectedImagePath : " + mSelectedImagePath);

            try {
                File sd = Environment.getExternalStorageDirectory();
                if (sd.canWrite()) {
                    System.out.println("(sd.canWrite()) = " + (sd.canWrite()));
                    destinationImagePath = "/" + complaintId + "-VMC-20170129130015.jpg";   // this is the destination image path.
                    File source = new File(mSelectedImagePath);
                    File destination = new File(sd, destinationImagePath);
                    if (source.exists()) {
                        FileChannel src = new FileInputStream(source).getChannel();
                        FileChannel dst = new FileOutputStream(destination).getChannel();
                        dst.transferFrom(src, 0, src.size());       // copy the first file to second.....
                        src.close();
                        dst.close();
                        Toast.makeText(getApplicationContext(), "Check the copy of the image in the same path as the gallery image. File is name file.jpg", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "SDCARD Not writable.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println("Error :" + e.getMessage());
                Log.e("excepupload", e.getMessage() + "");
            }


            new UploadFileToServer().execute();
        } else {
            Toast.makeText(this, "please select ComplaintTypeId", Toast.LENGTH_LONG).show();
        }

    }

    public class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        public static final String FILE_UPLOAD_URL = "http://videomon.southindia.cloudapp.azure.com/api/Upload/user/PostUserImage";

        long totalSize = 0;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        //       @SuppressWarnings("deprecation")

        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                String path = MainActivity.destinationImagePath;
                path = "/storage/emulated/0/" + path;
                Log.e("opath", path);
                File sourceFile = new File(path);


                Log.d("path", path);
                Log.e("line91", "91");
                Log.e("line91", "96");
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                Log.e("line91", "101");
                // Making server call

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                Log.e("line91", "105");
                Log.e("line91", "115");
                responseString = EntityUtils.toString(r_entity);
            } catch (ClientProtocolException e) {
                Log.e("c ex", e.getMessage());
                responseString = e.toString();
            } catch (IOException e) {
                Log.e("i ex", e.getMessage());

                responseString = e.toString();

            } catch (Exception e) {
                Log.e(" ex", e.getMessage());

                e.printStackTrace();
            }


            return responseString;

        }


        @Override
        protected void onPostExecute(String result) {
            try {

                Log.e("result", result);
                showAlert(result);

            } catch (Exception e) {
                Log.e("excep148", e.getMessage());
            }

            super.onPostExecute(result);


        }


        private void showAlert(String message) {
            try {
                Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_LONG);
            /*    imageView.setVisibility(View.GONE);
                upload.setVisibility(View.GONE);*/
                Log.e("message", message);


            } catch (Exception e) {
                Log.e("excep", e.getMessage());
            }

        }
    }


}
