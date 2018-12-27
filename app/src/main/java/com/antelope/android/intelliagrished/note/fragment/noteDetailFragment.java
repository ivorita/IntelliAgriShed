package com.antelope.android.intelliagrished.note.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.note.util.NoteBean;
import com.antelope.android.intelliagrished.note.util.NoteLab;
import com.antelope.android.intelliagrished.note.util.PictureUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class noteDetailFragment extends Fragment {

    private static final String ARG_NOTE_ID = "id";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    @BindView(R.id.iv_note)
    ImageView mIvNote;
    Unbinder unbinder;
    @BindView(R.id.timer_select)
    TextView mTimerSelect;

    private EditText mTitle;
    private EditText mContent;

    private NoteBean mNoteBean;

    private File mPhotoFile;

    private static final int REQUEST_DATE = 0;

    private static final int REQUEST_PHOTO = 2;

    private String item_time;

    private String item_date;

    public static noteDetailFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, id);

        noteDetailFragment fragment = new noteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID id = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        Log.d("noteDetailFragment", "onCreate: id " + id);
        if (mNoteBean != null) {
            Log.d("noteDetailFragment", "mNoteBean not null");
        } else {
            mNoteBean = NoteLab.get(getActivity()).getNote(id);
            mPhotoFile = NoteLab.get(getActivity()).getPhotoFile(mNoteBean);
        }
        setHasOptionsMenu(true);
    }

    //此方法实例化fragment视图的布局，然后将实例化的View返回给托管activity。
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_detail, container, false);

        mTitle = v.findViewById(R.id.note_title);
        mContent = v.findViewById(R.id.note_content);

        unbinder = ButterKnife.bind(this, v);

        if (mNoteBean == null) {
            Log.d("noteDetailFragment", "mNoteBean is null");
        }

        if (mTitle != null) {
            mTitle.setText(mNoteBean.getTitle());
            Log.d("StudentFragment", "mTitle is not null");
        }
        Log.d("StudentFragment", "getTitle:" + mNoteBean.getTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mNoteBean.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mContent.setText(mNoteBean.getContent());
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mNoteBean.setContent(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ViewTreeObserver mPhotoObserver = mIvNote.getViewTreeObserver();
        mPhotoObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhotoView(mIvNote.getWidth(), mIvNote.getHeight());
            }
        });

        if (mNoteBean.getDate_time() != null) {
            mTimerSelect.setText(mNoteBean.getDate_time());
        } else {
            mTimerSelect.setText("点击选择时间");
        }

        Log.d("noteDetail: ", "getDate_time: " + mNoteBean.getDate_time());
        mTimerSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("noteDetail: ", "charSequence: " + String.valueOf(charSequence));
                mNoteBean.setDate_time(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("noteDetail: ", "charSequence: " + String.valueOf(editable));
                mNoteBean.setDate_time(String.valueOf(editable));
            }
        });
        //updatePhotoView(mIvNote.getWidth(), mIvNote.getHeight());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.camera:
                Toast.makeText(getActivity(), "相机", Toast.LENGTH_SHORT).show();
                final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                PackageManager packageManager = getActivity().getPackageManager();
                boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;//resolveActivity可以找到匹配给定Intent任务的activity。在此为判断设备是否支持拍照。
                Uri uri = FileProvider.getUriForFile(getActivity(),//调用getUriForFile()会把本地文件路径转换为相机能看得见的Uri形式
                        "com.antelope.android.intelliagrished.fileprovider", mPhotoFile);
                //通过传入保存在MediaStore.EXTRA_OUTPUT中的指向存储路径的Uri来完成。这个Uri会指向FileProvider提供的位置。
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//允许目标activity在指定的位置写文件
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.antelope.android.intelliagrished.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //相机已经保存文件，再次调用权限，关闭文件访问。
            updatePhotoView(mIvNote.getWidth(), mIvNote.getHeight());
        }
    }

    //刷新mPhotoView的方法
    private void updatePhotoView(int width, int height) {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mIvNote.setImageDrawable(null);
        } else {
            //Log.d("noteDetail", "updatePhotoView: " + mIvNote.getWidth() + " " + mIvNote.getHeight());
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), width, height);
            mIvNote.setImageBitmap(bitmap);
        }
    }

    //覆盖onPause()方法完成数据刷新
    @Override
    public void onPause() {
        super.onPause();
        NoteLab.get(getActivity()).updateNote(mNoteBean);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }


    @OnClick({R.id.timer_select, R.id.iv_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timer_select:

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String format1 = format.format(date);

                TimeSelector timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        mTimerSelect.setText(time);
                        Log.d("noteDetail", "year: " + time.substring(0, 4));

                        //mNoteBean.setDate_time(time);

                        item_date = time.substring(0,10);
                        item_time = time.substring(11,16);

                        mNoteBean.setItem_date(item_date);
                        mNoteBean.setItem_time(item_time);
                        Log.d("noteDetail", "date: " + item_date);
                        Log.d("noteDetail", "time: " + item_time);
                    }
                }, format1, "2050-1-1 24:00");
                timeSelector.show();

                break;
            case R.id.iv_note:

                if (mPhotoFile == null || !mPhotoFile.exists()){
                    mIvNote.setImageDrawable(null);
                } else {
                    FragmentManager fragment = getFragmentManager();
                    PhotoDetailFragment dialog = PhotoDetailFragment.newInstance(mPhotoFile);
                    dialog.setTargetFragment(noteDetailFragment.this,REQUEST_PHOTO);
                    dialog.show(fragment,DIALOG_PHOTO);
                }

                break;
        }
    }
}
