package com.self.link.selectseat;

import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * description：
 * author：Administrator on 2020/5/30 22:12
 */
public interface SelectSeatIView extends BaseView {

    void cancleSuccess(String seatSerialNumber);
    void selectSuccess(String seatSerialNumber);
}
