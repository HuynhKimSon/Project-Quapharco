package vn.com.irtech.core.common.constant;

/**
 * Code generation general constants
 * 
 * @author admin
 */
public class GenConstants {
	/** Single table (addition, deletion, modification and check) */
	public static final String TPL_CRUD = "crud";

	/** Tree table (addition, deletion, modification, query) */
	public static final String TPL_TREE = "tree";

	/** Main and sub-table (addition, deletion, modification, and check) */
	public static final String TPL_SUB = "sub";

	/** Tree encoding field */
	public static final String TREE_CODE = "treeCode";

	/** Tree parent encoding field */
	public static final String TREE_PARENT_CODE = "treeParentCode";

	/** Tree name field */
	public static final String TREE_NAME = "treeName";

	/** Upper menu ID field */
	public static final String PARENT_MENU_ID = "parentMenuId";

	/** Upper menu name field */
	public static final String PARENT_MENU_NAME = "parentMenuName";

	/** Database string type */
	public static final String[] COLUMNTYPE_STR = { "char", "varchar", "nvarchar", "varchar2" };

	/** Database text type */
	public static final String[] COLUMNTYPE_TEXT = { "tinytext", "text", "mediumtext", "longtext" };

	/** Database time type */
	public static final String[] COLUMNTYPE_TIME = { "datetime", "time", "date", "timestamp" };

	/** Database number type */
	public static final String[] COLUMNTYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
			"bit", "bigint", "float", "double", "decimal" };

	/** No need to edit fields on the page */
	public static final String[] COLUMNNAME_NOT_EDIT = { "id", "create_by", "create_time", "del_flag" };

	/** List fields that do not need to be displayed on the page */
	public static final String[] COLUMNNAME_NOT_LIST = { "id", "create_by", "create_time", "del_flag", "update_by",
			"update_time" };

	/** The page does not require query fields */
	public static final String[] COLUMNNAME_NOT_QUERY = { "id", "create_by", "create_time", "del_flag", "update_by",
			"update_time", "remark" };

	/** Entity base class field */
	public static final String[] BASE_ENTITY = { "createBy", "createTime", "updateBy", "updateTime", "remark" };

	/** Tree base class field */
	public static final String[] TREE_ENTITY = { "parentName", "parentId", "orderNum", "ancestors" };

	/** Text box */
	public static final String HTML_INPUT = "input";

	/** Text field */
	public static final String HTML_TEXTAREA = "textarea";

	/** Drop down box */
	public static final String HTML_SELECT = "select";

	/** Single box */
	public static final String HTML_RADIO = "radio";

	/** Checkbox */
	public static final String HTML_CHECKBOX = "checkbox";

	/** Date control */
	public static final String HTML_DATETIME = "datetime";

	/** Upload control */
	public static final String HTML_UPLOAD = "upload";

	/** Rich text control */
	public static final String HTML_SUMMERNOTE = "summernote";

	/** String type */
	public static final String TYPE_STRING = "String";

	/** Integer */
	public static final String TYPE_INTEGER = "Integer";

	/** Long */
	public static final String TYPE_LONG = "Long";

	/** Double */
	public static final String TYPE_DOUBLE = "Double";

	/** BigDecimal */
	public static final String TYPE_BIGDECIMAL = "BigDecimal";

	/** Time type */
	public static final String TYPE_DATE = "Date";

	/** Fuzzy query */
	public static final String QUERY_LIKE = "LIKE";

	/** Require */
	public static final String REQUIRE = "1";
}
