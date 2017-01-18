package com.laurel.up;

import com.laurel.up.AndroidMultiPartEntity.ProgressListener;
import com.nispok.snackbar.Snackbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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


import static android.R.id.message;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SAIRAM on 1/11/2017.
 */
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
                    new ProgressListener() {
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

    static String outmsg;

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

