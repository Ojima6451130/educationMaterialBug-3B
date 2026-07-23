/**
 * ファイル名：BaseShiftLogic.java
 *
 * 変更履歴
 * 1.0  2010/11/04 Kazuya.Naraki
 */
package jp.co.kikin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jp.co.kikin.dao.BaseShiftDao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.BaseShiftDto;
import jp.co.kikin.exception.CommonException;

/**
 * 説明：ログイン処理のロジック
 * @author nishioka
 *
 */
public class BaseShiftLogic {

    /**
     * シフト、基本シフトのデータを取得する
     *
     * @param employeeId 社員ID
     * @param yearMonth 対象年月
     * @return 勤務実績マップ
     * @author Kazuya.Naraki
     */
    public Map<String, BaseShiftDto> getBaseShiftData() throws SQLException, CommonException {

        BaseShiftDao baseShiftDao = new BaseShiftDao();

        // 基本シフトデータを取得する
        Map<String, BaseShiftDto> baseShiftMap = baseShiftDao.getBaseShiftDataList();

        return baseShiftMap;
    }

    /**
     * 勤務実績データの登録を行う
     *
     * @param employeeId 社員ID
     * @param yearMonth 対象年月
     * @return 勤務実績マップ
     * @author Kazuya.Naraki
     * @throws Exception
     */
    public void registerBaseShift(List<BaseShiftDto> workRecordDtoList, LoginUserDto loginUserDto) throws Exception {

        // 基本シフトDao
        BaseShiftDao baseShiftDao = new BaseShiftDao();
        // コネクション
        Connection connection = baseShiftDao.getConnection();

        // トランザクション処理
        connection.setAutoCommit(false);

        try {
            for (int i = 0; i < workRecordDtoList.size(); i++) {

            	BaseShiftDto baseShiftDto = workRecordDtoList.get(i);
                String employeeId = baseShiftDto.getEmployeeId();

                // データが存在するか確認
                boolean updateFlg = baseShiftDao.isData(employeeId);

                if (updateFlg) {
                    // 更新
                    baseShiftDao.updateWorkRecord(baseShiftDto, loginUserDto.getEmployeeId());
                } else {
                    // 登録
                    baseShiftDao.insertBaseShift(baseShiftDto, loginUserDto.getEmployeeId());
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

}
