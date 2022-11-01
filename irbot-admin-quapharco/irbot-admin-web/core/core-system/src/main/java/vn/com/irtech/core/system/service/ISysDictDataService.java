package vn.com.irtech.core.system.service;

import java.util.List;

import vn.com.irtech.core.common.domain.entity.SysDictData;

/**
 * 
 * @author admin
 */
public interface ISysDictDataService
{
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    public String selectDictLabel(String dictType, String dictValue);
    
    public String selectDictValue(String dictType, String dictLabel);

    public SysDictData selectDictDataById(Long dictCode);
    
    public int deleteDictDataByIds(String ids);

    public int insertDictData(SysDictData dictData);

    public int updateDictData(SysDictData dictData);
    
    public List<SysDictData> selectDictDataByType(String dictType);
}
