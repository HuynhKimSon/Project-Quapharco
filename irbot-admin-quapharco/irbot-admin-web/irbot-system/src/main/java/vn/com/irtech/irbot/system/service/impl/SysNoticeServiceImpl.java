package vn.com.irtech.irbot.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.irbot.system.domain.SysNotice;
import vn.com.irtech.irbot.system.mapper.SysNoticeMapper;
import vn.com.irtech.irbot.system.service.ISysNoticeService;

/**
 * Notification form Service Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-11-17
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService {
	@Autowired
	private SysNoticeMapper noticeMapper;

	/**
	 * 查询公告信息
	 * 
	 * @param noticeId 公告ID
	 * @return 公告信息
	 */
	@Override
	public SysNotice selectNoticeById(Long noticeId) {
		return noticeMapper.selectNoticeById(noticeId);
	}

	/**
	 * 查询公告列表
	 * 
	 * @param notice 公告信息
	 * @return 公告集合
	 */
	@Override
	public List<SysNotice> selectNoticeList(SysNotice notice) {
		return noticeMapper.selectNoticeList(notice);
	}

	/**
	 * 新增公告
	 * 
	 * @param notice 公告信息
	 * @return 结果
	 */
	@Override
	public int insertNotice(SysNotice notice) {
		return noticeMapper.insertNotice(notice);
	}

	/**
	 * 修改公告
	 * 
	 * @param notice 公告信息
	 * @return 结果
	 */
	@Override
	public int updateNotice(SysNotice notice) {
		return noticeMapper.updateNotice(notice);
	}

	/**
	 * 删除公告对象
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteNoticeByIds(String ids) {
		return noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
	}
}
