package vn.com.irtech.core.system.service;

import java.util.List;

import vn.com.irtech.core.common.domain.Ztree;
import vn.com.irtech.core.common.domain.entity.SysDictData;
import vn.com.irtech.core.common.domain.entity.SysDictType;

/**
 * 
 * @author admin
 */
public interface ISysDictTypeService
{ 
    public List<SysDictType> selectDictTypeList(SysDictType dictType);
    public List<SysDictType> selectDictTypeAll();
    public List<SysDictData> selectDictDataByType(String dictType);
    public SysDictType selectDictTypeById(Long dictId);
    public SysDictType selectDictTypeByType(String dictType);
    public int deleteDictTypeByIds(String ids);
    public void clearCache();
    public int insertDictType(SysDictType dictType);
    public int updateDictType(SysDictType dictType);
    public String checkDictTypeUnique(SysDictType dictType);
    public List<Ztree> selectDictTree(SysDictType dictType);
}
