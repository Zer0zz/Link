package com.leconssoft.common.NetService.retrofit;


import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2019/1/15 10:48
 * @Description
 */
public class GspInfo {

    /**
     * code : 200
     * msg : 成功
     * data : {"pageSize":15,"pageIndex":1,"pages":1,"count":1,"data":[{"JYFW":"建筑机械、桩工设备、新型桩工设备的研发、设计、制造、销售及生产服务；土建工程的技术研发、技术咨询、技术服务、技术转让；建筑设备安装、工程机械租赁；岩土新技术设备及工程施工；货物进出口、技术进出口、代理进出口（国家禁止的除外）；预应力混凝土管桩（方桩）、钢筋混凝土方桩、水泥构件生产及销售。（涉及许可经营项目，应取得相关部门许可后方可经营）****","DJJG":"429006","QYLXZL":"1153","YZBM":"431700","HYGBLB":"3431","JYDZ":"天门市经济开发区西湖路以东,(爱士康以南)","JYQXQ":"2017-05-04 00:00:00","JYLB":null,"TYSHXYDM":"91429006MA48YR8NX8","FDDBR":"孙伟","HYGBML":"C","LXDH":"18607115130","HZRQ":"2017-11-22 15:25:14","CLRQ":"2017-05-04 00:00:00","QYMC":"湖北谦诚智能桩工有限公司","JYQXZ":"2037-05-03 00:00:00","ZCZB":"4000.000000","SJZB":"4000.000000","ZS":"天门市经济开发区西湖路以东,(爱士康以南)","JYCS":"天门市经济开发区西湖路以东,(爱士康以南)","ZCH":"429006000161431","JYFS":null}]}
     */

    private String code;
    private String msg;
    private DataBeanX data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * pageSize : 15
         * pageIndex : 1
         * pages : 1
         * count : 1
         * data : [{"JYFW":"建筑机械、桩工设备、新型桩工设备的研发、设计、制造、销售及生产服务；土建工程的技术研发、技术咨询、技术服务、技术转让；建筑设备安装、工程机械租赁；岩土新技术设备及工程施工；货物进出口、技术进出口、代理进出口（国家禁止的除外）；预应力混凝土管桩（方桩）、钢筋混凝土方桩、水泥构件生产及销售。（涉及许可经营项目，应取得相关部门许可后方可经营）****","DJJG":"429006","QYLXZL":"1153","YZBM":"431700","HYGBLB":"3431","JYDZ":"天门市经济开发区西湖路以东,(爱士康以南)","JYQXQ":"2017-05-04 00:00:00","JYLB":null,"TYSHXYDM":"91429006MA48YR8NX8","FDDBR":"孙伟","HYGBML":"C","LXDH":"18607115130","HZRQ":"2017-11-22 15:25:14","CLRQ":"2017-05-04 00:00:00","QYMC":"湖北谦诚智能桩工有限公司","JYQXZ":"2037-05-03 00:00:00","ZCZB":"4000.000000","SJZB":"4000.000000","ZS":"天门市经济开发区西湖路以东,(爱士康以南)","JYCS":"天门市经济开发区西湖路以东,(爱士康以南)","ZCH":"429006000161431","JYFS":null}]
         */

        private int pageSize;
        private int pageIndex;
        private int pages;
        private int count;
        private List<DataBean> data;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * JYFW : 建筑机械、桩工设备、新型桩工设备的研发、设计、制造、销售及生产服务；土建工程的技术研发、技术咨询、技术服务、技术转让；建筑设备安装、工程机械租赁；岩土新技术设备及工程施工；货物进出口、技术进出口、代理进出口（国家禁止的除外）；预应力混凝土管桩（方桩）、钢筋混凝土方桩、水泥构件生产及销售。（涉及许可经营项目，应取得相关部门许可后方可经营）****
             * DJJG : 429006
             * QYLXZL : 1153
             * YZBM : 431700
             * HYGBLB : 3431
             * JYDZ : 天门市经济开发区西湖路以东,(爱士康以南)
             * JYQXQ : 2017-05-04 00:00:00
             * JYLB : null
             * TYSHXYDM : 91429006MA48YR8NX8
             * FDDBR : 孙伟
             * HYGBML : C
             * LXDH : 18607115130
             * HZRQ : 2017-11-22 15:25:14
             * CLRQ : 2017-05-04 00:00:00
             * QYMC : 湖北谦诚智能桩工有限公司
             * JYQXZ : 2037-05-03 00:00:00
             * ZCZB : 4000.000000
             * SJZB : 4000.000000
             * ZS : 天门市经济开发区西湖路以东,(爱士康以南)
             * JYCS : 天门市经济开发区西湖路以东,(爱士康以南)
             * ZCH : 429006000161431
             * JYFS : null
             */

