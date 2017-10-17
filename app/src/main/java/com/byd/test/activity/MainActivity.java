package com.byd.test.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.byd.test.adapter.MyAdapter;
import com.byd.test.R;
import com.byd.test.model.User;
import com.byd.test.fragment.DHFragment;
import com.byd.test.fragment.JJFragment;
import com.byd.test.fragment.MineFragment;
import com.byd.test.fragment.WXFragment;
import com.byd.test.utils.HandleUtils;
import com.byd.test.utils.MyContstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    protected List<ImageView> mList;
    private ViewPager mViewpager;
    private MyAdapter mAdapter;
    HandleUtils utils;
    private BottomNavigationBar mBar;
    protected DHFragment dhFragment;
    protected JJFragment jjFragment;
    protected MineFragment mineFragment;
    protected WXFragment wxFragment;
    private static String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mList=new ArrayList<>();
       //使用glide去下载图片
        String[] array = getArray();
        Message message=new Message();
        //循环遍历将图片加入imageView并加入到集合中
        for (int i = 0; i < array.length; i++) {
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new ViewPager.LayoutParams());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mList.add(imageView);
            Picasso.with(this).load(array[i]).placeholder(R.mipmap.ic_launcher).into(imageView);
            message.arg1=i;
        }
        //初始化适配器，并发送Handler实现轮播
        mAdapter=new MyAdapter(mList,this);
        mViewpager.setAdapter(mAdapter);
        utils =new HandleUtils(mViewpager);
        utils.sendEmptyMessageDelayed(HandleUtils.MSG_UPDATE,HandleUtils.MSG_DELAY);
        initBar();
        initClick();
    }
    /*在此生命周期中去实例化bottomNavigationBar*/
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        persistToFile();
    }
    private void persistToFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user=new User(1,"james");
                File dir=new File(MyContstants.CACHE_File_PATH);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File cacheFile=new File(MyContstants.CHAPTER_2_PATH);
                ObjectOutputStream outputStream=null;
                try {
                    outputStream=new ObjectOutputStream(new FileOutputStream(cacheFile));
                    outputStream.writeObject(user);
                    Log.e(TAG,"persist user:"+user);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if(outputStream!=null){
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void initBar()
    {

        BadgeItem item=new BadgeItem();
        item.setHideOnSelect(false).setText("10").setBackgroundColor(R.color.colorBottom3)
                .setBorderWidth(0);

        mBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBar.addItem(new BottomNavigationItem(R.mipmap.tab11,"导航").setInactiveIconResource(R.mipmap.tab12).setActiveColorResource(R.color.colorAccent)
                )
                .addItem(new BottomNavigationItem(R.mipmap.tab22,"无线").setInactiveIconResource(R.mipmap.tab21).setActiveColorResource(R.color.colorBottom3)
                       )
                .addItem(new BottomNavigationItem(R.mipmap.tab31,"家居").setInactiveIconResource(R.mipmap.tab32).setActiveColorResource(R.color.colorPrimary)
                       )
                .addItem(new BottomNavigationItem(R.mipmap.tab41,"我的").setInactiveIconResource(R.mipmap.tab42).setActiveColorResource(R.color.colorBottom4)
                       )
                .setFirstSelectedPosition(0)
                .initialise();
        mBar.setTabSelectedListener(this);
        setDefaultFragment();
    }
    //设置默认的fragment
    private void setDefaultFragment() {
        FragmentManager manager=this.getSupportFragmentManager();
        //开启事物
        FragmentTransaction transaction=manager.beginTransaction();
        dhFragment=DHFragment.newInstance("点我开启Second进程");
        transaction.replace(R.id.fragment_container,dhFragment);
        transaction.commit();
    }

    //初始化控件
    private void initView() {
        mViewpager= (ViewPager) findViewById(R.id.viewPager);
        mBar= (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }
    //监听事件
    private void initClick() {
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                utils.sendMessage(Message.obtain(utils, utils.MSG_PAGE, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        utils.sendEmptyMessage(utils.MSG_KEEP);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        utils.sendEmptyMessageDelayed(utils.MSG_UPDATE,
                                utils.MSG_DELAY);
                }
            }
        });
    }
    //得到图片的地址的数组
    public String[] getArray() {
        String array[]=new String[]{
                "http://bpic.588ku.com/back_pic/04/54/71/42586373fac94c0.jpg!/fw/400/quality/90/unsharp/true/compress/true",
                "http://bpic.588ku.com/back_pic/04/30/30/055840f125c5f44.jpg!/fw/400/quality/90/unsharp/true/compress/true",
                "http://bpic.588ku.com/back_pic/04/70/01/835898452e5c751.jpg!/fw/400/quality/90/unsharp/true/compress/true"
        };
        return array;
    }
   /*BottomNavigationBar的监听事件*/
    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (dhFragment == null) {
                    dhFragment = DHFragment.newInstance("位置");
                }
                transaction.replace(R.id.fragment_container, dhFragment);
                break;
            case 1:
                if (jjFragment == null) {
                    jjFragment = JJFragment.newInstance("发现");
                }
                transaction.replace(R.id.fragment_container, jjFragment);
                break;
            case 2:
                if (wxFragment == null) {
                    wxFragment = wxFragment.newInstance("爱好");
                }
                transaction.replace(R.id.fragment_container, wxFragment);
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = MineFragment.newInstance("图书");
                }
                transaction.replace(R.id.fragment_container, mineFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }
    @Override
    public void onTabReselected(int position) {

    }
}
