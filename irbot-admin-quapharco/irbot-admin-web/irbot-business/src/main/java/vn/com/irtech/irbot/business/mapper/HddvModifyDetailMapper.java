package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HddvDetail;

public interface HddvModifyDetailMapper {

	public HddvDetail selectHddvModifyDetailById(Long id);

	public List<HddvDetail> selectHddvModifyDetailList(HddvDetail hddvDetail);

	public int insertHddvModifyDetail(HddvDetail hddvDetail);

	public int updateHddvModifyDetail(HddvDetail hddvDetail);

	public int deleteHddvModifyDetailById(Long id);

	public int deleteHddvModifyDetailByIds(String[] ids);

}