package com.example.raidis.Intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.raidis.R;

public class IntroValdymas extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public IntroValdymas(Context context){
        this.context = context;
    }

    //Arrays

    public int[] slide_images = {
            R.drawable.friends,
            R.drawable.earth,
            R.drawable.money
    };

    public String[] slide_headings = {
            "SUSIRASKITE NAUJŲ DRAUGŲ",
            "BŪKITE EKOLOGIŠKI",
            "GAUKITE ATLYGĮ"
    };

    public String[] slide_des = {
            "Kooperuokitės ir važiuokite drauge!",
            "Važiuodami kartu prisidėsite prie žemės išsaugojimo!",
            "Gaukite atlygį iš keleivių!"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.skaidres_maketas, container, false);

        ImageView sliderImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView sliderTextHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView sliderTextDes = (TextView) view.findViewById(R.id.slide_des);

        sliderImageView.setImageResource(slide_images[position]);
        sliderTextHeading.setText(slide_headings[position]);
        sliderTextDes.setText(slide_des[position]);

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);

    }
}
