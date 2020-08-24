package com.leconssoft.common.pageMvp;

import com.leconssoft.common.BaseMvp.model.BaseModel;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/9/27 9:23
 * @Description
 */
public abstract class BasePageModel extends BaseModel {
    public int pageNo = 0;
    public abstract void loadData(int page, String url);

    public abstract int reqPageNo();


}
