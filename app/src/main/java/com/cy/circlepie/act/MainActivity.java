package com.cy.circlepie.act;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.bumptech.glide.Glide;
import com.cy.circlepie.MainAdapter;
import com.cy.circlepie.R;
import com.cy.circlepie.RecyclerItemDiliver;
import com.cy.circlepie.view.PictureGameAct;

import java.util.Arrays;

import static com.cy.circlepie.act.CirclePieAct.CirclePie_TITLE;
import static com.cy.circlepie.act.CirclePieAct.CirclePie_TYPE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcv;
    private AutoCompleteTextView atv;
    private String[] datas = {"beijing1", "beijing2", "beijing3"};

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
                if (position < 3) {
                    intent.setClass(MainActivity.this, CirclePieAct.class);
                    intent.putExtra(CirclePie_TYPE, position);
                    intent.putExtra(CirclePie_TITLE, item);
                } else if (position == 3) {//拼图游戏
                    intent.setClass(MainActivity.this, PictureGameAct.class);
                } else if (position == 4) {//吸附点击的recyclerview
                    intent.setClass(MainActivity.this, ViewPagerActivity.class);
                }

                startActivity(intent);
            }
        });
        mRcv.addItemDecoration(new RecyclerItemDiliver(this));
        mRcv.setAdapter(adapter);

        atv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        atv.setAdapter(adapter1);
        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = data.edit();

                edit.putString("name", datas[position]);
                edit.commit();
//                android.os.Process.killProcess(android.os.Process.myPid());
                startActivity(new Intent(MainActivity.this, ViewPagerActivity.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
        atv.setText(data.getString("name", ""));
        Glide.with(this);
    }
}

