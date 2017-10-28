package xtremedeveloper.allpurposenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class EditTextNotes extends AppCompatActivity {
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
        getSupportActionBar().setElevation(0);
        Intent intent=getIntent();
        setTitle(intent.getStringExtra("title"));

    }
}
