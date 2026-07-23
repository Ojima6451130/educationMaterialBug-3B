/**
 * ファイル名：BaseShiftPatternBean.java
 *
 * 変更履歴
 * 1.0  2010/11/04 Kazuya.Naraki
 */
package jp.co.kikin.model;

/**
 * 説明：基本シフト凡例
 * @author nishioka
 *
 */
public class BaseShiftPatternBean {

    /** シフト名 */
    private String shiftName;
    /** シンボル */
    private String symbol;
    /** 時間帯 */
    private String timeZone;
    /** 休憩 */
    private String breakTime;

	public String getShiftName() {
		return symbol;
	}
	public void setShiftName(String shiftName) {
		this.symbol = shiftName;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
}
