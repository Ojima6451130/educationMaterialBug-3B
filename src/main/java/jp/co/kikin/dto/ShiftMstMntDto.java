/**
 * ファイル名：ShiftMstMntDto.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 */
package jp.co.kikin.dto;

import java.util.Date;

/**
 * 説明：シフトマスタDto
 * @author naraki
 *
 */
public class ShiftMstMntDto {

    /** シフトID */
    private String shiftId;
    /** シフト名 */
    private String shiftName;
    /** シンボル */
    private String symbol;
    /** 開始時間 */
    private String startTime;
    /** 終了時間 */
    private String endTime;
    /** 休憩時間 */
    private String breakTime;
    /** 作成ユーザID */
    private String CreaterEmployeeId;
    /** 作成日付 */
    private Date CreationDatetime;
    /** 更新ユーザID */
    private String UpdaterEmployeeId;
    /** 更新日付 */
    private Date UpdateDatetime;
    /** 削除フラグ */
    private Boolean deleteFlg;

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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

	public Boolean getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(Boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
}
