
package com.example.MyFragsFinal;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements Fragment1.OnButtonClickListener  {

    private int[] frames;
    private boolean hiden;
    private int count_clockiwise =0;

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else if ( getSupportFragmentManager().getBackStackEntryAt(count-1).getName().equals("clockwise")) {
            super.onBackPressed();
            int t = frames[3];
            frames[3] = frames[2];
            frames[2] = frames[1];
            frames[1] = frames[0];
            frames[0] = t;

            //getSupportFragmentManager().popBackStack();

        }

        else {
            getSupportFragmentManager().popBackStack();
        }

    }
    @Override
    public void onButtonClickShuffle() {

        List<Integer> list = new ArrayList<Integer>(Arrays.asList(frames[0], frames[1], frames[2], frames[3]));
        Collections.shuffle(list);
        for (int i = 0; i < 4; i++) frames[i] = list.get(i).intValue();
        newFragments("shuffle");
    }

    @Override
    public void onButtonClickClockwise() {

        int t = frames[0];
        frames[0] = frames[1];
        frames[1] = frames[2];
        frames[2] = frames[3];
        frames[3] = t;

        newFragments("clockwise");
    }
    @Override
    public void onButtonClickHide() {

        if(hiden) return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Fragment f : fragmentManager.getFragments()) {

            if (f instanceof Fragment1 ) continue;

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(f);


            transaction.addToBackStack("hide");
            transaction.commit();
        }

        hiden = true;
    }

    @Override
    public void onButtonClickRestore() {

        if (!hiden) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment f : fragmentManager.getFragments()) {
            if (f instanceof Fragment1) continue;
            transaction.show(f);
        }

        transaction.addToBackStack("restore");
        transaction.commit();

        hiden = false;
    }
    private void newFragments(String c) {

        Fragment[] newFragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (int i = 0; i < 4; i++) {
            transaction.replace(frames[i], newFragments[i]);
            if (hiden && !(newFragments[i] instanceof Fragment1)) transaction.hide(newFragments[i]);
        }

        transaction.addToBackStack(c);
        transaction.commit();

    }
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof Fragment1) {
            ((Fragment1) fragment).setOnButtonClickListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            frames = new int[]{R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4};
            hiden = false;

            Fragment[] fragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (int i = 0; i < 4; i++) {
                transaction.add(frames[i], fragments[i]);
            }
            transaction.addToBackStack(null);
            transaction.commit();


        } else {
            frames = savedInstanceState.getIntArray("FRAMES");
            hiden = savedInstanceState.getBoolean("HIDEN");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDEN", hiden);
    }

}