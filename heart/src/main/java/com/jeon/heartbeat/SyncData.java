package com.jeon.heartbeat;

public class SyncData implements Runnable {
	
	private String ip=null;
	private String id=null;
	private String pw=null;
	private String remoteIp=null;
	private String remoteId=null;
	private String remotePw=null;
	private String originData=null;
	private String remoteData=null;
	
	private boolean stop=false;
	SSHConnect2 sshAgent=null;
	
	
	public SyncData (String ip, String id, String pw, String remoteIp, String remoteId, String remotePw, String originData, String remoteData)
	{
		this.ip=ip;
		this.id=id;
		this.pw=pw;
		this.remoteIp=remoteIp;
		this.remoteId=remoteId;
		this.remotePw=remotePw;
		this.originData=originData;
		this.remoteData=remoteData;
		
		sshAgent = new SSHConnect2(this.ip, this.id, this.pw);
		try {
			if(sshAgent.connect())
			{
				System.out.println("접속성공");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStop()
	{
		this.stop=false;
	}
	
	public void run()
	{
		
		//192.168.23.131 mariadb admin123 /opt/data /opt/data
		//while(!stop)
		//{
			stop=false;
		
				while(!stop)
				{
					try {
						sshAgent.executeCommand("sudo /opt/haadmin/sync.sh "+remoteIp+" "+remoteId+" "+ remotePw+" "+originData+" "+remoteData);
						Thread.sleep(1000);
						//wait(1);
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						
						//e.printStackTrace();
						stop=true;
						break;
					}
				}
				
				sshAgent.logout();
			
		//}
		
	}

}
