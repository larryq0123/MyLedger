package com.larry.larrylibrary.extension

import androidx.recyclerview.widget.RecyclerView


/**
 * Created by user on 2018/6/27.
 */

fun RecyclerView.Adapter<RecyclerView.ViewHolder>.defaultLP(matchParent: Boolean = false): RecyclerView.LayoutParams{
    return if(matchParent){
        RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
    }else{
        RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
    }
}