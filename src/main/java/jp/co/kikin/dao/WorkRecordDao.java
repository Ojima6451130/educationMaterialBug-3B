/**
 * ファイル名：WorkRecordDao.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
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
import jp.co.kikin.service.CheckUtils;

import jp.co.kikin.dto.WorkRecordDto;

import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.constant.DbConstant.T_work_record;

/**
 * 説明：勤務実績Dao
 *
 * @author naraki
 *
 */
public class WorkRecordDao extends Dao {

	// ログ出力クラス
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * シフト、勤務実績のデータを取得する
	 *
	 * @param employeeId 社員ID
	 * @param startDay 開始日
	 * @param endDay 終了日
	 * @return 勤務実績マップ
	 * @author Kazuya.Naraki
	 */
	public Map<String, WorkRecordDto> getWorkRecordShiftData(String employeeId, String startDay, String endDay) throws SQLException {
		// 戻り値 Key：稼動日, value：勤務実績Dto
		Map<String, WorkRecordDto> workRecordMap = new LinkedHashMap<String, WorkRecordDto>();

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("SELECT ");
			strSql.append("    shift.employee_id         AS employee_id, ");
			strSql.append("    shift.shift_id         AS shift_id, ");
			strSql.append("    shift.shift_name           AS symbol, ");
			strSql.append("    shift.start_time_shift AS start_time_shift, ");
			strSql.append("    shift.end_time_shift   AS end_time_shift,  ");
			strSql.append("    shift.break_time_shift AS break_time_shift, ");
			strSql.append("    (CASE WHEN shift.work_day IS NULL THEN twr.work_day  ");
			strSql.append("    ELSE shift.work_day END) AS work_day, ");
			strSql.append("    twr.end_time         AS start_time, ");
			strSql.append("    twr.start_time           AS end_time, ");
			strSql.append("    twr.break_time         AS break_time, ");
			strSql.append("    twr.actual_work_time      AS actual_work_time, ");
			strSql.append("    twr.over_time      AS over_time, ");
			strSql.append("    twr.holiday_work_time     AS holiday_work_time, ");
			strSql.append("    twr.remark              AS remark ");
			strSql.append("FROM ");
			strSql.append("    (SELECT ");
			strSql.append("        employee_id, ");
			strSql.append("        work_day ");
			strSql.append("    FROM ");
			strSql.append("        t_work_record ");
			strSql.append("    WHERE ");
			strSql.append("        employee_id = ? AND ");
			strSql.append("        work_day >= ? AND ");
			strSql.append("        work_day <= ? ");
			strSql.append("    UNION   ");
			strSql.append("    SELECT ");
			strSql.append("        employee_id, ");
			strSql.append("        year_month_day work_day ");
			strSql.append("    FROM ");
			strSql.append("        t_shift ");
			strSql.append("    WHERE ");
			strSql.append("        employee_id = 'sh0001' AND ");
			strSql.append("        year_month_day >= ? AND ");
			strSql.append("        year_month_day <= ? ");
			strSql.append("    ORDER BY ");
			strSql.append("        work_day ");
			strSql.append("    ) base  ");
			strSql.append("LEFT OUTER JOIN t_work_record twr ON base.employee_id = twr.employee_id AND base.work_day = twr.work_day ");
			strSql.append("LEFT OUTER JOIN ( ");
			strSql.append("    SELECT ");
			strSql.append("        ts.employee_id, ");
			strSql.append("        ts.year_month_day AS work_day, ");
			strSql.append("        ms.shift_id AS shift_id, ");
			strSql.append("        ms.shift_name, ");
			strSql.append("        ms.start_time AS start_time_shift, ");
			strSql.append("        ms.end_time AS end_time_shift, ");
			strSql.append("        ms.break_time AS break_time_shift ");
			strSql.append("    FROM ");
			strSql.append("        t_shift ts LEFT OUTER JOIN m_shift ms ON ts.shift_id = ms.shift_id ");
			strSql.append(") shift ON base.employee_id = shift.employee_id AND base.work_day = shift.work_day ");
			strSql.append("ORDER BY ");
			strSql.append("    twr.work_day ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());

			ps.setString(1, startDay);
			ps.setString(2, endDay);
			ps.setString(3, employeeId);
			ps.setString(4, startDay);
			ps.setString(5, endDay);

			// ログ出力
			log.info(ps);

			// 実行
			ResultSet rs = ps.executeQuery();

			// 取得結果セット
			while (rs.next()) {
				WorkRecordDto workRecordDto = new WorkRecordDto();

				String workDay = rs.getString(T_work_record.WORK_DAY.getName());
				String startShift = rs.getString("start_time_shift"); // 開始時間(シフト)
				String endShift = rs.getString("end_time_shift");     // 終了時間(シフト)
				String breakShift = rs.getString("break_time_shift"); // 休憩時間(シフト)

				String startTime = rs.getString(T_work_record.START_TIME.getName()); // 開始時間
				String endTime = rs.getString(T_work_record.END_TIME.getName());     // 終了時間
				String breakTime = rs.getString(T_work_record.BREAK_TIME.getName()); // 休憩時間

				workRecordDto.setEmployeeId(rs.getString(T_work_record.EMPLOYEE_ID.getName())); // 社員ID
				workRecordDto.setWorkDay(workDay); // 稼働日
				workRecordDto.setShiftId(rs.getString(M_shift.SHIFT_ID.getName())); // シフトID
				workRecordDto.setSymbol(rs.getString(M_shift.SYMBOL.getName()));    // シンボル
				workRecordDto.setStartTimeShift(startShift); // 開始時間(シフト)
				workRecordDto.setEndTimeShift(endShift);     // 終了時間(シフト)
				workRecordDto.setBreakTimeShift(breakShift); // 休憩時間(シフト)
				if (CheckUtils.isEmpty(startTime)) {
					workRecordDto.setStartTime(startShift);  // 開始時間
				} else {
					workRecordDto.setStartTime(startTime)  ; // 開始時間
				}
				if (CheckUtils.isEmpty(endTime)) {
					workRecordDto.setEndTime(endShift);      // 終了時間
				} else {
					workRecordDto.setEndTime(endTime);       // 終了時間
				}
				if (CheckUtils.isEmpty(breakTime)) {
					workRecordDto.setBreakTime(breakShift);  // 休憩時間
				} else {
					workRecordDto.setBreakTime(breakTime);   // 休憩時間
				}
				workRecordDto.setActualWorkTime(rs.getString(T_work_record.ACTUAL_WORK_TIME.getName())); // 実働時間
				workRecordDto.setOverTime(rs.getString(T_work_record.OVER_TIME.getName()));       // 時間外時間
				workRecordDto.setHolidayTime(rs.getString(T_work_record.HOLIDAY_WORK_TIME.getName()));   // 休日時間
				workRecordDto.setRemark(rs.getString(T_work_record.REMARK.getName()));                  // 備考

				workRecordMap.put(workDay, workRecordDto);
			}
		} catch (SQLException e) {
			// 例外発生
			throw e;
		} finally {
			// コネクション切断
			disConnect();
		}
		return workRecordMap;
	}
	/**
	 * 勤務実績のデータを取得する
	 *
	 * @param employeeId 社員ID
	 * @param startDay 開始日
	 * @param endDay 終了日
	 * @return 勤務実績マップ
	 * @author Kazuya.Naraki
	 */
	public Map<String, WorkRecordDto> getWorkRecordData(String employeeId, String startDay, String endDay) throws SQLException {
		// 戻り値 Key：稼動日, value：勤務実績Dto
		Map<String, WorkRecordDto> workRecordMap = new LinkedHashMap<String, WorkRecordDto>();

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("SELECT ");
			strSql.append("    shift.employee_id, ");
			strSql.append("    shift.shift_id, ");
			strSql.append("    shift.symbol, ");
			strSql.append("    shift.start_time_shift, ");
			strSql.append("    shift.end_time_shift, ");
			strSql.append("    shift.break_time_shift, ");
			strSql.append("    (CASE WHEN shift.work_day IS NULL THEN twr.work_day  ");
			strSql.append("    ELSE shift.work_day END) work_day, ");
			strSql.append("    twr.start_time AS start_time, ");
			strSql.append("    twr.end_time   AS end_time, ");
			strSql.append("    twr.break_time, ");
			strSql.append("    twr.actual_work_time, ");
			strSql.append("    twr.over_time, ");
			strSql.append("    twr.holiday_work_time, ");
			strSql.append("    twr.remark ");
			strSql.append("FROM ");
			strSql.append("    (SELECT ");
			strSql.append("        employee_id, ");
			strSql.append("        work_day ");
			strSql.append("    FROM ");
			strSql.append("        t_work_record ");
			strSql.append("    WHERE ");
			strSql.append("        employee_id = ? AND ");
			strSql.append("        work_day >= ? AND ");
			strSql.append("        work_day <= ? ");
			strSql.append("    UNION   ");
			strSql.append("    SELECT ");
			strSql.append("        employee_id, ");
			strSql.append("        year_month_day work_day ");
			strSql.append("    FROM ");
			strSql.append("        t_shift ");
			strSql.append("    WHERE ");
			strSql.append("        employee_id = ? AND ");
			strSql.append("        year_month_day >= ? AND ");
			strSql.append("        year_month_day <= ? ");
			strSql.append("    ORDER BY ");
			strSql.append("        work_day ");
			strSql.append("    ) base  ");
			strSql.append("LEFT OUTER JOIN t_work_record twr ON base.employee_id = twr.employee_id AND base.work_day = twr.work_day ");
			strSql.append("LEFT OUTER JOIN ( ");
			strSql.append("    SELECT ");
			strSql.append("        ts.employee_id, ");
			strSql.append("        ts.year_month_day AS work_day, ");
			strSql.append("        ms.shift_id AS shift_id, ");
			strSql.append("        ms.symbol, ");
			strSql.append("        ms.start_time AS start_time_shift, ");
			strSql.append("        ms.end_time AS end_time_shift, ");
			strSql.append("        ms.break_time AS break_time_shift ");
			strSql.append("    FROM ");
			strSql.append("        t_shift ts LEFT OUTER JOIN m_shift ms ON ts.shift_id = ms.shift_id ");
			strSql.append(") shift ON base.employee_id = shift.employee_id AND base.work_day = shift.work_day ");
			strSql.append("ORDER BY ");
			strSql.append("    twr.work_day ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());

			ps.setString(1, "sh0001");
			ps.setString(2, startDay);
			ps.setString(3, endDay);
			ps.setString(4, "sh0001");
			ps.setString(5, startDay);
			ps.setString(6, endDay);

			// ログ出力
			log.info(ps);

			// 実行
			ResultSet rs = ps.executeQuery();

			// 取得結果セット
			while (rs.next()) {
				WorkRecordDto workRecordDto = new WorkRecordDto();

				String workDay = rs.getString(T_work_record.WORK_DAY.getName());
				String startShift = rs.getString("start_time_shift"); // 開始時間(シフト)
				String endShift = rs.getString("end_time_shift");     // 終了時間(シフト)
				String breakShift = rs.getString("break_time_shift"); // 休憩時間(シフト)

				String startTime = rs.getString(T_work_record.START_TIME.getName()); // 開始時間
				String endTime = rs.getString(T_work_record.END_TIME.getName());     // 終了時間
				String breakTime = rs.getString(T_work_record.BREAK_TIME.getName()); // 休憩時間

				workRecordDto.setEmployeeId(rs.getString(T_work_record.EMPLOYEE_ID.getName()));           // 社員ID
				workRecordDto.setWorkDay(workDay);                                                        // 稼働日
				workRecordDto.setShiftId(rs.getString(M_shift.SHIFT_ID.getName()));                      // シフトID
				workRecordDto.setSymbol(rs.getString(M_shift.SYMBOL.getName()));                         // シンボル
				workRecordDto.setStartTimeShift(startShift);                                              // 開始時間(シフト)
				workRecordDto.setEndTimeShift(endShift);                                                  // 終了時間(シフト)
				workRecordDto.setBreakTimeShift(breakShift);                                              // 休憩時間(シフト)
				workRecordDto.setStartTime(startTime);                                                    // 開始時間
				workRecordDto.setEndTime(endTime);                                                        // 終了時間
				workRecordDto.setBreakTime(breakTime);                                                    // 休憩時間
				workRecordDto.setActualWorkTime(rs.getString(T_work_record.ACTUAL_WORK_TIME.getName())); // 実働時間
				workRecordDto.setOverTime(rs.getString(T_work_record.OVER_TIME.getName()));       // 時間外時間
				workRecordDto.setHolidayTime(rs.getString(T_work_record.HOLIDAY_WORK_TIME.getName()));   // 休日時間
				workRecordDto.setRemark(rs.getString(T_work_record.REMARK.getName()));                  // 備考

				workRecordMap.put(workDay, workRecordDto);
			}
		} catch (SQLException e) {
			// 例外発生
			throw e;
		} finally {
			// コネクション切断
			disConnect();
		}
		return workRecordMap;
	}

