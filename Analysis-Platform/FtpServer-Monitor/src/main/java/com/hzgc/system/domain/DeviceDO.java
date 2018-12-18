package com.hzgc.system.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备(device)
 * 
 * @author liang
 * @date 2018-12-11 14:15:03
 */
public class DeviceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键Id
	private String deviceId;
	//备名称
	private String deviceName;
	//厂商类型：1 海康 2 大华
	private String deviceManufacturer;
	//设备号
	private String deviceSn;
	//设备类型
	private String deviceType;
	//客户端IP
	private String clientIp;
	//客户端主机名
	private String clientHostname;
	//所属容器
	private String container;
	//最后上报数据的时间
	private Date lastDataTime;


	public DeviceDO (){}

	public DeviceDO(String deviceId, String deviceName, String deviceManufacturer, String deviceSn, String deviceType, String clientIp, String clientHostname, String container, Date lastDataTime) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.deviceManufacturer = deviceManufacturer;
		this.deviceSn = deviceSn;
		this.deviceType = deviceType;
		this.clientIp = clientIp;
		this.clientHostname = clientHostname;
		this.container = container;
		this.lastDataTime = lastDataTime;
	}

	/**
	 * 设置：主键ID(UUID)
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：主键ID(UUID)
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：备名称
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	/**
	 * 获取：备名称
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/**
	 * 设置：厂商类型：1 海康 2 大华
	 */
	public void setDeviceManufacturer(String deviceManufacturer) {
		this.deviceManufacturer = deviceManufacturer;
	}
	/**
	 * 获取：厂商类型：1 海康 2 大华
	 */
	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}
	/**
	 * 设置：设备号
	 */
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	/**
	 * 获取：设备号
	 */
	public String getDeviceSn() {
		return deviceSn;
	}
	/**
	 * 设置：设备类型
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * 获取：设备类型
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * 设置：客户端IP
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	/**
	 * 获取：客户端IP
	 */
	public String getClientIp() {
		return clientIp;
	}
	/**
	 * 设置：客户端主机名
	 */
	public void setClientHostname(String clientHostname) {
		this.clientHostname = clientHostname;
	}
	/**
	 * 获取：客户端主机名
	 */
	public String getClientHostname() {
		return clientHostname;
	}
	/**
	 * 设置：所属容器
	 */
	public void setContainer(String container) {
		this.container = container;
	}
	/**
	 * 获取：所属容器
	 */
	public String getContainer() {
		return container;
	}
	/**
	 * 设置：最后上报数据的时间
	 */
	public void setLastDataTime(Date lastDataTime) {
		this.lastDataTime = lastDataTime;
	}
	/**
	 * 获取：最后上报数据的时间
	 */
	public Date getLastDataTime() {
		return lastDataTime;
	}
}
