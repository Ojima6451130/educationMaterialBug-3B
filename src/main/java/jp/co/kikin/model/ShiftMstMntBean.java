/**
 * ファイル名：ShiftMstMntBean.java
 *
 * 変更履歴
 * 1.0  2010/08/23 Kazuya.Naraki
 */
package jp.co.kikin.model;

import jp.co.kikin.dto.ShiftMstMntDto;
import lombok.Data;
/**
 * 説明：シフトマスタメンテナンスフォームクラス
 * @author naraki
 *
 */

public class ShiftMstMntBean{

/** シフトＩＤ */
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
/** 削除シフトＩＤ */
private String deleteShiftId;
/** 削除シフト */
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
public String getDeleteShiftId() {
    return deleteShiftId;
}
public void setDeleteShiftId(String deleteShiftId) {
    this.deleteShiftId = deleteShiftId;
}
public boolean getDeleteFlg() {
    return deleteFlg;
}
public void setDeleteFlg(Boolean deleteFlg) {
    this.deleteFlg = deleteFlg;
}

}
