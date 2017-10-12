package com.jeon.heartbeat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;



public class ReadProperties {

	private volatile static ReadProperties instacne=null;
	
	private static Properties pro;
	private static InputStream fis; 
	String path="";	//소스파일 경로
	
	//FileInputStream fis=new FileInputStream(path+"");
	//BufferedInputStream bis=new BufferedInputStream(fis);
	
	
	public ReadProperties()
	{
		pro=new Properties();
		//String Path=;	//소스파일 경로
		 
		try {
			fis = getClass().getResourceAsStream("/config/spring/node.properties");
			//BufferedInputStream bis=new BufferedInputStream(fis);
			pro.load(fis);
			//System.out.println(pro.getProperty("server1"));
			//pro.setProperty("server1", "3232");
			//System.out.println(pro.getProperty("server1"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
				
	}
	
	public static ReadProperties getInstance(){
		if(instacne==null)
		{
			synchronized(ReadProperties.class){
				if(instacne==null)
				{
					instacne=new ReadProperties();
				}
			}
		}
		return instacne;
	}
	
	
	public String getParameter(String key)
	{
		return pro.getProperty(key);
	}
	
	public int setParameter(String key, String obj)
	{
		try {
			String setFile=getClass().getResource("/config/spring/node.properties").getPath();
			System.out.println(setFile);
			FileOutputStream out=new FileOutputStream(setFile);
			pro.setProperty(key,obj);	
			pro.store(out, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		
		return 0;
	}
	
	public static void reload()
	{
		try {
			pro.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
