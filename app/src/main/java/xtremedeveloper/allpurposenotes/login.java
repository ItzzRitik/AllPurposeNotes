package xtremedeveloper.allpurposenotes;
import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdsmdg.kd.trianglify.models.Palette;
import com.sdsmdg.kd.trianglify.views.TrianglifyView;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;

public class login extends AppCompatActivity
{
    TextView signin,gender_text,verify_l1,verify_l2,verify_l4;
    ImageView ico_splash,dob_chooser,gender_swap,click,flash,camera_flip,social_google_logo,social_facebook_logo,sign_dialog_close;
    RelativeLayout login_div,logo_div,splash_cover,email_reset,sign_dialog,forget_pass,gender,permission_camera;
    RelativeLayout camera_pane,parentPanel,click_pane,galary,social_trigger,social_google,social_facebook,socialPane;
    Animation anim;
    boolean isDP_added =false,camStarted=false,camOn=false,galaryOn=false,isflash=false,isBack=false,profile_lp=false;
    boolean loginStarted=false,socialOn=false;
    EditText email,pass,con_pass,f_name,l_name,dob;
    TrianglifyView backG;
    FirebaseAuth auth;
    GoogleSignInOptions gso;
    StorageReference storageReference;
    GoogleApiClient gso_client;
    GoogleSignInAccount account;
    DatabaseReference fdb;
    View divider3,divider4,divider5;
    CircularImageView profile;
    Button allow_camera;
    CameraView cameraView;
    ToolTipsManager toolTip;
    Animator animator;
    String profile_url="",profile_path="";
    UCrop.Options options;
    Bitmap profile_dp=null;
    ProgressBar dp_Loader,nextLoad;
    SharedPreferences pref;
    String buttonText="";
    Point screenSize;
    private float social_Y;
    int logs;
    user_details userD;
    @Override
    protected void onResume() {
        super.onResume();
        if(loginStarted)
        {
            if (ContextCompat.checkSelfPermission(login.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            {if(!camStarted){cameraView.start();camStarted=true;}}
            new Handler().postDelayed(new Runnable() {@Override public void run()
            {
                click.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(login.this, R.anim.click_grow);click.startAnimation(anim);
            }},500);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (loginStarted)
        {
            if(camStarted){cameraView.stop();camStarted=false;}
            super.onPause();click.setVisibility(View.INVISIBLE);
        }
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
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        auth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        pref = getSharedPreferences("app_settings",Context.MODE_PRIVATE);
        if(auth.getCurrentUser()!=null)
        {
            String json = pref.getString(auth.getCurrentUser().getUid()+"user_details", "");
            user_details userx = (new Gson()).fromJson(json, user_details.class);
            if(userx!=null){startActivity(new Intent(login.this,Home.class));finish();}
            else {createActivity();}
        }
        else{createActivity();}
    }
    @SuppressLint("ClickableViewAccessibility")
    public void createActivity()
    {
        loginStarted=true;
        setContentView(R.layout.activity_login);

        toolTip = new ToolTipsManager();
        parentPanel= findViewById(R.id.parentPanel);
        email=findViewById(R.id.email);
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
                            if(logs==0 &&isEmailValid(email.getText().toString())){performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });

        pass=findViewById(R.id.pass);
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
                    else if(logs==2 || logs==5){con_pass.setEnabled(true);}
                }
                else
                {
                    pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_nok,0,0,0);
                    if(logs==1){setButtonEnabled(false);}
                    else if(logs==2 || logs==5){con_pass.setText("");con_pass.setEnabled(false);}
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

        con_pass=findViewById(R.id.con_pass);
        con_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(logs==2 || logs==5)
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

        forget_pass =findViewById(R.id.forget_pass);
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_dialog.setVisibility(View.VISIBLE);vibrate(20);
                auth.sendPasswordResetEmail(email.getText().toString())
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
        email_reset=findViewById(R.id.email_reset);
        email_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleY(login_div,48,300,new AccelerateDecelerateInterpolator());
                scaleY(forget_pass,0,300,new AccelerateDecelerateInterpolator());email_reset.setVisibility(View.GONE);email.setEnabled(true);
                pass.setText("");con_pass.setText("");buttonText=getString(R.string.next);
                social_trigger.setVisibility(View.VISIBLE);
                signin.setText(getString(R.string.next));setButtonEnabled(true);logs=0;vibrate(20);
                email.setVisibility(View.VISIBLE);pass.setVisibility(View.GONE);con_pass.setVisibility(View.GONE);
                sign_dialog.setVisibility(View.GONE);

            }
        });

        f_name=findViewById(R.id.f_name);
        f_name.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {final_signUp();}
        });
        l_name=findViewById(R.id.l_name);
        dob=findViewById(R.id.dob);
        dob.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {final_signUp();}
        });
        divider3=findViewById(R.id.divider3);
        divider4=findViewById(R.id.divider4);
        divider5=findViewById(R.id.divider5);
        dob_chooser=findViewById(R.id.dob_chooser);
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

        verify_l1=findViewById(R.id.verify_l1);
        verify_l2=findViewById(R.id.verify_l2);
        verify_l4=findViewById(R.id.verify_l4);
        verify_l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerification();
            }
        });
        gender_text=findViewById(R.id.gender_text);
        gender_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));

        cameraView=findViewById(R.id.cam);
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

        permission_camera=findViewById(R.id.permission_camera) ;
        allow_camera =findViewById(R.id.allow_camera);
        allow_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                ActivityCompat.requestPermissions(login.this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, 1);
            }
        });
        click_pane=findViewById(R.id.click_pane);
        galary= findViewById(R.id.galary);
        flash=findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                if(!isflash){cameraView.setFlash(CameraKit.Constants.FLASH_ON);isflash=true;flash.setImageResource(R.drawable.flash_on);}
                else {cameraView.setFlash(CameraKit.Constants.FLASH_OFF);isflash=false;flash.setImageResource(R.drawable.flash_off);}
            }
        });
        camera_flip=findViewById(R.id.camera_flip);
        camera_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                if(!isBack){cameraView.setFacing(CameraKit.Constants.FACING_BACK);isBack=true;}
                else {cameraView.setFacing(CameraKit.Constants.FACING_FRONT);isBack=false;}
            }
        });
        click=findViewById(R.id.click);
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
                if(camStarted){cameraView.captureImage();}
            }
        });
        camera_pane=findViewById(R.id.camera_pane);
        dp_Loader= findViewById(R.id.dp_Loader);
        dp_Loader.getIndeterminateDrawable().setColorFilter(getColor(R.color.profile_text), PorterDuff.Mode.MULTIPLY);
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profile_lp) {profile_lp=false;}
                else
                {
                    camera_pane.setVisibility(View.VISIBLE);permission_camera.setVisibility(View.VISIBLE);camOn=true;vibrate(20);
                    int cy=(int)(profile.getY() + profile.getHeight() / 2);
                    final Animator animator = ViewAnimationUtils.createCircularReveal(camera_pane,backG.getRight()/2,cy,0, backG.getHeight()*141/100);
                    animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(500);animator.start();
                    if (ContextCompat.checkSelfPermission(login.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(login.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        permission_camera.setVisibility(View.GONE);if(!camStarted){cameraView.start();camStarted=true;}
                    }
                    new Handler().postDelayed(new Runnable() {@Override public void run()
                    {
                        click.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(login.this, R.anim.click_grow);click.startAnimation(anim);
                    }},500);
                    if(ContextCompat.checkSelfPermission(login.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(login.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        new Handler().postDelayed(new Runnable() {@Override public void run()
                        {
                            ToolTip.Builder builder = new ToolTip.Builder(login.this, click,camera_pane, getString(R.string.open_galary), ToolTip.POSITION_ABOVE);
                            builder.setBackgroundColor(getColor(R.color.profile));
                            builder.setTextColor(getColor(R.color.profile_text));
                            builder.setGravity(ToolTip.GRAVITY_CENTER);
                            builder.setTextSize(15);
                            toolTip.show(builder.build());
                        }},1300);
                        new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(click);}},4000);
                    }
                }
            }
        });
        profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                vibrate(35);profile_lp=true;
                if(gender_text.getText().equals(getString(R.string.he))) {profile.setImageDrawable(getDrawable(R.mipmap.boy));}
                else if(gender_text.getText().equals(getString(R.string.she))) {profile.setImageDrawable(getDrawable(R.mipmap.girl));}
                isDP_added=false;profile_dp=null;
                return false;
            }
        });

        gender_swap=findViewById(R.id.gender_swap);
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

        sign_dialog_close=findViewById(R.id.sign_dialog_close);
        sign_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_dialog.setVisibility(View.GONE);
            }
        });
        nextLoad=findViewById(R.id.nextLoad);
        signin=findViewById(R.id.signin);setButtonEnabled(false);
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
                        if(signin.getText().toString().equals("╳")){nextLoading(false);}
                        else{performSignIn();}vibrate(20);
                        break;
                }
                return true;
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gso_client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        social_google=findViewById(R.id.social_google);
        social_google_logo=findViewById(R.id.social_google_logo);
        social_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleX(social_google_logo,(int)pxtodp(social_google.getWidth()),150,new AccelerateInterpolator());
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gso_client);
                    startActivityForResult(signInIntent,2);
                }},50);

            }
        });
        social_facebook=findViewById(R.id.social_facebook);
        social_facebook_logo=findViewById(R.id.social_facebook_logo);
        social_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleX(social_facebook_logo,(int)pxtodp(social_facebook.getWidth()),150,new AccelerateInterpolator());
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gso_client);
                    startActivityForResult(signInIntent,2);
                }},50);
            }
        });
        socialPane=findViewById(R.id.socialPane);
        social_trigger=findViewById(R.id.social_trigger);
        social_trigger.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        social_Y = event.getY();break;
                    case MotionEvent.ACTION_UP:
                        if (social_Y-event.getY() > 100)
                        {
                            scaleY(login_div,93,300,new AccelerateDecelerateInterpolator());
                            scaleY(socialPane,45,300,new AccelerateDecelerateInterpolator());socialOn=true;
                        }
                        else if (social_Y-event.getY() < -100)
                        {
                            scaleY(login_div,48,300,new AccelerateDecelerateInterpolator());
                            scaleY(socialPane,0,300,new AccelerateDecelerateInterpolator());socialOn=false;
                        }
                        break;
                }
                return true;
            }
        });

        backG=findViewById(R.id.backG);
        backG.setPalette(new Palette(getResources().getIntArray(R.array.theme)));

        ico_splash=findViewById(R.id.ico_splash);
        login_div=findViewById(R.id.login_div);
        logo_div=findViewById(R.id.logo_div);
        sign_dialog=findViewById(R.id.sign_dialog);
        splash_cover=findViewById(R.id.splash_cover);
        gender=findViewById(R.id.gender);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_trans);
                splash_cover.setVisibility(View.GONE);
                ico_splash.setImageResource(R.mipmap.logo);
                Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_reveal);
                logo_div.setVisibility(View.VISIBLE);logo_div.startAnimation(anima);ico_splash.startAnimation(anim);
                new Handler().postDelayed(new Runnable()
                {@Override public void run() {scaleY(login_div,48,300,new AccelerateDecelerateInterpolator());social_trigger.setVisibility(View.VISIBLE);}},800);
            }
        },1500);
    }
    public void performSignIn()
    {
        email.setEnabled(false);pass.setEnabled(false);con_pass.setEnabled(false);nextLoading(true);
        social_trigger.setVisibility(View.GONE);
        if(socialOn)
        {
            scaleY(socialPane,0,300,new AccelerateDecelerateInterpolator());
            scaleY(login_div,48,300,new AccelerateDecelerateInterpolator());socialOn=false;
        }
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
                                        pass.setVisibility(View.VISIBLE);con_pass.setVisibility(View.GONE);scaleY(login_div,98,300,new AccelerateDecelerateInterpolator());
                                        email_reset.setVisibility(View.VISIBLE);buttonText=getString(R.string.signin);pass.requestFocus();
                                        pass.setEnabled(true);nextLoading(false);setButtonEnabled(false);scaleY(forget_pass,22,300,new OvershootInterpolator());logs=1;
                                    }
                                    catch (FirebaseAuthInvalidUserException e)
                                    {
                                        pass.setVisibility(View.VISIBLE);con_pass.setVisibility(View.VISIBLE);scaleY(login_div,148,300,new AccelerateDecelerateInterpolator());
                                        email_reset.setVisibility(View.VISIBLE);buttonText=getString(R.string.signup);pass.requestFocus();
                                        pass.setEnabled(true);nextLoading(false);setButtonEnabled(false);logs=2;
                                    }
                                    catch (Exception e) {
                                        Toast.makeText(login.this,getString(R.string.error), Toast.LENGTH_SHORT).show();
                                        nextLoading(false);email_reset.performClick();
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
                                sign_dialog.setVisibility(View.GONE);pass.setText("");pass.setEnabled(true);nextLoading(false);
                                Toast.makeText(login.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                fdb= FirebaseDatabase.getInstance().getReference("user_details");
                                fdb.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final user_details user=dataSnapshot.getValue(user_details.class);
                                        try
                                        {
                                            user.getfname();email_reset.performClick();
                                            new Handler().postDelayed(new Runnable() {@Override public void run() {startActivity(new Intent(login.this,Home.class));finish();}},1500);
                                            signin.setText("✓");
                                        }
                                        catch (NullPointerException e)
                                        {
                                            ToolTip.Builder builder = new ToolTip.Builder(login.this, email,parentPanel, getString(R.string.complete_signUp), ToolTip.POSITION_ABOVE);
                                            builder.setBackgroundColor(getColor(R.color.profile));
                                            builder.setTextColor(getColor(R.color.profile_text));
                                            builder.setGravity(ToolTip.GRAVITY_CENTER);builder.setTextSize(15);
                                            toolTip.show(builder.build());
                                            scaleY(forget_pass,0,300,new AccelerateDecelerateInterpolator());
                                            new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(email);}},4000);
                                            new Handler().postDelayed(new Runnable() {@Override public void run() {
                                                nextLoading(false);setButtonEnabled(true);verify_l4.setVisibility(View.VISIBLE);sign_dialog.setVisibility(View.GONE);
                                                sendVerification();
                                            }},4500);
                                        }
                                        catch (Exception e) {Toast.makeText(login.this, e.toString(), Toast.LENGTH_SHORT).show();}
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError error) {}
                                });
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
                                                nextLoading(false);setButtonEnabled(true);verify_l4.setVisibility(View.VISIBLE);sign_dialog.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {sendVerification();}
                                                else {verify_l2.setVisibility(View.VISIBLE);verify_l2.setText(getString(R.string.failed_send));}
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
                                nextLoading(false);
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser().isEmailVerified())
                                    {
                                        verify_l1.setVisibility(View.GONE);verify_l2.setVisibility(View.GONE);logs=4;
                                        verify_l4.setVisibility(View.GONE);buttonText=getString(R.string.signup);nextLoading(false);
                                        if(f_name.getText().length()>0 && isDate(dob.getText().toString())){setButtonEnabled(true);}
                                        else{setButtonEnabled(false);}
                                        scaleY(login_div,260,300,new AccelerateDecelerateInterpolator());
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
            completeSignUp(profile_dp);
        }
        else if(logs==5)
        {
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(con_pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String fName=account.getGivenName(),lName=account.getFamilyName();
                                try {fName=fName.substring(0,fName.indexOf(" "));}catch (Exception e){}
                                try {lName=lName.substring(0,lName.indexOf(" "));} catch (Exception e){}
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(account.getPhotoUrl())
                                        .setDisplayName(fName+" "+lName).build();
                                auth.getCurrentUser().updateProfile(profileUpdates);
                                upload_data(new user_details(fName,lName,"",""));
                                email_reset.performClick();nextLoading(true);signin.setText("✓");
                            } else {}
                        }
                    });
        }

        profile_url=new File(new ContextWrapper(getApplicationContext()).getDir("imageDir", Context.MODE_PRIVATE),"profile.jpg").getAbsolutePath();
    }
    public void completeSignUp(Bitmap bitmap)
    {
        dp_Loader.setVisibility(View.VISIBLE);profile.setEnabled(false);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)profile.getLayoutParams();
        layoutParams.removeRule(RelativeLayout.ABOVE);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        profile.setLayoutParams(layoutParams);
        profile.setShadowRadius(8);
        profile.setBorderWidth(2);
        scaleY(login_div,0,300,new AccelerateDecelerateInterpolator());
        if(isDP_added)
        {
            StorageReference riversRef = storageReference.child("UserDP/"+auth.getCurrentUser().getUid()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //noinspection SuspiciousNameCombination
            bitmap=Bitmap.createScaledBitmap(bitmap, screenSize.x, screenSize.x, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            riversRef.putBytes(baos.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressLint("ApplySharedPref")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(f_name.getText()+" "+l_name.getText()).setPhotoUri(downloadUrl).build();
                            auth.getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {profile.setEnabled(true);}
                                        }
                                    });
                            upload_data(new user_details(f_name.getText().toString(),l_name.getText().toString(),gender_text.getText().toString(),dob.getText().toString()));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            dp_Loader.setVisibility(View.GONE);profile.setEnabled(true);
                        }
                    });
        }
        else
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(f_name.getText()+" "+l_name.getText()).build();
            auth.getCurrentUser().updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {profile.setEnabled(true);}
                        }
                    });
            upload_data(new user_details(f_name.getText().toString(),l_name.getText().toString(),gender_text.getText().toString(),dob.getText().toString()));
        }
    }
    @SuppressLint("ApplySharedPref")
    public void upload_data(user_details user)
    {
        fdb= FirebaseDatabase.getInstance().getReference("user_details");
        fdb.child(auth.getCurrentUser().getUid()).setValue(user);
        dp_Loader.setVisibility(View.GONE);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(auth.getCurrentUser().getUid()+"user_details", new Gson().toJson(user));
        prefsEditor.commit();
        new Handler().postDelayed(new Runnable() {@Override public void run() {startActivity(new Intent(login.this,Home.class));finish();}},2000);
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
        divider5.setVisibility(View.VISIBLE);dob_chooser.setVisibility(View.VISIBLE);scaleY(login_div,345,300,new AccelerateDecelerateInterpolator());
        scaleY(logo_div,0,300,null);scaleX(logo_div,0,300,null);scaleY(ico_splash,0,300,null);scaleX(ico_splash,0,300,null);signIn_moveLeft(true);
        buttonText=getString(R.string.check);signin.setText(getString(R.string.check));
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
        if(what) {signin.setBackgroundResource(R.drawable.signin);signin.setTextColor(Color.parseColor("#02723B"));}
        else {signin.setBackgroundResource(R.drawable.signin_disabled);signin.setTextColor(Color.parseColor("#c7c7c7"));}
        signin.setEnabled(what);
    }
    public void nextLoading(Boolean loading)
    {
        if(loading)
        {
            scaleX(signin,30,150,new AnticipateInterpolator());buttonText=signin.getText().toString();
            signin.setBackgroundResource(R.drawable.signin_disabled);signin.setTextColor(Color.parseColor("#616161"));
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                nextLoad.setVisibility(View.VISIBLE);signin.setText("╳");
            }},150);
        }
        else
        {
            nextLoad.setVisibility(View.GONE);signin.setText("");scaleX(signin,85,300,new OvershootInterpolator());
            new Handler().postDelayed(new Runnable()
            {@Override public void run() {signin.setText(buttonText);}},300);
        }
    }
    public void scaleX(final View view,int x,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(),(int)dptopx(x));anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(t);anim.start();
    }
    public void scaleY(final View view,int y,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),(int)dptopx(y));anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);view.invalidate();
            }
        });
        anim.setDuration(t);anim.start();
    }
    public void signIn_moveLeft(boolean where)
    {
        if(where)
        {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) signin.getLayoutParams();
            lp.removeRule(RelativeLayout.ALIGN_PARENT_END);lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            signin.setLayoutParams(lp);
        }
    }
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
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
                        new Handler().postDelayed(new Runnable() {@Override public void run()
                        {
                            ToolTip.Builder builder = new ToolTip.Builder(login.this, click,camera_pane, getString(R.string.open_galary), ToolTip.POSITION_ABOVE);
                            builder.setBackgroundColor(getColor(R.color.profile));
                            builder.setTextColor(getColor(R.color.profile_text));
                            builder.setGravity(ToolTip.GRAVITY_CENTER);
                            builder.setTextSize(15);
                            toolTip.show(builder.build());
                        }},1300);
                        new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(click);}},4000);
                    }
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        if (requestCode == 1 && resultcode == RESULT_OK) {
            UCrop.of(intent.getData(),Uri.parse(profile_url)).withOptions(options).withAspectRatio(1,1)
                    .withMaxResultSize(maxWidth, maxHeight).start(login.this);
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            if(resultcode == RESULT_OK)
            {
                try {
                    final Uri resultUri = UCrop.getOutput(intent);
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(login.this.getContentResolver(), resultUri);
                    profile.setImageBitmap(bitmap);profile_dp=bitmap;isDP_added=true;
                    closeCam();
                    new Handler().postDelayed(new Runnable() {@Override public void run()
                    {
                        ToolTip.Builder builder = new ToolTip.Builder(login.this, profile,parentPanel, getString(R.string.remove_pic), ToolTip.POSITION_ABOVE);
                        builder.setBackgroundColor(getColor(R.color.profile));
                        builder.setTextColor(getColor(R.color.profile_text));
                        builder.setGravity(ToolTip.GRAVITY_CENTER);
                        builder.setTextSize(15);
                        toolTip.show(builder.build());vibrate(35);
                    }},1000);
                    new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(profile);}},3500);
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
        else if (requestCode == 2)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess())
            {
                sign_dialog.setVisibility(View.VISIBLE);social_trigger.setVisibility(View.GONE);
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                try
                {
                    account = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    auth.signInWithCredential(credential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        fdb= FirebaseDatabase.getInstance().getReference("user_details");
                                        fdb.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                scaleX(social_google_logo,50,100,new AccelerateDecelerateInterpolator());
                                                userD=dataSnapshot.getValue(user_details.class);
                                                try
                                                {
                                                    userD.getfname();
                                                    storageReference.child("UserDP/"+auth.getCurrentUser().getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(userD.getfname()+" "+userD.getlname())
                                                                    .setPhotoUri(uri).build();
                                                            auth.getCurrentUser().updateProfile(profileUpdates);newPageAnim();
                                                            new Handler().postDelayed(new Runnable() {@Override public void run() {startActivity(new Intent(login.this,Home.class));finish();}},2500);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            String picURL=account.getPhotoUrl().toString().substring(0,account.getPhotoUrl().toString().indexOf("s96-c/photo.jpg"));
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(userD.getfname()+" "+userD.getlname())
                                                                    .setPhotoUri(Uri.parse(picURL+"s500-c/photo.jpg")).build();
                                                            auth.getCurrentUser().updateProfile(profileUpdates);newPageAnim();
                                                            new Handler().postDelayed(new Runnable() {@Override public void run() {startActivity(new Intent(login.this,Home.class));finish();}},2500);
                                                        }
                                                    });
                                                }
                                                catch (NullPointerException e)
                                                {
                                                    sign_dialog.setVisibility(View.GONE);social_trigger.setVisibility(View.VISIBLE);
                                                    email_reset.performClick();email.setText(account.getEmail());social_trigger.setVisibility(View.GONE);
                                                    scaleY(socialPane,0,300,new AccelerateDecelerateInterpolator());socialOn=false;
                                                    pass.setVisibility(View.VISIBLE);con_pass.setVisibility(View.VISIBLE);buttonText=getString(R.string.signup);
                                                    scaleY(login_div,148,300,new AccelerateDecelerateInterpolator());pass.requestFocus();
                                                    pass.setEnabled(true);nextLoading(false);setButtonEnabled(false);email.setEnabled(false);logs=5;
                                                    new Handler().postDelayed(new Runnable() {@Override public void run()
                                                    {
                                                        ToolTip.Builder builder = new ToolTip.Builder(login.this, email,parentPanel, getString(R.string.add_password), ToolTip.POSITION_ABOVE);
                                                        builder.setBackgroundColor(getColor(R.color.profile));
                                                        builder.setTextColor(getColor(R.color.profile_text));
                                                        builder.setGravity(ToolTip.GRAVITY_CENTER);builder.setTextSize(15);
                                                        toolTip.show(builder.build());
                                                    }},500);
                                                }
                                                catch (Exception e) {Toast.makeText(login.this, e.toString(), Toast.LENGTH_SHORT).show();}
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError error) {}
                                        });
                                    }
                                    else {
                                    }
                                }
                            });

                }
                catch (ApiException e) {}
                catch (Exception e){
                }
            }
            else {scaleX(social_google_logo,50,100,new AccelerateDecelerateInterpolator());}
        }
    }
    public void newPageAnim()
    {
        scaleY(login_div,00,300,new AccelerateDecelerateInterpolator());
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_grow);
        Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_hide);
        logo_div.setVisibility(View.VISIBLE);logo_div.startAnimation(anima);ico_splash.startAnimation(anim);
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
