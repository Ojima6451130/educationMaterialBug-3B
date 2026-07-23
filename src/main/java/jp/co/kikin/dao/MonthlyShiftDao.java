/**
 * ファイル名：MonthlyShiftDao.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
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
import jp.co.kikin.constant.DbConstant.T_Shift;
import jp.co.kikin.dao.Dao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.MonthlyShiftDto;
import jp.co.kikin.dto.WorkDateRequestCheckDto;
import jp.co.kikin.service.CommonUtils;

/**
 * 説明：月別シフトのDao
 * @author naraki
 *
 */
public class MonthlyShiftDao extends Dao{

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * シフトテーブルのデータを指定した条件で検索する。
     * @param yearMonth 検索対象年月
     * @param shiftFlg true：シフトIDを取得 false：希望シフトIDを取得
     * @return 出勤希望Dtoリスト
     * @author Kazuya.Naraki
     */
    public List<MonthlyShiftDto> getShiftTblData(String yearMonth, boolean shiftFlg) throws SQLException {
        // 戻り値
        List<MonthlyShiftDto> monthlyShiftDtoList = new ArrayList<MonthlyShiftDto>();

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT ");
            strSql.append("    emp.employee_id, ");
            strSql.append("    emp.employee_name, ");
            strSql.append("    ts.year_month_day, ");

            if (shiftFlg) {
                strSql.append("    ts.shift_id AS shift_id, ");
            } else {
                strSql.append("    ts.request_shift_id AS shift_id, ");
            }
            strSql.append("(SELECT ms.symbol FROM m_shift ms WHERE ms.shift_id = ts.shift_id) AS symbol ");
            strSql.append("FROM ");
            strSql.append("    m_employee emp LEFT OUTER JOIN  ");
            strSql.append("    (SELECT * FROM t_shift WHERE     SUBSTRING(year_month_day, 1, 6) = ?)  ");
            strSql.append("    ts ON emp.employee_id = ts.employee_id ");
            strSql.append("WHERE emp.employee_name not like '社員%' ");
            strSql.append("ORDER BY ");
            strSql.append("    employee_id, ");
            strSql.append("    year_month_day ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, yearMonth);

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {
                MonthlyShiftDto monthlyShiftDto = new MonthlyShiftDto();
                monthlyShiftDto.setEmployeeId(rs.getString(M_employee.EMPLOYEE_ID.getName()));
                monthlyShiftDto.setEmployeeName(rs.getString(M_employee.EMPLOYEE_NAME.getName()));
                monthlyShiftDto.setYearMonthDay(rs.getString(T_Shift.YEAR_MONTH_DAY.getName()));
                monthlyShiftDto.setShiftId(rs.getString(T_Shift.SHIFT_ID.getName()));
                monthlyShiftDto.setSymbol(rs.getString(M_shift.SYMBOL.getName()));

                monthlyShiftDtoList.add(monthlyShiftDto);
            }

            // 最後の社員分
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return monthlyShiftDtoList;
    }

    /**
     * シフトテーブルに対象データが存在するか確認する
     *
     * @param employeeId 社員ID
     * @param yearMonthDay 対象日
     * @return true：あり,false：なし
     * @author Kazuya.Naraki
     */
    public boolean isData(String employeeId, String yearMonthDay) throws SQLException {
        try {
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT ");
            strSql.append("    * ");
            strSql.append("FROM ");
            strSql.append("    t_shift ");
            strSql.append("WHERE ");
            strSql.append("    employee_id = ? AND ");
            strSql.append("    year_month_day = ? ");


            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, employeeId);
            ps.setString(2, yearMonthDay);

            // ログ出力
            log.info(ps);

            // 実行
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        }
    }

