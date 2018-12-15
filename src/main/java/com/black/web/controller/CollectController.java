package com.black.web.controller;

import java.lang.Thread.State;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.black.web.base.bean.PageResponse;
import com.black.web.bean.CollectBean;
import com.black.web.bean.ThreadBean;
import com.black.web.bean.ThreadProcessPool;
import com.google.gson.Gson;

@RestController
@RequestMapping()
public class CollectController{
	
    @PostMapping("/collect")
	@ResponseBody
	public String collect(@RequestBody CollectBean bean) throws Exception {
    	if(bean.getCount()==null && bean.getTime()==null) {
    		bean.setCount(1);
    	}
    	ThreadBean tbean = new ThreadBean(bean);
    	ThreadProcessPool.process.put(tbean.getName(), tbean);
    	tbean.start();
        return new Gson().toJson(
				new PageResponse<Object>(true,"任务已创建",tbean.getName()));
    }
    
    @GetMapping("/process/{id}")
   	@ResponseBody
   	public String process(@PathVariable("id") String id) throws Exception {
    	ThreadBean bean = ThreadProcessPool.process.get(id);
    	if(bean!=null) {
    		if(bean.getState().compareTo(State.TERMINATED)==0) {
    			return new Gson().toJson(new PageResponse<Object>(true,"","100.00"));
    		}
    		System.out.println(bean.getState().name());
    		return bean.getProcess();
    	}
    	return new Gson().toJson(new PageResponse<Object>(false,"编号为【"+id+"】的任务未创建或已结束"));
    }
}
