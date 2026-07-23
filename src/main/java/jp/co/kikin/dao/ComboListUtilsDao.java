/**
 * ファイル名：ComboListUtilsDao.java
 *
 * 変更履歴
 * 1.0  2010/08/29 Kazuya.Naraki
 */
package jp.co.kikin.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.dao.Dao;

import jp.co.kikin.dto.ComboListUtilsDto;

import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.DbConstant.M_employee;
import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.constant.DbConstant.Mcategory;

/**
 * 説明：分類マスタアクセス部品
 * @author naraki
 *
 */
public class ComboListUtilsDao extends Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 分類マスタより対象レコードを取得しリストで返却する。
     *
     * @param 検索用エンティティ
     * @return 分類マスタ検索Ｄｔｏ
     * @author Kazuya.Naraki
     */
    public Map<String, String> getComboMap(ComboListUtilsDto mcategorySearch,
            boolean blankFlg) throws SQLException {

        Map<String, String> comboMap = new LinkedHashMap<String, String>();

        if (blankFlg) {
            // 空白あり
            comboMap.put("-1", CommonConstant.BLANK);
        }

        boolean displayBool = mcategorySearch.getDisplay();

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT * FROM m_category ");
            strSql.append("WHERE category_id = ? ");
            if (displayBool) {
                strSql.append("AND display = ? ");
            }
            strSql.append("ORDER BY display_order ASC ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            int index = 1;
            // 分類ＩＤ
            ps.setString(index, mcategorySearch.getCategoryId());
            index++;
            // 表示
            if (displayBool) {
                ps.setBoolean(index, displayBool);
                index++;
            }
            // ログ出力
            log.info(ps);

            // 実行
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {
                comboMap.put(rs.getString(Mcategory.CODE.getName()), rs.getString(Mcategory.NAME.getName()));
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return comboMap;
    }

    /**
     * シフトマスタよりコンボ用のマップを作成する。
     * @param 空白ありフラグ
     * @return シフトコンボマップ
     * @author Kazuya.Naraki
     */
    public Map<String, String> getShiftComboMap(boolean blankFlg) throws SQLException{

        // 戻り値
        Map<String, String> comboMap = new LinkedHashMap<String, String>();

        if (blankFlg) {
            // 空白あり
            comboMap.put(CommonConstant.BLANK_ID, CommonConstant.BLANK);
        }

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
           

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {
                comboMap.put(rs.getString(M_shift.SHIFT_ID.getName()), rs.getString(M_shift.SYMBOL.getName()));
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return comboMap;
    }

    /**
     * 社員マスタよりコンボ用のマップを作成する。
     * @param 空白ありフラグ
     * @return 社員コンボマップ
     * @author Kazuya.Naraki
     */
    public Map<String, String> getEmployeeComboMap(boolean blankFlg) throws SQLException{

        // 戻り値
        Map<String, String> comboMap = new LinkedHashMap<String, String>();

        if (blankFlg) {
            // 空白あり
            comboMap.put("-1", CommonConstant.BLANK);
        }

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT employee_id, ");
            strSql.append("CONCAT(CONCAT(employee_id ,':'), employee_name) employee_name ");
            strSql.append("FROM ");
            strSql.append("m_employee ");
            strSql.append("ORDER BY employee_id ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {
                comboMap.put(rs.getString(M_employee.EMPLOYEE_ID.getName()), rs.getString(M_employee.EMPLOYEE_NAME.getName()));
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return comboMap;
    }
}
