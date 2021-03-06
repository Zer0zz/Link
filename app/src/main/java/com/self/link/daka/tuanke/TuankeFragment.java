package com.self.link.daka.tuanke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.self.link.R;
import com.self.link.daka.sijiao.SijiaoModel;
import com.self.link.daka.sijiao.dto.CoachFitnessPunchListDto;
import com.self.link.daka.tuanke.dto.CoursePunchListDto;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TuankeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private int mCurrentPage = 1;
    private int pageSize = 10;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout;

    MyTuankeRecyclerViewAdapter adapter;
    //位置信息
    Location mLocation = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TuankeFragment() {
    }

    public static TuankeFragment newInstance() {
        TuankeFragment fragment = new TuankeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static TuankeFragment newInstance(Bundle args) {
        TuankeFragment fragment = new TuankeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            //获取初始参数
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void getLocation(Location location) {
        mLocation = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuanke_list, container, false);
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

    private void getData(int pageNum) {

        new TuankeModel().getPunchData(pageNum, pageSize, mLocation, new TuankeModel.OnGetDataListener() {
            @Override
            public void onComplited(boolean hasNextPage, List<CoursePunchListDto> punchListDtos, boolean isFirstPage, int currentPage) {
                if (isFirstPage) {
                    adapter.setNewData(punchListDtos);
                } else adapter.addData(punchListDtos);
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

    private void initAdapter() {
        swipeLayout.setEnabled(false);//禁用刷新
        swipeLayout.setOnRefreshListener(this);
        adapter = new MyTuankeRecyclerViewAdapter(R.layout.fragment_tuanke_item);

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
//                //判断子控件
//                if (view.getId() == R.id.coach_info_ll) {
////                    Toast.makeText(getContext(), "教练详情待开发中...", Toast.LENGTH_SHORT).show();
//                    PersonalCourseDto model = (PersonalCourseDto) adapter.getData().get(position);
//                    Intent intent = new Intent(getContext(), CoachActivity.class);
//                    intent.putExtra(FITNESS_NAME, model.fitnessName);
//                    intent.putExtra(COACH_ID, model.coachId);
//                    intent.putExtra(COACH_NAME, model.name);
//                    startActivity(intent);
//                } else
                if (view.getId() == R.id.oper_tv) {
                    CoursePunchListDto punchListDto = (CoursePunchListDto) adapter.getData().get(position);
                   /*fitnessId (string, optional): 健身房id ,
                     id (string, optional): 课程id/教练排期id ,
                     latitude (number, optional): 纬度 ,
                    longitude (number, optional): 经度 ,
                    type (string, optional): 类型：1.团课 2.私教*/
                    String fitnessId = punchListDto.fitnessId;
                    String id = punchListDto.courseId;
                    new TuankeModel().punchCard(fitnessId, id, mLocation, new SijiaoModel.PunchCardListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void oncomplited() {//打卡成功
                            TextView operate_caurse_tv = (TextView) adapter.getViewByPosition(position, R.id.oper_tv);
                            operate_caurse_tv.setBackgroundColor(R.color.oper_bg_grey);
                            operate_caurse_tv.setText("已打卡");
                        }

                        @Override
                        public void onfailed(String err) {//打卡失败
                            new AlertDialog.Builder(getContext())
                                    .setTitle("打卡失败")
                                    .setMessage(err)
                                    .setIcon(R.mipmap.cuowu_tanchuang)
                                    .setNegativeButton("返回打卡", null)
                                    .show();
                        }
                    });
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}