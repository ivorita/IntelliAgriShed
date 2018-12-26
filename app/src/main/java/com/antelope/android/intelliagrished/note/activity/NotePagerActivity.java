package com.antelope.android.intelliagrished.note.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.fragment.noteFragment;
import com.antelope.android.intelliagrished.note.fragment.noteDetailFragment;
import com.antelope.android.intelliagrished.note.util.NoteBean;
import com.antelope.android.intelliagrished.note.util.NoteLab;

import java.util.List;
import java.util.UUID;

/**
 * Created by ivorita on 2018/7/23.
 */

public class NotePagerActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE_ID = "com.antelope.android.intelliagrished.note_id";
    private ViewPager mViewPager;
    private List<NoteBean> mNoteBeans;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext,NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE_ID,crimeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);

        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_NOTE_ID);

        mViewPager = findViewById(R.id.activity_note_pager_view_pager);
        mNoteBeans = NoteLab.get(this).getNotes();//从Lab中获取数据集

        FragmentManager fragmentManager = getSupportFragmentManager();//获取activity的FragmentManager实例
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                NoteBean noteBean = mNoteBeans.get(position);
                return noteDetailFragment.newInstance(noteBean.getId());
            }

            @Override
            public int getCount() {
                return mNoteBeans.size();
            }
        });

        //设置初始分页显示项
        for (int i = 0 ; i < mNoteBeans.size(); i++){
            if (mNoteBeans.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
