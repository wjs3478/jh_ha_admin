package com.jeon.heartbeat;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	 
	@Autowired
	AbstractDAO sql;
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public void ajax(HttpServletRequest request,  HttpServletResponse response) {
		
		System.out.println("�ȳ��ϼ��� �����ؿ� ���־��");
		String sqls=request.getParameter("sql");
		
		System.out.println(sqls);
		
		Enumeration paramNames = request.getParameterNames();

		// ������ ��
		Map paramMap = new HashMap();

		// �ʿ� ����
		while(paramNames.hasMoreElements()) {
		String name	= paramNames.nextElement().toString();
		String value	= request.getParameter(name);

			paramMap.put(name, value);
		}
		
		
		System.out.println(paramMap.get("id"));
		List<HashMap> result=sql.selectList(sqls, paramMap);
		
		System.out.println(result);
		
		try {
			response.getWriter().println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
	}
	
	
	@RequestMapping(value="/getJsonByMap")
	public @ResponseBody Map<String , Object> getJsonByMap() {
	    Map<String, Object> jsonObject = new HashMap<String, Object>();
	    Map<String, Object> jsonSubObject = null;
	    ArrayList<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
	         
	    //1��° ������
	    jsonSubObject = new HashMap<String, Object>();
	    jsonSubObject.put("idx", 1);
	    jsonSubObject.put("title", "�����Դϴ�");
	    jsonSubObject.put("create_date", new Date());
	    jsonList.add(jsonSubObject);
	    //2��° ������
	    jsonSubObject = new HashMap<String, Object>();
	    jsonSubObject.put("idx", 2);
	    jsonSubObject.put("title", "�ι�°�����Դϴ�");
	    jsonSubObject.put("create_date", new Date());
	    jsonList.add(jsonSubObject);
	         
	    jsonObject.put("success", true);
	    jsonObject.put("total_count", 10);
	    jsonObject.put("result_list", jsonList);
	         
	    return jsonObject;
	}
	
	@RequestMapping(value = "/ssh.do", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> sshClient(HttpServletRequest request,  HttpServletResponse response) {
		
		String ip=request.getParameter("ip");
		String cmd=request.getParameter("cmd");
		String info="";
		String[] ips=null;
		Map<String, Object> jsonObject=new HashMap<String, Object>();
		
		System.out.println("==========================");
		System.out.println(ip);
		System.out.println(cmd);
		System.out.println("==========================");
		
		SSHConnect sshAgent = new SSHConnect(ip, "root", "a1004zgp!");

		try {
			if( sshAgent.connect() ) 
			
			{
			
				if(cmd.equals("ip"))
				{
					info = sshAgent.executeCommand("/home/test/monitor "+cmd);
					
					ips=info.split("\n");
					for(int c=0;c<ips.length;c++)
					{
						System.out.println( "���� : " + ips[c] );
						jsonObject.put("ip"+c,ips[c]);
						jsonObject.put("length",+c+1);
					}
				}else if(cmd.equals("cpu"))
				{
					info = sshAgent.executeCommand("/home/test/monitor "+cmd);
					System.out.println( "���� : " + info );

					jsonObject.put("data",info);

				}else if(cmd.equals("os"))
				{
					info = sshAgent.executeCommand("/home/test/monitor "+cmd);
					System.out.println( "���� : " + info );
					jsonObject.put("os",info);
					
				}else if(cmd.equals("mem"))
				{
					info = sshAgent.executeCommand("/home/test/monitor "+cmd);
					System.out.println( "���� : " + info );
					jsonObject.put("data",info);
				}else if(cmd.equals("dh"))
				{
					info = sshAgent.executeCommand("/home/test/monitor "+cmd);
					System.out.println( "���� : " + info );
					jsonObject.put("data",info);
				}else if(cmd.equals("standby"))
				{
					info=sshAgent.executeCommand("/etc/ha.d/hb_standby");
					jsonObject.put("data",info);
				}
			
			
			// �α׾ƿ�
			//response.getWriter().println(jsonObject);
			
			sshAgent.logout();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}

}
