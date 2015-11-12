package com.joanzapata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.joanzapata.pdfview.sample.R;

import java.io.IOException;
import java.util.ArrayList;

public class PdfsActivity extends AppCompatActivity {

    private GridView mGridView;
    private ArrayList<String> pdfList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_layout);
        pdfList = new ArrayList<>();
        listAssetFiles("");
        System.out.println(pdfList);
        mGridView = (GridView) findViewById(R.id.grid);
        CustomGrid adapter = new CustomGrid(PdfsActivity.this, pdfList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                 start activity
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void listAssetFiles(String path) {
        String [] list;
        try {
            list = getAssets().list(path);
            for (String item: list) {
                if (item.endsWith("pdf")) {
                    pdfList.add(item);
                }
            }
        }catch (IOException e) {

            }
        }


    }
