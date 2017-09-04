package com.dreamgear.majiang.utils;

import java.lang.management.ManagementFactory;


import com.sun.management.OperatingSystemMXBean;

/**
 * 系统信息
 * @author admin
 * @date 2012-4-23 TODO
 * @see http://blog.csdn.net/huohu9080/article/details/6755913
 */
public class ServerInfo {
	long totalMemory;
	long maxMemory;
	long freeMemory;
	String osName;
	String osVersion;
	long physicalFree;
	long physicalTotal;
	long physicalUse;
	int totalThread;
	double cpuLoad;
	double sysLoad;
//	private static final Logger logger = LoggerFactory.getLogger(ServerInfo.class);

	public ServerInfo() {
		int kb = 1024;
		// 可以使用内存
		totalMemory = Runtime.getRuntime().totalMemory() / kb;
		// 最大可用内存
		maxMemory = Runtime.getRuntime().maxMemory() / kb;
		// 剩余内存
		freeMemory = Runtime.getRuntime().freeMemory() / kb;
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		// 系统名称
		osName = osmxb.getName();
		// 系统版本
		osVersion = osmxb.getVersion();
		// osmxb.getArch();// 系统架构
		// osmxb.getAvailableProcessors();// 可使用的处理器的数目
		// 总的物理内存
		physicalTotal = osmxb.getTotalPhysicalMemorySize() / kb;
		// 剩余的物理内存
		physicalFree = osmxb.getFreePhysicalMemorySize() / kb;
		// 已经使用的物理内存
		physicalUse = physicalTotal - physicalFree;
		// 获得线程总数
		ThreadGroup parentThread;
		for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
				.getParent() != null; parentThread = parentThread.getParent())
			;
		totalThread = parentThread.activeCount();
		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean( 
                OperatingSystemMXBean.class); 
// What % CPU load this current JVM is taking, from 0.0-1.0 
////system.out.println(osBean.getProcessCpuLoad()); 
		cpuLoad=osBean.getProcessCpuLoad();

// What % load the overall system is at, from 0.0-1.0 
////system.out.println(osBean.getSystemCpuLoad()); 
		sysLoad=osBean.getSystemCpuLoad();
	}

	public String toString() {
		String os = System.getProperty("os.name");
		StringBuffer sb=new StringBuffer();
		sb.append("os：" + os).append("\n");
		sb.append("version:"+osVersion).append("\n");
		sb.append("physicalFree:").append(physicalFree).append("k").append("\n");
		sb.append("physicalUse:").append(physicalUse ).append( "k").append("\n");
		sb.append("physicalTotal:").append( physicalTotal ).append( "k").append("\n");
		sb.append("totalThread:").append( totalThread).append("\n");
		sb.append("totalMemory:").append(totalMemory).append("\n");
		sb.append("freeMemory:").append(freeMemory).append("\n");
		sb.append("cpuLoad:").append(cpuLoad).append("\n");
		sb.append("sysLoad:").append(sysLoad).append("\n");
		sb.append("maxMemory:").append(maxMemory);
		return sb.toString();
	}
//	键	相关值的描述
//	java.version 	Java 运行时环境版本
//	java.vendor 	Java 运行时环境供应商
//	java.vendor.url 	Java 供应商的 URL
//	java.home 	Java 安装目录
//	java.vm.specification.version 	Java 虚拟机规范版本
//	java.vm.specification.vendor 	Java 虚拟机规范供应商
//	java.vm.specification.name 	Java 虚拟机规范名称
//	java.vm.version 	Java 虚拟机实现版本
//	java.vm.vendor 	Java 虚拟机实现供应商
//	java.vm.name 	Java 虚拟机实现名称
//	java.specification.version 	Java 运行时环境规范版本
//	java.specification.vendor 	Java 运行时环境规范供应商
//	java.specification.name 	Java 运行时环境规范名称
//	java.class.version 	Java 类格式版本号
//	java.class.path 	Java 类路径
//	java.library.path 	加载库时搜索的路径列表
//	java.io.tmpdir 	默认的临时文件路径
//	java.compiler 	要使用的 JIT 编译器的名称
//	java.ext.dirs 	一个或多个扩展目录的路径
//	os.name 	操作系统的名称
//	os.arch 	操作系统的架构
//	os.version 	操作系统的版本
//	file.separator 	文件分隔符（在 UNIX 系统中是“/”）
//	path.separator 	路径分隔符（在 UNIX 系统中是“:”）
//	line.separator 	行分隔符（在 UNIX 系统中是“/n”）
//	user.name 	用户的账户名称
//	user.home 	用户的主目录
//	user.dir 	用户的当前工作目录

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public long getPhysicalFree() {
		return physicalFree;
	}

	public void setPhysicalFree(long physicalFree) {
		this.physicalFree = physicalFree;
	}

	public long getPhysicalTotal() {
		return physicalTotal;
	}

	public void setPhysicalTotal(long physicalTotal) {
		this.physicalTotal = physicalTotal;
	}

	public long getPhysicalUse() {
		return physicalUse;
	}

	public void setPhysicalUse(long physicalUse) {
		this.physicalUse = physicalUse;
	}

	public int getTotalThread() {
		return totalThread;
	}

	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public void setCpuLoad(double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	public double getSysLoad() {
		return sysLoad;
	}

	public void setSysLoad(double sysLoad) {
		this.sysLoad = sysLoad;
	}
	
	
}
