package xtremedeveloper.allpurposenotes;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Home extends AppCompatActivity
{
    FloatingActionButton close_menu;
    AppBarLayout appbar;
    TextView display_name;
    ViewPager notePager;
    ImageView profile_menu,menu_cover;
    FirebaseAuth auth;
    StorageReference fbs;
    DatabaseReference fdb;
    user_details user;
    File rootPath;
    String userName,userId;
    Bitmap profile_pic;
    SharedPreferences pref;
    ProgressBar loading_profile;
    private String[] note_title = {"Notes 1","Notes 2","Notes 3","Notes 4","Notes 5"};
    private int[] notes_type={1,2,1,2,1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();
        pref = getPreferences(MODE_PRIVATE);

        loading_profile=(ProgressBar)findViewById(R.id.loading_profile);
        loading_profile.getIndeterminateDrawable().setColorFilter(getColor(R.color.profile_text), PorterDuff.Mode.MULTIPLY);
        appbar = (AppBarLayout)findViewById(R.id.appbar);
        close_menu=(FloatingActionButton)findViewById(R.id.close_menu);
        close_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {appbar.setExpanded(false);}
        });

        display_name=(TextView)findViewById(R.id.display_name);
        display_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        profile_menu=(ImageView)findViewById(R.id.profile_menu);
        menu_cover=(ImageView)findViewById(R.id.menu_cover);


        notePager= (ViewPager) findViewById(R.id.notesPager);
        notePager.setClipChildren(false);
        notePager.setOffscreenPageLimit(3);
        notePager.setPageTransformer(false, new CarouselEffectTransformer(this));
        notePager.setAdapter(new MyPagerAdapter(Home.this,note_title,notes_type));

        userName=auth.getCurrentUser().getDisplayName();
        userId=auth.getCurrentUser().getUid();
        Toast.makeText(this, userName+" , "+userId, Toast.LENGTH_SHORT).show();
        String userDataFolder=userName.substring(0,userName.indexOf(' '))+userId.substring(0,userId.length()/3);
        rootPath = new File(getCacheDir(),"UserData/"+userDataFolder);
        if(!rootPath.exists()){rootPath.mkdirs();}
        receiveProfile();
    }
    public void receiveProfile()
    {
        String json = pref.getString("user_details", "");
        user = (new Gson()).fromJson(json, user_details.class);
        if(user!=null) {display_name.setText(auth.getCurrentUser().getDisplayName());getDP();}
        else
        {
            fdb= FirebaseDatabase.getInstance().getReference("user_details");
            fdb.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(user_details.class);
                    SharedPreferences.Editor prefsEditor = pref.edit();
                    prefsEditor.putString("user_details", new Gson().toJson(user));
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
        profile_pic=decodeBase64(pref.getString("profile_pic",""));
        if(profile_pic!=null) {
            profile_menu.setImageBitmap(profile_pic);menu_cover.setImageBitmap(profile_pic);
            loading_profile.setVisibility(View.GONE);}
        else
        {
            fbs = FirebaseStorage.getInstance().getReference().child("UserDP/"+auth.getCurrentUser().getUid()+".jpg");
            final File localFile = new File(rootPath,"picture.jpg");
            fbs.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.toString(), new BitmapFactory.Options());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("profile_pic", encodeTobase64(bitmap));
                    editor.commit();
                    if (!localFile.delete()){localFile.delete();}
                    getDP();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(Home.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    profile_menu.setImageResource(R.mipmap.boy);
                    try{if(user.getgender().equals("SHE")){profile_menu.setImageResource(R.mipmap.girl);}}
                    catch (NullPointerException e){}
                }
            });
        }
    }
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}