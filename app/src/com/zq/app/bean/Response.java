/**
 * 
 */
package com.zq.app.bean;

/**
 * <p>Title:Response</p>
 * <p>Description:</p>
 * @author 郑强
 * @date 2016年6月20日
 */
public class Response {
	
	/**
	 * 状态
	 */
	private boolean state;
	
	/**
	 * 信息
	 */
	private String msg;
	
	/**
	 * 内容
	 */
	private Object data;
	
	/**
	 * 总条数
	 */
	private int total;

	/**
	 * @return the state
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	
}
