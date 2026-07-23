/**
 * ファイル名：EmployeeMstMntLogic.java
 *
 * 変更履歴
 * 1.0  2010/08/24 Kazuya.Naraki
 */
package jp.co.kikin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.EmployeeMstMntDto;
import jp.co.kikin.CheckUtils;
import jp.co.kikin.service.CommonUtils;

import jp.co.kikin.dao.EmployeeMstMntDao;

import jp.co.kikin.constant.DbConstant.M_employee;

/**
 * 説明：社員マスタメンテナンス処理のロジック
 * @author naraki
 *
 */
public class EmployeeMstMntLogic {

    /**
     * 社員マスタの更新系の処理を行う
     * @param employeeMstMntDtoList 更新対象社員マスタDtoリスト
     *
     * @param loginUserDto ログインユーザーDto
     * @author naraki
     */
    public void updateM_employee(List<EmployeeMstMntDto> employeeMstMntDtoList, LoginUserDto loginUserDto) throws Exception{

        // 社員マスタDao
        EmployeeMstMntDao employeeMstMntDao = new EmployeeMstMntDao();
        // コネクション
        Connection connection = employeeMstMntDao.getConnection();

        // トランザクション処理
        connection.setAutoCommit(false);

        try {
            for (int i = 0; i < employeeMstMntDtoList.size(); i++) {

                EmployeeMstMntDto employeeMstMntDto = employeeMstMntDtoList.get(i);
                boolean deleteFlg = employeeMstMntDto.getDeleteFlg();

                if (deleteFlg) {
                    // 削除
                    employeeMstMntDao.updateEmployeeMst(employeeMstMntDto, loginUserDto);
                } else {
                    // 更新
                    employeeMstMntDao.updateEmployeeMst(employeeMstMntDto, loginUserDto);
                }

            }
        } catch (Exception e) {
            // ロールバック処理
            connection.rollback();

            // 切断
            connection.close();

            throw e;
        }

        // コミット
        connection.commit();
        // 切断
        connection.close();

    }

    /**
     * 社員マスタの登録処理を行う
     * @param memployeeDtoList 更新対象社員マスタDtoリスト
     *
     * @param loginUserDto ログインユーザーDto
     * @author naraki
     */
    public void registerM_employee(EmployeeMstMntDto employeeMstMntDto, LoginUserDto loginUserDto) throws Exception{

        // 社員マスタDao
        EmployeeMstMntDao employeeMstMntDao = new EmployeeMstMntDao();

        // 社員ＩＤを採番する。
        CommonUtils commonUtils = new CommonUtils();
        String nextEmployeeID = commonUtils.getNextId(M_employee.TABLE_NAME.getName());

        employeeMstMntDto.setEmployeeId(nextEmployeeID);

        // 登録
        employeeMstMntDao.registerEmployeeMst(employeeMstMntDto, loginUserDto);

    }

    /**
     * 社員マスタ情報を取得する。
     * @return 社員マスタリスト
     * @author naraki
     */
    public List<EmployeeMstMntDto> getEmployeeData(LoginUserDto loginUserDto) throws SQLException{

        // 社員マスタDao
        EmployeeMstMntDao employeeMstMntDao = new EmployeeMstMntDao();

        // 社員情報を取得する。
        List<EmployeeMstMntDto> m_employeeList = null;

        // 自分をリストから省く
        // 削除対象
        EmployeeMstMntDto removeM_employeeDto = null;

        for (EmployeeMstMntDto m_employeeDto : m_employeeList) {

            String employeeId = m_employeeDto.getEmployeeId();

            if (loginUserDto.getEmployeeId().equals(employeeId)) {

                removeM_employeeDto = m_employeeDto;
                break;
            }
        }

        if (!CheckUtils.isEmpty(removeM_employeeDto)) {
            // 削除対象が空でない場合
            m_employeeList.remove(removeM_employeeDto);
        }

        return m_employeeList;
    }
}
