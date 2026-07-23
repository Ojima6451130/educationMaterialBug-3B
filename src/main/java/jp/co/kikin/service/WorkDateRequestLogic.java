/**
 * ファイル名：WorkDateRequestLogic.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 * spring framework移行　2025/04/08 Shuta.Hashimoto
 * 
 */
package jp.co.kikin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

// import business.dto.LoginUserDto;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.bean.ResWorkDateRequestLogic;
import jp.co.kikin.dao.WorkDateRequestDao;
import jp.co.kikin.dto.WorkDateRequestCheckDto;
import jp.co.kikin.dto.WorkDateRequestInputDto;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.WorkDateRequestInputBean;

/**
 * 説明：希望出勤日入力処理のロジック
 * @author naraki
 *
 */
@Service
public class WorkDateRequestLogic {


    /**
     * 出勤希望確認画面に表示するリストを取得する。
     * 戻り値・・・全社員分の希望シフトリストのリスト
     * @param yearMonth 年月
     * @return 出勤希望Dtoリストのリスト
     * @author naraki
     */
    public List<List<WorkDateRequestCheckDto>> getWorkDateRequestCheckDtoList(String yearMonth) throws SQLException{

        // Dao
        WorkDateRequestDao dao = new WorkDateRequestDao();

        // シフト情報を取得する。
        List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList = dao.getShiftTblNestedList(yearMonth);

        return workRequestCheckDtoNestedList;
    }

    /**
     * 出勤希望確認画面に表示するリストを取得する。
     * 戻り値・・・社員分の希望シフトリスト
     * @param yearMonth 年月
     * @param employeeId 社員ID
     * @return 出勤希望Dtoリストのリスト
     * @author naraki
     */
    public List<WorkDateRequestInputDto> getWorkRequestInputDto(String employeeId, String yearMonth) throws SQLException {

        // Dao
        WorkDateRequestDao dao = new WorkDateRequestDao();

        List<WorkDateRequestInputDto> workDateRequestInputDtoList = dao.getShiftTblData(employeeId, yearMonth);

        return workDateRequestInputDtoList;
    }

    /**
     * シフトテーブルのデータを登録・更新する。
     * @param requestDtoNestedList 月別シフト一覧
     * @return 基本シフトマップ
     * @author naraki
     * @throws SQLException
     */
    public void registerRequestShift(List<List<WorkDateRequestInputDto>> requestDtoNestedList, LoginUserDto loginUserDto) throws SQLException {

        // Dao
        WorkDateRequestDao dao = new WorkDateRequestDao();
        // コネクション
        Connection connection = dao.getConnection();

        // トランザクション処理
        connection.setAutoCommit(false);

        try {
        	for (List<WorkDateRequestInputDto> workDateRequestInputDtoList : requestDtoNestedList) {

        		for (WorkDateRequestInputDto workDateRequestInputDto : workDateRequestInputDtoList) {
        		// 日数分ループ

        		// 社員ID
        		String employeeId = workDateRequestInputDto.getEmployeeId();
            	// 対象年月
            	String yearMonthDay = workDateRequestInputDto.getYearMonthDay();

            	if (employeeId.equals(loginUserDto.getEmployeeId())) {
            		// レコードの存在を確認する
            		boolean isData = dao.isData(employeeId, yearMonthDay);

            		if (isData) {
            			// 更新
            			dao.updateShiftTbl(workDateRequestInputDto, loginUserDto);
            		} else {
            			// 登録
            			dao.insertShiftTbl(workDateRequestInputDto, loginUserDto);
            		}
            	}
        	}
        }


        } catch (SQLException e) {
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
     * 出勤希望シフトデータを画面用に編集する
     * @param requestDtoNestedList 月別シフト一覧
     * @return 基本シフトマップ
     * @author naraki
     * @throws SQLException
     */
    public List<ResWorkDateRequestLogic> getWorkDateRequestScreenData (List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList, List<DateBean> dateBeanList) throws Exception {
        
        List<ResWorkDateRequestLogic> workDateRequestList = new ArrayList<>();

        for (List<WorkDateRequestCheckDto> listWorkRequest : workRequestCheckDtoNestedList) {
            // 各社員の対象月画面データを格納
            ResWorkDateRequestLogic resWorkDateRequestLogic = new ResWorkDateRequestLogic();

            if (listWorkRequest.size() < 2) {
                // 対象年月の日数分データがない＝出勤希望登録未実施
                resWorkDateRequestLogic.setEmployeeId(listWorkRequest.get(0).getEmployeeId());
                resWorkDateRequestLogic.setEmployeeName(listWorkRequest.get(0).getEmployeeName());
                // 対象月の日数分以下の処理を行う
                for(int i = 1; i <= dateBeanList.size(); i++) {
                    this.setWorkRequestSymbols(i, "-", resWorkDateRequestLogic);
                }
                
            }else {
                int indexDateNom = 1;
                String symbol;
                for (WorkDateRequestCheckDto workRequest : listWorkRequest) {
                    symbol = workRequest.getMyRequestSymbol();
                    if (indexDateNom == 1) {
                        resWorkDateRequestLogic.setEmployeeId(listWorkRequest.get(0).getEmployeeId());
                        resWorkDateRequestLogic.setEmployeeName(listWorkRequest.get(0).getEmployeeName());
                    }
                    this.setWorkRequestSymbols(indexDateNom, symbol, resWorkDateRequestLogic);
                    indexDateNom ++;
                }
            }
            workDateRequestList.add(resWorkDateRequestLogic);
        }

        return workDateRequestList;
    }

    // setter呼び出し 出勤希望確認
    public void setWorkRequestSymbols(int no, String value, ResWorkDateRequestLogic entity) throws Exception {
        entity.getClass().getMethod("setWorkRequest" + String.valueOf(no), String.class).invoke(entity, value);
    }

    // setter呼び出し 出勤希望入力
    public void setWorkRequestShiftId(int no, String value, WorkDateRequestInputBean entity) throws Exception {
        entity.getClass().getMethod("setShiftId" + CommonUtils.padWithZero(String.valueOf(no), 2), String.class).invoke(entity, value);
    }
}