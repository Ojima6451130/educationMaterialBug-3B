/**
 * ファイル名：LoginDao.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 */
package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.constant.DbConstant.M_employee;
import jp.co.kikin.dto.EmployeeMstMntDto;
import jp.co.kikin.dto.LoginDto;



/**
 * 説明：ログイン処理のロジック
 *
 * @author naraki
 *
 */
public class LoginDao extends Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * ユーザID、パスワードをキーにデータを取得する。(S2daoを使用しない場合)
     *
     * @param 検索用Dto
     * @return 社員マスタ検索Ｄｔｏ
     * @author Kazuya.Naraki
     */
    public LoginDto getEmployee(EmployeeMstMntDto m_employeeDtoSearch) throws SQLException {
        LoginDto loginDto = new LoginDto();
        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT * FROM m_employee ");
            strSql.append("WHERE employee_id = 'sh0001' ");
            strSql.append("AND password = ?");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, m_employeeDtoSearch.getPassword());

            // ログ出力
            log.info(ps);

            // 実行
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            if (rs.next()) {
                loginDto.setEmployeeId(rs.getString(M_employee.EMPLOYEE_ID.getName()));                // 社員ID
                loginDto.setPassword(rs.getString(M_employee.PASSWORD.getName()));                  // パスワード
                loginDto.setEmployeeName(rs.getString(M_employee.EMPLOYEE_NAME.getName()));           // 社員名
                loginDto.setEmployeeNameKana(rs.getString(M_employee.EMPLOYEE_NAME_KANA.getName())); // 社員名カナ
                loginDto.setAuthorityId(rs.getString(M_employee.AUTHORITY_ID.getName()));             // 権限ID
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
        return loginDto;
    }


    /**
     * ユーザID、パスワードをキーにデータを取得する。(S2daoを使用した場合)
     *
     * @param 検索用Dto
     * @return 社員マスタ検索Ｄｔｏ
     * @author Kazuya.Naraki
     */
    // public LoginDto getEmployeeS2(EmployeeMstMntDto m_employeeDtoSearch) throws SQLException {
    //     LoginDto logicDto = new LoginDto();

    //     M_employeeDao dao = (M_employeeDao)container.getComponent(M_employeeDao.class);

    //     s2.entity.M_employee m_employee = dao.getData(m_employeeDtoSearch.getEmployeeId(), m_employeeDtoSearch.getPassword());

    //     if (!CheckUtils.isEmpty(m_employee)) {
    //         logicDto.setEmployeeId(m_employee.getEmployee_id());              // 社員ID
    //         logicDto.setPassword(m_employee.getPassword());                   // パスワード
    //         logicDto.setEmployeeName(m_employee.getEmployee_name());          // 社員名
    //         logicDto.setEmployeeNameKana(m_employee.getEmployee_name_kana()); // 社員名カナ
    //         logicDto.setAuthorityId(m_employee.getAuthority_id());            // 権限ID
    //     } else {
    //         return null;
    //     }

    //     return logicDto;
    // }
}
