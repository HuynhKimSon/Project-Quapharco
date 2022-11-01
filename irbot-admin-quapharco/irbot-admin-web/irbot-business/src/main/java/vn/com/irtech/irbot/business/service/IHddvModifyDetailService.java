package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.HddvDetail;

public interface IHddvModifyDetailService {

	public HddvDetail selectHddvModifyDetailById(Long id);

	public List<HddvDetail> selectHddvModifyDetailList(HddvDetail hddvDetail);

}