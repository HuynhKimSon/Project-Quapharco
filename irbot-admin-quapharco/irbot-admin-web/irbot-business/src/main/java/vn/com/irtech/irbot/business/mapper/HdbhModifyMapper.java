package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Hdbh;

public interface HdbhModifyMapper {

	public Hdbh selectHdbhModifyById(Long id);

	public List<Hdbh> selectHdbhModifyList(Hdbh hdbh);

	public int insertHdbhModify(Hdbh hdbh);

	public int updateHdbhModify(Hdbh hdbh);

	public int deleteHdbhModifyById(Long id);

	public int deleteHdbhModifyByIds(String[] ids);

}