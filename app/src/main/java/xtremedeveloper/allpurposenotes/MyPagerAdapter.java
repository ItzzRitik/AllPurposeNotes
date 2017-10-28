package xtremedeveloper.allpurposenotes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MyPagerAdapter extends PagerAdapter{
    Context context;
    String[] listItems;
    RelativeLayout notes_text,notes_pic;
    Integer []Ctype;
    public MyPagerAdapter(Context context, String[] listItems,Integer[] Ctype)
    {
        this.context = context;
        this.listItems = listItems;
        this.Ctype=Ctype;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.note_items, null);
        CardView note_card = view.findViewById(R.id.note_card);
        note_card.setTag(position);
        notes_text=view.findViewById(R.id.notes_text);
        notes_pic=view.findViewById(R.id.notes_pic);
        try
        {
            if(Ctype[position]==1)
            {
                notes_text.setVisibility(View.VISIBLE);

                TextView notes_title=view.findViewById(R.id.notes_title_text);
                notes_title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/exo2.ttf"));
                notes_title.setText(listItems[position]);
            }
            else if(Ctype[position]==2)
            {
                notes_pic.setVisibility(View.VISIBLE);
                TextView notes_title_pic=view.findViewById(R.id.notes_title_pic);
                notes_title_pic.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/exo2.ttf"));
                notes_title_pic.setText(listItems[position]);
            }
            container.addView(view);
        } catch (Exception e) {e.printStackTrace();
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();}
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