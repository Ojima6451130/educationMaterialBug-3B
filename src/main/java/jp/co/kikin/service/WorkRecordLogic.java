/**
 * ファイル名：WorkRecordLogic.java
 *
 * 変更履歴
 * 1.0  2010/11/04 Kazuya.Naraki
 */
package jp.co.kikin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.service.CheckUtils;
import jp.co.kikin.service.CommonUtils;

import jp.co.kikin.dao.WorkRecordDao;
import jp.co.kikin.dto.WorkRecordDto;

import jp.co.kikin.exception.CommonException;

/**
 * 説明：ログイン処理のロジック
 * @author naraki
 *
 */
public class WorkRecordLogic {
	/**
	 * シフト、勤務実績のデータを取得する
	 *
	 * @param employeeId 社員ID
	 * @param yearMonth 対象年月
	 * @return 勤務実績マップ
	 * @author Kazuya.Naraki
	 */
	public Map<String, WorkRecordDto> getWorkRecordShiftData(String employeeId, String yearMonth) throws SQLException, CommonException {

		WorkRecordDao workRecordDao = new WorkRecordDao();

		// 開始日
		String startDay = yearMonth.concat("01");
		// 終了日
		String endDay = CommonUtils.getEndYearMonthDay(startDay);

		// 勤務実績データを取得する
		Map<String, WorkRecordDto> workRecordMap = workRecordDao.getWorkRecordShiftData(employeeId, startDay, endDay);

		return workRecordMap;
	}

