package vn.com.irtech.irbot.business.service;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhCnDetail;

public interface IHdbhCnModifyDetailService {

	public HdbhCnDetail selectHdbhCnModifyDetailById(Long id);

	public List<HdbhCnDetail> selectHdbhCnModifyDetailList(HdbhCnDetail hdbhCnDetail);

}