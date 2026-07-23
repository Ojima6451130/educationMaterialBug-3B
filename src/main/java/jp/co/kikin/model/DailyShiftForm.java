/**
 * ファイル名：DailyShiftForm.java
 *
 * 変更履歴
 * 1.0  2010/10/23 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;

import lombok.Data;


/**
 * 説明：日別シフトフォーム
 * @author naraki
 *
 */
@Data
public class DailyShiftForm {

    /** 日別シフトBeanリスト（表示一覧）*/
    private List<DailyShiftBean> dailyShiftBeanList;

    /** 表示対象日 */
    private String yearMonthDay;
    /** 表示対象日（画面表示用） */
    private String yearMonthDayDisplay;
	
}
