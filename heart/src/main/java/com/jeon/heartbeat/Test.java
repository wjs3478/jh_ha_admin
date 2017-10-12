package com.jeon.heartbeat;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
	
		ReadProperties.getInstance().setParameter("server1","3333");
		System.out.println(ReadProperties.getInstance().getParameter("server1"));
		ReadProperties.getInstance().setParameter("server2","3232");
		System.out.println(ReadProperties.getInstance().getParameter("server2"));
		
	}

}
