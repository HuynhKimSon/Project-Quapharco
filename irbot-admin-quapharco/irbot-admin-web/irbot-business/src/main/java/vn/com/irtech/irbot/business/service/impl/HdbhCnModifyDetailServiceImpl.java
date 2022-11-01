package vn.com.irtech.irbot.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.domain.HdbhCnDetail;
import vn.com.irtech.irbot.business.mapper.HdbhCnModifyDetailMapper;
import vn.com.irtech.irbot.business.service.IHdbhCnModifyDetailService;

@Service
public class HdbhCnModifyDetailServiceImpl implements IHdbhCnModifyDetailService {

	@Autowired
	private HdbhCnModifyDetailMapper hdbhCnModifyDetailMapper;

	@Override
	public HdbhCnDetail selectHdbhCnModifyDetailById(Long id) {
		return hdbhCnModifyDetailMapper.selectHdbhCnModifyDetailById(id);
	}

	@Override
	public List<HdbhCnDetail> selectHdbhCnModifyDetailList(HdbhCnDetail hdbhCnDetail) {
		return hdbhCnModifyDetailMapper.selectHdbhCnModifyDetailList(hdbhCnDetail);
	}
}