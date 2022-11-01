package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhNbDetail;

public interface HdbhNbModifyDetailMapper {

	public HdbhNbDetail selectHdbhNbModifyDetailById(Long id);

	public List<HdbhNbDetail> selectHdbhNbModifyDetailList(HdbhNbDetail hdbhNbDetail);

	public int insertHdbhNbModifyDetail(HdbhNbDetail hdbhNbDetail);

	public int updateHdbhNbModifyDetail(HdbhNbDetail hdbhNbDetail);

	public int deleteHdbhNbModifyDetailById(Long id);

	public int deleteHdbhNbModifyDetailByIds(String[] ids);

}