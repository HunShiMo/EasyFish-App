package com.wlh.epiboly.easyfish.Activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.data.Entry;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.wlh.epiboly.easyfish.Adapter.DataAdapter;
import com.wlh.epiboly.easyfish.Adapter.HisDataAdapter;
import com.wlh.epiboly.easyfish.FishConfig;
import com.wlh.epiboly.easyfish.R;
import com.wlh.epiboly.easyfish.Utils.DBOpenHelper;
import com.wlh.epiboly.easyfish.Utils.RxTimer;
import com.wlh.epiboly.easyfish.Utils.SJService;
import com.wlh.epiboly.easyfish.View.DbData;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.wlh.epiboly.easyfish.Utils.SJService.getSJService;


public class IndexActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener, TextWatcher {

    String TAG="IndexActivity.java";
    Context context = IndexActivity.this;
    @BindView(R.id.VF_Main)
    ViewFlipper VFMain;

    @BindView(R.id.RG_Main_Img)
    RadioGroup RGMainImg;
    @BindView(R.id.RG_Main_Tab)
    RadioGroup RGMainTab;
    SharedPreferences FishSP;
    Gson gson;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //??????????????????????????????????????????????????????
    String[] Titles = new String[]{"????????????", "????????????", "????????????"};
    Integer[] rbTabId = new Integer[]{R.id.RBtn_Main_Index, R.id.RBtn_Main_Recharge, R.id.RBtn_Main_Person};

    @BindView(R.id.tv_useremail)
    TextView tvUseremail;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    SJService sjService;

    // ???????????????????????????
    DataAdapter data1Adapter;
    DataAdapter data2Adapter;
    DataAdapter data3Adapter;

    // ???????????????????????????
    HisDataAdapter hisData1Adapter;
    HisDataAdapter hisData2Adapter;
    HisDataAdapter hisData3Adapter;

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    Dialog SetThresholdDialog;
    View SetThresholdView;
    EditText etThreshold;
    Button btnOk;
    ImageButton ibCancle;
    int SetThresholdId = 0;
    int SetThresholdPosition = 0;
    Integer[] imgId = new Integer[]{R.drawable.ic_sal, R.drawable.ic_temperature, R.drawable.ic_hum, R.drawable.ic_ds, R.drawable.ic_ph, R.drawable.ic_oxi};
    @BindView(R.id.RBtn_Main_Index)
    RadioButton RBtnMainIndex;
    @BindView(R.id.RBtn_Main_Recharge)
    RadioButton RBtnMainRecharge;
    @BindView(R.id.RBtn_Main_Person)
    RadioButton RBtnMainPerson;
    @BindView(R.id.RBtn_Main_Tab_0)
    RadioButton RBtnMainTab0;
    @BindView(R.id.RBtn_Main_Tab_2)
    RadioButton RBtnMainTab2;
    @BindView(R.id.RBtn_Main_Tab_3)
    RadioButton RBtnMainTab3;
    /**
     * ??????????????????????????????
     *  1.???TabLayout??????tab
     *  2.???ViewPager??????adapter
     *  3.??????TabLayout???ViewPager??????
     */
    ViewPager vp_index; //????????????????????????
    TabLayout tab_index;
    private View index1View, index2View, index3View;
    private List<View> IndexViewList = new ArrayList<>();
    GridView gvIndex1,gvIndex2,gvIndex3;

    ViewPager vp_his;   //????????????????????????
    TabLayout tab_his;
    private View his1View, his2View, his3View;
    private List<View> HisViewList = new ArrayList<>();
    ListView lv_hisdata1,lv_hisdata2,lv_hisdata3;
    List<List<DataList>> dataLists=new ArrayList<>();
    List<List<DataList>> realTimeDataLists=new ArrayList<>();
    //dbName:?????????????????????????????????????????????????????????????????????????????????????????????
    List<List<String>> dbName= Arrays.asList(Arrays.asList("p1tem","p1sal","p1deep","p1yls","p1rjy"),Arrays.asList("p2tem","p2sal","p2deep","p2yls","p2rjy"),Arrays.asList("p3tem","p3sal","p3deep","p3yls","p3rjy"));
    //DateName:???????????????????????????????????????????????????????????????????????????
    List<String> DateName =  Arrays.asList("??????", "?????????", "??????", "?????????", "?????????");
    //ic:????????????????????????????????????icon???????????????????????????
    List<Integer> ic =  Arrays.asList(R.drawable.ic_temperature,R.drawable.ic_sal,R.drawable.ic_hum,R.drawable.ic_ds,R.drawable.ic_oxi);

