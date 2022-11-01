package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdxNccDetail;

public interface HdxNccModifyDetailMapper {

	public HdxNccDetail selectHdxNccModifyDetailById(Long id);

	public List<HdxNccDetail> selectHdxNccModifyDetailList(HdxNccDetail hdxNccDetail);

	public int insertHdxNccModifyDetail(HdxNccDetail hdxNccDetail);

	public int updateHdxNccModifyDetail(HdxNccDetail hdxNccDetail);

	public int deleteHdxNccModifyDetailById(Long id);

	public int deleteHdxNccModifyDetailByIds(String[] ids);

}