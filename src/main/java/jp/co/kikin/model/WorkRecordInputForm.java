/**
 * ファイル名：WorkRecordInputForm.java
 *
 * 変更履歴
 * 1.0  2010/11/04 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

// import jp.co.kikin.model.DateBean;

/**
 * 説明：勤務実績入力確認フォーム
 * @author naraki
 *
 */
@Data
public class WorkRecordInputForm {

    /** 勤務実績入力確認BeanList */
    private List<WorkRecordInputBean> workRecordInputList;
    /** 日付リスト */
    private List<DateBean> dateBeanList;
    /** 年月コンボ */
    private Map<String, String> yearMonthCmbMap;
    /** 年月 */
    private String yearMonth;
    /** 社員コンボ */
    private Map<String, String> employeeCmbMap;
    /** 社員ID */
    private String employeeId;
    /** 社員名 */
    private String employeeName;

    /** ページング用 */
    private String paging;


}