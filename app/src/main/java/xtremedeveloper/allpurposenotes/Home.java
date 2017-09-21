package xtremedeveloper.allpurposenotes;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

    private String[] note_title = {"Notes 1","Notes 2","Notes 3","Notes 4","Notes 5"};
    private int[] notes_type={1,2,1,2,1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();

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

        recieveProfile();
    }
    public void recieveProfile()
    {
        display_name.setText(auth.getCurrentUser().getDisplayName());
        fdb= FirebaseDatabase.getInstance().getReference("user_details");
        fdb.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(user_details.class);getDP();
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });


    }
    public void getDP()
    {
        fbs = FirebaseStorage.getInstance().getReference().child("UserDP/"+auth.getCurrentUser().getUid()+".jpg");
        File rootPath = new File(getFilesDir(), "UserDP/"+auth.getCurrentUser().getUid()+".jpg");
        if(!rootPath.exists()) {rootPath.mkdirs();}
        final File localFile = new File(rootPath,auth.getCurrentUser().getUid()+".jpg");

        fbs.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.toString(), options);
                profile_menu.setImageBitmap(bitmap);
                menu_cover.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                profile_menu.setImageResource(R.mipmap.boy);
                if(display_name.getText().equals("")){display_name.setText(getString(R.string.your_name));}
                try{if(user.getgender().equals("SHE")){profile_menu.setImageResource(R.mipmap.girl);}}
                catch (NullPointerException e){}
            }
        });
    }
}