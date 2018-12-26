package com.antelope.android.intelliagrished.note.activity;

import android.support.v4.app.Fragment;

import com.antelope.android.intelliagrished.fragment.noteFragment;

public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new noteFragment();
    }

}
