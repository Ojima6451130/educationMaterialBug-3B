/**
 * ファイル名：EmployeeMstMntForm.java
 *
 * 変更履歴
 * 1.0  2010/08/23 Kazuya.Naraki
 */
package jp.co.kikin.model;


import java.util.List;
import java.util.Map;

import lombok.Data;


/**
 * 説明：社員マスタメンテナンスフォームクラス
 * @author naraki
 *
 */
@Data
public class EmployeeMstMntForm {

    /** 社員一覧 */
    private List<EmployeeMstMntBean> employeeMstMntBeanList;
    /** 権限コンボ */
    private Map<String, String> authorityCmbMap;
    
}
