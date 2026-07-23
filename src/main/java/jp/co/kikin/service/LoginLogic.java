package jp.co.kikin.service;
/**
 * ファイル名：LoginLogic.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 * Spring 2025/04/09 Hironori.itaki
 */

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import jp.co.kikin.dao.LoginDao;
import jp.co.kikin.dto.EmployeeMstMntDto;
import jp.co.kikin.dto.LoginDto;
import jp.co.kikin.model.LoginForm;
/**
 * 説明：ログイン処理のロジック実装クラス
 *
 * @author naraki
 *
 */
@Service
public class LoginLogic {
    public LoginDto getEmployeeData(LoginForm loginForm) throws SQLException{

        // 社員マスタ検索用エンティティ
        EmployeeMstMntDto m_employeeDtoSearch = new EmployeeMstMntDto();

        // 検索条件セット
        m_employeeDtoSearch.setEmployeeId(loginForm.getEmployeeId()); // 社員ID
        m_employeeDtoSearch.setPassword(loginForm.getPassword());     // パスワード

        // 社員マスタDao
        LoginDao m_EmployeeDao = new LoginDao();
        // 社員情報を取得する。
        LoginDto loginDto = m_EmployeeDao.getEmployee(m_employeeDtoSearch);
        return loginDto;
    }
}
