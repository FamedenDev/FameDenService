package com.fameden.common.service;

public interface ICommonService{

	public boolean validate(Object dto) throws Exception;

	public Object processRequest(Object dto);
	
	public int insertRequest(Object dto) throws Exception;

}
