package vn.com.irtech.core.framework.web.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.core.common.domain.entity.SysDictData;
import vn.com.irtech.core.system.service.ISysDictDataService;
import vn.com.irtech.core.system.service.ISysDictTypeService;

/**
 * html calls thymeleaf to implement dictionary reading
 * 
 * @author admin
 */
@Service("dict")
public class DictService
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * Query dictionary data information according to dictionary type
     * 
     * @param dictType Dictionary type
     * @return Parameter key value
     */
    public List<SysDictData> getType(String dictType)
    {
        return dictTypeService.selectDictDataByType(dictType);
    }

    /**
     * Query dictionary data information according to dictionary type and dictionary key value
     * 
     * @param dictType Dictionary type
     * @param dictValue Dictionary key
     * @return Dictionary tag
     */
    public String getLabel(String dictType, String dictValue)
    {
        return dictDataService.selectDictLabel(dictType, dictValue);
    }
    
    /**
     * Select list dictionary tag by dictionary type
     * 
     * @param dictType
     * @return List<String>
     */
    public List<String> getListTag(String dictType) {
    	List<SysDictData> dictDatas = dictTypeService.selectDictDataByType(dictType);
    	List<String> dictTagList = new ArrayList<>();
    	if (CollectionUtils.isNotEmpty(dictDatas)) {
    		for (SysDictData sysDictData : dictDatas) {
    			dictTagList.add(sysDictData.getDictValue());
    		}
    	}
    	return dictTagList;
     }
}
