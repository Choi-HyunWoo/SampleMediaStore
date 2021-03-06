package com.hw.corcow.samplemediastore;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA };
    String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC";

    GridView gridView;
    SimpleCursorAdapter mAdapter;

    int dataColumnIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.gridView);

        String[] from = { MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA };
        int[] to = { R.id.text_name, R.id.image_icon };
        mAdapter = new SimpleCursorAdapter(this, R.layout.view_item, null, from , to, 0);
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == dataColumnIndex) {
                    ImageView iv = (ImageView) view;
                    String path = cursor.getString(columnIndex);

                    // 사이즈가 작은 이미지로 만들자.
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 4;

                    Bitmap bm = BitmapFactory.decodeFile(path, opts);       // BitmapFactory에 옵션 추가 가능
                    iv.setImageBitmap(bm);
                    return true;
                }
                return false;
            }
        });

        gridView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return new CursorLoader(this, uri, projection, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
