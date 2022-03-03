package com.wlh.epiboly.easyfish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wlh.epiboly.easyfish.Activity.IndexActivity;
import com.wlh.epiboly.easyfish.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 实时数据：每个实时数据的适配器
 */
public class DataAdapter extends BaseAdapter {

    private List<IndexActivity.DataList> datas;
    private Context mContext;

    public DataAdapter(List<IndexActivity.DataList> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return datas.size();
    }
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            // 使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_index, null);
            // 减少findView的次数
            holder = new ViewHolder(convertView);
            // 初始化布局中的元素
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(datas.get(position).getName());
        if (datas.get(position).getDbData().size()>0) {
            holder.tvData.setText(String.valueOf(datas.get(position).getDbData().get(datas.get(position).getDbData().size()-1).getData()));
        }else{
            holder.tvData.setText(String.valueOf(0));
        }
        Glide.with(mContext).load(datas.get(position).getIc()).into(holder.ivIc);
        if (datas.get(position).getThreshold()>0) {
            holder.tvThreshold.setText("阈值:"+ String.valueOf(datas.get(position).getThreshold()));
        } else {
            holder.tvThreshold.setText("长按设置阈值");
        }
        return convertView;
    }
    static
    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_ic)
        ImageView ivIc;
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.tv_threshold)
        TextView tvThreshold;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}