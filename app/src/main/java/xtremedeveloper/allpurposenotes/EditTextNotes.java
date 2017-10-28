package xtremedeveloper.allpurposenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditTextNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_notes);
        getSupportActionBar().setElevation(0);
        Intent intent=getIntent();
        setTitle(intent.getStringExtra("title"));

    }
}
