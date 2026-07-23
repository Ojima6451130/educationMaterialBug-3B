package jp.co.kikin.service;

import java.sql.SQLException;
import java.util.List;

import jp.co.kikin.dao.DailyShiftDao;
import jp.co.kikin.dto.DailyShiftDto;

public class DailyShiftLogic {
    /**
     * 日別シフトDtoリストを取得する。
     * @return 社員マスタリスト
     * @author naraki
     */
    public  List<DailyShiftDto> getDailyShiftDtoList(String yearMonthDay) throws SQLException{

        // 日別シフトDao
        DailyShiftDao dao = new DailyShiftDao();

        // 日別シフトDtoリストを取得
        List<DailyShiftDto> dailyShiftDtoList = dao.getDailyShiftDtoList(yearMonthDay);

        return dailyShiftDtoList;
    }
}
