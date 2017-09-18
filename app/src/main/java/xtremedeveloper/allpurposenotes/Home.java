package xtremedeveloper.allpurposenotes;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;

import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class Home extends AppCompatActivity
{
    FloatingActionButton close_menu;
    AppBarLayout appbar;
    TextView display_name;
    private ECPagerView notesPager;
    ECPagerViewAdapter notesPagerAdapter;
    ECBackgroundSwitcherView notesPagerBackground;
    @Override
    public void onBackPressed() {
        if (!notesPager.collapse()) {super.onBackPressed();}
        else{appbar.setVisibility(View.VISIBLE);}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home);

        appbar = (AppBarLayout)findViewById(R.id.appbar);
        close_menu=(FloatingActionButton)findViewById(R.id.close_menu);
        close_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {appbar.setExpanded(false);}
        });

        display_name=(TextView)findViewById(R.id.display_name);
        display_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));


        notesPager = (ECPagerView) findViewById(R.id.notesPager);
        List<ECCardData> dataset = CardDataImpl.generateExampleData();
        notesPagerAdapter = new ECPagerViewAdapter(getApplicationContext(), dataset) {
            @Override
            public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, final ListView list, ECCardData data) {
                CardDataImpl cardData = (CardDataImpl) data;
                final List<String> listItems = cardData.getListItems();
                final CardListItemAdapter listItemAdapter = new CardListItemAdapter(getApplicationContext(), listItems);
                list.setAdapter(listItemAdapter);
                list.setBackgroundColor(Color.WHITE);
                TextView cardTitle = new TextView(getApplicationContext());
                cardTitle.setText(cardData.getCardTitle());
                cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                head.addView(cardTitle, layoutParams);
                head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if(!notesPager.collapse())appbar.setVisibility(View.GONE);
                        else appbar.setVisibility(View.VISIBLE);
                        notesPager.toggle();
                    }
                });
            }
        };
        notesPager.setPagerViewAdapter(notesPagerAdapter);
        notesPagerBackground=(ECBackgroundSwitcherView) findViewById(R.id.notesPagerBackground);
        notesPager.setBackgroundSwitcherView(notesPagerBackground);
        /*dataset.remove(2);
        notesPagerAdapter.notifyDataSetChanged();*/
    }
}