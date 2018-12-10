package com.hzgc.system.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 物理机器
 * 
 * @author liang
 * @email 1992lcg@163.com
 * @date 2018-12-07 18:35:53
 */
public class MachineDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键Id
	private Integer machineId;
	//机器IP
	private String machineIp;
	//主机名
	private String machineName;
	//内存
	private String machineMemory;
	//显卡
	private String machineNvidia;
	//创建用户id
	private Long userIdCreate;
	//创建时间
	private Date gmtCreate;
	//修改时间
	private Date gmtModified;
	//cpu
	private String cpu;
	//cpu数量
	private String cpuNumber;
	//带宽
	private String broadBand;

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getCpuNumber() {
		return cpuNumber;
	}

	public void setCpuNumber(String cpuNumber) {
		this.cpuNumber = cpuNumber;
	}

	public String getBroadBand() {
		return broadBand;
	}

	public void setBroadBand(String broadBand) {
		this.broadBand = broadBand;
	}

	/**
	 * 设置：主键Id
	 */
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	/**
	 * 获取：主键Id
	 */
	public Integer getMachineId() {
		return machineId;
	}
	/**
	 * 设置：机器IP
	 */
	public void setMachineIp(String machineIp) {
		this.machineIp = machineIp;
	}
	/**
	 * 获取：机器IP
	 */
	public String getMachineIp() {
		return machineIp;
	}
	/**
	 * 设置：主机名
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	/**
	 * 获取：主机名
	 */
	public String getMachineName() {
		return machineName;
	}
	/**
	 * 设置：内存
	 */
	public void setMachineMemory(String machineMemory) {
		this.machineMemory = machineMemory;
	}
	/**
	 * 获取：内存
	 */
	public String getMachineMemory() {
		return machineMemory;
	}
	/**
	 * 设置：显卡
	 */
	public void setMachineNvidia(String machineNvidia) {
		this.machineNvidia = machineNvidia;
	}
	/**
	 * 获取：显卡
	 */
	public String getMachineNvidia() {
		return machineNvidia;
	}
	/**
	 * 设置：创建用户id
	 */
	public void setUserIdCreate(Long userIdCreate) {
		this.userIdCreate = userIdCreate;
	}
	/**
	 * 获取：创建用户id
	 */
	public Long getUserIdCreate() {
		return userIdCreate;
	}
	/**
	 * 设置：创建时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}
	/**
	 * 设置：修改时间
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getGmtModified() {
		return gmtModified;
	}
}
