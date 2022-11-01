package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhNb;

public interface HdbhNbModifyMapper {

	public HdbhNb selectHdbhNbModifyById(Long id);

	public List<HdbhNb> selectHdbhNbModifyList(HdbhNb hdbhNb);

	public int insertHdbhNbModify(HdbhNb hdbhNb);

	public int updateHdbhNbModify(HdbhNb hdbhNb);

	public int deleteHdbhNbModifyById(Long id);

	public int deleteHdbhNbModifyByIds(String[] ids);

}