package me.jiantao.po;

public class Me {
	
	private String name="建涛同学";
	
	public static volatile Me me;
	
	private Me(){}
	
	public static Me getInstance(){
		if(me==null){
			synchronized (Me.class) {
				if(me==null){
					me = new Me();
				}
			}
		}
		return me;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
