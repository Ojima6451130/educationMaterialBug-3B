/**
 * ファイル名：LoginDto.java
 *
 * 変更履歴
 * 1.0  2010/08/07 Kazuya.Naraki
 */
package jp.co.kikin.dto;

/**
 * 説明：ログイン処理のDto
 *
 * @author naraki
 *
 */
public class LoginDto {

    // 社員ID
    private String employeeId;
    // パスワード
    private String password;
    // 社員名
    private String employeeName;
    // 社員名カナ
    private String employeeNameKana;
    // 権限ID
    private String authorityId;

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
}