    //????????????
    View dialogDate;
    LinearLayout llDate;
    TextView tvDate;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        initView();     //?????????indexAcdivity??????
        initDateTime(); //????????????????????????
        initDialog();   //?????????"??????????????????"???Dialog
        initList();     //????????? ??????????????????????????????????????????????????????????????????????????????
//        GetData("2020-04-02");
        realTimeDataLists.clear();
        for(int i=0;i<dbName.size();i++){
            List<DataList> dataList=new ArrayList<>();

            for(int j=0;j<dbName.get(i).size();j++){
                DataList data=new DataList();
                data.setDbName(dbName.get(i).get(j));
                data.setName(DateName.get(j));
                data.setIc(ic.get(j));
                data.setDbData(new ArrayList<DbData>());
                dataList.add(data);
            }
            realTimeDataLists.add(dataList);
        }
        init();
        initListener();
        initDataRel();
        // ?????????????????????????????????
        initTimedSearch();
        //????????????
    }

    RxTimer realTimeData = null;   //?????????

    /**
     * ???????????????????????????
     *
     * @return void
     */
    private void initTimedSearch() {
        // ????????????????????????
        if(realTimeData != null){
            realTimeData.cancel();
        }
        // ?????????????????????
        realTimeData = new RxTimer();
        realTimeData.Interval(60, new RxTimer.RxAction() {
            @Override
            public void action(long number) {
                // ????????????????????????
                IndexActivity.this.getLastData();
            }
        });
    }

    private void getLastData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //???????????????????????????
                String sql="";
                //???????????????????????????
                Connection conn = DBOpenHelper.getConn();
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    if(conn != null && (!conn.isClosed())) {
                        int listLength = realTimeDataLists.size();
//                        System.out.println(listLength);
                        List<String> tableList = new ArrayList<>();
                        tableList.add("fish_qixia_20160101_20200701");
                        tableList.add("fish_sanggou_20160101_20200701");
                        tableList.add("fish_xiaoshidao_20160101_20200701");
                        for (int k=0; k<tableList.size(); k++) {
                            sql="select * from "+ tableList.get(k) + " order by id DESC limit 1";
                            System.out.println("sql---------------" + sql);
                            preparedStatement = (PreparedStatement) conn.prepareStatement(sql);
                            if(preparedStatement != null) {
                                resultSet = preparedStatement.executeQuery();
                                if(resultSet != null) {
                                    List<DbData> data= new ArrayList<>();
                                    List<Entry> entries = new ArrayList<>();
                                    int num = 0;
                                    // rs?????????????????????????????????i???????????????
                                    List<String> dataNames = new ArrayList<>();
                                    dataNames.add("temp");
                                    dataNames.add("salt");
                                    dataNames.add("deep");
                                    dataNames.add("yls_con");
                                    dataNames.add("rjy_con");
                                    while (resultSet.next()) {
                                        for (int i=0; i<dataNames.size(); i++) {
                                            DbData dbData = new DbData();
                                            dbData.setID(resultSet.getInt("id"));
                                            dbData.setDate(resultSet.getString("date"));
                                            dbData.setHour(resultSet.getInt("hour"));
                                            dbData.setMinute(resultSet.getInt("minute"));
                                            dbData.setData(resultSet.getInt(dataNames.get(i)));
                                            dbData.setNote(resultSet.getString("note"));
                                            dbData.setTimestamp(resultSet.getString("timestamp"));
                                            data.add(dbData);
                                            Entry e = new Entry((float) num++, (float) dbData.getData());
                                            entries.add(e);
                                            realTimeDataLists.get(k).get(i).getDbData().clear();
                                            realTimeDataLists.get(k).get(i).getDbData().addAll(data);
                                            realTimeDataLists.get(k).get(i).getValue().addAll(entries);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DBOpenHelper.closeAll(conn,preparedStatement, resultSet);//??????????????????
                // ??????
                Log.d(TAG, "onCompleteGetLastData=>" + gson.toJson(realTimeDataLists));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data1Adapter.notifyDataSetChanged();
                        data2Adapter.notifyDataSetChanged();
                        data3Adapter.notifyDataSetChanged();
                        for (List<DataList> dataList : realTimeDataLists) {
                            for (DataList data : dataList) {
                                if (data.getThreshold() > 0) {
                                    if (data.getDbData().size() > 0) {
                                        if (data.getDbData().get(data.getDbData().size() - 1).getData() > data.getThreshold()) {
                                            NotificationMana("????????????",
                                                    "??????" + data.getPosition() + "???" + data.getName() + "???????????????:" + data.getDbData().get(data.getDbData().size()-1).getData());
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private void initList() {
        dataLists.clear();
//        List<DataList> dataList=new ArrayList<>();
        /**
         * 1.?????????dbName?????????????????????????????????
         * 2.???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * 3.?????????datalist?????????dataLists???
         */
        for(int i=0;i<dbName.size();i++){
            List<DataList> dataList=new ArrayList<>();

            for(int j=0;j<dbName.get(i).size();j++){
                DataList data=new DataList();
                data.setDbName(dbName.get(i).get(j));
                data.setName(DateName.get(j));
                data.setIc(ic.get(j));
                data.setDbData(new ArrayList<DbData>());
                data.setPosition(i);
                dataList.add(data);
            }
            dataLists.add(dataList);
        }
    }

    private void initDataRel() {
        data1Adapter = new DataAdapter(realTimeDataLists.get(0), IndexActivity.this);
        data2Adapter = new DataAdapter(realTimeDataLists.get(1), IndexActivity.this);
        data3Adapter = new DataAdapter(realTimeDataLists.get(2), IndexActivity.this);
        gvIndex1.setAdapter(data1Adapter);
        gvIndex2.setAdapter(data2Adapter);
        gvIndex3.setAdapter(data3Adapter);


        hisData1Adapter = new HisDataAdapter(dataLists.get(0), IndexActivity.this);
        hisData2Adapter = new HisDataAdapter(dataLists.get(1), IndexActivity.this);
        hisData3Adapter = new HisDataAdapter(dataLists.get(2), IndexActivity.this);
        lv_hisdata1.setAdapter(hisData1Adapter);
        lv_hisdata2.setAdapter(hisData2Adapter);
        lv_hisdata3.setAdapter(hisData3Adapter);

        //?????????????????????
        //????????????????????????
        /**
         *  ?????????????????????????????????????????????????????????????????????????????????????????????????????????

         NetTaskTimer.Interval(30, new RxTimer.RxAction() {
        @Override
        public void action(long number) {
        System.out.println("-------------------------init------------------------");
        IndexActivity.this.GetData(date.toString());
        }
        });
         */
    }

    private void initView() {
        vp_index=this.findViewById(R.id.vp_index);
        tab_index=this.findViewById(R.id.tl_index);
        // ?????????????????????"??????"??????
        index1View = getLayoutInflater().inflate(R.layout.pager_index1, null);
        index2View = getLayoutInflater().inflate(R.layout.pager_index2, null);
        index3View = getLayoutInflater().inflate(R.layout.pager_index3, null);
        //?????????????????????"??????"?????????GridView??????
        gvIndex1=index1View.findViewById(R.id.gv_index1);
        gvIndex2=index2View.findViewById(R.id.gv_index2);
        gvIndex3=index3View.findViewById(R.id.gv_index3);

        vp_his=this.findViewById(R.id.vp_his);
        tab_his=this.findViewById(R.id.tl_his);
        // ?????????????????????"??????"??????
        his1View = getLayoutInflater().inflate(R.layout.pager_hisdata1, null);
        his2View = getLayoutInflater().inflate(R.layout.pager_hisdata2, null);
        his3View = getLayoutInflater().inflate(R.layout.pager_hisdata3, null);
        // ?????????????????????"??????"?????????listView??????
        lv_hisdata1=his1View.findViewById(R.id.lv_hisdata1);
        lv_hisdata2=his2View.findViewById(R.id.lv_hisdata2);
        lv_hisdata3=his3View.findViewById(R.id.lv_hisdata3);

        //??????????????????
        llDate = (LinearLayout)his1View.findViewById(R.id.ll_date);
        tvDate = (TextView) his1View.findViewById(R.id.tv_date);
        llDate.setOnClickListener(this);
        tvDate.addTextChangedListener(this);
    }


    private int year, month, day;   //??????
    private StringBuffer date = new StringBuffer();  //???????????????

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(date.append(String.valueOf(year)).append("-").append(String.format("%02d",month)).append("-").append(String.format("%02d",day)));
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear + 1;
        this.day = dayOfMonth;
        System.out.println("" + year + "-" + monthOfYear + "-" + dayOfMonth);
    }

    private void initDialog() {
        SetThresholdView = LayoutInflater.from(IndexActivity.this).inflate(R.layout.dialog_setthreshold, null);//???????????????view?????????????????????view
        etThreshold = SetThresholdView.findViewById(R.id.et_threshold);
        btnOk = SetThresholdView.findViewById(R.id.btn_ok);
        ibCancle = SetThresholdView.findViewById(R.id.ib_cancle);
        etThreshold = SetThresholdView.findViewById(R.id.et_threshold);
        SetThresholdDialog = new Dialog(IndexActivity.this, R.style.DialogTheme);
        SetThresholdDialog.setContentView(SetThresholdView);
        SetThresholdDialog.setCanceledOnTouchOutside(false);
        SetThresholdDialog.setCancelable(false);
    }

    private void initListener() {
        RGMainTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int DisplayedChild = 0;
                DisplayedChild = group.indexOfChild(group.findViewById(checkedId));
                RGMainImg.check(rbTabId[DisplayedChild]);
                VFMain.setDisplayedChild(DisplayedChild);
                tvTitle.setText(Titles[DisplayedChild]);
            }
        });
        btnLogout.setOnClickListener(this);


        gvIndex1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SetThresholdId = position;
                SetThresholdPosition=0;
                SetThresholdDialog.show();
                return false;
            }
        });
        gvIndex2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SetThresholdId = position;
                SetThresholdPosition=1;
                SetThresholdDialog.show();
                return false;
            }
        });
        gvIndex3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SetThresholdId = position;
                SetThresholdPosition=2;
                SetThresholdDialog.show();
                return false;
            }
        });
        btnOk.setOnClickListener(this);
        ibCancle.setOnClickListener(this);

        System.out.println(llDate);
        llDate.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        gson = new Gson();
        FishSP = getSharedPreferences("FishSP", MODE_PRIVATE);
        tvUseremail.setText("??????:" + FishConfig.email);
        tvUsername.setText("?????????:" + FishConfig.username);
        sjService = getSJService();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, IndexActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);


        IndexViewList.add(index1View);
        IndexViewList.add(index2View);
        IndexViewList.add(index3View);
        vp_index.setAdapter(IndexPagerAdapter);

        HisViewList.add(his1View);
        HisViewList.add(his2View);
        HisViewList.add(his3View);
        vp_his.setAdapter(HisPagerAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                FishConfig.username="";
                FishConfig.email="";
                FishConfig.password="";
                SharedPreferences.Editor editor = FishSP.edit();
                editor.putString("email",   FishConfig.username);
                editor.putString("username",  FishConfig.username);
                editor.putString("password",  FishConfig.password);
                editor.apply();
                Intent intent=new Intent();
                intent.setClass(IndexActivity.this,RegisterActivity.class);
                startActivity(intent);
                Toast.makeText(IndexActivity.this,"??????????????????", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_ok:
                dataLists.get(SetThresholdPosition).get(SetThresholdId).setThreshold(Integer.valueOf(etThreshold.getText().toString()));
                if(SetThresholdPosition==0){
                    data1Adapter.notifyDataSetChanged();
                    hisData1Adapter.notifyDataSetChanged();

                }else if(SetThresholdPosition==1){
                    data2Adapter.notifyDataSetChanged();
                    hisData2Adapter.notifyDataSetChanged();
                }else{
                    data3Adapter.notifyDataSetChanged();
                    hisData3Adapter.notifyDataSetChanged();
                }
                SetThresholdDialog.dismiss();
                break;
            case R.id.ib_cancle:
                SetThresholdId = 0;
                SetThresholdDialog.dismiss();
                break;
            case R.id.ll_date:{

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //???????????????????????????
                            date.delete(0, date.length());
                        }
                        tvDate.setText(date.append(String.valueOf(year)).append("-").append(String.format("%02d",month)).append("-").append(String.format("%02d",day)));
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        date.delete(0, date.length());
                        initDateTime();
                        tvDate.setText(date);
                        dialog.dismiss();
                    }
                });
