package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.HdxNccDetail;

public interface IHdxNccModifyDetailService {

	public HdxNccDetail selectHdxNccModifyDetailById(Long id);

	public List<HdxNccDetail> selectHdxNccModifyDetailList(HdxNccDetail hdxNccDetail);

}