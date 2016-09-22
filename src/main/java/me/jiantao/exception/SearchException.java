package me.jiantao.exception;

public class SearchException extends RuntimeException{

	private static final long serialVersionUID = -4108742903093530376L;

	public SearchException(Exception e){
		super("索引操作异常", e);
	}
	
	public SearchException(Exception e, String msg){
		super(msg, e);
	}
}
