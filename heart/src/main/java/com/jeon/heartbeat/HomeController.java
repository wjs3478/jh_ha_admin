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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	
	private ExecutorService exc;
	private Boolean syncState=false;
	 
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
	
	
	@RequestMapping(value = "/test.tdo", method = RequestMethod.GET)
	public void ajax(HttpServletRequest request,  HttpServletResponse response) {
		
		System.out.println("안녕하세요 감사해요 잘있어요");
		String sqls=request.getParameter("sql");
		
		System.out.println(sqls);
		
		Enumeration paramNames = request.getParameterNames();

		// 저장할 맵
		Map paramMap = new HashMap();

		// 맵에 저장
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
	         
	    //1번째 데이터
	    jsonSubObject = new HashMap<String, Object>();
	    jsonSubObject.put("idx", 1);
	    jsonSubObject.put("title", "제목입니다");
	    jsonSubObject.put("create_date", new Date());
	    jsonList.add(jsonSubObject);
	    //2번째 데이터
	    jsonSubObject = new HashMap<String, Object>();
	    jsonSubObject.put("idx", 2);
	    jsonSubObject.put("title", "두번째제목입니다");
	    jsonSubObject.put("create_date", new Date());
	    jsonList.add(jsonSubObject);
	         
	    jsonObject.put("success", true);
	    jsonObject.put("total_count", 10);
	    jsonObject.put("result_list", jsonList);
	         
	    return jsonObject;
	}
	
	
	@RequestMapping(value = "/init_load.tdo", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> initDo(HttpServletRequest request,  HttpServletResponse response) {
		

		Map<String, Object> jsonObject=new HashMap<String, Object>();
		
		
		jsonObject.put("server1_name",ReadProperties.getInstance().getParameter("server1_name"));
		jsonObject.put("server1_ip",ReadProperties.getInstance().getParameter("server1_ip"));
		jsonObject.put("server1_id",ReadProperties.getInstance().getParameter("server1_id"));
		//jsonObject.put("server1_pw",ReadProperties.getInstance().getParameter("server1_pw"));
		jsonObject.put("server1_sync",ReadProperties.getInstance().getParameter("server1_sync"));
		
		jsonObject.put("server2_name",ReadProperties.getInstance().getParameter("server2_name"));
		jsonObject.put("server2_ip",ReadProperties.getInstance().getParameter("server2_ip"));
		jsonObject.put("server2_id",ReadProperties.getInstance().getParameter("server2_id"));
		//jsonObject.put("server2_pw",ReadProperties.getInstance().getParameter("server2_pw"));
		jsonObject.put("server2_sync",ReadProperties.getInstance().getParameter("server2_sync"));
		
		jsonObject.put("sv1_sync1_origin_dir",ReadProperties.getInstance().getParameter("sv1_sync1_origin_dir"));
		jsonObject.put("sv1_sync1_remote_dir",ReadProperties.getInstance().getParameter("sv2_sync1_remote_dir"));
		jsonObject.put("sv1_sync1_remote_id",ReadProperties.getInstance().getParameter("sv1_sync1_remote_id"));
		
		jsonObject.put("sv1_sync2_origin_dir",ReadProperties.getInstance().getParameter("sv1_sync2_origin_dir"));
		jsonObject.put("sv1_sync2_remote_dir",ReadProperties.getInstance().getParameter("sv1_sync2_remote_dir"));
		jsonObject.put("sv1_sync2_remote_id",ReadProperties.getInstance().getParameter("sv1_sync2_remote_id"));

		
		jsonObject.put("sv2_sync1_origin_dir",ReadProperties.getInstance().getParameter("sv2_sync1_origin_dir"));
		jsonObject.put("sv2_sync1_remote_dir",ReadProperties.getInstance().getParameter("sv2_sync1_remote_dir"));
		jsonObject.put("sv2_sync1_remote_id",ReadProperties.getInstance().getParameter("sv2_sync1_remote_id"));
		
		jsonObject.put("sv2_sync2_origin_dir",ReadProperties.getInstance().getParameter("sv2_sync2_origin_dir"));
		jsonObject.put("sv2_sync2_remote_dir",ReadProperties.getInstance().getParameter("sv2_sync2_remote_dir"));
		jsonObject.put("sv2_sync2_remote_id",ReadProperties.getInstance().getParameter("sv2_sync2_remote_id"));
		jsonObject.put("sv_vip",ReadProperties.getInstance().getParameter("sv_vip"));
		//jsonObject.put("ip"+c,ips[c]);
		//jsonObject.put("length",+c+1);
				

		
		return jsonObject;
	}
	
	
	@RequestMapping(value = "/setting.tdo", method = RequestMethod.POST)
	public @ResponseBody int settingDo(HttpServletRequest request,  HttpServletResponse response) {
		
		int suc=0;
		
		String ip=request.getParameter("ip");
		String id=request.getParameter("id");
		String pw=request.getParameter("pw");
		//String sync=request.getParameter("sync");
		String snum=request.getParameter("s_num");
		
		
		suc=suc+ReadProperties.getInstance().setParameter("server"+snum+"_ip", ip);
		suc=suc+ReadProperties.getInstance().setParameter("server"+snum+"_id", id);
		suc=suc+ReadProperties.getInstance().setParameter("server"+snum+"_pw", pw);
		//suc=suc+ReadProperties.getInstance().setParameter("server"+snum+"_sync", sync);
		
		return suc;
	}
	
	@RequestMapping(value = "/vip_save.tdo", method = RequestMethod.POST)
	public @ResponseBody int vip_saveDo(HttpServletRequest request,  HttpServletResponse response) {
		
		int suc=0;
		
		String ip1=request.getParameter("ip1");
		String ip2=request.getParameter("ip2");
		String vip=request.getParameter("vip");
		
		suc=suc+ReadProperties.getInstance().setParameter("server1_ip", ip1);
		suc=suc+ReadProperties.getInstance().setParameter("server2_ip", ip2);
		suc=suc+ReadProperties.getInstance().setParameter("sv_vip", vip);
		
		
		String id1=ReadProperties.getInstance().getParameter("server1_id");
		String id2=ReadProperties.getInstance().getParameter("server2_id");
		String pw1=ReadProperties.getInstance().getParameter("server1_pw");
		String pw2=ReadProperties.getInstance().getParameter("server2_pw");
		
		String subnet=ip1.substring(0, ip1.lastIndexOf("."))+".255";
		
		System.out.println(subnet);
		
		SSHConnect2 sshAgent = new SSHConnect2(ip1, id1, pw1);

		try {
			if( sshAgent.connect() )
			{
				sshAgent.executeCommand("sudo /opt/haadmin/hare_config.sh "+ip2+" "+vip+" "+subnet);
				
			}else
			{
				return 1;
			}
		}
		catch(Exception e)
		{
			return 1;
		}
		
		sshAgent.logout();
		
		sshAgent = new SSHConnect2(ip2, id2, pw2);
		
		
		try {
			if( sshAgent.connect() )
			{
				sshAgent.executeCommand("sudo /opt/haadmin/hare_config.sh "+ip1+" "+vip+" "+subnet);
				
			}else
			{
				return 1;
			}
		}
		catch(Exception e)
		{
			return 1;
		}
		
		sshAgent.logout();
		
		return suc;
	}
	
	
	@RequestMapping(value = "/sync_setting.tdo", method = RequestMethod.POST)
	public @ResponseBody int sync_settingDo(HttpServletRequest request,  HttpServletResponse response) {
		
		int suc=0;
		
		String sync1_origin=request.getParameter("sync1_origin").equals(null)?"":request.getParameter("sync1_origin");
		String sync1_remote=request.getParameter("sync1_remote").equals(null)?"":request.getParameter("sync1_remote");
		String sync1_id=request.getParameter("sync1_id").equals(null)?"":request.getParameter("sync1_id");
		String sync1_pw=request.getParameter("sync1_pw").equals(null)?"":request.getParameter("sync1_pw");
		String sync2_origin=request.getParameter("sync2_origin").equals(null)?"":request.getParameter("sync2_origin");
		String sync2_remote=request.getParameter("sync2_remote").equals(null)?"":request.getParameter("sync2_remote");
		String sync2_id=request.getParameter("sync2_id").equals(null)?"":request.getParameter("sync2_id");
		String sync2_pw=request.getParameter("sync2_pw").equals(null)?"":request.getParameter("sync2_pw");
		String snum=request.getParameter("s_num");
		
		
		
		
		
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync1_origin_dir", sync1_origin);
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync1_remote_dir", sync1_remote);
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync1_remote_id", sync1_id);
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync1_remote_pw", sync1_pw);
		
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync2_origin_dir", sync2_origin);
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync2_remote_dir", sync2_remote);
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync2_remote_id", sync2_id);
		suc=suc+ReadProperties.getInstance().setParameter("sv"+snum+"_sync2_remote_pw", sync2_pw);
		
		return suc;
	}
	
	
	@RequestMapping(value = "/sync_play.tdo", method = RequestMethod.POST)
	public @ResponseBody int sync_playDo(HttpServletRequest request,  HttpServletResponse response) {
		

		
		if(exc!=null)
		{
			return 1;
		}
		
		exc=Executors.newFixedThreadPool(2);
				
				
		String snum=request.getParameter("s_num");
		String ckl=request.getParameter("check");
		
		
		String[] ar=ckl.split(",");
		
		String rnum=null;
		
		if(!snum.equals("1"))
		{
			rnum="1";
		}else
		{
			rnum="2";
		}
		
		String ip=ReadProperties.getInstance().getParameter("server"+snum+"_ip");
		String id=ReadProperties.getInstance().getParameter("server"+snum+"_id");
		String pw=ReadProperties.getInstance().getParameter("server"+snum+"_pw");
		String remoteip=ReadProperties.getInstance().getParameter("server"+rnum+"_ip");
		
		for(int i=0;i<ar.length;i++)
		{
			String sync1_origin=ReadProperties.getInstance().getParameter("sv"+snum+"_sync"+ar[i]+"_origin_dir");
			String sync1_remote=ReadProperties.getInstance().getParameter("sv"+snum+"_sync"+ar[i]+"_remote_dir");
			String sync1_id=ReadProperties.getInstance().getParameter("sv"+snum+"_sync"+ar[i]+"_remote_id");
			String sync1_pw=ReadProperties.getInstance().getParameter("sv"+snum+"_sync"+ar[i]+"_remote_pw");
			Runnable sync1=new SyncData(ip,id,pw,remoteip,sync1_id,sync1_pw,sync1_origin,sync1_remote);
			exc.execute(sync1);
		}
		
		/*String sync2_origin=ReadProperties.getInstance().getParameter("sv"+snum+"_sync2_origin_dir");
		String sync2_remote=ReadProperties.getInstance().getParameter("sv"+snum+"_sync2_remote_dir");
		String sync2_id=ReadProperties.getInstance().getParameter("sv"+snum+"_sync2_remote_id");
		String sync2_pw=ReadProperties.getInstance().getParameter("sv"+snum+"_sync2_remote_pw");*/
		
		//1 -> 2
		
		
		
		
		
		/*sync1=new SyncData(ip,id,pw,remoteip,sync2_id,sync2_pw,sync2_origin,sync2_remote);
		exc.execute(sync1);*/
		
		
		return 0;
	}
	
	@RequestMapping(value = "/sync_stop.tdo", method = RequestMethod.POST)
	public @ResponseBody int sync_stopDo(HttpServletRequest request,  HttpServletResponse response) 
	{
		
		if(exc==null)
		{
			return 1;
		}
		
		exc.shutdownNow();
		exc=null;

		//System.out.println("시바 뭐야");
		
		return 0;
	}
	
	
	@RequestMapping(value = "/ssh.tdo", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> sshClient(HttpServletRequest request,  HttpServletResponse response) {
		
		String ip=request.getParameter("ip");
		String cmd=request.getParameter("cmd");
		String snum=request.getParameter("s_num");
		String id=ReadProperties.getInstance().getParameter("server"+snum+"_id");
		String pw=ReadProperties.getInstance().getParameter("server"+snum+"_pw");
		String info="";
		String[] ips=null;
		Map<String, Object> jsonObject=new HashMap<String, Object>();
		
		
		System.out.println("==========================");
		System.out.println(ip);
		System.out.println(cmd);
		System.out.println(snum);
		System.out.println(pw);
		System.out.println("==========================");
		
		SSHConnect2 sshAgent = new SSHConnect2(ip, id, pw);

		try {
			if( sshAgent.connect() ) 
			
			{
			
				if(cmd.equals("ip"))
				{
					info = sshAgent.executeCommand("/opt/haadmin/monitor.sh "+cmd);
					
					ips=info.split("\n");
					for(int c=0;c<ips.length;c++)
					{
						//System.out.println( "정보 : " + ips[c] );
						jsonObject.put("ip"+c,ips[c]);
						jsonObject.put("length",+c+1);
					}
					
						jsonObject.put("sync_state", exc==null);
				}else if(cmd.equals("cpu"))
				{
					info = sshAgent.executeCommand("/opt/haadmin/monitor.sh "+cmd);
					//System.out.println( "정보 : " + info );

					jsonObject.put("data",info);

				}else if(cmd.equals("os"))
				{
					info = sshAgent.executeCommand("/opt/haadmin/monitor.sh "+cmd);
					//System.out.println( "정보 : " + info );
					jsonObject.put("os",info);
					
				}else if(cmd.equals("mem"))
				{
					info = sshAgent.executeCommand("/opt/haadmin/monitor.sh "+cmd);
					//System.out.println( "정보 : " + info );
					jsonObject.put("data",info);
				}else if(cmd.equals("dh"))
				{
					info = sshAgent.executeCommand("/opt/haadmin/monitor.sh "+cmd);
					//System.out.println( "정보 : " + info );
					jsonObject.put("data",info);
				}else if(cmd.equals("standby"))
				{
					if(exc!=null)
					{
						exc.shutdownNow();
					}
					
					info=sshAgent.executeCommand("sudo /etc/ha.d/hb_standby");
					jsonObject.put("data",info);
				}			
			
			// 로그아웃
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
