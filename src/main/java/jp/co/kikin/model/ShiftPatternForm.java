/**
 * ファイル名：ShiftPatternForm.java
 *
 * 変更履歴
 * 1.0  2010/11/04 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;



/**
 * 説明：基本シフト入力確認フォーム
 * @author naraki
 *
 */
public class ShiftPatternForm {

    /**
     *シリアル
     */
    private static final long serialVersionUID = 1483629197030517493L;

    /** 社員ID */
    private String employeeId;
    /** 社員名 */
    private String employeeName;

    /** 設定済み基本シフトBeanList */
    private List<ShiftPatternBean> shiftPatternBeanList;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<ShiftPatternBean> getShiftPatternBeanList() {
		return shiftPatternBeanList;
	}

	public void setShiftPatternBeanList(List<ShiftPatternBean> shiftPatternBeanList) {
		this.shiftPatternBeanList = shiftPatternBeanList;
	}


}