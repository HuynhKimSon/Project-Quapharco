package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Hddv;

public interface HddvModifyMapper {

	public Hddv selectHddvModifyById(Long id);

	public List<Hddv> selectHddvModifyList(Hddv hddv);

	public int insertHddvModify(Hddv hddv);

	public int updateHddvModify(Hddv hddv);

	public int deleteHddvModifyById(Long id);

	public int deleteHddvModifyByIds(String[] ids);

}