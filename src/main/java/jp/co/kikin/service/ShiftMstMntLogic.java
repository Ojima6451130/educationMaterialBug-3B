/**
 * ファイル名：ShiftMstMntLogic.java
 *
 * 変更履歴
 * 1.0  2010/08/24 Kazuya.Naraki
 */
package jp.co.kikin.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.dao.ShiftMstMntDao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.ShiftMstMntDto;

/**
 * 説明：シフトマスタメンテナンス処理のロジック
 * @author naraki
 *
 */
@Service
public class ShiftMstMntLogic {

    /**
     * シフトマスタの更新系の処理を行う
     * @param shiftMstMntDtoList 更新対象シフトマスタDtoリスト
     *
     * @param loginUserDto ログインユーザーDto
     * @author naraki
     */
    public void updateShiftMst(List<ShiftMstMntDto> shiftMstMntDtoList, LoginUserDto loginUserDto) throws Exception{

        // シフトマスタDao
        ShiftMstMntDao shiftMstMntDao = new ShiftMstMntDao();
        // コネクション
        Connection connection = shiftMstMntDao.getConnection();

        // トランザクション処理
        connection.setAutoCommit(false);

        try {
            for (int i = 0; i < shiftMstMntDtoList.size(); i++) {

                ShiftMstMntDto shiftMstMntDto = shiftMstMntDtoList.get(i);
                boolean deleteFlg = shiftMstMntDto.getDeleteFlg();

                if (deleteFlg) {
                    // 削除
                    shiftMstMntDao.deleteShiftMst(shiftMstMntDto.getShiftId());
                } else {
                    // 更新
                    shiftMstMntDao.updateShiftMst(shiftMstMntDto, loginUserDto);
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
     * シフトマスタの登録処理を行う
     * @param shiftMstMntDto 更新対象シフトマスタDto
     * @param loginUserDto ログインユーザーDto
     * @author naraki
     */
    public void registerMshift(ShiftMstMntDto shiftMstMntDto, LoginUserDto loginUserDto) throws Exception{

        // シフトマスタDao
        ShiftMstMntDao shiftMstMntDao = new ShiftMstMntDao();

        // シフトＩＤを採番する。
        CommonUtils commonUtils = new CommonUtils();
        String nextID = commonUtils.getNextId(M_shift.TABLE_NAME.getName());

        shiftMstMntDto.setShiftId(nextID);

        // 登録
        shiftMstMntDao.registerShiftMst(shiftMstMntDto, loginUserDto);

    }

    /**
     * シフトマスタ情報を取得する。
     * @return シフトマスタリスト
     * @author naraki
     */
    public List<ShiftMstMntDto> getShiftData(LoginUserDto loginUserDto) throws SQLException{

        // シフトマスタDao
        ShiftMstMntDao shiftMstMntDao = new ShiftMstMntDao();

        // シフト情報を取得する。
        List<ShiftMstMntDto> shiftMstMntDtoList = shiftMstMntDao.getAllList();

        return shiftMstMntDtoList;
    }
}
