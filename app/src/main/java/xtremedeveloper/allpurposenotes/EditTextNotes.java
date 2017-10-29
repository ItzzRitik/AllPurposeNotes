package xtremedeveloper.allpurposenotes;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EditTextNotes extends AppCompatActivity {
    SharedPreferences notes;
    EditText windowTitle,textValue;
    FloatingActionButton editNotes;
    RelativeLayout editAnim;
    Intent intent;
    Animation anim;
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
        note_text = new Gson().fromJson(notes.getString("note_title", null), new TypeToken<ArrayList<String>>() {}.getType());
        note_text.set(intent.getIntExtra("position",0),windowTitle.getText().toString());
        notes.edit().putString("note_title", new Gson().toJson(note_text)).apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_notes);
        notes = getSharedPreferences("notes", Context.MODE_PRIVATE);
        intent=getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setElevation(0);
        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.edit_actionbar, null);

        actionBar.setCustomView(customNav, lp1);


        windowTitle=customNav.findViewById(R.id.windowTitle);
        windowTitle.setShowSoftInputOnFocus(true);
        windowTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        windowTitle.setText(intent.getStringExtra("title"));
        windowTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(windowTitle.getInputType()!=InputType.TYPE_NULL){showKeyboard(windowTitle,true);}
            }
        });

        textValue=findViewById(R.id.textValue);
        textValue.setShowSoftInputOnFocus(true);
        textValue.setText(intent.getStringExtra("text"));
        textValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textValue.getInputType()!=InputType.TYPE_NULL){showKeyboard(textValue,true);}
            }
        });

        editAnim=findViewById(R.id.editAnim);
        editNotes=findViewById(R.id.editNotes);
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
                        anim = AnimationUtils.loadAnimation(EditTextNotes.this, R.anim.fade_out);editNotes.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {@Override public void run() {editAnim.startAnimation(anim);}},200);}
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        editAnim.setVisibility(View.GONE);
                        textValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        windowTitle.setInputType(InputType.TYPE_CLASS_TEXT);
                        textValue.requestFocus();showKeyboard(textValue,true);
                        textValue.setSelection(textValue.getText().length());
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                animator.start();
            }
        });

    }
    public void showKeyboard(View view,boolean what)
    {
        if(what)
        {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED, 0);
        }
        else
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
