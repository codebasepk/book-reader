/**
 * Copyright 2014 Joan Zapata
 *
 * This file is part of Android-pdfview.
 *
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.joanzapata;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnErrorOccurredListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.joanzapata.pdfview.sample.R;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnErrorOccurredListener, OnDrawListener {

    private PDFView mPdfView;
    private final int RESULT_CODE = 100;
    private static boolean isFileChooserShown = false;
    private static File currentFile;
    private static boolean isFileLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPdfView = (PDFView) findViewById(R.id.pdfview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Helpers.getPreviousSavedFile().equals("") && !isFileChooserShown) {
            showFileChooser();
            isFileChooserShown = true;
            isFileLoaded = false;
        } else {
            loadPdfFile(Helpers.getPreviousSavedFile(), true);
        }
    }

    private void loadPdfFile(String path, boolean assests) {
        File file = new File(path);
        currentFile = file;
        if (assests) {
            mPdfView.fromAsset("sample.pdf")
                    .defaultPage(1)
                    .showMinimap(true)
                    .enableSwipe(true)
                    .onDraw(this)
                    .onLoad(this)
                    .onPageChange(this)
                    .load();

        } else {
            mPdfView.fromFile(file)
                    .defaultPage(1)
                    .showMinimap(true)
                    .enableSwipe(true)
                    .onDraw(this)
                    .onLoad(this)
                    .onPageChange(this)
                    .load();
        }
        isFileLoaded = true;
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFileLoaded) {
            Helpers.saveCurrentPage(currentFile.getName(), mPdfView.getCurrentPage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFileLoaded) {
            Helpers.saveCurrentPage(currentFile.getName(), mPdfView.getCurrentPage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE && data != null) {
            isFileLoaded = true;
            Uri uri = data.getData();
            String path = uri.getPath();
            loadPdfFile(path, false);
            Helpers.savePreviousOpenedFile(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.open_file_chooser)   {
            showFileChooser();
            return true;
        } else if (id == R.id.pages)  {
            showAlertDialog(MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.pages).setTitle(mPdfView.getCurrentPage() + "/" + mPdfView.getPageCount());
        return super.onPrepareOptionsMenu(menu);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    RESULT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void loadComplete(int nbPages) {
        System.out.println(Helpers.getLastLoadedPage(currentFile.getName()));
        if (Helpers.getLastLoadedPage(currentFile.getName()) != 0) {
            mPdfView.jumpTo(Helpers.getLastLoadedPage(currentFile.getName())+1);
        }

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        invalidateOptionsMenu();
    }


    public void showAlertDialog(final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Move to page");
        alertDialog.setMessage("Enter page number");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
//        alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int num = 0;
                        String text = input.getText().toString();
                        try {
                            num = Integer.parseInt(text);
                            Log.i("", num + " is a number");
                        } catch (NumberFormatException e) {
                            Log.i("", text + "is not a number");
                            Toast.makeText(AppGlobals.getContext(), "please enter a valid number", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        if (num <= mPdfView.getPageCount()) {
                            mPdfView.jumpTo(num+1);
                        } else {
                            Toast.makeText(getApplicationContext(), "page limit exceeded", Toast.LENGTH_SHORT).show();

                        }
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void errorOccured() {

    }
}
