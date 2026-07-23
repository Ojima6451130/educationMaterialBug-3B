/**
 * ファイル名：DailyShiftDto.java
 *
 * 変更履歴
 * 1.0  2010/10/23 Kazuya.Naraki
 */
package jp.co.kikin.dto;

/**
 * 説明：日別シフトDto
 * @author naraki
 *
 */
public class DailyShiftDto {

    /** 社員名 */
    private String employeeName;
    /** 開始時間 */
    private String startTime;
    /** 終了時間 */
    private String endTime;
    /** 休憩時間 */
    private String breakTime;

	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
}
