package vn.com.irtech.core.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.core.common.domain.entity.SysDictData;
import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DictUtils;
import vn.com.irtech.core.system.mapper.SysDictDataMapper;
import vn.com.irtech.core.system.service.ISysDictDataService;

/**
 * 
 * @author admin
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }
    
    @Override
    public String selectDictValue(String dictType, String dictLabel)
    {
        return dictDataMapper.selectDictValue(dictType, dictLabel);
    }

    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    @Override
    public int deleteDictDataByIds(String ids)
    {
        int row = dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    @Override
    public int insertDictData(SysDictData dictData)
    {
        int row = dictDataMapper.insertDictData(dictData);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    @Override
    public int updateDictData(SysDictData dictData)
    {
        int row = dictDataMapper.updateDictData(dictData);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

	@Override
	public List<SysDictData> selectDictDataByType(String dictType) {
		return dictDataMapper.selectDictDataByType(dictType);
	}
}
