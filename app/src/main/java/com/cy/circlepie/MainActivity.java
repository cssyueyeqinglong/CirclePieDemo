package com.cy.circlepie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

import static com.cy.circlepie.CirclePieAct.CirclePie_TITLE;
import static com.cy.circlepie.CirclePieAct.CirclePie_TYPE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRcv = (RecyclerView) findViewById(R.id.rcv);
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(this, Arrays.asList(getResources().getStringArray(R.array.main_item)));
        adapter.setOnItemClickLisenter(new MainAdapter.OnItemClickLisenter() {
            @Override
            public void onItemClick(String item, int position) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CirclePieAct.class);
                intent.putExtra(CirclePie_TYPE,position);
                intent.putExtra(CirclePie_TITLE,item);
                startActivity(intent);
            }
        });
        mRcv.addItemDecoration(new RecyclerItemDiliver(this));
        mRcv.setAdapter(adapter);
    }
}
