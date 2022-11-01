package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhDetail;

public interface HdbhModifyDetailMapper {

	public HdbhDetail selectHdbhModifyDetailById(Long id);

	public List<HdbhDetail> selectHdbhModifyDetailList(HdbhDetail hdbhDetail);

	public int insertHdbhModifyDetail(HdbhDetail hdbhDetail);

	public int updateHdbhModifyDetail(HdbhDetail hdbhDetail);

	public int deleteHdbhModifyDetailById(Long id);

	public int deleteHdbhModifyDetailByIds(String[] ids);

}