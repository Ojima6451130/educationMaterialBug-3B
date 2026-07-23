/**
 * ファイル名：WorkDateRequestCheckDto.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 */
package jp.co.kikin.dto;

import lombok.Data;

/**
 * 説明：出勤希望日入力Dto
 * @author naraki
 *
 */
@Data
public class WorkDateRequestCheckDto {
    /** 社員ID */
    private String employeeId;
    /** 社員名 */
    private String employeeName;
    /** 年月日 */
    private String yearMonthDay;
    /** 希望シフト */
    private String myRequestShiftId;
    /** 希望シフトシンボル */
    private String myRequestSymbol;


}
