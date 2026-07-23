/**
 * ファイル名：ShiftPatternBean.java
 *
 * 変更履歴
 * 1.0  2010/11/04 Kazuya.Naraki
 */
package jp.co.kikin.model;

import lombok.Data;

/**
 * 説明：基本シフト凡例
 * @author nishioka
 *
 */
@Data
public class ShiftPatternBean {

    /** シフト名 */
    private String shiftName;
    /** シンボル */
    private String symbol;
    /** 時間帯 */
    private String timeZone;
    /** 休憩 */
    private String breakTime;

}
