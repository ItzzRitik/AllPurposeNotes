package xtremedeveloper.allpurposenotes;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EditTextNotes extends AppCompatActivity {
    SharedPreferences notes;
    EditText textValue;
    FloatingActionButton editNotes;
    RelativeLayout editAnim;
    Intent intent;
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
        List<String> note_text = new Gson().fromJson(notes.getString("note_text", null), new TypeToken<ArrayList<String>>() {}.getType());
        note_text.set(intent.getIntExtra("position",0),textValue.getText().toString());
        notes.edit().putString("note_text", new Gson().toJson(note_text)).apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_notes);
        notes = getSharedPreferences("notes", Context.MODE_PRIVATE);
        getSupportActionBar().setElevation(0);
        intent=getIntent();
        setTitle(intent.getStringExtra("title"));

        textValue=findViewById(R.id.textValue);
        textValue.setTag(textValue.getKeyListener());
        textValue.setKeyListener(null);
        textValue.setText(intent.getStringExtra("text"));

        editNotes=findViewById(R.id.editNotes);
        editAnim=findViewById(R.id.editAnim);
        editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cy=(int)(editNotes.getY() + editNotes.getHeight() / 2);
                int cx=(int)(editNotes.getX() + editNotes.getWidth() / 2);
                Animator animator = ViewAnimationUtils.createCircularReveal(editAnim,cx,cy, 0,textValue.getHeight()*141/100);
                animator.setInterpolator(new DecelerateInterpolator());animator.setDuration(500);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {editAnim.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(EditTextNotes.this, R.anim.fade_out);
                        anim.setDuration(500);editAnim.startAnimation(anim);editNotes.setVisibility(View.GONE);}
                    @Override
                    public void onAnimationEnd(Animator animation) {editAnim.setVisibility(View.GONE);
                    textValue.setKeyListener((KeyListener) textValue.getTag());}
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                animator.start();
            }
        });

    }
}
