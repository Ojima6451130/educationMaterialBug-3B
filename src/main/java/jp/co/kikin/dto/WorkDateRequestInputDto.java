/**
 * ファイル名：WorkDateRequestInputDto.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 */
package jp.co.kikin.dto;

/**
 * 説明：出勤希望日入力Dto
 * @author naraki
 *
 */
public class WorkDateRequestInputDto {
    /** 社員ID */
    private String employeeId;
    /** 社員名 */
    private String employeeName;
    /** 年月日 */
    private String yearMonthDay;
    /** 希望シフト */
    private String myRequestShiftId;
    /** 登録フラグ */
    private boolean registerFlg;

	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getYearMonthDay() {
		return yearMonthDay;
	}
	public void setYearMonthDay(String yearMonthDay) {
		this.yearMonthDay = yearMonthDay;
	}
	public String getMyRequestShiftId() {
		return myRequestShiftId;
	}
	public void setMyRequestShiftId(String myRequestShiftId) {
		this.myRequestShiftId = myRequestShiftId;
	}
	public boolean getRegisterFlg() {
		return registerFlg;
	}
	public void setRegisterFlg(boolean registerFlg) {
		this.registerFlg = registerFlg;
	}
}
