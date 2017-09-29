package com.moutum.mtplatform.common.utils;

/**
 * 数据字典 及 系统常量
 * @author liu.junyang
 */
public class ConstantUtils {
	public static final int RESOURCE_MENU = 0; // 菜单
	public final static String VACATION = "SVacation"; //业务类型为请假业务 TVacation,CounterSign,MailTest,WebserviceTest,SVacation
	public final static String MODEL_KEY = "modelkey"; //获取业务实体的KEY
	public final static double PERCENT = 0.6; //通过率
	
	/**
	 * SysOperLog(系统日志表)
	 */
	public static enum operStatus{
		SUCCESS("成功",1),
		FAIL("失败",2);
		
		private String name;
		private Integer value;
		
		private operStatus(String name,Integer value){
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}
	}
	
	
	
	/**
	 * TVacation(请假表) 请假状态
	 */
	public static enum vacationStatus{
		APPROVING("正在审批",1),
		PASSED("审批通过",2),
		NOT_PASSED("审批不通过",3);
		
		private String name;
		private Integer value;
		
		private vacationStatus(String name,Integer value){
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}
	}
	
	/**
	 * TVacation(请假表) 请假类型
	 */
	public static enum vacationType{
		PERSONAL("事假",1),
		SICK("病假",2);
		
		private String name;
		private Integer value;
		
		private vacationType(String name,Integer value){
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}
	}
}
