/**
 * ファイル名：MonthlyShiftDto.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 */
package jp.co.kikin.dto;

/**
 * 説明：月別シフトDto
 * @author naraki
 *
 */
public class MonthlyShiftDto {
    /** 社員ID */
    private String employeeId;
    /** 社員名 */
    private String employeeName;
    /** 年月日 */
    private String yearMonthDay;
    /** シフトID */
    private String shiftId;
    /** シンボル */
    private String symbol;

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
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
