package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhCnDetail;

public interface HdbhCnModifyDetailMapper {

	public HdbhCnDetail selectHdbhCnModifyDetailById(Long id);

	public List<HdbhCnDetail> selectHdbhCnModifyDetailList(HdbhCnDetail hdbhCnDetail);

	public int insertHdbhCnModifyDetail(HdbhCnDetail hdbhCnDetail);

	public int updateHdbhCnModifyDetail(HdbhCnDetail hdbhCnDetail);

	public int deleteHdbhCnModifyDetailById(Long id);

	public int deleteHdbhCnModifyDetailByIds(String[] ids);

}