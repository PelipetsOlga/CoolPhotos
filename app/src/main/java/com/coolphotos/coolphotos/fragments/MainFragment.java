package com.coolphotos.coolphotos.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.coolphotos.coolphotos.R;
import com.coolphotos.coolphotos.binders.PicturesViewBinder;
import com.coolphotos.coolphotos.provider.PictureProvider;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Calendar;

/**
 * Created by Olga on 14.05.2016.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private long countPictures;
    private RelativeLayout empty;
    private GridView grid;
    //private ImageView eyescreamImageView;
    private ProgressBar progressBar;
    private String picturePath;
    private boolean selectMenu = false;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabGallery;
    private FloatingActionsMenu fabMenu;

    private boolean isAddPicture = false;

    public static MainFragment getInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_layout, null);
        initViews(view);
       // fillData();
        //updateFAB();
        //addPictureForEmpty();
        //setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View root) {
        empty = (RelativeLayout) root.findViewById(R.id.empty);
        grid = (GridView) root.findViewById(R.id.grid);
        progressBar = (ProgressBar) root.findViewById(R.id.progressbar);
        fabCamera= (FloatingActionButton) root.findViewById(R.id.fab_camera);
        fabGallery= (FloatingActionButton) root.findViewById(R.id.fab_gallery);
        fabCamera.setOnClickListener(this);
        fabGallery.setOnClickListener(this);
        fabMenu= (FloatingActionsMenu) root.findViewById(R.id.multiply_actions);
    }

    private void fillData() {
        Cursor cursor = getActivity().getContentResolver().query(PictureProvider.PICTURE_CONTENT_URI, null, null, null, null);
        getActivity().startManagingCursor(cursor);

        String from[] = {PictureProvider.PICTURE_FILE_PREVIEW};
        int to[] = {R.id.picture};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.item_picture, cursor, from, to);

        adapter.setViewBinder(new PicturesViewBinder(getActivity()));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*View cell = adapter.getView(position, null, null);
                ImageView selecting = (ImageView) cell.findViewById(R.id.selecting_picture);
                selecting.setVisibility(View.INVISIBLE);*/
            }
        });
        grid.setAdapter(adapter);
    }

    private void updateFAB() {
        //fab set on click listener
    }

    private void addPictureForEmpty() {
    /*    if (isAddPicture) {
            selectPicture();
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /* private void selectPicture() {
        new Picker.Builder(this, new MyPickListener(), R.style.MIP_theme)
                .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                .build()
                .startActivity();
    }

    private class MyPickListener implements Picker.PickListener {
        @Override
        public void onPickedSuccessfully(final ArrayList<ImageEntry> images) {
            selectMenu = false;

            progressBar.setVisibility(View.VISIBLE);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch(images);
                }
            });
            t.start();

            // AddPicturesAsyncTask task = new AddPicturesAsyncTask();
            //task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, PicturesActivity.this, images);
        }

        @Override
        public void onCancel() {
            selectMenu = false;
        }
    }*/

//    private void selectPicture() {
//        // choose photo from gallery
//        Intent intentGallery = new Intent();
//        intentGallery.setType("image/*");
//        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
//        allowMultiple(intentGallery);
//        startActivityForResult(
//                Intent.createChooser(intentGallery,
//                        getResources().getString(R.string.chooser_gallery)),
//                Constants.REQUEST_CODE_GALLERY);
//    }

//    @SuppressLint("NewApi")
//    private void allowMultiple(Intent intent){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        }
//    }

 /*   private void capturePicture() {
        Intent intentCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File f = HappyMomentsUtils.generateCaptureFile(albumPath, 0);
        picturePath = f.getAbsolutePath();
        intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intentCapture, Constants.REQUEST_CODE_PHOTO);
    }*/
    ;

  /*  private void playPauseAlbum() {
        if (isPlaying == PictureProvider.PLAY) {
            isPlaying = PictureProvider.PlAY_NOT;
        } else {
            isPlaying = PictureProvider.PLAY;
        }
        updateFAB();

        //update album
        ContentValues cvPlayPause = new ContentValues();
        if (isPlaying == PictureProvider.PLAY) {
            cvPlayPause.put(PictureProvider.ALBUM_IS_PLAY, PictureProvider.PLAY);
        } else {
            cvPlayPause.put(PictureProvider.ALBUM_IS_PLAY, PictureProvider.PlAY_NOT);
        }
        Uri uriAlbumPlayStop = ContentUris.withAppendedId(PictureProvider.ALBUM_CONTENT_URI, idAlbum);
        getContentResolver().update(uriAlbumPlayStop, cvPlayPause, null, null);

        //update album's pictures
        cvPlayPause = new ContentValues();
        if (isPlaying == PictureProvider.PLAY) {
            cvPlayPause.put(PictureProvider.PICTURE_IS_PLAY, PictureProvider.PLAY);
        } else {
            cvPlayPause.put(PictureProvider.PICTURE_IS_PLAY, PictureProvider.PlAY_NOT);
        }
        getContentResolver().
                update(PictureProvider.PICTURE_CONTENT_URI, cvPlayPause,
                        PictureProvider.PICTURE_ALBUM_ID + " = " + idAlbum, null);


        refreshHeader();
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actions:
                //todo actionmode
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_camera:
                fabMenu.collapse();
                Toast.makeText(getActivity(), "Camera", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_gallery:
                fabMenu.collapse();
                Toast.makeText(getActivity(), "Gallery", Toast.LENGTH_SHORT).show();
                break;
        }
    }

/*
    private class AddPictureAsyncTask extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Object... param) {
            String oldPicturePath = (String) param[0];
            AppCompatActivity ctx = (AppCompatActivity) param[1];
            String newPicturePath = (String) param[2];
            String previewPath = (String) param[3];

            boolean done = HappyMomentsUtils.rotateAndSaveCapture(oldPicturePath, ctx, newPicturePath);

            if (!done) {
                return false;
            }

            HappyMomentsUtils.generatePreview(newPicturePath, previewPath);
            insertImagesToContentProvider(newPicturePath, previewPath);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            if (aBoolean) {
                countPictures++;
                updateUI();
            } else {
                Toast.makeText(PicturesActivity.this, R.string.error_picture, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void updateUI() {
        invalidateOptionsMenu();
        updateLayout();
        refreshHeader();
    }

    public void countDownLatch(final ArrayList<ImageEntry> images) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newFixedThreadPool(processors + 1);
        final int size = images.size();
        final boolean isDone[] = new boolean[size];
        final CountDownLatch countDownLatch = new CountDownLatch(size);
        for (final ImageEntry im : images) {
            final ImageEntry image = im;
            final int position = images.indexOf(image);
            isDone[position] = false;

            es.execute(new Runnable() {
                @Override
                public void run() {

                    File pictureFile = HappyMomentsUtils.generateCaptureFile(albumPath, position);
                    File previewFile = HappyMomentsUtils.generatePreviewFile(albumPath, position);
                    String oldPicturePath = image.path;
                    String newPicturePath = pictureFile.getAbsolutePath();
                    String previewPath = previewFile.getAbsolutePath();
                    boolean done = HappyMomentsUtils
                            .rotateAndSaveCapture(oldPicturePath, PicturesActivity.this, newPicturePath);
                    isDone[position] = done;

                    if (!done) {
                        // break;
                        Thread.currentThread().interrupt();
                        countDownLatch.countDown();
                    }
                    HappyMomentsUtils.generatePreview(newPicturePath, previewPath);
                    insertImagesToContentProvider(newPicturePath, previewPath);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();

            boolean aBoolean = false;
            for (boolean bool : isDone) {
                if (bool) {
                    aBoolean = bool;
                } else {
                    aBoolean = false;
                    break;
                }
            }
            final boolean finalABoolean = aBoolean;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    if (finalABoolean) {
                        countPictures = countPictures + images.size();
                        updateUI();
                    } else {
                        Toast.makeText(PicturesActivity.this, R.string.error_picture, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    private class AddPicturesAsyncTask extends AsyncTask<Object, Void, Boolean> {

        private ArrayList<ImageEntry> images;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Object... param) {
            AppCompatActivity ctx = (AppCompatActivity) param[0];
            images = (ArrayList<ImageEntry>) param[1];

            for (ImageEntry image : images) {
                //  String selectedImagePath = image.path;
                File pictureFile = HappyMomentsUtils.generateCaptureFile(albumPath);
                File previewFile = HappyMomentsUtils.generatePreviewFile(albumPath);

                String oldPicturePath = image.path;
                String newPicturePath = pictureFile.getAbsolutePath();
                String previewPath = previewFile.getAbsolutePath();

                boolean done = HappyMomentsUtils.rotateAndSaveCapture(oldPicturePath, ctx, newPicturePath);

                if (!done) {
                   // break;
                    return false;
                }

                HappyMomentsUtils.generatePreview(newPicturePath, previewPath);
                insertImagesToContentProvider(newPicturePath, previewPath);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            if (aBoolean) {
                countPictures = countPictures + images.size();
                updateUI();
            } else {
                Toast.makeText(PicturesActivity.this, R.string.error_picture, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
        }
    }*/

    private void insertImagesToContentProvider(String newPicturePath, String previewPath) {
        Calendar calendar = Calendar.getInstance();
        Long date = calendar.getTimeInMillis();
        ContentValues cv = new ContentValues();
        cv.put(PictureProvider.PICTURE_DATE, date);
        cv.put(PictureProvider.PICTURE_IS_PLAY, false);
        cv.put(PictureProvider.PICTURE_FILE, newPicturePath);
        cv.put(PictureProvider.PICTURE_FILE_PREVIEW, previewPath);
        Uri newUri = getActivity().getContentResolver()
                .insert(PictureProvider.PICTURE_CONTENT_URI, cv);
    }
}
