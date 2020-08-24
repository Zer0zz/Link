package com.self.link.fitness.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.R;
import com.self.link.base.PageObject;
import com.self.link.fitness.FitnessActivity;
import com.self.link.fitness.adapter.MyCourseRecyclerViewAdapter;
import com.self.link.fitness.body.Attachment;
import com.self.link.fitness.body.CoachBody;
import com.self.link.fitness.body.CourseBody;
import com.self.link.selectseat.SelectSeatActivity;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.self.link.fitness.FitnessActivity.FITNESS_ID;
import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;

public class CourseFragment extends Fragment {

    private static final String COURSE_ID = "courseId";
    private static final String COURSE_NAME = "courseName";
    String fitnessId;
    String fitnessName;
    MyCourseRecyclerViewAdapter adapter;

    public CourseFragment() {
    }

    public static CourseFragment newInstance(String fitnessId) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(FITNESS_ID, fitnessId);
        fragment.setArguments(args);
        return fragment;
    }

    public static CourseFragment newInstance(Bundle args) {
        CourseFragment fragment = new CourseFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.frg_course_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10, 10, 10, 10));
        adapter = new MyCourseRecyclerViewAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyCourseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, CourseBody model, MyCourseRecyclerViewAdapter.ViewHolder holder) {
                Intent intent = new Intent(context, SelectSeatActivity.class);
                intent.putExtra(COURSE_ID, model.id);
                intent.putExtra(COURSE_NAME, model.name);
                intent.putExtra(FITNESS_NAME, fitnessName);//COURSE_NAME
                intent.putExtra(FITNESS_ID, fitnessId);//COURSE_NAME
                startActivity(intent);
            }
        });
        getData(1);
        return view;
    }

    List<CourseBody> datas = null;

    private void getData(int pageNum) {
        new CourseModel().getCourseData(fitnessId, pageNum, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                try {
                    BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                    PageObject pageObject = JSONObject.parseObject(response.getRe(), PageObject.class);
                    datas = JSONObject.parseArray(pageObject.list, CourseBody.class);
                    if (adapter != null) {
                        adapter.addData(datas);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "解析数据错误:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", e.getMessage());
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                Toast.makeText(getContext(), "获取课程信息失败：" + err, Toast.LENGTH_SHORT).show();

            }
        });
    }
}