/**
 * ファイル名：BaseShiftDto.java
 *
 * 変更履歴
 * 1.0  2010/11/03 Kazuya.Naraki
 */
package jp.co.kikin.dto;

import java.util.Date;

/**
 * 説明：基本シフトDto
 * @author nishioka
 *
 */
public class BaseShiftDto {
    /** 社員ID */
    private String employeeId;
    /** 社員名 */
    private String employeeName;
    /** 月曜日シフト */
    private String shiftIdOnMonday;
    /** 火曜日シフト */
    private String shiftIdOnTuesday;
    /** 水曜日シフト */
    private String shiftIdOnWednesday;
    /** 木曜日シフト */
    private String shiftIdOnThursday;
    /** 金曜日シフト */
    private String shiftIdOnFriday;
    /** 土曜日シフト */
    private String shiftIdOnSaturday;
    /** 日曜日シフト */
    private String shiftIdOnSunday;
    /** 作成ユーザID */
    private String createrEmployeeId;
    /** 作成日付 */
    private Date creationDatetime;
    /** 更新ユーザID */
    private String updaterEmployeeId;
    /** 更新日付 */
    private Date updateDatetime;

    // Getter and Setter methods
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
    public String getShiftIdOnMonday() {
        return shiftIdOnMonday;
    }
    public void setShiftIdOnMonday(String shiftIdOnMonday) {
        this.shiftIdOnMonday = shiftIdOnMonday;
    }
    public String getShiftIdOnTuesday() {
        return shiftIdOnTuesday;
    }
    public void setShiftIdOnTuesday(String shiftIdOnTuesday) {
        this.shiftIdOnTuesday = shiftIdOnTuesday;
    }
    public String getShiftIdOnWednesday() {
        return shiftIdOnWednesday;
    }
    public void setShiftIdOnWednesday(String shiftIdOnWednesday) {
        this.shiftIdOnWednesday = shiftIdOnWednesday;
    }
    public String getShiftIdOnThursday() {
        return shiftIdOnThursday;
    }
    public void setShiftIdOnThursday(String shiftIdOnThursday) {
        this.shiftIdOnThursday = shiftIdOnThursday;
    }
    public String getShiftIdOnFriday() {
        return shiftIdOnFriday;
    }
    public void setShiftIdOnFriday(String shiftIdOnFriday) {
        this.shiftIdOnFriday = shiftIdOnFriday;
    }
    public String getShiftIdOnSaturday() {
        return shiftIdOnSaturday;
    }
    public void setShiftIdOnSaturday(String shiftIdOnSaturday) {
        this.shiftIdOnSaturday = shiftIdOnSaturday;
    }
    public String getShiftIdOnSunday() {
        return shiftIdOnSunday;
    }
    public void setShiftIdOnSunday(String shiftIdOnSunday) {
        this.shiftIdOnSunday = shiftIdOnSunday;
    }
    public String getCreaterEmployeeId() {
        return createrEmployeeId;
    }
    public void setCreaterEmployeeId(String createrEmployeeId) {
        this.createrEmployeeId = createrEmployeeId;
    }
    public Date getCreationDatetime() {
        return creationDatetime;
    }
    public void setCreationDatetime(Date creationDatetime) {
        this.creationDatetime = creationDatetime;
    }
    public String getUpdaterEmployeeId() {
        return updaterEmployeeId;
    }
    public void setUpdaterEmployeeId(String updaterEmployeeId) {
        this.updaterEmployeeId = updaterEmployeeId;
    }
    public Date getUpdateDatetime() {
        return updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}