	/**
	 * 勤務実績のデータを取得する
	 *
	 * @param employeeId 社員ID
	 * @param yearMonth 対象年月
	 * @return 勤務実績マップ
	 * @author Kazuya.Naraki
	 */
	public Map<String, WorkRecordDto> getWorkRecordData(String employeeId, String yearMonth) throws SQLException, CommonException {

		WorkRecordDao workRecordDao = new WorkRecordDao();

		// 開始日
		String startDay = yearMonth.concat("01");
		// 終了日
		String endDay = CommonUtils.getEndYearMonthDay(startDay);

		// 勤務実績データを取得する
		Map<String, WorkRecordDto> workRecordMap = workRecordDao.getWorkRecordData(employeeId, startDay, endDay);

		return workRecordMap;
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
	public void registerWorkRecord(List<WorkRecordDto> workRecordDtoList, LoginUserDto loginUserDto) throws Exception {

		// 勤務実績Dao
		WorkRecordDao workRecordDao = new WorkRecordDao();
		// コネクション
		Connection connection = workRecordDao.getConnection();

		// トランザクション処理
		connection.setAutoCommit(false);

		try {
			for (int i = 0; i < workRecordDtoList.size(); i++) {

				WorkRecordDto workRecordDto = workRecordDtoList.get(i);
				String employeeId = workRecordDto.getEmployeeId();
				String workDay = workRecordDto.getWorkDay();

				// データが存在するか確認
				boolean updateFlg = workRecordDao.isData(employeeId, workDay);

				if (updateFlg) {
					// 更新
					workRecordDao.updateWorkRecord(workRecordDto, loginUserDto.getEmployeeId());
				} else {
					// 登録
					workRecordDao.insertWorkRecord(workRecordDto, loginUserDto.getEmployeeId());
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
	 * 実労働時間、残業時間の計算を行う
	 * @param workRecordDtoList 勤務実績Dtoリスト
	 * @author Kazuya.Naraki
	 * @throws ParseException
	 */
	public void calculation(List<WorkRecordDto> workRecordDtoList) throws ParseException {

		final String colon = ":";

		for (WorkRecordDto workRecordDto : workRecordDtoList) {

			// 画面のリスト分処理を繰り返す

			// 開始、終了、休憩時間を取得する
			String startTime = workRecordDto.getStartTime();
			String endTime = workRecordDto.getEndTime();
			String breakTime = workRecordDto.getBreakTime();

			if (CheckUtils.isEmpty(startTime) || CheckUtils.isEmpty(endTime) || CheckUtils.isEmpty(breakTime)) {
				// 入力なしの場合次へ
				continue;
			}

			/*
			 * 計算処理を行うために各時間を
			 * 秒に変換する。
			 */
			long startTimeLong = CommonUtils.getSecond(startTime);
			long endTimeLong = CommonUtils.getSecond(endTime);
			long breakTimeLong = CommonUtils.getSecond(breakTime);

			// 実労働時間(終了時間 - 開始時間)
			long actualWorkTimeSeconds = (endTimeLong - startTimeLong - breakTimeLong); // 秒

			if (actualWorkTimeSeconds < 0) {
				// 休憩が多かったとき
				actualWorkTimeSeconds = 0;
			}

			// 秒を60で除算する → 分に変換。
			long actualWorkTimeMinutes = actualWorkTimeSeconds / 60; // 分
			// 分を60で除算する → 時に変換。
			long actualWorkTimeHours = actualWorkTimeMinutes / 60; // 時
			// 分を60で除算したときの余り → 分を算出する。
			actualWorkTimeMinutes = actualWorkTimeMinutes % 60; // 余りが分になる

			// 算出した値を画面へ表示する形式にする hh:mm
			StringBuffer actualWorkTime = new StringBuffer();
			actualWorkTime.append(CommonUtils.padWithZero(String.valueOf(actualWorkTimeHours), 2));
			actualWorkTime.append(colon);
			actualWorkTime.append(CommonUtils.padWithZero(String.valueOf(actualWorkTimeMinutes), 2));

			/*
			 * 時間外時間算出のために
			 * シフトの時間を取得する。
			 */
			String startTimeShift = workRecordDto.getStartTimeShift();
			String endTimeShift = workRecordDto.getEndTimeShift();
			String breakTimeShift = workRecordDto.getBreakTimeShift();

			if (CheckUtils.isEmpty(startTimeShift) || CheckUtils.isEmpty(endTimeShift) || CheckUtils.isEmpty(breakTimeShift)) {
				// シフトがない場合、休日時間にセット
				workRecordDto.setHolidayTime(actualWorkTime.toString());
				continue;
			}


			/*
			 * 計算処理を行うために各時間を
			 * 秒に変換する。
			 */
			long startTimeShiftLong = CommonUtils.getSecond(startTimeShift);
			long endTimeShiftLong = CommonUtils.getSecond(endTimeShift);
			long breakTimeShiftLong = CommonUtils.getSecond(breakTimeShift);

			// 時間外時間(実働時間 - 終了時間（シフト） - 開始時間（シフト） - 休憩時間（シフト）)
			long overTimeSeconds = (actualWorkTimeSeconds - (endTimeShiftLong - startTimeShiftLong - breakTimeShiftLong)); // 秒

			if (overTimeSeconds < 0) {
				// 休憩が多かったとき
				overTimeSeconds = 0;
			}

			// 秒を60で除算する → 分に変換。
			long overTimeMinutes = overTimeSeconds / 60; // 分
			// 分を60で除算する → 時に変換。
			long overTimeHours = overTimeMinutes / 60; // 時
			// 分を60で除算したときの余り → 分を算出する。
			overTimeMinutes = overTimeMinutes % 60; // 余りが分になる

			// 算出した値を画面へ表示する形式にする hh:mm
			StringBuffer overTime = new StringBuffer();
			overTime.append(CommonUtils.padWithZero(String.valueOf(overTimeHours), 2));
			overTime.append(colon);
			overTime.append(CommonUtils.padWithZero(String.valueOf(overTimeMinutes), 2));


			if (CheckUtils.isEmpty(startTimeShift) || CheckUtils.isEmpty(endTimeShift) || CheckUtils.isEmpty(breakTimeShift)) {
				// シフトがない（休日の場合）
				// 実働時間を勤務実績Dtoの休日へセット
				workRecordDto.setHolidayTime(actualWorkTime.toString());
			} else {
				// シフトがある場合

				// 実働時間を勤務実績Dtoの勤務実績へセット
				workRecordDto.setActualWorkTime(actualWorkTime.toString());

				// 時間外時間を勤務実績Dtoの勤務実績へセット
				workRecordDto.setOverTime(overTime.toString());
			}
		}
	}
}
