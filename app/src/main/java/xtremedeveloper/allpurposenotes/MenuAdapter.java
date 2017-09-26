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

public class MenuAdapter extends PagerAdapter{
    Context context;
    String[] listItems;
    ImageView menu_icon;
    int []menu;
    public MenuAdapter(Context context,int[] menu)
    {
        this.context = context;
        //this.listItems = listItems;
        this.menu=menu;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_items, null);
        CardView menu_card = (CardView) view.findViewById(R.id.menu_card);
        menu_card.setTag(position);
        menu_icon=(ImageView) view.findViewById(R.id.menu_icon);
        //menu_icon.setImageResource(menu[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {container.removeView((View) object);}
    @Override
    public int getCount() {return menu.length;}
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

}