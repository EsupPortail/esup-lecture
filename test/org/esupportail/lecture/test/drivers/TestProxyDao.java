package org.esupportail.lecture.test.drivers;

import org.esupportail.lecture.dao.DaoService;

public class TestProxyDao {
	DaoService daoService;

	public DaoService getDaoService() {
		return daoService;
	}

	public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}

}