            private String JYFW;
            private String DJJG;
            private String QYLXZL;
            private String YZBM;
            private String HYGBLB;
            private String JYDZ;
            private String JYQXQ;
            private Object JYLB;
            private String TYSHXYDM;
            private String FDDBR;
            private String HYGBML;
            private String LXDH;
            private String HZRQ;
            private String CLRQ;
            private String QYMC;
            private String JYQXZ;
            private String ZCZB;
            private String SJZB;
            private String ZS;
            private String JYCS;
            private String ZCH;
            private Object JYFS;

            public String getJYFW() {
                return JYFW;
            }

            public void setJYFW(String JYFW) {
                this.JYFW = JYFW;
            }

            public String getDJJG() {
                return DJJG;
            }

            public void setDJJG(String DJJG) {
                this.DJJG = DJJG;
            }

            public String getQYLXZL() {
                return QYLXZL;
            }

            public void setQYLXZL(String QYLXZL) {
                this.QYLXZL = QYLXZL;
            }

            public String getYZBM() {
                return YZBM;
            }

            public void setYZBM(String YZBM) {
                this.YZBM = YZBM;
            }

            public String getHYGBLB() {
                return HYGBLB;
            }

            public void setHYGBLB(String HYGBLB) {
                this.HYGBLB = HYGBLB;
            }

            public String getJYDZ() {
                return JYDZ;
            }

            public void setJYDZ(String JYDZ) {
                this.JYDZ = JYDZ;
            }

            public String getJYQXQ() {
                return JYQXQ;
            }

            public void setJYQXQ(String JYQXQ) {
                this.JYQXQ = JYQXQ;
            }

            public Object getJYLB() {
                return JYLB;
            }

            public void setJYLB(Object JYLB) {
                this.JYLB = JYLB;
            }

            public String getTYSHXYDM() {
                return TYSHXYDM;
            }

            public void setTYSHXYDM(String TYSHXYDM) {
                this.TYSHXYDM = TYSHXYDM;
            }

            public String getFDDBR() {
                return FDDBR;
            }

            public void setFDDBR(String FDDBR) {
                this.FDDBR = FDDBR;
            }

            public String getHYGBML() {
                return HYGBML;
            }

            public void setHYGBML(String HYGBML) {
                this.HYGBML = HYGBML;
            }

            public String getLXDH() {
                return LXDH;
            }

            public void setLXDH(String LXDH) {
                this.LXDH = LXDH;
            }

            public String getHZRQ() {
                return HZRQ;
            }

            public void setHZRQ(String HZRQ) {
                this.HZRQ = HZRQ;
            }

            public String getCLRQ() {
                return CLRQ;
            }

            public void setCLRQ(String CLRQ) {
                this.CLRQ = CLRQ;
            }

            public String getQYMC() {
                return QYMC;
            }

            public void setQYMC(String QYMC) {
                this.QYMC = QYMC;
            }

            public String getJYQXZ() {
                return JYQXZ;
            }

            public void setJYQXZ(String JYQXZ) {
                this.JYQXZ = JYQXZ;
            }

            public String getZCZB() {
                return ZCZB;
            }

            public void setZCZB(String ZCZB) {
                this.ZCZB = ZCZB;
            }

            public String getSJZB() {
                return SJZB;
            }

            public void setSJZB(String SJZB) {
                this.SJZB = SJZB;
            }

            public String getZS() {
                return ZS;
            }

            public void setZS(String ZS) {
                this.ZS = ZS;
            }

            public String getJYCS() {
                return JYCS;
            }

            public void setJYCS(String JYCS) {
                this.JYCS = JYCS;
            }

            public String getZCH() {
                return ZCH;
            }

            public void setZCH(String ZCH) {
                this.ZCH = ZCH;
            }

            public Object getJYFS() {
                return JYFS;
            }

            public void setJYFS(Object JYFS) {
                this.JYFS = JYFS;
            }
        }
    }
}
