package xtremedeveloper.allpurposenotes;
import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Home extends AppCompatActivity
{
    FloatingActionButton close_menu,add_notes;
    AppBarLayout appbar;
    RelativeLayout add_panel;
    TextView display_name;
    ViewPager notePager,menuPager;
    ImageView profile_menu,menu_cover;
    FirebaseAuth auth;
    GoogleSignInOptions gso;
    GoogleApiClient gso_client;
    DatabaseReference fdb;
    user_details user;
    String userName,userId;
    Bitmap profile_pic;
    SharedPreferences pref,notes;
    ProgressBar loading_profile;
    ObjectAnimator anim;
    Point screenSize;
    PatternLockView patternLock;
    ImageView add_text;
    ImageLoader imageLoader;
    int appbarOffset=0;
    float addNotes_x1=0,addNotes_y1=0;
    boolean isAdd=false,signIn=true;
    private int[] menu_icons={R.drawable.dob,
            R.drawable.dob,
            R.drawable.dob,
            R.drawable.dob,
            R.drawable.dob};
    private int[] menu_list={1,1,1,1,1};
    List<String> note_title = new ArrayList<>();
    List<String> note_text = new ArrayList<>();
    List<String> note_type = new ArrayList<>();
    int currentPage;
    @Override
    public void onBackPressed()
    {
        if(appbarOffset==0){appbar.setExpanded(false);}
        else if(isAdd){add_notes.performClick();}
        else{finish();}
    }
    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            note_type = new Gson().fromJson(notes.getString("note_type", null), new TypeToken<ArrayList<String>>() {}.getType());
            note_title = new Gson().fromJson(notes.getString("note_title", null), new TypeToken<ArrayList<String>>() {}.getType());
            note_text = new Gson().fromJson(notes.getString("note_text", null), new TypeToken<ArrayList<String>>() {}.getType());
            try {note_type.size();}
            catch (NullPointerException e)
            {
                note_title = new ArrayList<>();
                note_text = new ArrayList<>();
                note_type = new ArrayList<>();
            }
            loadNotes(false);
            notePager.setCurrentItem(notes.getInt("pagerPOS",0), false);
        }
        catch (NullPointerException e){}
    }
    @Override
    public void onPause() {
        super.onPause();
        if(signIn)
        {
            notes.edit().putString("note_type", new Gson().toJson(note_type)).apply();
            notes.edit().putString("note_title", new Gson().toJson(note_title)).apply();
            notes.edit().putString("note_text", new Gson().toJson(note_text)).apply();
            notes.edit().putInt("pagerPOS",notePager.getCurrentItem()).apply();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();notes.edit().putInt("pagerPOS",0).apply();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        imageLoader = ImageLoader.getInstance();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        auth=FirebaseAuth.getInstance();
        pref = getSharedPreferences("app_settings",Context.MODE_PRIVATE);
        notes = getSharedPreferences("notes",Context.MODE_PRIVATE);

        loading_profile=findViewById(R.id.loading_profile);
        loading_profile.getIndeterminateDrawable().setColorFilter(getColor(R.color.profile_text), PorterDuff.Mode.MULTIPLY);
        appbar = findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                appbarOffset=verticalOffset;
            }
        });
        close_menu=findViewById(R.id.close_menu);
        close_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {appbar.setExpanded(false);}
        });

        display_name=findViewById(R.id.display_name);
        display_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gso_client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        profile_menu=findViewById(R.id.profile_menu);
        profile_menu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Auth.GoogleSignInApi.signOut(gso_client);
                auth.signOut();signIn=false;
                pref.edit().clear().apply();notes.edit().clear().apply();
                notes.edit().clear().apply();notes.edit().clear().apply();
                startActivity(new Intent(Home.this,login.class));finish();
                return false;
            }
        });
        menu_cover=findViewById(R.id.menu_cover);

        add_panel=findViewById(R.id.add_panel);
        add_notes=findViewById(R.id.add_notes);
        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float x3=(notePager.getWidth()/2)-(add_notes.getWidth()/2);
                final float y3=notePager.getHeight()-add_notes.getHeight()*54/50;
                final Path path = new Path();
                if(!isAdd)
                {
                    addNotes_x1 = add_notes.getX();
                    addNotes_y1 = add_notes.getY();
                    path.moveTo(addNotes_x1, addNotes_y1);
                    final float x2 = (addNotes_x1 + x3) / 2;
                    final float y2 = addNotes_y1-add_notes.getHeight();
                    path.quadTo(x2, y2, x3, y3);
                    ViewCompat.animate(add_notes).rotation(135*5).withLayer().setDuration(600)
                            .setInterpolator(new AccelerateDecelerateInterpolator()).start();
                    isAdd=true;
                }
                else
                {
                    path.moveTo(add_notes.getX(), add_notes.getY());
                    final float x2 = (add_notes.getX() + addNotes_x1) / 2;
                    final float y2 = add_notes.getY()+add_notes.getHeight();
                    path.quadTo(x2, y2, addNotes_x1,addNotes_y1);
                    ViewCompat.animate(add_notes).rotation(0).withLayer().setDuration(700).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                    scaleX(add_panel,0,250,new AnticipateInterpolator());
                    isAdd=false;
                }
                anim = ObjectAnimator.ofFloat(add_notes, View.X, View.Y, path);
                anim.setDuration(200);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationEnd(Animator animator) {
                        if(isAdd)
                        {
                            scaleX(add_panel,250,250,new OvershootInterpolator());add_notes.setElevation(0);
                        }
                        else {add_notes.setElevation(6);}
                    }
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                });
                ObjectAnimator colAnim = ObjectAnimator.ofInt(add_notes, "backgroundTint",getColor(R.color.colorPrimaryDark),getColor(R.color.colorPrimary));
                if(isAdd)
                {
                    colAnim = ObjectAnimator.ofInt(add_notes, "backgroundTint",getColor(R.color.colorPrimary),getColor(R.color.colorPrimaryDark));
                    anim.start();
                }
                else{new Handler().postDelayed(new Runnable() {@Override public void run() {anim.start();}},500);}
                colAnim.setDuration(200);
                colAnim.setEvaluator(new ArgbEvaluator());
                colAnim.setInterpolator(new DecelerateInterpolator(2));
                colAnim.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        add_notes.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
                    }
                });
                colAnim.start();
            }
        });
        add_text=findViewById(R.id.add_text);
        add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_notes.performClick();
                note_title.add("Text Note "+(note_title.size()+1));
                note_type.add("1");note_text.add("");loadNotes(true);
            }
        });


        patternLock=findViewById(R.id.patternLock);
        patternLock.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

            }

            @Override
            public void onCleared() {

            }
        });


        notePager= findViewById(R.id.notesPager);
        notePager.setClipChildren(false);
        notePager.setOffscreenPageLimit(3);
        notePager.setPageTransformer(false, new CarouselEffectTransformer(this));
        loadNotes(false);

        menuPager= findViewById(R.id.menuPager);
        menuPager.setClipChildren(false);
        menuPager.setOffscreenPageLimit(3);
        menuPager.setPageTransformer(false, new CarouselMenuTransformer(this));
        menuPager.setAdapter(new MenuAdapter(Home.this,menu_list));

        userId=auth.getCurrentUser().getUid();
        receiveProfile();
    }
    public void receiveProfile()
    {
        String json = pref.getString(userId+"user_details", "");
        user = (new Gson()).fromJson(json, user_details.class);
        if(user!=null) {userName=user.getfname()+" "+user.getlname();display_name.setText(userName);getDP();}
        else
        {
            fdb= FirebaseDatabase.getInstance().getReference("user_details");
            fdb.child(userId).addValueEventListener(new ValueEventListener() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(user_details.class);
                    SharedPreferences.Editor prefsEditor = pref.edit();
                    prefsEditor.putString(userId+"user_details", new Gson().toJson(user));
                    prefsEditor.commit();receiveProfile();
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getDP()
    {
        profile_menu.setImageResource(R.mipmap.boy);loading_profile.setVisibility(View.VISIBLE);
        if(user.getgender().equals("SHE")){profile_menu.setImageResource(R.mipmap.girl);}
        profile_pic=decodeBase64(pref.getString(userId+"profile_pic",""));
        if(profile_pic!=null)
        {
            profile_menu.setImageBitmap(profile_pic);menu_cover.setImageBitmap(profile_pic);
            loading_profile.setVisibility(View.GONE);
        }
        else
        {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(auth.getCurrentUser().getPhotoUrl().toString(), new ImageLoadingListener() {
                @Override public void onLoadingStarted(String imageUri, View view) {}
                @Override public void onLoadingCancelled(String imageUri, View view) {}
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason)
                {
                    Toast.makeText(Home.this,"Uploading a display picture is recommended", Toast.LENGTH_SHORT).show();
                    profile_menu.setImageResource(R.mipmap.boy);loading_profile.setVisibility(View.GONE);
                    try{if(user.getgender().equals("SHE")){profile_menu.setImageResource(R.mipmap.girl);}}
                    catch (NullPointerException e){}
                }
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(userId+"profile_pic", encodeTobase64(bitmap));
                    editor.commit();getDP();
                }
            });
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
    public String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //noinspection SuspiciousNameCombination
        image=Bitmap.createScaledBitmap(image, screenSize.x, screenSize.x, false);
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 100);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public void loadNotes(boolean scroll)
    {
        currentPage=notePager.getCurrentItem();
        notePager.setAdapter(new MyPagerAdapter(Home.this,note_title.toArray(new String[note_title.size()]),note_type.toArray(new String[note_type.size()]),note_text.toArray(new String[note_text.size()])));
        if(scroll){scrollNotes();}
    }
    public void scrollNotes()
    {
        notePager.setCurrentItem(currentPage, false);
        new Handler().post(new Runnable() {@Override public void run() {
            while(currentPage<note_type.size())
            {
                notePager.setCurrentItem(currentPage++, true);
            }
        }});
    }
    public float dptopx(float num)
    {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, getResources().getDisplayMetrics());}
    public float pxtodp(float num)
    {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, num, getResources().getDisplayMetrics());}
}