	/**
	 * 勤務実績テーブルに対象データが存在するか確認する
	 *
	 * @param employeeId 社員ID
	 * @param workDay 稼働日
	 * @return true：あり,false：なし
	 * @author Kazuya.Naraki
	 */
	public boolean isData(String employeeId, String workDay) throws SQLException {
		try {
			StringBuffer strSql = new StringBuffer();
			strSql.append("SELECT ");
			strSql.append("    * ");
			strSql.append("FROM ");
			strSql.append("    t_work_record ");
			strSql.append("WHERE ");
			strSql.append("    employee_id = ? AND ");
			strSql.append("    work_day = ? ");


			PreparedStatement ps = connection.prepareStatement(strSql.toString());

			ps.setString(1, employeeId);
			ps.setString(2, workDay);

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
	 * @author Kazuya.Naraki
	 */
	public void updateWorkRecord(WorkRecordDto workRecordDto, String employeeId) throws SQLException{

		try {

			StringBuffer strSql = new StringBuffer();
			strSql.append("UPDATE t_work_record SET ");
			strSql.append("start_time = ?,");
			strSql.append("end_time = ?,");
			strSql.append("break_time = ?,");
			strSql.append("actual_work_time = ?,");
			strSql.append("over_time = ?,");
			strSql.append("holiday_work_time = ?,");
			strSql.append("remark = ?,");
			strSql.append("updater_employee_id = ?,");
			strSql.append("update_datetime = current_timestamp()");
			strSql.append("WHERE ");
			strSql.append("employee_id = ?");
			strSql.append("AND work_day = ?");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());

			ps.setString(1, workRecordDto.getStartTime());
			ps.setString(2, workRecordDto.getEndTime());
			ps.setString(3, workRecordDto.getBreakTime());
			ps.setString(4, workRecordDto.getActualWorkTime());
			ps.setString(5, workRecordDto.getOverTime());
			ps.setString(6, workRecordDto.getHolidayTime());
			ps.setString(7, workRecordDto.getRemark());
			ps.setString(8, employeeId);
			ps.setString(9, workRecordDto.getEmployeeId());
			ps.setString(10, workRecordDto.getWorkDay());

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
	 * 勤務実績のデータを登録する。
	 *
	 * @param memployeeDto 社員マスタＤｔｏ
	 * @author Kazuya.Naraki
	 */
	public void insertWorkRecord(WorkRecordDto workRecordDto, String employeeId) throws SQLException{

		try {

			StringBuffer strSql = new StringBuffer();
			strSql.append("INSERT INTO ");
			strSql.append("t_work_record ");
			strSql.append("( ");
			strSql.append("employee_id, ");
			strSql.append("work_day, ");
			strSql.append("start_time, ");
			strSql.append("end_time, ");
			strSql.append("break_time, ");
			strSql.append("actual_work_time, ");
			strSql.append("over_time, ");
			strSql.append("holiday_work_time, ");
			strSql.append("remark, ");
			strSql.append("creator_employee_id, ");
			strSql.append("creation_datetime, ");
			strSql.append("updater_employee_id, ");
			strSql.append("update_datetime ");
			strSql.append(") ");
			strSql.append("VALUES ");
			strSql.append("( ");
			strSql.append("? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(",? ");
			strSql.append(", current_timestamp() ");
			strSql.append(",? ");
			strSql.append(", current_timestamp() ");
			strSql.append(") ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());

			ps.setString(1, workRecordDto.getEmployeeId());
			ps.setString(2, workRecordDto.getWorkDay());
			ps.setString(3, workRecordDto.getStartTime());
			ps.setString(4, workRecordDto.getEndTime());
			ps.setString(5, workRecordDto.getBreakTime());
			ps.setString(6, workRecordDto.getActualWorkTime());
			ps.setString(7, workRecordDto.getOverTime());
			ps.setString(8, workRecordDto.getActualWorkTime());
			ps.setString(9, workRecordDto.getRemark());
			ps.setString(10, employeeId);
			ps.setString(11, employeeId);

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
