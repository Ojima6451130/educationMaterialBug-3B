/**
 * ファイル名：ComboListUtilsDto.java
 *
 * 変更履歴
 * 1.0  2010/08/29 Kazuya.Naraki
 */
package jp.co.kikin.dto;

import java.util.Date;

/**
 * 説明：分類マスタDto
 * @author naraki
 *
 */
public class ComboListUtilsDto {
    /** 分類ID */
    private String categoryId;
    /** コード */
    private String code;
    /** 名称 */
    private String name;
    /** 表示順 */
    private String displayOrder;
    /** 表示１ */
    private boolean display;
    /** 作成ユーザID */
    private String CreaterEmployeeId;
    /** 作成日付 */
    private Date CreationDatetime;
    /** 更新ユーザID */
    private String UpdaterEmployeeId;
    /** 更新日付 */
    private Date UpdateDatetime;

	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public boolean getDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public String getCreaterEmployeeId() {
		return CreaterEmployeeId;
	}
	public void setCreaterEmployeeId(String CreaterEmployeeId) {
		this.CreaterEmployeeId = CreaterEmployeeId;
	}
	public Date getCreationDatetime() {
		return CreationDatetime;
	}
	public void setCreationDatetime(Date CreationDatetime) {
		this.CreationDatetime = CreationDatetime;
	}
	public String getUpdaterEmployeeId() {
		return UpdaterEmployeeId;
	}
	public void setUpdaterEmployeeId(String UpdaterEmployeeId) {
		this.UpdaterEmployeeId = UpdaterEmployeeId;
	}
	public Date getUpdateDatetime() {
		return UpdateDatetime;
	}
	public void setUpdateDatetime(Date UpdateDatetime) {
		this.UpdateDatetime = UpdateDatetime;
	}
}
