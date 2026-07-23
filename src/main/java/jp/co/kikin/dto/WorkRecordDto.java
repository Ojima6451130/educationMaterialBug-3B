/**
 * ファイル名：WorkRecordDto.java
 *
 * 変更履歴
 * 1.0  2010/11/03 Kazuya.Naraki
 */
package jp.co.kikin.dto;

import java.util.Date;

/**
 * 説明：勤務実績Dto
 * @author naraki
 *
 */
public class WorkRecordDto {
    /** 社員ID */
    private String employeeId;
    /** 稼働日 */
    private String workDay;
    /** シフトID */
    private String shiftId;
    /** シンボル */
    private String symbol;
    /** 開始時間(シフト) */
    private String startTimeShift;
    /** 終了時間(シフト) */
    private String endTimeShift;
    /** 休憩時間(シフト) */
    private String breakTimeShift;
    /** 開始時間 */
    private String startTime;
    /** 終了時間 */
    private String endTime;
    /** 休憩時間 */
    private String breakTime;
    /** 実働時間 */
    private String actualWorkTime;
    /** 時間外時間 */
    private String overTime;
    /** 休日時間 */
    private String holidayTime;
    /** 備考 */
    private String remark;
    /** 作成ユーザID */
    private String CreaterEmployeeId;
    /** 作成日付 */
    private Date CreationDatetime;
    /** 更新ユーザID */
    private String UpdaterEmployeeId;
    /** 更新日付 */
    private Date UpdateDatetime;

	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getWorkDay() {
		return workDay;
	}
	public void setWorkDay(String workDay) {
		this.workDay = workDay;
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
	public String getStartTimeShift() {
		return startTimeShift;
	}
	public void setStartTimeShift(String startTimeShift) {
		this.startTimeShift = startTimeShift;
	}
	public String getEndTimeShift() {
		return endTimeShift;
	}
	public void setEndTimeShift(String endTimeShift) {
		this.endTimeShift = endTimeShift;
	}
	public String getBreakTimeShift() {
		return breakTimeShift;
	}
	public void setBreakTimeShift(String breakTimeShift) {
		this.breakTimeShift = breakTimeShift;
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
	public String getActualWorkTime() {
		return actualWorkTime;
	}
	public void setActualWorkTime(String actualWorkTime) {
		this.actualWorkTime = actualWorkTime;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public String getHolidayTime() {
		return holidayTime;
	}
	public void setHolidayTime(String holidayTime) {
		this.holidayTime = holidayTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
