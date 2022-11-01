package vn.com.irtech.irbot.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.domain.HdxNccDetail;
import vn.com.irtech.irbot.business.mapper.HdxNccModifyDetailMapper;
import vn.com.irtech.irbot.business.service.IHdxNccModifyDetailService;

@Service
public class HdxNccModifyDetailServiceImpl implements IHdxNccModifyDetailService {

	@Autowired
	private HdxNccModifyDetailMapper hdxNccModifyDetailMapper;

	@Override
	public HdxNccDetail selectHdxNccModifyDetailById(Long id) {
		return hdxNccModifyDetailMapper.selectHdxNccModifyDetailById(id);
	}

	@Override
	public List<HdxNccDetail> selectHdxNccModifyDetailList(HdxNccDetail hdxNccDetail) {
		return hdxNccModifyDetailMapper.selectHdxNccModifyDetailList(hdxNccDetail);
	}
}