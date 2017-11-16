package com.unicool.ch2o_bluetooth.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicool.ch2o_bluetooth.R;

import java.util.ArrayList;
import java.util.List;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.presenter.adapter
 *  @文件名:   BaseRecyclerAdapter
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 11:33
 *  @描述：    TODO
 */
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    private final LayoutInflater mInflater;
    private volatile List<T> mDatas = new ArrayList<>();
    private boolean mNeedClear;

    public BaseRecyclerAdapter(Context context, RecyclerView recyclerView) {
        mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearDatas() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setLock() {
        mNeedClear = true;
    }

    public synchronized void addData(int type, int position, T data) {
        Log.i("HomeFragment", "data:" + data);
        if (mNeedClear) {
            mNeedClear = false;
            mDatas.clear();
            notifyDataSetChanged();
        } else {
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).equals(data)) {
                    T remove = mDatas.remove(i--);
                    notifyItemRemoved(position);
                    Log.i("HomeFragment", "remove:" + remove);
                }
            }
        }

        mDatas.add(0, data);
        notifyItemInserted(position);
    }

    public void removeData(int type, int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerVHolder<T>(mInflater.inflate(R.layout.list_item_simple, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            BaseRecyclerVHolder<T> holder0 = (BaseRecyclerVHolder<T>) holder;
            holder0.setData(mDatas.get(position));
        }

        if (mOnItemViewClickListener == null) return;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemViewClickListener.onItemClick(v, mDatas.get(holder.getAdapterPosition()), getItemViewType(holder.getAdapterPosition()));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemViewClickListener.onItemLongClick(v, mDatas.get(holder.getAdapterPosition()), getItemViewType(holder.getAdapterPosition()));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public interface OnItemViewClickListener<T> {
        void onItemClick(View view, T t, int itemViewType);

        void onItemLongClick(View view, T t, int itemViewType);
    }

    private OnItemViewClickListener mOnItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener listener) {
        this.mOnItemViewClickListener = listener;
    }
}
