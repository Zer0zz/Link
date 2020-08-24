package com.self.link.mycenter.personal;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.R;
import com.self.link.base.PageObject;
import com.self.link.cancle_sijiao.CancleTheReservationActivity;
import com.self.link.coach.CoachActivity;
import com.self.link.fitness.coach.CoachModel;
import com.self.link.mycenter.adapter.MyPersonalRecyclerViewAdapter;
import com.self.link.mycenter.dto.PersonalCourseDto;
import com.self.link.utils.CommUtils;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.self.link.coach.CoachActivity.COACH_ID;
import static com.self.link.coach.CoachActivity.COACH_NAME;
import static com.self.link.fitness.FitnessActivity.FITNESS_ID;
import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;

/**
 * A fragment representing a list of Items.
 */
public class PersonalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    String fitnessId;
    String fitnessName;
    private static final String ARG_COLUMN_COUNT = "column-count";

    //位置信息
    Location mLocation = null;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout;

    List<PersonalCourseDto> datas;
    MyPersonalRecyclerViewAdapter adapter;

    private int mCurrentPage = 1;
    private int pageSize = 10;
    private int requestCode = 0x11;
    private int canclePosition;

    public PersonalFragment() {
    }


    public static PersonalFragment newInstance(int columnCount) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static PersonalFragment newInstance(Bundle args) {
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fitnessId = getArguments().getString(FITNESS_ID);
            fitnessName = getArguments().getString(FITNESS_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_list, container, false);
        EventBus.getDefault().register(this);
        Context context = view.getContext();
        swipeLayout = view.findViewById(R.id.swipeLayout);
        recyclerView = view.findViewById(R.id.frg_personal_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10, 10, 10, 10));
        initAdapter();
        initRecyclerView();
        getData(mCurrentPage);
        return view;
    }

    private void initAdapter() {
        swipeLayout.setEnabled(false);//禁用刷新
        swipeLayout.setOnRefreshListener(this);
        adapter = new MyPersonalRecyclerViewAdapter(R.layout.personal_item_layouy);

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
                //判断子控件
                if (view.getId() == R.id.coach_info_ll) {
                    PersonalCourseDto model = (PersonalCourseDto) adapter.getData().get(position);

                    Intent intent = new Intent(getContext(), CoachActivity.class);
                    intent.putExtra(FITNESS_NAME, model.fitnessName);
                    intent.putExtra(COACH_ID, model.coachId);
                    intent.putExtra(COACH_NAME, model.name);
                    startActivity(intent);

                } else if (view.getId() == R.id.operate_caurse_tv) {
                    PersonalCourseDto model = (PersonalCourseDto) adapter.getData().get(position);
                    if (model.isCancel == 1) {// 团课状态：1.已结束 2.进行中 3.未开始 ,
                        Toast.makeText(getContext(), "课程已结束！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (model.isCancel == 2) {
                        Toast.makeText(getContext(), "课程进行中，不可取消！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (model.isCancel == 3) {
                        canclePosition = position;
                        Intent intent = new Intent(getContext(), CancleTheReservationActivity.class);
                        intent.putExtra("personalCourseDto", model);
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

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10, 10, 10, 10));
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void getLocation(Location location) {
        mLocation = location;
    }

    private void getData(int pageNum) {

        new PersonalCourseModel().getPersonalCourseData(pageNum, pageSize, fitnessId, mLocation, new PersonalCourseModel.OnGetPersonalCourseDataListener() {
            @Override
            public void onComplited(boolean hasNextPage, List<PersonalCourseDto> personalCourseDtos, boolean isFirstPage, int currentPage) {
                if (isFirstPage) {
                    adapter.setNewData(personalCourseDtos);
                } else adapter.addData(personalCourseDtos);
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
    public void onRefresh() {
        mCurrentPage = 1;
        getData(mCurrentPage);
    }

    @Override
    public void onLoadMoreRequested() {
//        mCurrentPage++;
        getData(mCurrentPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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