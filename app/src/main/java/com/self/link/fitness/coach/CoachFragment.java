package com.self.link.fitness.coach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.R;
import com.self.link.base.PageObject;
import com.self.link.coach.CoachActivity;
import com.self.link.fitness.adapter.MyCoachRecyclerViewAdapter;
import com.self.link.fitness.body.CoachBody;
import com.self.link.selectseat.SelectSeatActivity;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;

import java.util.List;

import static com.self.link.fitness.FitnessActivity.FITNESS_ID;
import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;

/**
 * A fragment representing a list of Items.
 */
public class CoachFragment extends Fragment {

    private static final String COACH_ID = "CoachId";
    private static final String COACH_NAME = "CoachName";

    String fitnessId;
    String fitnessName;
    MyCoachRecyclerViewAdapter adapter;
    List<CoachBody> datas = null;

    public CoachFragment() {

    }

    public static CoachFragment newInstance(String fitnessId) {
        CoachFragment fragment = new CoachFragment();
        Bundle args = new Bundle();
        args.putString(FITNESS_ID, fitnessId);
        fragment.setArguments(args);
        return fragment;
    }

    public static CoachFragment newInstance(Bundle args) {
        CoachFragment fragment = new CoachFragment();
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


        View view = inflater.inflate(R.layout.fragment_coach_list, container, false);

//        if (view instanceof RecyclerView) {
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.frg_coach_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10, 10, 10, 10));
        adapter = new MyCoachRecyclerViewAdapter(context);
        recyclerView.setAdapter(adapter);
        getData(1);
        adapter.setOnItemClickListener((position, model, holder) -> {
            Intent intent = new Intent(context, CoachActivity.class);
            intent.putExtra(COACH_ID, model.id);
            intent.putExtra(COACH_NAME, model.name);
            intent.putExtra(FITNESS_NAME, fitnessName);//COURSE_NAME

            startActivity(intent);
        });
//        }
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
    }


    private void getData(int pageNum) {

        new CoachModel().getCoachData(fitnessId, pageNum, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                try {
                    BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                    if (response.isSuccess()){
                        PageObject pageObject = JSONObject.parseObject(response.getRe(), PageObject.class);
                        datas = JSONObject.parseArray(pageObject.list, CoachBody.class);
                        if (adapter != null) {
                            adapter.addData(datas);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "解析数据错误:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", e.getMessage());
                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }

}