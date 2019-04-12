package com.leaderpower.baechelin_owner_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.leaderpower.baechelin_owner_android.util.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestMainActivity extends AppCompatActivity {
    @BindView(R.id.test_listview)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        ButterKnife.bind(this);
        ArrayList<String> activities = new ArrayList<>();
        for (Class c : Constant.activities) activities.add(c.getSimpleName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TestMainActivity.this, Constant.activities[i]);
                Log.d("TEST", Constant.activities[i].getName());

                startActivity(intent);
            }
        });
    }
}
