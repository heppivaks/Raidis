package com.example.raidis.Intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raidis.Pagrindinis.PagrindinisPaieska;
import com.example.raidis.Prisijungimas.PrisijungimoLangas;
import com.example.raidis.R;
import com.google.firebase.auth.FirebaseAuth;

public class IntroSkaidre extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private IntroValdymas sliderAdapter;

    private TextView[] bottomNav;

    private Button prevBtn;
    private Button nextBtn;
    private Button jungtisBtn;

    private int dabartinisSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_skaidre);

        patikrintiPrisijungima();

        viewPager = (ViewPager) findViewById(R.id.slideViewPager) ;
        linearLayout = (LinearLayout) findViewById(R.id.bottomView);

        prevBtn = (Button) findViewById(R.id.prevBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        jungtisBtn = (Button) findViewById(R.id.jungtisBtn);

        sliderAdapter = new IntroValdymas(this);

        viewPager.setAdapter(sliderAdapter);

        addDotsIndi(0);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(dabartinisSlide + 1);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(dabartinisSlide - 1);
            }
        });

        viewPager.addOnPageChangeListener(viewListener);
    }

    private void patikrintiPrisijungima(){
       String uid = FirebaseAuth.getInstance().getUid();
       if(uid != null){
           Log.d("IntroSkaidre", "Yra prisijungęs");
           Intent i = new Intent(IntroSkaidre.this, PagrindinisPaieska.class);
           startActivity(i);
       }
    }

    public void addDotsIndi(int position) {
        bottomNav = new TextView[3];
        linearLayout.removeAllViews();

        for (int i = 0; i < bottomNav.length; i++){
            bottomNav[i] = new TextView(this);
            bottomNav[i].setText(Html.fromHtml("&#8226;"));
            bottomNav[i].setTextSize(35);
            bottomNav[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            linearLayout.addView(bottomNav[i]);
        }

        if(bottomNav.length > 0){
            bottomNav[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndi(position);

            dabartinisSlide = position;

            if(position == 0){

                nextBtn.setEnabled(true);
                prevBtn.setEnabled((false));
                jungtisBtn.setEnabled(false);
                prevBtn.setVisibility(View.INVISIBLE);
                jungtisBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("KITAS");
                prevBtn.setText("");

            } else if (position == bottomNav.length - 1){

                nextBtn.setEnabled(false);
                prevBtn.setEnabled((true));
                jungtisBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                jungtisBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("");
                jungtisBtn.setText("JUNGTIS");
                prevBtn.setText("ATGAL");

                jungtisBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent loginIntent = new Intent(IntroSkaidre.this, PrisijungimoLangas.class);
                        startActivity(loginIntent);
                    }
                });

            } else {

                nextBtn.setEnabled(true);
                prevBtn.setEnabled((true));
                jungtisBtn.setEnabled(false);
                prevBtn.setVisibility(View.VISIBLE);
                jungtisBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("KITAS");
                prevBtn.setText("ATGAL");
                jungtisBtn.setText("");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