//                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                dialog.setView(dialogView);
                dialog.show();
                //???????????????????????????
                datePicker.init(year, month - 1, day, this);
            }

        }
    }

    //15???????????????15????????????
    private Connection conn=null; //?????????????????????
    private PreparedStatement ps=null;//????????????sql???????????????
    private ResultSet rs=null;//?????????????????????
    /*??????id??????*/
    private void GetData(String searchDate)  {
        Observable.create(new ObservableOnSubscribe< List<DbData>>() {
            //???????????????
            @Override
            public void subscribe(final ObservableEmitter< List<DbData>> e1) throws Exception {
                //???????????????????????????
                String sql="";
                //???????????????????????????
                conn= DBOpenHelper.getConn();
                try {
                    if(conn!=null&&(!conn.isClosed())){
                        int listLength = dataLists.size();
                        for(int i=0;i<listLength;i++){
                            int listItemLength = dataLists.get(i).size();
                            for(int j=0;j<listItemLength;j++){
                                System.out.println(dataLists.get(i).get(j).getDbName());
                                int id=0;
                                if(dataLists.get(i).get(j).getDbData().size()>0){
                                    id=dataLists.get(i).get(j).getDbData().get(dataLists.get(i).get(j).getDbData().size()-1).getID();
                                }

                                sql="select * from "+dataLists.get(i).get(j).getDbName() + " where date=\"" + searchDate + "\" limit 100"/*+" WHERE ID > "+id*/;

                                System.out.println("sql---------------" + sql);
                                ps= (PreparedStatement) conn.prepareStatement(sql);
                                if(ps!=null){

                                    rs= ps.executeQuery();
                                    if(rs!=null){
                                        List<DbData> data=new ArrayList<>();
                                        List<Entry> entries=new ArrayList<>();
                                        int num = 0;
                                        while(rs.next()){
                                            DbData dbData=new DbData();
                                            dbData.setID(rs.getInt("ID"));
                                            dbData.setDate(rs.getString("Date"));
                                            dbData.setHour(rs.getInt("Hour"));
                                            dbData.setMinute(rs.getInt("Minute"));
                                            dbData.setData(rs.getInt("Data"));
                                            dbData.setNote(rs.getString("note"));
                                            dbData.setTimestamp(rs.getString("Timestamp"));
                                            data.add(dbData);
                                            Entry e = new Entry(Float.valueOf(num++),Float.valueOf(dbData.getData()));
                                            entries.add(e);
                                        }
                                        dataLists.get(i).get(j).getDbData().clear();
                                        dataLists.get(i).get(j).getDbData().addAll(data);
                                        dataLists.get(i).get(j).getValue().addAll(entries);
                                        e1.onNext(data);
                                    }
                                }
                            }

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                DBOpenHelper.closeAll(conn,ps,rs);//??????????????????
                e1.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Observer< List<DbData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext( List<DbData> S) {//??????ui

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "???????????????????????????");
                        Log.d(TAG, e.toString());
                    }
                    @Override
                    public void onComplete() {
                        //??????
                        Log.d(TAG, "onComplete: "+gson.toJson(dataLists));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hisData1Adapter.notifyDataSetChanged();
                                hisData2Adapter.notifyDataSetChanged();
                                hisData3Adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
    }



    private void NotificationMana(final String Title, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelID = "1";
                    String channelName = "channel_name";
                    NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                    mBuilder.setContentTitle(Title)
                            //????????????
                            .setContentText(text)
                            //???????????????
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            //???????????????
                            .setSmallIcon(R.drawable.ic_index)
                            //??????????????????
                            .setWhen(System.currentTimeMillis())
                            //???????????????????????????
                            .setTicker("")
                            .setChannelId(channelID)
                            //???????????????????????????????????????????????????????????????????????????????????????
                            .setDefaults(Notification.DEFAULT_SOUND);
                    notificationManager.notify(10, mBuilder.build());
                } else {
                    mBuilder.setContentTitle(Title)
                            //????????????
                            .setContentText(text)
                            //???????????????
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            //???????????????
                            .setSmallIcon(R.drawable.ic_index)
                            //??????????????????
                            .setWhen(System.currentTimeMillis())
                            //???????????????????????????
                            .setTicker("")
                            //???????????????????????????????????????????????????????????????????????????????????????
                            .setDefaults(Notification.DEFAULT_SOUND);
                    //??????????????????
                    notificationManager.notify(10, mBuilder.build());
                }

            }
        });
    }


    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(IndexActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    PagerAdapter IndexPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return IndexViewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(IndexViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(IndexViewList.get(position));
            return IndexViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] RechargeTitle = new String[]{"??????1","??????2", "??????3"};
            return RechargeTitle[position];
        }

    };



    PagerAdapter HisPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return HisViewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(HisViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(HisViewList.get(position));
            return HisViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] RechargeTitle = new String[]{"??????1","??????2", "??????3"};
            return RechargeTitle[position];
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /** ???????????????????????????????????????????????? */

    RxTimer NetTaskTimer = new RxTimer();   //?????????
    @Override
    public void afterTextChanged(Editable s) {

        Calendar calendar = Calendar.getInstance();
        System.out.println("??????????????????" + this.year + "-" + (this.month) + "-" + this.day);
        System.out.println("???????????????" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));

        IndexActivity.this.GetData(date.toString());
    }


    public class DataList{
        String dbName;  //???????????????
        String Name;    //????????????
        List<DbData> dbData=new ArrayList<>();  //???????????????????????????
        int ic; //??????
        ArrayList<Entry> value = new ArrayList<Entry>();
        int Threshold;
        int position;   //??????????????????
        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getThreshold() {
            return Threshold;
        }

        public void setThreshold(int threshold) {
            Threshold = threshold;
        }

        public int getIc() {
            return ic;
        }

        public void setIc(int ic) {
            this.ic = ic;
        }

        public ArrayList<Entry> getValue() {
            return value;
        }

        public void setValue(ArrayList<Entry> value) {
            this.value = value;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public List<DbData> getDbData() {
            return dbData;
        }

        public void setDbData(List<DbData> dbData) {
            this.dbData = dbData;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        @Override
        public String toString() {
            return "DataList{" +
                    "dbName='" + dbName + '\'' +
                    ", Name='" + Name + '\'' +
                    ", dbData=" + dbData +
                    ", ic=" + ic +
                    ", value=" + value +
                    ", Threshold=" + Threshold +
                    ", position=" + position +
                    '}';
        }
    }
}
