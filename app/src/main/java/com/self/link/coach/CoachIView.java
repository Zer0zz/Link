package com.self.link.coach;

import com.leconssoft.common.BaseMvp.view.BaseView;
import com.self.link.coach.body.CoachScheduleBody;

import java.util.List;

/**
 * description：
 * author：Administrator on 2020/5/31 10:32
 */
public interface CoachIView extends BaseView {
    void setScheduleData(List<CoachScheduleBody> coachSchedules);
}
