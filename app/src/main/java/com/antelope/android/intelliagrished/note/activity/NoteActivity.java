package com.antelope.android.intelliagrished.note.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.note.fragment.noteDetailFragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_ID = "com.antelope.android.intelliagrished.note_id";

    public static Intent newIntent(Context packageContext, UUID id) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        return noteDetailFragment.newInstance(id);
    }

}
