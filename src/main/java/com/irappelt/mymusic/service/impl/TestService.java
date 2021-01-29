package com.irappelt.mymusic.service.impl;

import javax.annotation.Resource;


import com.irappelt.mymusic.common.AbstractService;
import com.irappelt.mymusic.common.IOperations;
import com.irappelt.mymusic.mapper.ITestMapper;
import com.irappelt.mymusic.model.Test;
import com.irappelt.mymusic.service.ITestService;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestService extends AbstractService<Test, Test> implements ITestService {

	public TestService() {
		this.setTableName("test");
	}

	@Resource   
	private ITestMapper testMapper;

	@Override
	protected IOperations<Test, Test> getMapper() {
		return testMapper;
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
		;
	}

	@Override
	public void insert(Test entity) {

	}
}
