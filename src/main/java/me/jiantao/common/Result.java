package me.jiantao.common;

public class Result {
	
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAIL = 0;
	
	private static Result success;
	private static Result fail;
	
	static{
		success = new Result(STATUS_SUCCESS, "成功");
		fail = new Result(STATUS_FAIL, "失败");
	}

	// 状态码
	private int status;
	// 消息
	private String msg;
	
	public Result(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	
	public static Result success(){
		return success;
	}
	
	public static Result fail(){
		return fail;
	}
	
	public static Result fail(String msg){
		return new Result(STATUS_FAIL, msg);
	}
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{status:").append(status);
		sb.append(",msg:").append(msg).append("}");
		return sb.toString();
	}
	
}
