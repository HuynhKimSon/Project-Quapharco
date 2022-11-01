package vn.com.irtech.irbot.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.domain.HdbhDetail;
import vn.com.irtech.irbot.business.mapper.HdbhModifyDetailMapper;
import vn.com.irtech.irbot.business.service.IHdbhModifyDetailService;

@Service
public class HdbhModifyDetailServiceImpl implements IHdbhModifyDetailService {

	@Autowired
	private HdbhModifyDetailMapper hdbhModifyDetailMapper;

	@Override
	public HdbhDetail selectHdbhModifyDetailById(Long id) {
		return hdbhModifyDetailMapper.selectHdbhModifyDetailById(id);
	}

	@Override
	public List<HdbhDetail> selectHdbhModifyDetailList(HdbhDetail hdbhDetail) {
		return hdbhModifyDetailMapper.selectHdbhModifyDetailList(hdbhDetail);
	}
}