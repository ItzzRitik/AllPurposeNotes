package xtremedeveloper.allpurposenotes;

import android.content.Context;
import android.content.Intent;
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
    String[] listItems,Ntext;
    RelativeLayout notes_text,notes_pic;
    String []Ctype;
    public MyPagerAdapter(Context context, String[] listItems,String[] Ctype,String[] Ntext)
    {
        this.context = context;
        this.listItems = listItems;
        this.Ctype=Ctype;
        this.Ntext=Ntext;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        int type=Integer.parseInt(Ctype[position]);
        View view = LayoutInflater.from(context).inflate(R.layout.note_items, null);
        CardView note_card = view.findViewById(R.id.note_card);
        note_card.setTag(position);
        try
        {
            if(type==1)
            {
                notes_text=view.findViewById(R.id.notes_text);notes_text.setVisibility(View.VISIBLE);
                notes_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent edit = new Intent(context,EditTextNotes.class);
                        edit.putExtra("title",listItems[position]);
                        edit.putExtra("position",position);
                        edit.putExtra("text",Ntext[position]);
                        context.startActivity(edit);
                    }
                });

                TextView notes_title=view.findViewById(R.id.notes_title_text);
                notes_title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/exo2.ttf"));
                notes_title.setText(listItems[position]);

                TextView notes_textData=view.findViewById(R.id.notes_textData);
                notes_textData.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/exo2.ttf"));
                notes_textData.setText(Ntext[position]);
            }
            else if(type==2)
            {
                notes_pic=view.findViewById(R.id.notes_pic);notes_pic.setVisibility(View.VISIBLE);
                notes_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
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