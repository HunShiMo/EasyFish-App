package com.wlh.epiboly.easyfish.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.wlh.epiboly.easyfish.Activity.IndexActivity;
import com.wlh.epiboly.easyfish.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HisDataAdapter extends BaseAdapter {
    private List<IndexActivity.DataList> datas;

    private Context mContext;

    public HisDataAdapter(List<IndexActivity.DataList> datas, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hisdata, null);
            // 减少findView的次数
            holder = new ViewHolder(convertView);
            // 初始化布局中的元素
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(datas.get(position).getName());
//        SetLineChart( holder.line);
        Glide.with(mContext).load(datas.get(position).getIc()).into(holder.ivIc);
        setData(datas.get(position).getValue(),holder.line,datas.get(position).getName()+"曲线");
        SetLineChart( holder.line);
        return convertView;
    }

    LineDataSet set1;
    private void setData(ArrayList<Entry> values, LineChart mLineChar,String s) {
        if (mLineChar.getData() != null && mLineChar.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChar.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChar.getData().notifyDataChanged();
            mLineChar.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, s);
            // 在这里设置线
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // 填充背景只支持18以上
                //Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                //set1.setFillDrawable(drawable);
                set1.setFillColor(Color.YELLOW);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            //添加数据集
            dataSets.add(set1);

            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);
            //设置数据
            mLineChar.setData(data);
            //默认动画
            mLineChar.animateX(500);
            //刷新
            mLineChar.invalidate();
            // 得到这个文字
            Legend l = mLineChar.getLegend();
            // 修改文字 ...
            l.setForm(Legend.LegendForm.LINE);
            l.setWordWrapEnabled(false);
            //传递数据集
        }
    }



    private void SetLineChart(LineChart lineSal1) {
        /*//设置手势滑动事件
        lineSal1.setOnChartGestureListener(IndexActivity.this);
        //设置数值选择监听
        lineSal1.setOnChartValueSelectedListener(this);*/
        //后台绘制
        lineSal1.setDrawGridBackground(false);
        //设置描述文本
        lineSal1.getDescription().setEnabled(false);
        //设置支持触控手势
        lineSal1.setTouchEnabled(true);
        //设置缩放
        lineSal1.setDragEnabled(true);
        //设置推动
        lineSal1.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        lineSal1.setPinchZoom(true);


        YAxis rightYAxis = lineSal1.getAxisRight();
        rightYAxis.setEnabled(false);
        XAxis xAxis = lineSal1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        lineSal1.setVisibleXRange(0,10);
    }



    static class ViewHolder {
        @BindView(R.id.iv_ic)
        ImageView ivIc;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.line)
        LineChart line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
