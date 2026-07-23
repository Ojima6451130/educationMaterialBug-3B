/**
 * ファイル名：WorkDateRequestInputForm.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 説明：出勤希望日入力フォーム
 * @author naraki
 *
 */
@Data
public class WorkDateRequestInputForm {

    /** 出勤希望入力リスト */
    private List<WorkDateRequestInputBean> workDateRequestInputBeanList;
    /** 日付リスト */
    private List<DateBean> dateBeanList;
    /** 年月 */
    private String yearMonth = "";
    /** シフトコンボ */
    private Map<String, String> shiftCmbMap;
    /** 年月コンボ */
    private Map<String, String> yearMonthCmbMap;

    //---------------
    // 登録のため
    //---------------
    /** 希望シフトID集約 */
    private String[] shiftIdList;
}
