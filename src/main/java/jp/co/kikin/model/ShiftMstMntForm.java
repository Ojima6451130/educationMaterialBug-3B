/**
 * ファイル名：ShiftMstMntForm.java
 *
 * 変更履歴
 * 1.0  2010/08/23 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;


/**
 * 説明：シフトマスタメンテナンスフォームクラス
 * @author naraki
 *
 */
public class ShiftMstMntForm extends ShiftMstMntBean{

    /** シフト */
    private List<ShiftMstMntBean> shiftMstMntBeanList;

    public List<ShiftMstMntBean> getShiftMstMntBeanList() {
        return shiftMstMntBeanList;
    }

    public void setShiftMstMntBeanList(List<ShiftMstMntBean> shiftMstMntBeanList) {
        this.shiftMstMntBeanList = shiftMstMntBeanList;
    }

}
