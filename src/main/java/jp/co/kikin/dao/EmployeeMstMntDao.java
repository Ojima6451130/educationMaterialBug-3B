/**
 * ファイル名：EmployeeMstMntDao.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 * 2.0  2024/04/19 Atsuko.Yoshioka
 */
package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.dao.Dao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.EmployeeMstMntDto;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.constant.DbConstant.M_employee;



/**
 * 説明：社員マスタメンテナンスDao
 *
 * @author naraki
 *
 */
public class EmployeeMstMntDao extends Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * ユーザID、パスワードをキーにデータを取得する。
     *
     * @param 検索用Dto
     * @return 社員マスタ検索Ｄｔｏ
     * @author Kazuya.Naraki
     */
    public EmployeeMstMntDto getEmployee(EmployeeMstMntDto m_employeeDtoSearch) throws SQLException {

        EmployeeMstMntDto m_employeeDto = new EmployeeMstMntDto();

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT * FROM ");
            strSql.append("m_employee ");
            strSql.append("WHERE ");
            strSql.append("employee_id = ? ");
            strSql.append("AND password = ?");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, m_employeeDtoSearch.getEmployeeId());
            ps.setString(2, m_employeeDtoSearch.getPassword());

            // ログ出力
            log.info(ps);

            // 実行
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            if (rs.next()) {
                m_employeeDto.setEmployeeId(rs.getString(M_employee.EMPLOYEE_ID.getName()));               // 社員ID
                m_employeeDto.setPassword(rs.getString(M_employee.PASSWORD.getName()));                 // パスワード
                m_employeeDto.setEmployeeName(rs.getString(M_employee.EMPLOYEE_NAME.getName()));           // 社員名
                m_employeeDto.setEmployeeNameKana(rs.getString(M_employee.EMPLOYEE_NAME_KANA.getName())); // 社員名カナ
                m_employeeDto.setAuthorityId(rs.getString(M_employee.AUTHORITY_ID.getName()));             // 権限ID
                m_employeeDto.setCreaterEmployeeId(rs.getString(M_employee.CREATOR_EMPLOYEE_ID.getName())); // 作成者ID
                m_employeeDto.setCreationDatetime(rs.getDate(M_employee.CREATION_DATETIME.getName()));                // 作成日時
                m_employeeDto.setUpdaterEmployeeId(rs.getString(M_employee.UPDATER_EMPLOYEE_ID.getName())); // 更新者ID
                m_employeeDto.setUpdateDatetime(rs.getDate(M_employee.UPDATE_DATETIME.getName()));                // 更新日時
            } else {
                return null;
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return m_employeeDto;
    }

    /**
     * 社員マスタのデータを全件取得する。
     *
     * @return 社員マスタエンティティ
     * @author Kazuya.Naraki
     */
    public List<EmployeeMstMntDto> getEmployeeAllList() throws SQLException{

        // 戻り値
        List<EmployeeMstMntDto> m_employeeList = new ArrayList<EmployeeMstMntDto>();

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT * FROM m_employee ");
            strSql.append("ORDER BY employee_id, pasword ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {
                // 社員情報
                EmployeeMstMntDto m_employeeDto = new EmployeeMstMntDto();

                m_employeeDto.setEmployeeId(rs.getString(M_employee.EMPLOYEE_ID.getName()));                                                 // 社員ID
                m_employeeDto.setPassword(rs.getString(M_employee.PASSWORD.getName()));                                                   // パスワード
                m_employeeDto.setEmployeeName(CommonUtils.changeNullToBlank(rs.getString(M_employee.EMPLOYEE_NAME.getName())));           // 社員名
                m_employeeDto.setEmployeeNameKana(CommonUtils.changeNullToBlank(rs.getString(M_employee.EMPLOYEE_NAME_KANA.getName()))); // 社員名カナ
                m_employeeDto.setAuthorityId(CommonUtils.changeNullToBlank(rs.getString(M_employee.AUTHORITY_ID.getName())));             // 権限ID
                m_employeeDto.setCreaterEmployeeId(CommonUtils.changeNullToBlank(rs.getString(M_employee.CREATOR_EMPLOYEE_ID.getName()))); // 作成者ID
                m_employeeDto.setCreationDatetime(rs.getDate(M_employee.CREATION_DATETIME.getName()));                                                  // 作成日時
                m_employeeDto.setUpdaterEmployeeId(CommonUtils.changeNullToBlank(rs.getString(M_employee.UPDATER_EMPLOYEE_ID.getName()))); // 更新者ID
                m_employeeDto.setUpdateDatetime(rs.getDate(M_employee.UPDATE_DATETIME.getName()));                                                  // 更新日時

                // 取得した値を戻り値のリストにセットする。
                m_employeeList.add(m_employeeDto);
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return m_employeeList;
    }

    /**
     * 社員マスタのデータを更新する。
     *
     * @param m_employeeDto 更新用社員マスタDto
     * @param loginUserDto ログインユーザDto
     * @author Kazuya.Naraki
     */
    public void updateEmployeeMst(EmployeeMstMntDto m_employeeDto, LoginUserDto loginUserDto) throws SQLException{

        try {
            // コネクションの取得

            StringBuffer strSql = new StringBuffer();
            strSql.append("UPDATE ");
            strSql.append("m_employee ");
            strSql.append("password = ?, ");
            strSql.append("employee_name = ?, ");
            strSql.append("employee_name_kana = ?, ");
            strSql.append("authority_id = ?, ");
            strSql.append("updater_employee_id = ?, ");
            strSql.append("update_datetime = current_timestamp() ");
            strSql.append("WHERE ");
            strSql.append("employee_id = ? ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, m_employeeDto.getPassword());
            ps.setString(2, m_employeeDto.getEmployeeName());
            ps.setString(3, m_employeeDto.getEmployeeNameKana());
            ps.setString(4, m_employeeDto.getAuthorityId());
            ps.setString(5, loginUserDto.getEmployeeId());
            ps.setString(6, m_employeeDto.getEmployeeId());

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
     * 社員マスタのデータを削除する。
     *
     * @param employeeId 社員ＩＤ
     * @return なし
     * @author Kazuya.Naraki
     */
    public void deleteEmployeeMst(String employeeId) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            strSql.append("DELETE FROM ");
            strSql.append("m_employee ");
            strSql.append("WHERE ");
            strSql.append("employee_id = ? ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, employeeId);

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
     * 社員マスタのデータを登録する。
     *
     * @param m_employeeDto 社員マスタＤｔｏ
     * @author Kazuya.Naraki
     */
    public void registerEmployeeMst(EmployeeMstMntDto m_employeeDto, LoginUserDto loginUserDto) throws SQLException{

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
           
            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, m_employeeDto.getEmployeeId());
            ps.setString(2, m_employeeDto.getPassword());
            ps.setString(3, m_employeeDto.getEmployeeName());
            ps.setString(4, m_employeeDto.getEmployeeNameKana());
            ps.setString(5, m_employeeDto.getAuthorityId());
            ps.setString(6, loginUserDto.getEmployeeId());
            ps.setString(7, loginUserDto.getEmployeeId());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ps.executeUpdate();

        } catch (SQLException e) {
            // 例外発生
            throw e;

        } finally {
            // コネクション切断
            disConnect();
        }
    }

}
