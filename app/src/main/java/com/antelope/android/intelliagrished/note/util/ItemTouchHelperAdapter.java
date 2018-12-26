package com.antelope.android.intelliagrished.note.util;

/**
 * Created by ivorita on 2018/7/27.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);
    //数据删除
    void onItemDismiss(int position);
}
