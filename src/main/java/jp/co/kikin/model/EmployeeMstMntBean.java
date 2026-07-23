/**
 * ファイル名：EmployeeMstMntBean.java
 *
 * 変更履歴
 * 1.0  2010/08/23 Kazuya.Naraki
 */
package jp.co.kikin.model;

/**
 * 説明：社員マスタメンテナンスフォームクラス
 * @author naraki
 *
 */
public class EmployeeMstMntBean {

    /** 社員ＩＤ */
    private String employeeId;
    /** パスワード */
    private String password;
    /** 社員名 */
    private String employeeName;
    /** 社員名カナ */
    private String employeeNameKana;
    /** 権限ＩＤ */
    private String authorityId;
    /** 削除社員フラグ */
    private boolean deleteFlg;
    /** 削除社員ＩＤ */
    private String deleteEmployeeId;

	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getPassword() {
		return password;
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
	public boolean getDeleteFlg() {
		return deleteFlg;
	}
	public void setDeleteFlg(boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
	public String getDeleteEmployeeId() {
		return deleteEmployeeId;
	}
	public void setDeleteEmployeeId(String deleteEmployeeId) {
		this.deleteEmployeeId = deleteEmployeeId;
	}

}