/**
 * ファイル名：BaseShiftDao.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kotaro.Nishioka
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
import jp.co.kikin.dto.BaseShiftDto;
import jp.co.kikin.constant.DbConstant.M_base_shift;
import jp.co.kikin.constant.DbConstant.M_employee;

/**
 * 説明：基本シフトDao
 *
 * @author nishioka
 *
 */
public class BaseShiftDao extends Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * シフト、基本シフトのデータを取得する
     *
     * @return 勤務実績マップ
     * @author Kotaro.Nishioka
     */
    public Map<String, BaseShiftDto> getBaseShiftDataList() throws SQLException {
        // 戻り値 Key：稼動日, value：基本シフトDto
        Map<String, BaseShiftDto> baseShiftList = new LinkedHashMap<String, BaseShiftDto>();

        try {
            // コネクション接続
            this.connect();

            /* 基本シフトを取得する */
            StringBuffer strSql = new StringBuffer();
            strSql.append(" SELECT             ");
            strSql.append("     emp.employee_id    ");
            strSql.append("   , emp.employee_name  ");
            strSql.append("   , mbs.monday     ");
            strSql.append("   , mbs.tuesday    ");
            strSql.append("   , mbs.wednesday  ");
            strSql.append("   , mbs.thursday   ");
            strSql.append("   , mbs.friday     ");
            strSql.append("   , mbs.saturday   ");
            strSql.append("   , mbs.sunday     ");
            strSql.append("   , mbs.creation_datetime  ");
            strSql.append("   , mbs.creator_employee_id  ");
            strSql.append("   , mbs.update_datetime  ");
            strSql.append("   , mbs.updater_employee_id  ");
            strSql.append(" FROM           ");
            strSql.append("           m_employee      emp");
            strSql.append(" LEFT JOIN m_base_shift mbs");
            strSql.append(" ON        emp.employee_id = mbs.employee_id ");
            strSql.append(" ORDER BY       ");
            strSql.append("   mbs.employee_id     ");
            strSql.append("   limit 3     ");
            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            // ログ出力
            log.info(ps);

            // 実行
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {
                BaseShiftDto baseShiftDto = new BaseShiftDto();

                baseShiftDto.setEmployeeId        (rs.getString(M_base_shift.EMPLOYEE_ID.getName()));          // 社員ID
                baseShiftDto.setEmployeeName      (rs.getString(M_employee.EMPLOYEE_NAME.getName()));             // 社員名
                baseShiftDto.setShiftIdOnSunday   (rs.getString(M_base_shift.SUNDAY.getName()));            // 日曜日シフトＩＤ
                baseShiftDto.setShiftIdOnTuesday  (rs.getString(M_base_shift.TUESDAY.getName()));           // 火曜日シフトＩＤ
                baseShiftDto.setShiftIdOnWednesday(rs.getString(M_base_shift.WEDNESDAY.getName()));         // 水曜日シフトＩＤ
                baseShiftDto.setShiftIdOnThursday (rs.getString(M_base_shift.THURSDAY.getName()));          // 木曜日シフトＩＤ
                baseShiftDto.setShiftIdOnFriday   (rs.getString(M_base_shift.FRIDAY.getName()));            // 金曜日シフトＩＤ
                baseShiftDto.setShiftIdOnSaturday (rs.getString(M_base_shift.SATURDAY.getName()));          // 土曜日シフトＩＤ

                baseShiftDto.setCreationDatetime(rs.getDate(M_base_shift.CREATION_DATETIME.getName()));          // 作成日
                baseShiftDto.setCreaterEmployeeId(rs.getString(M_base_shift.CREATOR_EMPLOYEE_ID.getName()));    // 作成者
                baseShiftDto.setUpdateDatetime(rs.getDate(M_base_shift.UPDATE_DATETIME.getName()));              // 更新日
                baseShiftDto.setUpdaterEmployeeId(rs.getString(M_base_shift.UPDATER_EMPLOYEE_ID.getName()));    // 更新者

                baseShiftList.put(baseShiftDto.getEmployeeId(), baseShiftDto);
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return baseShiftList;
    }

    /**
     * 基本シフトマスタに対象データが存在するか確認する
     *
     * @param employeeId 社員ID
     * @param workDay 稼働日
     * @return true：あり,false：なし
     * @author Kotaro.Nishioka
     */
    public boolean isData(String employeeId) throws SQLException {
        try {
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT           ");
            strSql.append("    1            ");
            strSql.append("FROM             ");
            strSql.append("    m_base_shift ");
            strSql.append("WHERE            ");
            

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, employeeId);

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
     * 勤務実績のデータを更新する。
     *
     * @param memployeeDto 更新用社員マスタDto
     * @param loginUserDto ログインユーザDto
     * @author Kotaro.Nishioka
     */
    public void updateWorkRecord(BaseShiftDto baseShiftDto, String loginEmployeeId) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            strSql.append(" UPDATE ");
            strSql.append("     m_base_shift ");
            strSql.append(" SET ");
            strSql.append("      monday          = ? ");
            strSql.append("     ,tuesday         = ? ");
            strSql.append("     ,wednesday       = ? ");
            strSql.append("     ,thursday        = ? ");
            strSql.append("     ,friday          = ? ");
            strSql.append("     ,saturday        = ? ");
            strSql.append("     ,sunday          = ? ");
            strSql.append("     ,updater_employee_id = ? ");
            strSql.append("     ,update_datetime       = CURRENT_DATE ");
            strSql.append(" WHERE ");
            strSql.append("     employee_id   = ? ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, baseShiftDto.getShiftIdOnMonday());
            ps.setString(2, baseShiftDto.getShiftIdOnTuesday());
            ps.setString(3, baseShiftDto.getShiftIdOnWednesday());
            ps.setString(4, baseShiftDto.getShiftIdOnThursday());
            ps.setString(5, baseShiftDto.getShiftIdOnFriday());
            ps.setString(6, baseShiftDto.getShiftIdOnSaturday());
            ps.setString(7, baseShiftDto.getShiftIdOnSunday());
            ps.setString(8, loginEmployeeId);
            ps.setString(9, baseShiftDto.getEmployeeId());

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
     * 基本シフトのデータを登録する。
     *
     * @param baseShiftDto 基本シフトＤｔｏ
     * @author Kotaro.Nishioka
     */
    public void insertBaseShift(BaseShiftDto baseShiftDto, String loginEmployeeId) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            strSql.append(" INSERT INTO ");
            strSql.append("     m_base_shift  ");
            strSql.append("     ( ");
            strSql.append("          employee_id        ");
            strSql.append("         ,monday          ");
            strSql.append("         ,tuesday         ");
            strSql.append("         ,wednesday       ");
            strSql.append("         ,thursday        ");
            strSql.append("         ,friday          ");
            strSql.append("         ,saturday        ");
            strSql.append("         ,sunday          ");
            strSql.append("         ,creator_employee_id ");
            strSql.append("         ,creation_datetime       ");
            strSql.append("         ,updater_employee_id ");
            strSql.append("         ,update_datetime       ");
            strSql.append("     ) ");
            strSql.append("     VALUES ");
            strSql.append("     ( ");
            strSql.append("          ? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,? ");
            strSql.append("         ,current_timestamp() ");
            strSql.append("         ,? ");
            strSql.append("         ,current_timestamp() ");
            strSql.append("     ) ");


            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, baseShiftDto.getEmployeeId());
            ps.setString(2, baseShiftDto.getShiftIdOnMonday());
            ps.setString(3, baseShiftDto.getShiftIdOnTuesday());
            ps.setString(4, baseShiftDto.getShiftIdOnWednesday());
            ps.setString(5, baseShiftDto.getShiftIdOnThursday());
            ps.setString(6, baseShiftDto.getShiftIdOnFriday());
            ps.setString(7, baseShiftDto.getShiftIdOnSaturday());
            ps.setString(8, baseShiftDto.getShiftIdOnSunday());
            ps.setString(9, loginEmployeeId);
            ps.setString(10,loginEmployeeId);

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ps.executeUpdate();

        } catch (SQLException e) {
            // 例外発生
            throw e;
        }
    }

}
