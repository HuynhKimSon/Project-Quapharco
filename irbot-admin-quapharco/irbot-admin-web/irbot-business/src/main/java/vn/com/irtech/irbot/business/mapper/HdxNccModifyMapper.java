package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdxNcc;

public interface HdxNccModifyMapper {

	public HdxNcc selectHdxNccModifyById(Long id);

	public List<HdxNcc> selectHdxNccModifyList(HdxNcc hdxNcc);

	public int insertHdxNccModify(HdxNcc hdxNcc);

	public int updateHdxNccModify(HdxNcc hdxNcc);

	public int deleteHdxNccModifyById(Long id);

	public int deleteHdxNccModifyByIds(String[] ids);

}