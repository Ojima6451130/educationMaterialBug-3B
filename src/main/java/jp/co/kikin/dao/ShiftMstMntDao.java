/**
 * ファイル名：ShiftMstMntDao.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 */
package jp.co.kikin.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import jp.co.kikin.dao.Dao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.ShiftMstMntLogic;
import jp.co.kikin.dto.ShiftMstMntDto;

import jp.co.kikin.constant.DbConstant.M_shift;



/**
 * 説明：シフトマスタメンテナンスDao
 *
 * @author naraki
 *
 */
public class ShiftMstMntDao extends Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * シフトマスタのデータを全件取得する。
     *
     * @return 社員マスタエンティティ
     * @author Kazuya.Naraki
     */
    public List<ShiftMstMntDto> getAllList() throws SQLException{

        // 戻り値
        List<ShiftMstMntDto> mshiftDataList = new ArrayList<ShiftMstMntDto>();

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT * FROM ");
            strSql.append("m_shift ");
            strSql.append(" ORDER BY start_time desc ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            // ログ出力
            log.info(ps);

            // SQLを実行する
            ResultSet rs = ps.executeQuery();

            // 取得結果セット
            while (rs.next()) {

                ShiftMstMntDto shiftMstMntDto = new ShiftMstMntDto();
                shiftMstMntDto.setShiftId(rs.getString(M_shift.SHIFT_ID.getName())); // シフトID
                shiftMstMntDto.setShiftName(rs.getString(M_shift.SHIFT_NAME.getName())); // パスワード
                shiftMstMntDto.setSymbol(CommonUtils.changeNullToBlank(rs.getString(M_shift.SHIFT_NAME.getName()))); // シンボル
                shiftMstMntDto.setStartTime(rs.getString(M_shift.START_TIME.getName())); // 開始時間
                shiftMstMntDto.setEndTime(rs.getString(M_shift.END_TIME.getName())); // 終了時間
                shiftMstMntDto.setBreakTime(rs.getString(M_shift.BREAK_TIME.getName())); // 休憩時間
                shiftMstMntDto.setCreaterEmployeeId(CommonUtils.changeNullToBlank(rs.getString(M_shift.CREATOR_EMPLOYEE_ID.getName()))); // 作成者ID
                shiftMstMntDto.setCreationDatetime(rs.getDate(M_shift.CREATION_DATETIME.getName())); // 作成日時
                shiftMstMntDto.setUpdaterEmployeeId(CommonUtils.changeNullToBlank(rs.getString(M_shift.UPDATER_EMPLOYEE_ID.getName()))); // 更新者ID
                shiftMstMntDto.setUpdateDatetime(rs.getDate(M_shift.UPDATE_DATETIME.getName())); // 更新日時

                // 取得した値を戻り値のリストにセットする。
                mshiftDataList.add(shiftMstMntDto);
            }
        } catch (SQLException e) {
            // 例外発生
            throw e;
        } finally {
            // コネクション切断
            disConnect();
        }
        return mshiftDataList;
    }
     /**
     * 新規カラムを取得する。
     *
     * @return 社員マスタエンティティ
     * @author Yokota.Yuito
     */
    public void selectNewCalam(ShiftMstMntDto mshiftDto)throws SQLException{
        // コネクション接続
        this.connect();

        StringBuffer strSql = new StringBuffer();
        strSql.append("SELECT ");
        strSql.append("shift_id, ");
        strSql.append("CASE ");
        strSql.append("WHEN shift_id IN ( ");
        strSql.append("  SELECT shift_id FROM t_shift WHERE shift_id = ? ");
        strSql.append(") THEN 1 ");
        strSql.append("ELSE 0 ");
        strSql.append("END AS use_flg ");
        strSql.append("FROM m_shift ");

        PreparedStatement ps = connection.prepareStatement(strSql.toString());
        ps.setString(1, mshiftDto.getShiftId());
        // ログ出力
        log.info(ps);

        // SQLを実行する
        ResultSet rs = ps.executeQuery();
        
}

    /**
     * シフトマスタのデータを更新する。
     *
     * @param mshiftDto 更新用シフトマスタDto
     * @param loginUserDto ログインユーザDto
     * @author Kazuya.Naraki
     */
    public void updateShiftMst(ShiftMstMntDto mshiftDto, LoginUserDto loginUserDto) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            strSql.append("UPDATE ");
            strSql.append("m_shift ");
            strSql.append("SET ");
            strSql.append("shift_name = ?, ");
            strSql.append("symbol = ?, ");
            strSql.append("start_time = ?, ");
            strSql.append("end_time = ?, ");
            strSql.append("break_time = ?, ");
            strSql.append("updater_employee_id = ?, ");
            strSql.append("update_datetime = current_timestamp() ");
            strSql.append(" WHERE ");
            strSql.append("shift_id = ? ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, mshiftDto.getShiftName());
            ps.setString(2, mshiftDto.getSymbol());
            ps.setString(3, mshiftDto.getStartTime());
            ps.setString(4, mshiftDto.getEndTime());
            ps.setString(5, mshiftDto.getBreakTime());
            ps.setString(6, loginUserDto.getEmployeeId());
            ps.setString(7, mshiftDto.getShiftId());

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
     * シフトマスタのデータを削除する。
     *
     * @param shiftId シフトＩＤ
     * @return なし
     * @author Kazuya.Naraki
     */
    public void deleteShiftMst(String shiftId) throws SQLException{

        try {

            StringBuffer strSql = new StringBuffer();
            strSql.append("DELETE FROM ");
            strSql.append("m_shift ");
            strSql.append("WHERE ");
           

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, shiftId);

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
     * シフトマスタのデータを登録する。
     *
     * @param shiftMstMntDto シフトマスタＤｔｏ
     * @author Kazuya.Naraki
     */
    public void registerShiftMst(ShiftMstMntDto shiftMstMntDto, LoginUserDto loginUserDto) throws SQLException{

        try {
            // コネクション接続
            this.connect();

            StringBuffer strSql = new StringBuffer();
            strSql.append("INSERT INTO ");
            strSql.append("m_shift ");
            strSql.append(" ( ");
            strSql.append("shift_id, ");
            strSql.append("shift_name, ");
            strSql.append("symbol, ");
            strSql.append("end_time, ");
            strSql.append("end_time, ");
            strSql.append("break_time, ");
            strSql.append("updater_employee_id, ");
            strSql.append("update_datetime, ");
            strSql.append("creator_employee_id, ");
            strSql.append("creation_datetime");
            strSql.append(") ");
            strSql.append("VALUES ");
            strSql.append(" ( ");
            strSql.append("? ");
            strSql.append(",? ");
            strSql.append(",? ");
            strSql.append(",? ");
            strSql.append(",? ");
            strSql.append(",? ");
            strSql.append(",? ");
            strSql.append(", current_timestamp()");
            strSql.append(",? ");
            strSql.append(", current_timestamp()");
            strSql.append(") ");

            PreparedStatement ps = connection.prepareStatement(strSql.toString());

            ps.setString(1, shiftMstMntDto.getShiftId());
            ps.setString(2, shiftMstMntDto.getShiftName());
            ps.setString(3, shiftMstMntDto.getSymbol());
            ps.setString(4, shiftMstMntDto.getStartTime());
            ps.setString(5, shiftMstMntDto.getStartTime());
            ps.setString(6, shiftMstMntDto.getBreakTime());
            ps.setString(7, loginUserDto.getEmployeeId());
            ps.setString(8, loginUserDto.getEmployeeId());

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
