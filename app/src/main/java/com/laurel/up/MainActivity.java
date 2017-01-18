package com.laurel.up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
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
import android.widget.Toast;

import java.io.File;
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
}
