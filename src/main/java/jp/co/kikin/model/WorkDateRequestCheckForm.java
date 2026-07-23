/**
 * ファイル名：WorkDateRequestCheckForm.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

// import org.apache.struts.action.ActionForm;

// import form.common.DateBean;

/**
 * 説明：出勤希望日確認フォーム
 * @author naraki
 *
 */
@Data
public class WorkDateRequestCheckForm {

    /** 出勤希望確認リスト */
    private List<WorkDateRequestCheckBean> workDateRequestCheckBeanList;
    /** 日付リスト */
    private List<DateBean> dateBeanList;
    /** 年月 */
    private String yearMonth;
    /** シフトコンボ */
    private Map<String, String> shiftCmbMap;
    /** 年月コンボ */
    private Map<String, String> yearMonthCmbMap;
	
}
