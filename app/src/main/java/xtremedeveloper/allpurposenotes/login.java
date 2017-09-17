package xtremedeveloper.allpurposenotes;

import android.*;
import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.OvalShape;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdsmdg.kd.trianglify.models.Palette;
import com.sdsmdg.kd.trianglify.views.TrianglifyView;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;

public class login extends AppCompatActivity {

    TextView signin,gender_text,verify_l1,verify_l2,verify_l4;
    ImageView ico_splash,dob_chooser,gender_swap,click,flash,camera_flip;
    RelativeLayout login_div,logo_div,splash_cover,email_reset,sign_dialog,forget_pass,gender,permission_camera;
    RelativeLayout camera_pane,parentPanel,click_pane,galary;
    Animation anim;
    boolean isDP_added =false,camStarted=false,camOn=false,galaryOn=false,isflash=false,isBack=false;
    EditText email,pass,con_pass,f_name,l_name,dob;
    int logs=0;
    TrianglifyView backG;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    View divider3,divider4,divider5;
    CircularImageView profile;
    Button allow_camera;
    CameraView cameraView;
    ToolTipsManager toolTip;
    Animator animator;
    String profile_url="",profile_path="";
    UCrop.Options options;
    Bitmap profile_dp=null;
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(login.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {if(!camStarted){cameraView.start();camStarted=true;}}
        new Handler().postDelayed(new Runnable() {@Override public void run()
        {
            click.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(login.this, R.anim.click_grow);click.startAnimation(anim);
        }},500);
    }

    @Override
    protected void onPause() {
        if(camStarted){cameraView.stop();camStarted=false;}
        super.onPause();click.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed() {
        if(camOn && !galaryOn) {closeCam();}
        if(galaryOn)
        {
            int cy=(int)(click.getY() + click.getHeight() / 2);
            animator = ViewAnimationUtils.createCircularReveal(galary,backG.getRight()/2,cy,backG.getHeight()*141/100,0);
            animator.setInterpolator(new DecelerateInterpolator());animator.setDuration(500);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {galaryOn=false;}
                @Override
                public void onAnimationEnd(Animator animation) {galary.setVisibility(View.GONE);}
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            animator.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        toolTip = new ToolTipsManager();
        parentPanel=(RelativeLayout) findViewById(R.id.parentPanel);
        email=(EditText)findViewById(R.id.email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(isEmailValid(email.getText().toString()))
                {setButtonEnabled(true);}
                else{setButtonEnabled(false);}
            }
        });
        email.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(logs==0){performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });

