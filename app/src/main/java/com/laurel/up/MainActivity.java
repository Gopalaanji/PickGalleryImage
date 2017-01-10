package com.laurel.up;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private ImageAdapter imageAdapter;
    CheckBox check;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFromSdcard();
        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        check = (CheckBox) findViewById(R.id.itemCheckBox);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
    }

    public void getFromSdcard() {
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "/gStorage/snapshot");

        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {

                f.add(listFile[i].getAbsolutePath());

            }
        }
    }


    public class ImageAdapter extends BaseAdapter {
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

        public View getView(int position, View convertView, ViewGroup parent) {
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
        }
    }

    class ViewHolder {
        ImageView imageview;

    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        Toast.makeText(this, f.indexOf(check.isChecked()), Toast.LENGTH_LONG).show();
//        new UploadFileToServer().execute();
        Log.d("line",""+f.indexOf(check.isChecked()));

    }
}

  /* class UploadFileToServer extends AsyncTask<Void, Integer, String> {

      private ProgressBar progressBar;
      private TextView txtPercentage;
       public static final String FILE_UPLOAD_URL = "http://videomon.southindia.cloudapp.azure.com/api/Upload/user/PostUserImage";

    @Override
    protected void onPreExecute() {
        // setting progress bar to zero

        progressBar.setProgress(0);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Making progress bar visible
        progressBar.setVisibility(View.VISIBLE);

        // updating progress bar value
        progressBar.setProgress(progress[0]);

        // updating percentage value
        txtPercentage.setText(String.valueOf(progress[0]) + "%");
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
            *//*AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new ProgressListener() {

                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
*//*
            File sourceFile = new File(filePath);

            // Adding file data to http body

            entity.addPart("image", new FileBody(sourceFile));

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            statusCode = response.getStatusLine().getStatusCode();

//                if (statusCode != 200) {
//
//                    responseString = response.getStatusLine().getReasonPhrase();
//                }
//                else {
//                    responseString = "Attendance marked successfully";
//                }

            responseString = EntityUtils.toString(r_entity);



        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e(TAG, "Response from server: " + result);

        showAlert(result);
        // showing the s
        // erver response in an alert dialog

        super.onPostExecute(result);
    }
}
*/