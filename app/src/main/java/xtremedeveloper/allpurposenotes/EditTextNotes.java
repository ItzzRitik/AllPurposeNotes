package xtremedeveloper.allpurposenotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class EditTextNotes extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_notes);
        pref = getSharedPreferences("notes", Context.MODE_PRIVATE);
        getSupportActionBar().setElevation(0);
        Intent intent=getIntent();
        setTitle(intent.getStringExtra("title"));

    }
}
