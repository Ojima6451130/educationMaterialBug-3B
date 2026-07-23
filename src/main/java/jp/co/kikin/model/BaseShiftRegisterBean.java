/**
 * ファイル名：BaseShiftRegisterBean.java
 *
 * 変更履歴
 * 1.0  2010/10/05 Satoshi.Sugi
 */
package jp.co.kikin.model;

/**
 * 説明：基本シフトフォームクラス
 * @author sugi
 *
 */
public class BaseShiftRegisterBean {

	/* シンボル */
	private String symbol = "";
	/* TO時間 */
	private String toTime = "";
	/* FROM時間 */
	private String fromTime = "";
	/* 休憩 */
	private String breakTime = "";

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
}