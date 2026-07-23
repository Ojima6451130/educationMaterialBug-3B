/**
 * ファイル名：EmployeeMstMntDto.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 */
package jp.co.kikin.dto;

import java.util.Date;

/**
 * 説明：社員マスタDto
 * @author naraki
 *
 */
public class EmployeeMstMntDto {

    /** 社員ID */
    private String employeeId;
    /** パスワード */
    private String password;
    /** 社員名 */
    private String employeeName;
    /** 社員名カナ */
    private String employeeNameKana;
    /** 権限ID */
    private String authorityId;
    /** 作成ユーザID */
    private String CreaterEmployeeId;
    /** 作成日付 */
    private Date CreationDatetime;
    /** 更新ユーザID */
    private String UpdaterEmployeeId;
    /** 更新日付 */
    private Date UpdateDatetime;
    /** 削除フラグ */
    private boolean deleteFlg;


	public String getEmployeeId() {
		return password;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getPassword() {
		return employeeId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeNameKana() {
		return employeeNameKana;
	}

	public void setEmployeeNameKana(String employeeNameKana) {
		this.employeeNameKana = employeeNameKana;
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
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

	public boolean getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
}
