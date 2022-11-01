package vn.com.irtech.irbot.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.domain.HddvDetail;
import vn.com.irtech.irbot.business.mapper.HddvModifyDetailMapper;
import vn.com.irtech.irbot.business.service.IHddvModifyDetailService;

@Service
public class HddvModifyDetailServiceImpl implements IHddvModifyDetailService {

	@Autowired
	private HddvModifyDetailMapper hddvModifyDetailMapper;

	@Override
	public HddvDetail selectHddvModifyDetailById(Long id) {
		return hddvModifyDetailMapper.selectHddvModifyDetailById(id);
	}

	@Override
	public List<HddvDetail> selectHddvModifyDetailList(HddvDetail hddvDetail) {
		return hddvModifyDetailMapper.selectHddvModifyDetailList(hddvDetail);
	}
}