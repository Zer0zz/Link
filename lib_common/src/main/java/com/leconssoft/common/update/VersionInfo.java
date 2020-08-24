package com.leconssoft.common.update;

import java.io.Serializable;

public class VersionInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long id;
	
	public String appKey;
	
	public String appValue;
	
	public String creater;
	/** 是否有新的版本？ 1=true或者2=false */
	public String hasNewVersion;

	/** 新的版本号 如果hasNewVersion为true，那么返回新版本的版本号 */
	public long versionCode;
	
	/** 版本名称*/
	public String versionName;

	/** 程序下载路径 如果hasNewVersion为true，那么返回新的apk的下载地址， 如果hasNewVersion为false，则为空 */
	public String updatePath;

	/** 新版本的更新描述 如果hasNewVersion为true，那么返回新版本的更新描述，如果hasNewVersion为false，则为空 */
	public String versionDesc;

	/** 新版本发布日期 YYYYMMDDHHMMSS */
	public String publishDate;

	/** 版本类别 0为测试版，1为正式版 */
	public String versionType;

	/** 客户端是否需要强制更新 2=否 1=是 */
	public String forceUpdate;

	// ////旧的字段/////////////////////////////////////
	public String localVersion;
	/**
	 * 最低版本号
	 */
	public long supportAndoridVersion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppValue() {
		return appValue;
	}

	public void setAppValue(String appValue) {
		this.appValue = appValue;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getHasNewVersion() {
		return hasNewVersion;
	}

	public void setHasNewVersion(String hasNewVersion) {
		this.hasNewVersion = hasNewVersion;
	}

	public long getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(long versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUpdatePath() {
		return updatePath;
	}

	public void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(String forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public String getLocalVersion() {
		return localVersion;
	}

	public void setLocalVersion(String localVersion) {
		this.localVersion = localVersion;
	}

	public long getSupportAndoridVersion() {
		return supportAndoridVersion;
	}

	public void setSupportAndoridVersion(long supportAndoridVersion) {
		this.supportAndoridVersion = supportAndoridVersion;
	}

}
