package vn.com.irtech.irbot.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.domain.HdbhNbDetail;
import vn.com.irtech.irbot.business.mapper.HdbhNbModifyDetailMapper;
import vn.com.irtech.irbot.business.service.IHdbhNbModifyDetailService;

@Service
public class HdbhNbModifyDetailServiceImpl implements IHdbhNbModifyDetailService {

	@Autowired
	private HdbhNbModifyDetailMapper hdbhNbModifyDetailMapper;

	@Override
	public HdbhNbDetail selectHdbhNbModifyDetailById(Long id) {
		return hdbhNbModifyDetailMapper.selectHdbhNbModifyDetailById(id);
	}

	@Override
	public List<HdbhNbDetail> selectHdbhNbModifyDetailList(HdbhNbDetail hdbhNbDetail) {
		return hdbhNbModifyDetailMapper.selectHdbhNbModifyDetailList(hdbhNbDetail);
	}
}