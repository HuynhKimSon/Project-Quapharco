package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.HdbhNbDetail;

public interface IHdbhNbModifyDetailService {

	public HdbhNbDetail selectHdbhNbModifyDetailById(Long id);

	public List<HdbhNbDetail> selectHdbhNbModifyDetailList(HdbhNbDetail hdbhNbDetail);

}