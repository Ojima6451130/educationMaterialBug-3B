/**
 * ファイル名：MonthlyShiftLogic.java
 *
 * 変更履歴
 * 1.0  2010/10/06 Kazuya.Naraki
 * 2.0  2024/05/15 Sho.Kiyota
 */
package jp.co.kikin.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.tomcat.util.buf.UriUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.kikin.CheckUtils;
import jp.co.kikin.dao.MonthlyShiftDao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.MonthlyShiftDto;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.MonthlyShiftCheckBean;
import jp.co.kikin.model.MonthlyShiftCheckForm;
import jp.co.kikin.model.MonthlyShiftInputBean;
import jp.co.kikin.model.WorkDateRequestInputBean;
import jp.co.kikin.service.CommonUtils;

/**
 * 説明：希望出勤日入力処理のロジック
 * @author naraki
 *
 */
@Service
public class MonthlyShiftLogic {

    // テンプレートファイルURL
    private final String EXCEL_PASS = "/Excel/月別シフト確認テンプレート.xls";

    /** Excelダウンロード用ContentType*/
    protected static final String CONTENT_TYPE = "application/vnd.ms-excel";

    /**
     * シフトテーブルより情報を取得する。
     * @param yearMonth 検索対象年月
     * @param shiftFlg true：シフトIDを取得 false：希望シフトIDを取得
     * @return 出勤希望Dtoリスト
     * @author naraki
     */
    public Map<String, List<MonthlyShiftDto>> getMonthlyShiftDtoMap(String yearMonth, boolean shiftFlg) throws SQLException{

        // 戻り値
        Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap = new LinkedHashMap<String, List<MonthlyShiftDto>>();

        // Dao
        MonthlyShiftDao monthlyShiftDao = new MonthlyShiftDao();

        // シフト情報を取得する。
        List<MonthlyShiftDto> monthlyShiftDtoList = monthlyShiftDao.getShiftTblData(yearMonth, shiftFlg);

        String oldEmployeeId = "";

        // 一時領域
        List<MonthlyShiftDto> tmpList = new ArrayList<MonthlyShiftDto>();

        // DB取得より取得する値を各社員づつ区切る
        for(MonthlyShiftDto dto : monthlyShiftDtoList) {
            if (CheckUtils.isEmpty(oldEmployeeId)) {

                // 社員IDが空のとき（初回）
                oldEmployeeId = dto.getEmployeeId();

                // 取得した値を戻り値のリストにセットする。
                tmpList.add(dto);

            } else {
                if (oldEmployeeId.equals(dto.getEmployeeId())) {
                    // 同一社員のデータ
                    // 取得した値を戻り値のリストにセットする。
                    tmpList.add(dto);
                } else {
                    // 別社員のデータのとき
                    // 前の社員分をマップにつめる
                    monthlyShiftDtoMap.put(oldEmployeeId, tmpList);

                    // oldEmployeeId を入れ替える
                    oldEmployeeId = dto.getEmployeeId();

                    // 新しい社員のデータを追加していく
                    tmpList = new ArrayList<MonthlyShiftDto>();
                    tmpList.add(dto);
                }
            }
        }

        if (!CheckUtils.isEmpty(oldEmployeeId)) {
            // 最後分を追加する
            monthlyShiftDtoMap.put(oldEmployeeId, tmpList);
        }

        return monthlyShiftDtoMap;
    }

