/**
 * ファイル名：ComboListUtilLogic.java
 *
 * 変更履歴
 * 1.0  2010/08/29 Kazuya.Naraki
 */
package jp.co.kikin.service;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jp.co.kikin.dao.ComboListUtilsDao;
import jp.co.kikin.dto.ComboListUtilsDto;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.DbConstant.Mcategory;
import jp.co.kikin.exception.CommonException;

/**
 * 説明：コンボボックス生成共通部品
 * @author naraki
 *
 */
@Service
public class ComboListUtilLogic {

    /** 年月コンボ区分(前年) */
    public static final int KBN_YEARMONTH_PRE = 0;
    /** 年月コンボ区分(次年) */
    public static final int KBN_YEARMONTH_NEXT = 1;
    /** 年月コンボ区分(前年次年) */
    public static final int KBN_YEARMONTH_PRE_NEXT= 2;

    // 分類マスタDao
    private ComboListUtilsDao mcategoryDao = new ComboListUtilsDao();


    /**
     * コンボボックスのマップを取得する。
     * @param categoryId 分類ＩＤ
     * @param display 表示対象を絞りこむために使用するカラム名
     * @param blankFlg 空白有りフラグ true：空白あり false：空白行なし
     * @return
     * @author naraki
     */
    public Map<String, String> getCombo(String categoryId, String display,
            boolean blankFlg) throws SQLException, CommonException {

        // 検索条件
        ComboListUtilsDto mcategorySearch = new ComboListUtilsDto();

        // 分類ＩＤ
        mcategorySearch.setCategoryId(categoryId);

        // 表示対象を設定する
        if (Mcategory.DISPLAY.getName().equals(display)) {
            mcategorySearch.setDisplay(true);
        } else {
            throw new CommonException("不正な値 display：" + display);
        }

        // コンボマップを取得する。
        Map<String, String> cmbMap = mcategoryDao.getComboMap(mcategorySearch,
                                                            blankFlg);

        return cmbMap;
    }

    /**
     * コンボボックス(社員)のマップを取得する。
     * @param blankFlg 空白有りフラグ true：空白あり false：空白行なし
     * @return
     * @author naraki
     */
    public Map<String, String> getComboEmployee(boolean blankFlg) throws SQLException, CommonException {

        // コンボマップを取得する。
        Map<String, String> cmbMap = mcategoryDao.getEmployeeComboMap(blankFlg);

        return cmbMap;
    }

    /**
     * コンボボックス(シフト)のマップを取得する。
     * @param blankFlg 空白有りフラグ true：空白あり false：空白行なし
     * @return key：シフトID,value：シンボル
     * @author naraki
     */
    public Map<String, String> getComboShift(boolean blankFlg) throws SQLException, CommonException {

        // コンボマップを取得する。
        Map<String, String> cmbMap = mcategoryDao.getShiftComboMap(blankFlg);

        return cmbMap;
    }

    /**
     * コンボボックス(年月)のマップを取得する。
     * @param yearMonth 対象年月
     * @param count 対象年月から表示させる月数
     * @param blankFlg 空白有りフラグ true：空白あり false：空白行なし
     * @return
     * @author naraki
     */
    public Map<String, String> getComboYearMonth(String yearMonth, int count, int kbn, boolean blankFlg) throws SQLException, CommonException {

        // コンボマップを取得する。
        Map<String, String> cmbMap = new LinkedHashMap<String, String>();

        // 空白追加
        if (blankFlg) {
            // 空白あり
            cmbMap.put("-1", CommonConstant.BLANK);
        }

        // 年
        int intYear = Integer.parseInt(yearMonth.substring(0, 4));
        // 月
        int intMonth = Integer.parseInt(yearMonth.substring(4, 6)) - 1;
        // 日
        final int DAY = 1;

        Calendar calendar = new GregorianCalendar(intYear, intMonth, DAY);

        StringBuffer strBuf = new StringBuffer();
        intYear = calendar.get(Calendar.YEAR);
        intMonth = calendar.get(Calendar.MONTH) + 1;

        strBuf.append(String.valueOf(intYear));
        if (intMonth < 10) {
            strBuf.append("0");
        }
        strBuf.append(String.valueOf(intMonth));


        if (kbn == KBN_YEARMONTH_PRE) {
            // 対象年月から前の年月

            cmbMap.put(strBuf.toString(), CommonUtils.addSlash(strBuf.toString()));

            for (int i = count; i >= 0; i--) {
                strBuf = new StringBuffer();
                // 加算処理
                CommonUtils.add(calendar, 0, -1, 0);

                this.putYearMonthMap(calendar, cmbMap);
            }
        } else if (kbn == KBN_YEARMONTH_NEXT) {
            // 対象年月から次の年月

            cmbMap.put(strBuf.toString(), CommonUtils.addSlash(strBuf.toString()));

            for (int i = 0; i < count; i++) {

                // 加算処理
                CommonUtils.add(calendar, 0, 1, 0);

                this.putYearMonthMap(calendar, cmbMap);
            }
        } else {
            // 対象年月から前年、次年
            CommonUtils.add(calendar, 0, -count, 0);

            this.putYearMonthMap(calendar, cmbMap);

            for (int i = 0; i < count * 2; i++) {

                // 加算処理
                CommonUtils.add(calendar, 0, 1, 0);

                this.putYearMonthMap(calendar, cmbMap);
            }

        }

        return cmbMap;
    }

    /**
     * 年月コンボのマップに値をセットする
     * @param calendar カレンダー
     * @param cmbMap 年月コンボマップ
     * @author naraki
     */
    private void putYearMonthMap(Calendar calendar, Map<String, String> cmbMap) {

        StringBuffer strBuf = new StringBuffer();

        // 年、月を取得する
        int intYear = calendar.get(Calendar.YEAR);
        int intMonth = calendar.get(Calendar.MONTH) + 1;

        strBuf.append(String.valueOf(intYear));
        if (intMonth < 10) {
            strBuf.append("0");
        }
        strBuf.append(String.valueOf(intMonth));
        cmbMap.put(strBuf.toString(), CommonUtils.addSlash(strBuf.toString()));

    }


}