        pass=(EditText)findViewById(R.id.pass);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                con_pass.setText("");
                if(pass.getText().length()>=6)
                {
                    pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_ok,0,0,0);
                    if(logs==1){setButtonEnabled(true);}
                    else if(logs==2){con_pass.setEnabled(true);}
                }
                else
                {
                    pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_nok,0,0,0);
                    if(logs==1){setButtonEnabled(false);}
                    else if(logs==2){con_pass.setText("");con_pass.setEnabled(false);}
                }
            }
        });
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        con_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP:
                        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        con_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                }
                return false;
            }
        });
        pass.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(logs==1)
                            {performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });

        con_pass=(EditText)findViewById(R.id.con_pass);
        con_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(logs==2)
                {
                    if(con_pass.getText().toString().equals(pass.getText().toString()) && con_pass.getText().length()>=6)
                    {con_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.con_password_ok,0,0,0);setButtonEnabled(true);}
                    else{con_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.con_password_nok,0,0,0);setButtonEnabled(false);}
                }
            }
        });
        con_pass.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(logs==2) {performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });

        forget_pass =(RelativeLayout)findViewById(R.id.forget_pass);
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_dialog.setVisibility(View.VISIBLE);vibrate(20);
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sign_dialog.setVisibility(View.GONE);email_reset.performClick();
                                    Toast.makeText(login.this,getString(R.string.pass_reset), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(login.this,getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        email_reset=(RelativeLayout)findViewById(R.id.email_reset);
        email_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleY(48,login_div);scaleY(0,forget_pass);email_reset.setVisibility(View.GONE);email.setEnabled(true);
                pass.setText("");con_pass.setText("");signin.setText(getString(R.string.next));setButtonEnabled(true);logs=0;vibrate(20);
                email.setVisibility(View.VISIBLE);pass.setVisibility(View.GONE);con_pass.setVisibility(View.GONE);
            }
        });

        f_name=(EditText)findViewById(R.id.f_name);
        f_name.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {final_signUp();}
        });
        l_name=(EditText)findViewById(R.id.l_name);
        dob=(EditText)findViewById(R.id.dob);
        dob.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {final_signUp();}
        });
        divider3=findViewById(R.id.divider3);
        divider4=findViewById(R.id.divider4);
        divider5=findViewById(R.id.divider5);
        dob_chooser=(ImageView)findViewById(R.id.dob_chooser);
        dob_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(20);
                DatePickerDialog dd = new DatePickerDialog(login.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);
                                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    dob.setText(formatter.format(date));
                                } catch (Exception ex) {}
                            }
                        }, 2000,  Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

        verify_l1=(TextView)findViewById(R.id.verify_l1);
        verify_l2=(TextView)findViewById(R.id.verify_l2);
        verify_l4=(TextView)findViewById(R.id.verify_l4);
        verify_l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerification();
            }
        });
        gender_text=(TextView)findViewById(R.id.gender_text);
        gender_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));

        cameraView=(CameraView)findViewById(R.id.cam);
        options=new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridColumnCount(0);
        options.setCropGridRowCount(0);
        options.setToolbarColor(ContextCompat.getColor(login.this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(login.this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(login.this, R.color.colorPrimary));
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(final byte[] picture) {
                super.onPictureTaken(picture);
                Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                if(!isBack)
                {
                    Matrix matrix = new Matrix();
                    matrix.postScale(-1, 1,result.getWidth()/2, result.getHeight()/2);
                    result= Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
                }
                vibrate(20);
                profile_path = MediaStore.Images.Media.insertImage(login.this.getContentResolver(), result, "Title", null);
                UCrop.of(Uri.parse(profile_path),Uri.parse(profile_url)).withOptions(options).withAspectRatio(1,1)
                        .withMaxResultSize(maxWidth, maxHeight).start(login.this);
            }
        });

        permission_camera=(RelativeLayout)findViewById(R.id.permission_camera) ;
        allow_camera =(Button)findViewById(R.id.allow_camera);
        allow_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                ActivityCompat.requestPermissions(login.this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, 1);
            }
        });
        click_pane=(RelativeLayout)findViewById(R.id.click_pane);
        galary=(RelativeLayout) findViewById(R.id.galary);
        flash=(ImageView)findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                if(!isflash){cameraView.setFlash(CameraKit.Constants.FLASH_ON);isflash=true;flash.setImageResource(R.drawable.flash_on);}
                else {cameraView.setFlash(CameraKit.Constants.FLASH_OFF);isflash=false;flash.setImageResource(R.drawable.flash_off);}
            }
        });
        camera_flip=(ImageView)findViewById(R.id.camera_flip);
        camera_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                if(!isBack){cameraView.setFacing(CameraKit.Constants.FACING_BACK);isBack=true;}
                else {cameraView.setFacing(CameraKit.Constants.FACING_FRONT);isBack=false;}
            }
        });
        click=(ImageView)findViewById(R.id.click);
        click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int cy=(int)(click.getY() + click.getHeight() / 2);galaryOn=true;vibrate(35);
                animator = ViewAnimationUtils.createCircularReveal(galary,backG.getRight()/2,cy,0,backG.getHeight());
                animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(300);galary.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(login.this, R.anim.fade_out);
                galary.startAnimation(anim);
                animator.start();

                Intent intent = new Intent();
                intent.setType("image/*");intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                overridePendingTransition(R.anim.slide_up,0);
                return false;
            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.captureImage();
            }
        });
        camera_pane=(RelativeLayout)findViewById(R.id.camera_pane);
        profile=(CircularImageView)findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_pane.setVisibility(View.VISIBLE);camOn=true;vibrate(20);
                int cy=(int)(profile.getY() + profile.getHeight() / 2);
                final Animator animator = ViewAnimationUtils.createCircularReveal(camera_pane,backG.getRight()/2,cy,0, backG.getHeight()*141/100);
                animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(500);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        permission_camera.setVisibility(View.VISIBLE);
                        if (ContextCompat.checkSelfPermission(login.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(login.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            permission_camera.setVisibility(View.GONE);if(!camStarted){cameraView.start();camStarted=true;}
                        }
                        else {permission_camera.setVisibility(View.VISIBLE);}
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });animator.start();
                new Handler().postDelayed(new Runnable() {@Override public void run()
                {
                    click.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(login.this, R.anim.click_grow);click.startAnimation(anim);
                }},500);
            }
        });

        gender_swap=(ImageView) findViewById(R.id.gender_swap);
        gender_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(20);
                if(gender_text.getText().equals(getString(R.string.he)))
                {
                    gender_text.setText(getString(R.string.she));
                    if(!isDP_added)profile.setImageDrawable(getDrawable(R.mipmap.girl));
                }
                else if(gender_text.getText().equals(getString(R.string.she)))
                {
                    gender_text.setText(getString(R.string.he));
                    if(!isDP_added)profile.setImageDrawable(getDrawable(R.mipmap.boy));
                }
            }
        });

        signin=(TextView)findViewById(R.id.signin);setButtonEnabled(false);
        signin.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));
        signin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signin.setBackgroundResource(R.drawable.signin_pressed);signin.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case MotionEvent.ACTION_UP:
                        signin.setBackgroundResource(R.drawable.signin);signin.setTextColor(Color.parseColor("#02723B"));
                        performSignIn();vibrate(20);
                        break;
                }
                return true;
            }
        });

        backG=(TrianglifyView)findViewById(R.id.backG);
        backG.setPalette(new Palette(getResources().getIntArray(R.array.theme)));

        ico_splash=(ImageView)findViewById(R.id.ico_splash);
        login_div=(RelativeLayout)findViewById(R.id.login_div);
        logo_div=(RelativeLayout)findViewById(R.id.logo_div);
        sign_dialog=(RelativeLayout)findViewById(R.id.sign_dialog);
        splash_cover=(RelativeLayout)findViewById(R.id.splash_cover);
        gender=(RelativeLayout)findViewById(R.id.gender);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_trans);
                        splash_cover.setVisibility(View.GONE);
                        ico_splash.setImageResource(R.mipmap.logo);
                        Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_reveal);
                        logo_div.setVisibility(View.VISIBLE);logo_div.startAnimation(anima);ico_splash.startAnimation(anim);
                        new Handler().postDelayed(new Runnable() {@Override public void run() {scaleY(48,login_div);}},800);
                    }},1000);
            }
        },500);
    }
    public void performSignIn()
    {
        email.setEnabled(false);pass.setEnabled(false);con_pass.setEnabled(false);setButtonEnabled(false);
        if(logs==0)
        {
            if(isEmailValid(email.getText().toString()))
            {
                auth.signInWithEmailAndPassword(email.getText().toString(), "testing_mail")
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()) {
                                    try {throw task.getException();}
                                    catch(FirebaseAuthInvalidCredentialsException e)
                                    {
                                        pass.setVisibility(View.VISIBLE);con_pass.setVisibility(View.GONE);scaleY(98,login_div);
                                        email_reset.setVisibility(View.VISIBLE);signin.setText(getString(R.string.signin));pass.requestFocus();
                                        pass.setEnabled(true);setButtonEnabled(false);scaleY(22,forget_pass);logs=1;
                                    }
                                    catch (FirebaseAuthInvalidUserException e)
                                    {
                                        pass.setVisibility(View.VISIBLE);con_pass.setVisibility(View.VISIBLE);scaleY(148,login_div);
                                        email_reset.setVisibility(View.VISIBLE);signin.setText(getString(R.string.signup));pass.requestFocus();
                                        pass.setEnabled(true);setButtonEnabled(false);logs=2;
                                    }
                                    catch (Exception e) {
                                        Toast.makeText(login.this,getString(R.string.error), Toast.LENGTH_SHORT).show();
                                        email_reset.performClick();
                                    }
                                }
                            }
                        });
            }
            else{Toast.makeText(login.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();}
        }
        else if(logs==1)
        {
            sign_dialog.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())
                            {
                                sign_dialog.setVisibility(View.GONE);pass.setText("");pass.setEnabled(true);
                                Toast.makeText(login.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                sign_dialog.setVisibility(View.GONE);email.setText("");pass.setEnabled(true);
                                Toast.makeText(login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();email_reset.performClick();
                            }
                        }
                    });
        }
        else if(logs==2)
        {
            sign_dialog.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())
                            {
                                pass.setText("");pass.setEnabled(true);sign_dialog.setVisibility(View.GONE);
                                Toast.makeText(login.this, "Account Creation Failed !", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                auth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(login.this, new OnCompleteListener<java.lang.Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                setButtonEnabled(true);verify_l4.setVisibility(View.VISIBLE);sign_dialog.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {sendVerification();}
                                                else
                                                {
                                                    verify_l2.setVisibility(View.VISIBLE);
                                                    verify_l2.setText(getString(R.string.failed_send));
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        else if(logs==3)
        {
            try
            {
                auth.signOut();toolTip.findAndDismiss(signin);
                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser().isEmailVerified())
                                    {
                                        verify_l1.setVisibility(View.GONE);verify_l2.setVisibility(View.GONE);
                                        verify_l4.setVisibility(View.GONE);
                                        signin.setText(getString(R.string.signup));logs=4;
                                        if(f_name.getText().length()>0 && isDate(dob.getText().toString())){setButtonEnabled(true);}
                                        else{setButtonEnabled(false);}scaleY(260,login_div);
                                        new Handler().postDelayed(new Runnable() {@Override public void run() {
                                            ToolTip.Builder builder = new ToolTip.Builder(login.this, signin, login_div,getString(R.string.verify_done), ToolTip.POSITION_ABOVE);
                                            builder.setBackgroundColor(getColor(R.color.profile));
                                            builder.setTextColor(getColor(R.color.profile_text));
                                            builder.setGravity(ToolTip.GRAVITY_CENTER);
                                            builder.setTextSize(15);
                                            toolTip.show(builder.build());
                                        }},1000);
                                    }
                                    else {
                                        ToolTip.Builder builder = new ToolTip.Builder(login.this, signin, login_div, getString(R.string.verify_fail), ToolTip.POSITION_ABOVE);
                                        builder.setBackgroundColor(getColor(R.color.profile));
                                        builder.setTextColor(getColor(R.color.profile_text));
                                        builder.setGravity(ToolTip.GRAVITY_CENTER);
                                        builder.setTextSize(15);
                                        toolTip.show(builder.build());
                                        setButtonEnabled(true);
                                    }
                                }
                            }
                        });
            }
            catch (Exception e){Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();setButtonEnabled(true);}
        }
        else if(logs==4)
        {
            profile.setDrawingCacheEnabled(true);
            Bitmap bmp=profile.getDrawingCache();
            String path=uploadFile(bmp).getPath();
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        }

        profile_url=new File(new ContextWrapper(getApplicationContext()).getDir("imageDir", Context.MODE_PRIVATE),"profile.jpg").getAbsolutePath();
    }
    public Uri uploadFile(Bitmap bitmap) {
        Uri downloadUrl=null;
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://allpurposenotes-a3eb6.appspot.com");
        StorageReference mountainsRef = storageRef.child(auth.getCurrentUser().getUid()+".jpg");
        StorageReference mountainImagesRef = storageRef.child("users/"+auth.getCurrentUser().getUid()+".jpg");
        mountainsRef.getName().equals(mountainImagesRef.getName());
        mountainsRef.getPath().equals(mountainImagesRef.getPath());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {}
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                f_name.setText(downloadUrl.getPath());
            }
        });
        return downloadUrl;
    }
    public void closeCam()
    {
        int cy=(int)(profile.getY() + profile.getHeight() / 2);
        Animation anim = AnimationUtils.loadAnimation(login.this, R.anim.click_shrink);click.startAnimation(anim);
        animator = ViewAnimationUtils.createCircularReveal(camera_pane,backG.getRight()/2,cy, backG.getHeight()*141/100,0);
        animator.setInterpolator(new DecelerateInterpolator());animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {camOn=false;click.setVisibility(View.INVISIBLE);}
            @Override
            public void onAnimationEnd(Animator animation) {camera_pane.setVisibility(View.GONE);click.setVisibility(View.INVISIBLE);}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        new Handler().postDelayed(new Runnable() {@Override public void run() {animator.start();}},300);

    }
    public void final_signUp()
    {
        if(f_name.getText().length()>0&&isDate(dob.getText().toString())) {setButtonEnabled(true);}
        else{setButtonEnabled(false);}
    }
    public boolean isDate(String date_string)
    {
        Date date = null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            date = sdf.parse(date_string);
            if (!date_string.equals(sdf.format(date))) {date = null;}
        }
        catch (ParseException ex) {ex.printStackTrace();}
        if (date == null) {return false;}
        else {return true;}
    }
    public void sendVerification()
    {
        verify_l1.setVisibility(View.VISIBLE);verify_l2.setVisibility(View.VISIBLE);
        verify_l2.setText(auth.getCurrentUser().getEmail());
        sign_dialog.setVisibility(View.GONE);pass.setEnabled(true);
        email_reset.setVisibility(View.GONE);email.setVisibility(View.GONE);logs=3;
        pass.setVisibility(View.GONE);con_pass.setVisibility(View.GONE);
        profile.setVisibility(View.VISIBLE);gender.setVisibility(View.VISIBLE);
        f_name.setVisibility(View.VISIBLE);l_name.setVisibility(View.VISIBLE);divider3.setVisibility(View.VISIBLE);
        dob.setVisibility(View.VISIBLE);divider4.setVisibility(View.VISIBLE);
        divider5.setVisibility(View.VISIBLE);dob_chooser.setVisibility(View.VISIBLE);scaleY(345,login_div);
        scaleY(0,logo_div);scaleX(0,logo_div);scaleY(0,ico_splash);scaleX(0,ico_splash);signIn_moveLeft(true);
        signin.setText(getString(R.string.check));
        new Handler().postDelayed(new Runnable() {@Override public void run()
        {
            ToolTip.Builder builder = new ToolTip.Builder(login.this, signin,login_div, getString(R.string.verify2), ToolTip.POSITION_ABOVE);
            builder.setBackgroundColor(getColor(R.color.profile));
            builder.setTextColor(getColor(R.color.profile_text));
            builder.setGravity(ToolTip.GRAVITY_CENTER);
            builder.setTextSize(15);
            toolTip.show(builder.build());
        }},2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(!camOn)
                {
                    ToolTip.Builder builder = new ToolTip.Builder(login.this, gender_swap,parentPanel, getString(R.string.select_gender), ToolTip.POSITION_BELOW);
                    builder.setBackgroundColor(getColor(R.color.profile));
                    builder.setTextColor(getColor(R.color.profile_text));
                    builder.setGravity(ToolTip.GRAVITY_LEFT);
                    builder.setTextSize(15);
                    toolTip.show(builder.build());
                }
            }},4000);
        new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(gender_swap);}},6700);

                new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(!camOn)
                {
                    ToolTip.Builder builder = new ToolTip.Builder(login.this, profile,parentPanel, getString(R.string.add_dp), ToolTip.POSITION_ABOVE);
                    builder.setBackgroundColor(getColor(R.color.profile));
                    builder.setTextColor(getColor(R.color.profile_text));
                    builder.setGravity(ToolTip.GRAVITY_CENTER);
                    builder.setTextSize(15);
                    toolTip.show(builder.build());
                }
            }},7000);
        new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(profile);}},9700);
    }
    public void resendVerification()
    {
        try
        {
            verify_l1.setVisibility(View.INVISIBLE);verify_l2.setText(getString(R.string.resend));verify_l4.setVisibility(View.INVISIBLE);
            auth.getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(login.this, new OnCompleteListener<java.lang.Void>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                verify_l1.setVisibility(View.VISIBLE);verify_l2.setText(email.getText().toString());
                            } else {
                                verify_l2.setText(getString(R.string.failed_send));
                            }
                        }
                    });
            new Handler().postDelayed(new Runnable() {@Override public void run() {if(logs<4){verify_l4.setVisibility(View.VISIBLE);}}},30000);
        }
        catch (Exception e){
            Toast.makeText(this, getString(R.string.error_resend), Toast.LENGTH_SHORT).show();
        }
    }
    public void setButtonEnabled(Boolean what)
    {
        if(what) {signin.setBackgroundResource(R.drawable.signin);signin.setTextColor(Color.parseColor("#02723B"));signin.setEnabled(true);}
        else {signin.setBackgroundResource(R.drawable.signin_disabled);signin.setTextColor(Color.parseColor("#c7c7c7"));signin.setEnabled(false);}
    }
    public void scaleX(int x,final View view)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),(int)dptopx(x));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(300);anim.start();
    }
    public void scaleY(int y,final View view)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),(int)dptopx(y));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(300);anim.start();
    }
    public void signIn_moveLeft(boolean where)
    {
        if(where)signin.animate().translationX(-(signin.getX()-((login_div.getWidth()/2)-(signin.getWidth()/2))));
        else signin.animate().translationX((signin.getX()-((login_div.getWidth()/2)-(signin.getWidth()/2))));
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
    public static boolean isEmailValid(String emailStr)
    {return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE) .matcher(emailStr).find();}
    public float dptopx(float num)
    {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, getResources().getDisplayMetrics());}
    public float pxtodp(float num)
    {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, num, getResources().getDisplayMetrics());}
    public void vibrate(int ms){((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(ms);}
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(login.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(login.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        permission_camera.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        if (resultcode == RESULT_OK && requestCode == 1) {
            UCrop.of(intent.getData(),Uri.parse(profile_url)).withOptions(options).withAspectRatio(1,1)
                    .withMaxResultSize(maxWidth, maxHeight).start(login.this);
        }
        if (resultcode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            try {
                final Uri resultUri = UCrop.getOutput(intent);
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(login.this.getContentResolver(), resultUri);
                profile.setImageBitmap(bitmap);profile_dp=bitmap;isDP_added=true;
                closeCam();
                new Handler().postDelayed(new Runnable() {@Override public void run()
                {
                    ToolTip.Builder builder = new ToolTip.Builder(login.this, profile,parentPanel, getString(R.string.osm_dp), ToolTip.POSITION_ABOVE);
                    builder.setBackgroundColor(getColor(R.color.profile));
                    builder.setTextColor(getColor(R.color.profile_text));
                    builder.setGravity(ToolTip.GRAVITY_CENTER);
                    builder.setTextSize(15);
                    toolTip.show(builder.build());vibrate(35);
                }},500);
                new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(profile);}},3000);
                new File(getRealPathFromURI(login.this,Uri.parse(profile_path))).delete();
            }
            catch (Exception e){}
        }
        else if (resultcode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(intent);
            Toast.makeText(login.this,getString(R.string.error)+cropError, Toast.LENGTH_LONG).show();
            new File(getRealPathFromURI(login.this,Uri.parse(profile_path))).delete();
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
