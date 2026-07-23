/**
 * ファイル名：DateBean.java
 *
 * 変更履歴
 * 1.0  2010/10/13 Kazuya.Naraki
 */
package jp.co.kikin.model;

import jp.co.kikin.constant.CommonConstant.DayOfWeek;

/**
 * 説明：日付情報格納クラス
 * @author naraki
 *
 */
public class DateBean {
    /** 年月日 yyyy/MM/dd */
    private String yearMonthDay;
    /** 曜日 */
    private String weekDay;
    /** 曜日(Enum) */
    private DayOfWeek weekDayEnum;
    /** 祝日フラグ */
    private boolean publicHolidayFlg;

	public String getYearMonthDay() {
		return yearMonthDay;
	}
	public void setYearMonthDay(String yearMonthDay) {
		this.yearMonthDay = yearMonthDay;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public DayOfWeek getWeekDayEnum() {
		return weekDayEnum;
	}
	public void setWeekDayEnum(DayOfWeek weekDayEnum) {
		this.weekDayEnum = weekDayEnum;
	}
	public boolean getPublicHolidayFlg() {
		return publicHolidayFlg;
	}
	public void setPublicHolidayFlg(boolean publicHolidayFlg) {
		this.publicHolidayFlg = false;
	}
}
