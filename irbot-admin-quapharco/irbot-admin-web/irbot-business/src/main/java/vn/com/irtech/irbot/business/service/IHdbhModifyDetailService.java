package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.HdbhDetail;

public interface IHdbhModifyDetailService {

	public HdbhDetail selectHdbhModifyDetailById(Long id);

	public List<HdbhDetail> selectHdbhModifyDetailList(HdbhDetail hdbhDetail);

}