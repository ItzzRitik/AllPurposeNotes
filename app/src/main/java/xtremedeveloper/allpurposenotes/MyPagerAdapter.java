package xtremedeveloper.allpurposenotes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPagerAdapter extends PagerAdapter{
    Context context;
    String[] listItems;
    public MyPagerAdapter(Context context, String[] listItems)
    {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.note_items, null);
        try {
            CardView note_card = (CardView) view.findViewById(R.id.note_card);
            note_card.setTag(position);
            TextView notes_title=(TextView)view.findViewById(R.id.notes_title);
            notes_title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/exo2.ttf"));
            notes_title.setText(listItems[position]);
            container.addView(view);
        } catch (Exception e) {e.printStackTrace();}
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {container.removeView((View) object);}
    @Override
    public int getCount() {
        return listItems.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

}