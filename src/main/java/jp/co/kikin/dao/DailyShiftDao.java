/**
 * ファイル名：DailyShiftDao.java
 *
 * 変更履歴
 * 1.0  2010/10/23 Kazuya.Naraki
 */
package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.constant.DbConstant.M_employee;
import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.dto.DailyShiftDto;
import jp.co.kikin.service.CommonUtils;

/**
 * 説明：日別シフトDao
 * @author naraki
 *
 */
public class DailyShiftDao extends Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * シフト情報を取得。
     * @param yearMonth 検索対象年月
     * @return 日別シフトリスト
     * @author Kazuya.Naraki
     */
    public List<DailyShiftDto> getDailyShiftDtoList(String yearMonthDay) throws SQLException {
        // 戻り値
        List<DailyShiftDto> dailyShiftDtoList = new ArrayList<DailyShiftDto>();

        try {
            // コネクション接続
            this.connect();

            /*
             * 社員マスタを主テーブルに
             * 特定の日付のシフト情報を取得する。
             */
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT ");
            strSql.append("    emp.employee_id, ");
            strSql.append("    emp.employee_name, ");
            strSql.append("    shift.start_time, ");
            strSql.append("    shift.end_time, ");
            strSql.append("    shift.break_time ");
            strSql.append("FROM ");
            strSql.append("    m_employee emp INNER JOIN  ");
            strSql.append("    (SELECT ");
            strSql.append("        ts.employee_id, ");
            strSql.append("        ms.start_time, ");
            strSql.append("        ms.end_time, ");
            strSql.append("        ms.break_time ");
            strSql.append("    FROM ");
            strSql.append("        t_shift ts LEFT OUTER JOIN  ");
            strSql.append("        m_shift ms ");
            strSql.append("        ON ts.shift_id = ms.shift_id ");
            strSql.append("    WHERE ");
            strSql.append("        year_month_day = ? ");
            strSql.append("    ) shift ON  emp.employee_id = shift.employee_id ");
            strSql.append("ORDER BY ");
            strSql.append("    employee_id, ");
            strSql.append("    start_time ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, yearMonthDay);

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {

                DailyShiftDto dto = new DailyShiftDto();
                dto.setEmployeeName(CommonUtils.changeNullToBlank(rs.getString(M_employee.EMPLOYEE_NAME.getName())));
                dto.setStartTime(CommonUtils.changeNullToBlank(rs.getString(M_shift.START_TIME.getName())));
                dto.setEndTime(CommonUtils.changeNullToBlank(rs.getString(M_shift.END_TIME.getName())));
                dto.setBreakTime(CommonUtils.changeNullToBlank(rs.getString(M_shift.BREAK_TIME.getName())));

                // 取得した値を戻り値のリストにセットする。
                dailyShiftDtoList.add(dto);
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return dailyShiftDtoList;
    }
}
