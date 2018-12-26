package com.antelope.android.intelliagrished.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.adapter.RecAdapter;
import com.antelope.android.intelliagrished.db.Rec_item;
import com.antelope.android.intelliagrished.note.activity.NotePagerActivity;
import com.antelope.android.intelliagrished.note.fragment.noteDetailFragment;
import com.antelope.android.intelliagrished.note.util.ItemTouchHelperAdapter;
import com.antelope.android.intelliagrished.note.util.NoteBean;
import com.antelope.android.intelliagrished.note.util.NoteLab;
import com.antelope.android.intelliagrished.note.util.OnDeleteNoteListener;
import com.antelope.android.intelliagrished.note.util.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class noteFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    RecAdapter recAdapter;

    NoteAdapter mNoteAdapter;

    RecyclerView mRecView;

    private static final String ARG_NOTE_ID = "note_id";

    private NoteBean mNoteBean;

    private List<Rec_item> mItemList = new ArrayList<>();

    private int position;

    private OnDeleteNoteListener mDeleteCalLBack;

    public static noteFragment newInstance() {
        noteFragment fragment = new noteFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        mRecView = view.findViewById(R.id.rec_view);

        mRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        for (int i = 0; i < 20; i++) {
//            Rec_item item = new Rec_item("00:36", "2018年12月12日", "test");
//            mItemList.add(item);
//        }
//
//        RecAdapter recAdapter = new RecAdapter(mItemList);
//
//        mRecView.setAdapter(recAdapter);

        unbinder = ButterKnife.bind(this, view);

        updateUI();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mNoteAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecView);

        return view;
    }

    private void updateUI() {
        NoteLab noteLab = NoteLab.get(getActivity());
        List<NoteBean> notes = noteLab.getNotes();

//        mNoteAdapter = new NoteAdapter(notes);
//        mRecView.setAdapter(mNoteAdapter);

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notes);
            mRecView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.setNoteBeans(notes);
            mNoteAdapter.notifyDataSetChanged();
            //mNoteAdapter.notifyItemChanged(position);//此方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容,可以实现动态的刷新列表的功能，单例更新
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mTimeTextView;
        private TextView mDateTextView;
        //        private TextView mContentTextView;
        private NoteBean mNoteBean;

        //每次绑定前，首先实例化相关组件，由于这是一次性事件，因此直接放在构造方法里处理
        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recyclerview_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.list_item_title);
            mTimeTextView = itemView.findViewById(R.id.item_time);
            mDateTextView = itemView.findViewById(R.id.item_date);
//            mContentTextView = itemView.findViewById(R.id.note_content);
        }


        //每次有新的Crime要在CrimeHolder中显示时，都要调用它一次
        public void bind(NoteBean noteBean) {
            mNoteBean = noteBean;
            mTitleTextView.setText(mNoteBean.getTitle());
            Log.d("noteFragment", "getItem_time" + mNoteBean.getItem_time());
            mTimeTextView.setText(mNoteBean.getItem_time());

            if (mNoteBean.getItem_date() != null) {
                mDateTextView.setText(mNoteBean.getItem_date().substring(0,4) + "年" +
                        mNoteBean.getItem_date().substring(6,7) + "月" +
                        mNoteBean.getItem_date().substring(9,10) + "日");
            }

        }

        //列表项视图都关联着ViewHolder，就可以让ViewHolder为它监听用户触摸事件。
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(),mCrime.getTitle() + " Clicked",Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(),CrimeActivity.class);
            position = mRecView.getChildAdapterPosition(view);//通过点击事件获取当前位置
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNoteBean.getId());
            startActivity(intent);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> implements ItemTouchHelperAdapter {
        private List<NoteBean> mNoteBeans;
        private NoteBean mNoteBean;
        private static final String ARG_NOTE_ID = "note_id";
        private NoteHolder mHolder;

        public NoteAdapter(List<NoteBean> noteBeans) {
            mNoteBeans = noteBeans;
            Log.d("noteFragment", String.valueOf(mNoteBeans.size()));
        }

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new NoteHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mNoteBeans.size();
        }

        public void setNoteBeans(List<NoteBean> noteBeans) {
            mNoteBeans = noteBeans;
        }

        //每次RecyclerView要求Holder绑定对应的note时，都会调用bind方法
        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull List<Object> payloads) {
            mNoteBean = mNoteBeans.get(position);
            Log.d("noteFragment", "onBindViewHolder: " + mNoteBean.getId());
            mHolder = holder;
            holder.bind(mNoteBean);
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            //交换位置
            Collections.swap(mNoteBeans, fromPosition, toPosition);

            notifyItemMoved(fromPosition, toPosition);
            //updateUI();
        }

        @Override
        public void onItemDismiss(int position) {
            //移除数据

            //UUID crimeId = (UUID)getArguments().getSerializable(ARG_NOTE_ID);
            //mNoteBean = NoteLab.get(getActivity()).getNote(crimeId);
//            NoteLab.get(getActivity()).removeNote(mNoteBeans.get(position));
//            NoteLab n1 = NoteLab.get(getActivity());
            //n1.removeNote(mNoteAdapter.mNoteBeans.get(mHolder.getAdapterPosition()));

            Log.d("noteFragment", "onItemDismiss: id");

            NoteBean noteBean1 = mNoteBeans.get(position);

            Log.d("noteFragment", "position: " + position);

            //Log.d("noteFragment", "onSwiped: " + noteBean.getId());
//            if (noteBean.getId() != null) {
//                mDeleteCalLBack.onNoteIdSelected(noteBean.getId());
//            }

            NoteBean noteBean = NoteLab.get(getActivity()).getNote(noteBean1.getId());
            NoteLab.get(getActivity()).removeNote(noteBean);
            mNoteBeans.remove(position);

            updateUI();
            notifyItemRemoved(position);
        }

    }

    //悬浮按钮
    @OnClick(R.id.fab)
    public void onViewClicked() {
        NoteBean noteBean = new NoteBean();
        NoteLab.get(getActivity()).addNote(noteBean);
        Log.d("CLF", "onOptionsItemSelected: " + noteBean.getTitle());
        Intent intent = NotePagerActivity.newIntent(getActivity(), noteBean.getId());
        startActivity(intent);
    }
}