    /**
     * シフトテーブルのデータを登録する。
     *
     * @param monthlyShiftDto 月別シフトＤｔｏ
     * @author Kazuya.Naraki
     */
    public void registerShiftTbl(MonthlyShiftDto monthlyShiftDto, LoginUserDto loginUserDto) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, monthlyShiftDto.getEmployeeId());
            ps.setString(2, monthlyShiftDto.getYearMonthDay());
            ps.setString(3, monthlyShiftDto.getShiftId());
            ps.setString(4, loginUserDto.getEmployeeId());
            ps.setString(5, loginUserDto.getEmployeeId());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ps.executeUpdate();

        } catch (SQLException e) {
            // 例外発生
            throw e;
        }
    }


    /**
     * シフトテーブルのデータを更新する。
     *
     * @param monthlyShiftDto 月別シフトDto
     * @param loginUserDto ログインユーザDto
     * @author Kazuya.Naraki
     */
    public void updateShiftTbl(MonthlyShiftDto monthlyShiftDto, LoginUserDto loginUserDto) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            strSql.append("UPDATE ");
            strSql.append("t_shift ");
            strSql.append("SET ");
            strSql.append("shift_id = ?, ");
            strSql.append("updater_employee_id = ?, ");
            strSql.append("update_datetime = current_timestamp() ");
            strSql.append("WHERE ");
            strSql.append("employee_id = ? ");
            


            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, monthlyShiftDto.getShiftId());
            ps.setString(2, loginUserDto.getEmployeeId());
            ps.setString(3, monthlyShiftDto.getEmployeeId());
            ps.setString(4, monthlyShiftDto.getYearMonthDay());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ps.executeUpdate();

        } catch (SQLException e) {
            // 例外発生
            throw e;
        }
    }


    /**
     * 社員分の希望シフトリストのリストを取得する。
     * @param yearMonth 検索対象年月
     * @return 出勤希望Dtoリスト
     * @author Kazuya.Naraki
     */
    public List<List<WorkDateRequestCheckDto>> getShiftTblNestedList(String yearMonth) throws SQLException {
        // 戻り値
        List<List<WorkDateRequestCheckDto>> shiftRequestCheckDtoNestedList = new ArrayList<List<WorkDateRequestCheckDto>>();
        List<WorkDateRequestCheckDto> shiftRequestCheckDtoList = new ArrayList<WorkDateRequestCheckDto>();

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT ");
            strSql.append("emp.employee_id, ");
            strSql.append("emp.employee_name, ");
            strSql.append("ts.year_month_day, ");
            strSql.append("ts.request_shift_id, ");
            strSql.append("ts.symbol ");
            strSql.append("FROM ");
            strSql.append("m_employee emp ");
            strSql.append("LEFT OUTER JOIN ");
            strSql.append("(SELECT ");
            strSql.append("employee_id, ");
            strSql.append("request_shift_id, ");
            strSql.append("ms.symbol, ");
            strSql.append("year_month_day ");
            strSql.append("FROM ");
            strSql.append("t_shift ts RIGHT OUTER JOIN ");
            strSql.append("m_shift ms ON ");
            strSql.append("ts.request_shift_id = ");
            strSql.append("ms.shift_id ");
            strSql.append("WHERE ");
            strSql.append("SUBSTRING(year_month_day, 1, 6) = ?) ts  ON ");
            strSql.append("emp.employee_id = ts.employee_id ");
            strSql.append("ORDER BY ");
            strSql.append("employee_id,");
            strSql.append("year_month_day");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, yearMonth);

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            String employeeId = "";

            // 取得結果セット
            while (rs.next()) {

                WorkDateRequestCheckDto monthlyShiftDto = new WorkDateRequestCheckDto();
                String newEmployeeId = rs.getString(M_employee.EMPLOYEE_ID.getName());
                if ("".equals(employeeId)) {
                    // 初回
                    employeeId = newEmployeeId;
                } else if (newEmployeeId.equals(employeeId)) {
                    // 同一社員のデータ
                    // 特になにもしない
                } else {
                    // 別の社員のデータに切り替わる場合

                    // 戻り値のリストに前の社員分のリストを追加する。
                    shiftRequestCheckDtoNestedList.add(shiftRequestCheckDtoList);

                    // 比較対象を入れ替える。
                    employeeId = newEmployeeId;

                    shiftRequestCheckDtoList = new ArrayList<WorkDateRequestCheckDto>();

                }

                monthlyShiftDto.setEmployeeId(newEmployeeId);
                monthlyShiftDto.setEmployeeName(rs.getString(M_employee.EMPLOYEE_NAME.getName()));
                monthlyShiftDto.setYearMonthDay(rs.getString(T_Shift.YEAR_MONTH_DAY.getName()));
                monthlyShiftDto.setMyRequestShiftId(rs.getString(T_Shift.REQUEST_SHIFT_ID.getName()));
                monthlyShiftDto.setMyRequestSymbol(CommonUtils.changeNullToHyphen(rs.getString(M_shift.SYMBOL.getName())));
                // 取得した値を戻り値のリストにセットする。
                shiftRequestCheckDtoList.add(monthlyShiftDto);
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }

        // 最後の社員分のリストを追加する
        shiftRequestCheckDtoNestedList.add(shiftRequestCheckDtoList);

        return shiftRequestCheckDtoNestedList;
    }


}
