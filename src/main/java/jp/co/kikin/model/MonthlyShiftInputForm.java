/**
 * ファイル名：MonthlyShiftInputForm.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 */
package jp.co.kikin.model;

import java.util.List;
import java.util.Map;


import jp.co.kikin.model.DateBean;
import lombok.Data;

/**
 * 説明：月別シフトフォーム
 * @author naraki
 *
 */
@Data
public class MonthlyShiftInputForm{

    /** 出勤希望入力リスト */
    private List<MonthlyShiftInputBean> monthlyShiftInputBeanList;
    /** 日付リスト */
    private List<DateBean> dateBeanList;
    /** 年月 */
    private String yearMonth = "";
    /** シフトコンボ */
    private Map<String, String> shiftCmbMap;
    /** 年月コンボ */
    private Map<String, String> yearMonthCmbMap;

    /** ページング用 */
    private String paging;
    /** オフセット */
    private int offset;
    /** 表示ページ */
    private int countPage;
    /** 最大ページ */
    private int maxPage;

       //---------------
    // 登録のため
    //---------------
    /** 希望シフトID集約 */
    private String[] shiftIdList;

    public List<MonthlyShiftInputBean> getMonthlyShiftInputBeanList() {
        return monthlyShiftInputBeanList;
    }

    public void setMonthlyShiftInputBeanList(
            List<MonthlyShiftInputBean> monthlyShiftInputBeanList) {
        this.monthlyShiftInputBeanList = monthlyShiftInputBeanList;
    }

    public List<DateBean> getDateBeanList() {
        return dateBeanList;
    }

    public void setDateBeanList(List<DateBean> dateBeanList) {
        this.dateBeanList = dateBeanList;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Map<String, String> getShiftCmbMap() {
        return shiftCmbMap;
    }

    public void setShiftCmbMap(Map<String, String> shiftCmbMap) {
        this.shiftCmbMap = shiftCmbMap;
    }

    public Map<String, String> getYearMonthCmbMap() {
        return yearMonthCmbMap;
    }

    public void setYearMonthCmbMap(Map<String, String> yearMonthCmbMap) {
        this.yearMonthCmbMap = yearMonthCmbMap;
    }

    public String getPaging() {
        return paging;
    }

    public void setPaging(String paging) {
        this.paging = paging;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