    /**
     * シフトテーブルのデータを登録・更新する。
     * @param monthlyShiftDtoNestedList 月別シフト一覧
     * @return 基本シフトマップ
     * @author naraki
     * @throws SQLException
     */
    public void registerMonthlyShift(List<List<MonthlyShiftDto>> monthlyShiftDtoNestedList, LoginUserDto loginUserDto) throws SQLException {

        // Dao
        MonthlyShiftDao dao = new MonthlyShiftDao();
        // コネクション
        Connection connection = dao.getConnection();

        // トランザクション処理
        connection.setAutoCommit(false);

        try {
            for (List<MonthlyShiftDto> monthlyShiftDtoList : monthlyShiftDtoNestedList) {
                // 人数分のループ
                for (MonthlyShiftDto dto : monthlyShiftDtoList) {
                    // 日数分ループ

                    // 社員ID
                    String employeeId = dto.getEmployeeId();
                    // 対象年月
                    String yearMonthDay = dto.getYearMonthDay();

                    // レコードの存在を確認する
                    boolean isData = dao.isData(employeeId, yearMonthDay);

                    if (isData) {
                        // 更新
                    	dao.updateShiftTbl(dto, loginUserDto);
                    } else {
                        // 登録
                    	dao.registerShiftTbl(dto, loginUserDto);
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
     * 月別シフト確認の印刷を行う
     * @param monthlyShiftCheckForm 月別シフト確認フォーム
     * @param excelFileExporter
     * @return
     * @author naraki
     */
	public void print(HttpServletResponse response, List<DateBean> dateBeanList, List<MonthlyShiftCheckBean> monthlyShiftCheckBeanList) throws Exception {

        // テンプレートの読み込み
        InputStream is = getClass().getResourceAsStream(EXCEL_PASS);
        Workbook workbook = new HSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        //
        // Excelに格納する必要データを編集・作成していく
        //

        List<String> dateList = new ArrayList<String>(31);
        List<String> weekDayList = new ArrayList<String>(31);

        String yearMonth = "";

        int listSize = dateBeanList.size();
        for (int i = 0; i < 3; i++) {
            DateBean dateBean = null;

            if (i < listSize) {
                dateBean= dateBeanList.get(i);
            }

            if (CheckUtils.isEmpty(dateBean)) {
                dateList.add("");
                weekDayList.add("");
            } else {
                dateList.add(CommonUtils.changeFormat(dateBean.getYearMonthDay(), "yyyyMMdd", "dd"));
                weekDayList.add(dateBean.getWeekDay());
                yearMonth = CommonUtils.changeFormat(dateBean.getYearMonthDay(), "yyyyMMdd", "yyyy/MM");
            }
        }

        // Excel作成
        workbook = makeMonthlyShiftExcel(workbook, sheet, dateList, weekDayList, yearMonth, monthlyShiftCheckBeanList);

        // ファイル出力
        String outputFileName = "月別シフト確認_" + yearMonth + ".xls";
        String encodedName = UriUtils.encode(outputFileName, StandardCharsets.UTF_8);

        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + encodedName);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();


    }

    /**
     * Excel帳票本体の作成を行う
     */
    private Workbook makeMonthlyShiftExcel(Workbook workbook, Sheet sheet, List<String> dateList, List<String> weekDayList,
                                                String yearMonth, List<MonthlyShiftCheckBean> monthlyShiftCheckBeanList) throws Exception{

        /* 対象年月を記入 */
        replaceCellValue(sheet, "P1", yearMonth);

        /* 日付を記入 */
        String dateRowNum = "3";
        writeCellValues(sheet, dateRowNum, dateList);

        /* 曜日を記入 */
        String weekDayRowNum = "4";
        writeCellValues(sheet, weekDayRowNum, weekDayList);

        /* シフトデータを記入する */
        int count = 0;
        int rowNum = 5;

        // テンプレートのシフトデータ記入行を社員分コピー
        for(int i = 1; i < monthlyShiftCheckBeanList.size(); i++) {
            int rowIndex = rowNum - 1;
            int destRowIndex = rowIndex + i;
            copyRow(sheet, rowIndex, destRowIndex);
        }

        // 各社員分のデータを記入
        for (MonthlyShiftCheckBean monthlyShiftCheckBean : monthlyShiftCheckBeanList) {
            // シフトデータから各日のシフト情報のみ取得
            List<String> monthlyShiftData = new ArrayList<>();
            for(int i = 1; i <= 31; i++) {
                String symbol = this.getShiftSymbol(i, monthlyShiftCheckBean);
                monthlyShiftData.add(symbol);
            }

            // 社員名
            String empRowNum = String.valueOf(rowNum + count);
            String empRowAddress = "B" + empRowNum;
            replaceCellValue(sheet, empRowAddress, monthlyShiftCheckBean.getEmployeeName());

            // シフトデータ
            writeCellValues(sheet, empRowNum, monthlyShiftData);

            count++;

        }

        return workbook;
    }

    // セルへデータを記入
    private void replaceCellValue(Sheet sheet, String adress, String value) {
        CellReference cr = new CellReference(adress);
        Row row = sheet.getRow(cr.getRow());
        Cell cell = row.getCell(cr.getCol());

        // String oldValue = cell.getStringCellValue();
        // String newValue = oldValue.replaceAll(oldValue, value);

        cell.setCellValue(value);
    }

    private void writeCellValues (Sheet sheet, String rowNum, List<String> list) {

        for(int i = 0; i < 31; i++) {
            String column = String.valueOf((char) ('C' + i));
            if (i > 23) {
                String columnA = "A";
                int alph = i - 24;
                column = columnA + String.valueOf((char) ('A' + alph));
            }
            String address = column + rowNum;

            if (CheckUtils.isEmpty(list.get(i))) {
                continue;
            }else {
                replaceCellValue(sheet, address, list.get(i));
            }
        }
    }

    private String getShiftSymbol (int dayNum, MonthlyShiftCheckBean monthlyShiftCheckBean) throws Exception {

        return (String)monthlyShiftCheckBean.getClass().getMethod("getSymbol" + CommonUtils.padWithZero(String.valueOf(dayNum), 2)).invoke(monthlyShiftCheckBean);

    }

    // Excel　行のコピー
    private void copyRow(Sheet sheet, int srcRowNum, int destRowNum) {
        Row srcRow = sheet.getRow(srcRowNum);
        if (srcRow == null) return;

        Row destRow = sheet.createRow(destRowNum);
        destRow.setHeight(srcRow.getHeight());

        for (int i = 0; i < srcRow.getLastCellNum(); i++) {
            Cell srcCell = srcRow.getCell(i);
            if (srcCell == null) continue;

            Cell destCell = destRow.createCell(i);

            // セルスタイルのコピー
            destCell.setCellStyle(srcCell.getCellStyle());

            // セルタイプによって値を分けてコピー
            switch (srcCell.getCellType()) {
                case STRING:
                    destCell.setCellValue(srcCell.getStringCellValue());
                    break;
                case NUMERIC:
                    destCell.setCellValue(srcCell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    destCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case FORMULA:
                    destCell.setCellFormula(srcCell.getCellFormula());
                    break;
                case BLANK:
                    destCell.setBlank();
                    break;
                default:
                    break;
            }
        }
    }
      // setter呼び出し 出勤希望入力
    public void setShiftId(int no, String value, MonthlyShiftInputBean entity) throws Exception {
        entity.getClass().getMethod("setShiftId" + CommonUtils.padWithZero(String.valueOf(no), 2), String.class).invoke(entity, value);
    }
}
