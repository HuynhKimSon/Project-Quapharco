package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhCn;

public interface HdbhCnModifyMapper {

	public HdbhCn selectHdbhCnModifyById(Long id);

	public List<HdbhCn> selectHdbhCnModifyList(HdbhCn hdbhCn);

	public int insertHdbhCnModify(HdbhCn hdbhCn);

	public int updateHdbhCnModify(HdbhCn hdbhCn);

	public int deleteHdbhCnModifyById(Long id);

	public int deleteHdbhCnModifyByIds(String[] ids);

}