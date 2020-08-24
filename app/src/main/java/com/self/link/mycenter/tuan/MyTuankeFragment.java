package com.self.link.mycenter.tuan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.self.link.R;
import com.self.link.cancle_tuanke.CancleTuankeActivity;
import com.self.link.daka.sijiao.SijiaoModel;
import com.self.link.daka.sijiao.dto.CoachFitnessPunchListDto;
import com.self.link.daka.tuanke.MyTuankeRecyclerViewAdapter;
import com.self.link.daka.tuanke.TuankeModel;
import com.self.link.daka.tuanke.dto.CoursePunchListDto;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.self.link.fitness.FitnessActivity.FITNESS_ID;

/**
 * create an instance of this fragment.
 */
public class MyTuankeFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int mCurrentPage = 1;
    private int pageSize = 10;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout;
    private static final String FITNESSID = "fitnessId";

    private int requestCode = 0x12;
    private MyTuankeAdapter adapter;
    //位置信息
    Location mLocation = null;
    int canclePosition;
    String fitnessId;

    public MyTuankeFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyTuankeFragment.
     */
    public static MyTuankeFragment newInstance(String fitnessId) {
        MyTuankeFragment fragment = new MyTuankeFragment();
        Bundle args = new Bundle();
        args.putString(FITNESSID, fitnessId);
        fragment.setArguments(args);
        return fragment;
    }

    public static MyTuankeFragment newInstance(Bundle args) {
        MyTuankeFragment fragment = new MyTuankeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            //获取初始参数
            fitnessId = getArguments().getString(FITNESS_ID);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void getLocation(Location location) {
        mLocation = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tuanke, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10, 10, 10, 10));
        initAdapter();
        recyclerView.setAdapter(adapter);
        getData(mCurrentPage);
        return view;
    }

    private void initAdapter() {
        swipeLayout.setEnabled(false);//禁用刷新
        swipeLayout.setOnRefreshListener(this);
        adapter = new MyTuankeAdapter(R.layout.fragment_my_tuanke_item);

        /** --------------------------- 动画效果 --------------------------- **/
        //开启动画效果
        adapter.openLoadAnimation();
        //设置动画效果
        /**
         * 渐显 ALPHAIN
         * 缩放 SCALEIN
         * 从下到上 SLIDEIN_BOTTOM
         * 从左到右 SLIDEIN_LEFT
         * 从右到左 SLIDEIN_RIGHT
         */
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//        mAdapter.setNewData(mData);
        //设置自定义加载更多布局
//        mAdapter.setLoadMoreView(customView);
        /** --------------------------- 点击事件 --------------------------- **/

        //设置Item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("onItemClick：" + position);

            }
        });
        //设置Item长按事件
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("onItemLongClick：" + position);
                return false;
            }
        });
        //设置Item中子控件点击事件
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.oper_tv) {
                    CoursePunchListDto punchListDto = (CoursePunchListDto) adapter.getData().get(position);

//                    String fitnessId = punchListDto.fitnessId;
//                    String courseId = punchListDto.courseId;
//                    String courseSeatId = punchListDto.courseSeatId;

                    if (punchListDto.isCancel == 1) {// 团课状态：1.已结束 2.进行中 3.未开始 ,
                        Toast.makeText(getContext(), "课程已结束！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (punchListDto.isCancel == 2) {
                        Toast.makeText(getContext(), "课程进行中，不可取消！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (punchListDto.isCancel == 3) {
                        canclePosition = position;
                        Intent intent = new Intent(getContext(), CancleTuankeActivity.class);
                        intent.putExtra("CoursePunchListDto", punchListDto);
                        startActivityForResult(intent, requestCode);
                    }
                  
                }
            }
        });

        /** --------------------------- 加载更多 --------------------------- **/

        //设置加载更多
        adapter.setOnLoadMoreListener(this, recyclerView);
        //默认第一次加载会进入回调，如果不需要可以配置
        adapter.disableLoadMoreIfNotFullPage();

        //当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        adapter.setPreLoadNumber(1);
    }

    private void getData(int pageNum) {
        new MytuankModel().getMytuankeData(pageNum, pageSize, fitnessId, mLocation, new MytuankModel.OnGetMyCourseDataListener() {
            @Override
            public void onComplited(boolean hasNextPage, List<CoursePunchListDto> coursePunchDtos, boolean isFirstPage, int currentPage) {
                if (isFirstPage) {
                    adapter.setNewData(coursePunchDtos);
                } else adapter.addData(coursePunchDtos);
                if (!hasNextPage) {
                    adapter.loadMoreEnd();
                }
                mCurrentPage = currentPage + 1;
            }

            @Override
            public void onfailed(String errMsg) {
                Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        getData(mCurrentPage);
    }

    @Override
    public void onLoadMoreRequested() {
        getData(mCurrentPage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == this.requestCode) {
            adapter.remove(canclePosition);
        }
    }
}