package com.black.web.bean;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.Environment;

import com.black.web.Logger.Logger;
import com.black.web.base.bean.PageResponse;
import com.black.web.base.enums.SyncEnum;
import com.black.web.base.exception.RaysException;
import com.black.web.base.service.POIService;
import com.black.web.base.utils.CommonUtil;
import com.black.web.base.utils.mail.Mail;
import com.black.web.services.sync.SyncService;
import com.google.gson.Gson;

public class ThreadBean extends Thread{
	
	private String s;				//搜索关键字
	private Integer count = 100;	//采集数量
	private Integer time = 10;		//采集时间(分钟)
	private Long startTime = 0l;	//采集开始时间
	private String mail;			//采集成功后要发送的邮箱
	private SyncService service;	//service类型
	private Long endTime = 0l;		//结束时间
	private Boolean success = true;	//采集结果
	
	private List<GoodsEntity> data = new ArrayList<>();
	
	public ThreadBean(CollectBean bean) throws RaysException {
		this.s = bean.getS();
		this.count = bean.getCount();
		this.time = bean.getTime();
		this.mail = bean.getMail();
		try {
			this.service = (SyncService) Class.forName(SyncEnum.valueOf(bean.getType().toUpperCase()).getClassName()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RaysException("同步类型不存在!");
		}
	}
	//采集数据线程
	@Override
	public void run() {
		ThreadBean that = this;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String now = sdf.format(startTime);
		
		endTime = startTime+time*60*1000;
		
		Logger.info("START:采集线程["+that.getName()+"]开始采集数据:[开始时间:{},目标站:{},目标采集数量:{},目标采集时间(分钟):{},发送邮箱:{}]",now,this.service,this.count,this.time,this.mail);
		//如果执行时间不为空  则开启时间监听线程
		if(this.time!=null &&this.time>0) {
			new Thread(()->{
				Logger.info("-------------采集监控线程["+this.getName()+"]开启-------------");
				//如果当前时间大于停止时间  调用shutdown方法
				while(System.currentTimeMillis() <= endTime) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Logger.info("-------------当前时间大于规定采集结束时间，调用采集线程["+this.getName()+"]关闭方法-------------");
				//采集服务的shutdown方法  shutdown逻辑自己实现
				that.service.shutdown();
				Logger.info("-------------采集监控线程["+this.getName()+"]关闭-------------");
			}).start();
		}
		
		//主线程开始采集程序
		try {
			this.service.sync(data,s,count);
		} catch (Exception e) {
			//结束时间控制为0  为了停止监听线程
			Logger.error("采集线程["+that.getName()+"]报错,设置监听时间为0");
			endTime = 0l;
			success = false;
			e.printStackTrace();
		}
		
		//发送邮件（如果采集过程报错则添加内容  不报错带附件）
		
		Logger.info("-------------采集线程["+this.getName()+"]发送邮件,目标["+that.mail+"]------------");
		try {
			StringBuffer sb = new StringBuffer();
			if(this.success) {
				sb.append("采集成功!\n");
				data.forEach(d->{
					sb.append(d.toString()).append("\n");
				});
			}else {
				sb.append("采集失败!\n采集开始时间【"+now+"】,任务编码【"+this.getName()+"】\n请联系管理员查看文档!");
			}
			
			String[] dataFields = new String[]{"name","price","source","detail","url","subjectName"};
			String[] columns = new String[]{"商品名称","价格","来源","商品详情","商品url","厂家名称"};
			
			@SuppressWarnings("unchecked")
			POIService<GoodsEntity> poiService = CommonUtil.getApplicationContext().getBean(POIService.class);
			InputStream in = poiService.exportExcelInputStream(data, dataFields, columns, "yyyy-MM-dd");
			
			Mail mail = getMail();
			mail.setMailFrom("xyzhang@qysoft.cn");
			mail.setMailTo(new String[]{"272416634@qq.com"}, "to");
			mail.setSubject("采集数据文档!");
			mail.addTextContext(sb.toString());
			mail.addAttachmentFrombyte(in, "采集["+this.service.getType()+"-"+this.s+"]数据-"+now+".xlsx");
			mail.sendMail();
			Logger.info("-------------采集线程["+this.getName()+"]发送邮件成功,目标["+that.mail+"]------------");
		} catch (Exception e) {
			Logger.error("-------------采集线程["+this.getName()+"]发送邮件失败------------");
			e.printStackTrace();
		}
		endTime = 0l;
		Logger.info("END:采集线程["+this.getName()+"]关闭,任务结束");
		ThreadProcessPool.process.remove(that.getName());
	}

	@Override
	public synchronized void start() {
		this.startTime = System.currentTimeMillis();
		super.start();
	}
	
	public String getProcess() {
		Double process = 0d;
		//如果以数量为主  计算当前数量与总数量
		if(count!=null && count>0) {
			if(data.size()==count) {
				process = 100.00;
			}else {
				process = data.size()%count.doubleValue();
			}
		}else 
		//如果以时间为主  根据开始时间计算已运行时间  再与总时间计算
		if(time!=null && time>0) {
			Long after = System.currentTimeMillis()-startTime;
			Long running = time*60*1000l;
			if(after>=running) {
				process = 100.00;
			}else {
				process = after.doubleValue()%(running);
			}
		}
		return new Gson().toJson(new PageResponse<Object>(true, "",new DecimalFormat("0.00").format(process)));
	}
	
	private Mail getMail() {
		Environment env = CommonUtil.getApplicationContext().getEnvironment();
		return new Mail(env.getProperty("collect.mail.host"),
				env.getProperty("collect.mail.port"),
				env.getProperty("collect.mail.username"),
				env.getProperty("collect.mail.password"));
	}
	
	public String getS() {
		return s;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getTime() {
		return time;
	}

	public List<GoodsEntity> getData() {
		return data;
	}
	
}
