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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SAIRAM on 1/10/2017.
 */

public class MainActivity extends Activity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    // private ImageAdapter imageAdapter;
    private static final int MY_INTENT_CLICK = 302;
    static String picturePath;
    CheckBox check;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    public static final int REQUEST_CODE = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFromSdcard();
        // GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        // ImageView Image = (ImageView) findViewById(R.id.thumbImage);
        // check = (CheckBox) findViewById(R.id.itemCheckBox);
        //  imageAdapter = new ImageAdapter();
        //  imagegrid.setAdapter(imageAdapter);
      /*  check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // Toast.makeText(this, "hai", Toast.LENGTH_LONG).show();

            }
        });*/

/*

        Image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
            }
        });
*/
    }

    // public static final REQUEST_CODE = 1;
    public void select(View view) {


        File file = new File(android.os.Environment.getExternalStorageDirectory(), "/gStorage/snapshot");
        Intent intent = new Intent();
       // intent.setType("*/*");

           Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                   + "/gStorage/snapshot");
           intent.setDataAndType(uri, "text/csv");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), MY_INTENT_CLICK);
        // new UploadFileToServer().execute();

    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked()) {
            File file = new File(android.os.Environment.getExternalStorageDirectory(), "/gStorage/snapshot");
            Intent intent = new Intent();
            intent.setType("*/*");

//            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                    + "/gStorage/");
//            intent.setDataAndType(uri, "text/csv");

            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), MY_INTENT_CLICK);
            // new UploadFileToServer().execute();
            Toast.makeText(this, "Hai", Toast.LENGTH_LONG).show();

        }
    }

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
                new UploadFileToServer().execute();

            } catch (Exception e) {
                Log.e("Path Error", e.toString());
            }
        }
    }

    public void getFromSdcard() {
        try {
            File file = new File(android.os.Environment.getExternalStorageDirectory(), "/gStorage/snapshot");

            if (file.isDirectory()) {
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++) {

                    f.add(listFile[i].getAbsolutePath());

                }
            }
        } catch (Exception e) {
            Log.e("ee", e.getMessage());
        }
    }


    /* public class ImageAdapter extends BaseAdapter {
         private LayoutInflater mInflater;

         public ImageAdapter() {
             mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         }

         public int getCount() {
             return f.size();
         }

         public Object getItem(int position) {
             return position;
         }

         public long getItemId(int position) {
             return position;
         }

        *//* public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.gelleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }*//*
    }
*/
    class ViewHolder {
        ImageView imageview;
    }

    public class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        public static final String FILE_UPLOAD_URL = "http://videomon.southindia.cloudapp.azure.com/api/Upload/user/PostUserImage";

        // Directory name to store captured images and videos
        public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
        public static final String SESSION_NO = "SESSION_NO";
        public static final String MAIN = "MAIN";

        private ProgressBar progressBar;
        private TextView txtPercentage;
        long totalSize = 0;
        String shift, empno, empname, empid;
        int statusCode;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //  progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //  progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //  progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
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
                String path = MainActivity.picturePath;
                File sourceFile = new File(path);
                Log.d("path", path);
                Log.e("line91", "91");
                // File sourceFile = new File(android.os.Environment.getExternalStorageDirectory(), "/gStorage/snapshot/office.jpg");
                // Adding file data to http body
                Log.e("line91", "96");
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                Log.e("line91", "101");
                // Making server call

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                Log.e("line91", "105");


//            statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode != 200) {
//
//                    responseString = response.getStatusLine().getReasonPhrase();
//                }
//                else {
//                    responseString = "Attendance marked successfully";
//                }
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

            //  Log.e("128",responseString);
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
            // showing the s
            // erver response in an alert dialog

            super.onPostExecute(result);


        }


        private void showAlert(String message) {
            try {

                Log.e("message", message);


            } catch (Exception e) {
                Log.e("excep", e.getMessage());
            }
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Successfully marked attendance").setTitle("Response from Servers")

        message = statusCode == 200 ? "Succesfully marked attendance" : message;

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (statusCode == 200) {
                            SharedPreferences.Editor editor;
                            SharedPreferences pref;
                            pref = getSharedPreferences(MAIN, MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putInt(SESSION_NO, pref.getInt(SESSION_NO, 0) + 1);
                            editor.commit();
                        }
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
*/
        }
    }



}
