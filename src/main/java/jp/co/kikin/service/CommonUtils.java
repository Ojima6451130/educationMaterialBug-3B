package jp.co.kikin.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.CommonConstant.DayOfWeek;
import jp.co.kikin.constant.DbConstant.M_employee;
import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.dao.CommonUtilsDao;
import jp.co.kikin.exception.CommonException;
import jp.co.kikin.model.DateBean;

/**
 * 説明：共通部品クラス
 * @author naraki
 *
 */
@Component
public class CommonUtils {

    /**
     * 引数のタイムスタンプをミリ秒に変換
     * @param 時間
     * @return ミリ秒
     * @author naraki
     */
    public static Long changeTimestampToLong(Timestamp arg) {
        if (CheckUtils.isEmpty(arg)) {
            // null または ""
            return 0L;
        }

        return arg.getTime();
    }

    /**
     * 引数が null の場合 "" を返却する。
     * @param 文字列
     * @return 変換した文字列
     * @author naraki
     */
    public static String changeNullToBlank(String arg) {
        if (CheckUtils.isEmpty(arg)) {
            // null または ""
            return CommonConstant.BLANK;
        }

        return arg;
    }

    /**
     * 引数が null の場合 "-" を返却する。
     * @param 文字列
     * @return 変換した文字列
     * @author naraki
     */
    public static String changeNullToHyphen(String arg) {
        if (CheckUtils.isEmpty(arg)) {
            // null または ""
            return CommonConstant.HYPHEN;
        }

        return arg;
    }

    /**
     * 引数が null の場合 0 を返却する。
     * @param 整数(Ingeger)
     * @return 変換後の数値
     * @author naraki
     */
    public static int changeNullToBlank(Integer arg) {
        if (CheckUtils.isEmpty(arg)) {
            // null
            return 0;
        }

        return arg;
    }

    /**
     * 引数が null の場合 0 を返却する。
     * @param 整数(Long)
     * @return 変換後の数値
     * @author naraki
     */
    public static long changeNullToBlank(Long arg) {
        if (CheckUtils.isEmpty(arg)) {
            // null
            return 0;
        }

        return arg;
    }

    /**
     * 各種テーブルのＩＤを最大値を取得する
     * @param tableName 採番するテーブル名
     * @return nextId 次のＩＤ
     */
    public String getMaxId(String tableName) throws SQLException {

        // 共通部品用Dao
        CommonUtilsDao commonUtilsDao = new CommonUtilsDao();

        // ID 最大値
        String maxId = "";
        // ID 名称
        String idName = "";

        if (M_employee.TABLE_NAME.getName().equals(tableName)) {
            // 社員マスタ
            idName = M_employee.EMPLOYEE_ID.getName();
        } else if (M_shift.TABLE_NAME.getName().equals(tableName)) {
            // シフトマスタ
            idName = M_shift.SHIFT_ID.getName();
        }

        maxId = commonUtilsDao.getMaxId(tableName, idName);

        if (CheckUtils.isEmpty(maxId)) {
            // IDを新規作成する
            maxId = this.getNewId(tableName);
        }

        return maxId;
    }

    /**
     * 各種テーブルのＩＤを新規作成する。
     * @param tableName 採番するテーブル名
     * @return nextId 次のＩＤ
     */
    public String getNewId(String tableName) throws SQLException {

        final String ID = "0000";

        // 作成ＩＤ
        StringBuffer newId = new StringBuffer();

        if (M_employee.TABLE_NAME.getName().equals(tableName)) {
            // 社員マスタ
            newId.append(M_employee.PREFIX.getName());
        } else if (M_shift.TABLE_NAME.getName().equals(tableName)) {
            // シフトマスタ
            newId.append(M_shift.SHIFT_ID.getName());
        }

        newId.append(ID);

        return newId.toString();
    }

    /**
     * 各種テーブルのＩＤを採番する
     * @param tableName 採番するテーブル名
     * @return nextId 次のＩＤ
     */
    public String getNextId(String tableName) throws SQLException, CommonException {

        // ＩＤの最大値を取得する
        String maxId = this.getMaxId(tableName);

        if (CheckUtils.isEmpty(maxId)) {
            throw new CommonException("ＩＤが存在しない");
        }

        // ＩＤの数字部分
        String numId;
        if(maxId.startsWith("shift")) {
        	numId = maxId.substring(8, 12);
        }else {
        	numId = maxId.substring(2);
        }
        int intNumId = Integer.parseInt(numId);

        if (intNumId >= 9999) {
            throw new CommonException("登録ＩＤが最大を超える");
        }

        StringBuffer strBuf = new StringBuffer();
        strBuf.append(maxId.substring(0, 2));

        // ＩＤ加算処理
        intNumId ++;

        // 先頭0埋め処理
        DecimalFormat df = new DecimalFormat("0000");
        String formattedNum = df.format(intNumId);
        strBuf.append(formattedNum);

        return strBuf.toString();

    }

    /**
     * 文字列の置換を行う
     *
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @oaram replacement 置換後の文字列
     * @return 置換処理後の文字列
     */
    static public String substitute(String input, String pattern, String replacement) {
        // 置換対象文字列が存在する場所を取得
        int index = input.indexOf(pattern);

        // 置換対象文字列が存在しなければ終了
        if(index == -1) {
            return input;
        }

        // 処理を行うための StringBuffer
        StringBuffer buffer = new StringBuffer();

        buffer.append(input.substring(0, index) + replacement);

        if(index + pattern.length() < input.length()) {
            // 残りの文字列を再帰的に置換
            String rest = input.substring(index + pattern.length(), input.length());
            buffer.append(substitute(rest, pattern, replacement));
        }
        return buffer.toString();
    }

    /**
     * HTML 出力用に次の置換を行う
     * & -> &amp;
     * < -> &lt;
     * > -> &gt;
     * " -> &quot;
     *
     * @param input 置換対象の文字列
     * @return 置換処理後の文字列
     */
    static public String HTMLEscape(String input) {
        input = substitute(input, "&",  "&amp;");
        input = substitute(input, "<",  "&lt;");
        input = substitute(input, ">",  "&gt;");
        input = substitute(input, "\"", "&quot;");
        return input;
    }

    /**
     * 月末日を取得する
     * @param strDate 対象年月日
     * @return 対象月末日 yyyyMMdd
     */
    static public String getEndYearMonthDay(String strDate) throws CommonException {
        if (strDate == null || strDate.length() != 8) {
            throw new CommonException("引数の文字列["+ strDate +"]は不正です。");
        }
        int yyyy = Integer.parseInt(strDate.substring(0,4));
        int MM = Integer.parseInt(strDate.substring(4,6));
        int dd = Integer.parseInt(strDate.substring(6,8));
        Calendar cal = Calendar.getInstance();
        cal.set(yyyy, MM-1, dd);
        int last = cal.getActualMaximum(Calendar.DATE);

        StringBuffer strBuf = new StringBuffer();
        strBuf.append(strDate.substring(0,6));
        strBuf.append(String.valueOf(last));

        return strBuf.toString();

    }

    /**
     * 月末日を取得する
     * @param strDate 対象年月日
     * @return 対象月末日
     */
    static public int getEndMonthDay(String strDate) throws CommonException {
        if (strDate == null || strDate.length() != 8) {
            throw new CommonException("引数の文字列["+ strDate +"]は不正です。");
        }
        int yyyy = Integer.parseInt(strDate.substring(0,4));
        int MM = Integer.parseInt(strDate.substring(4,6));
        int dd = Integer.parseInt(strDate.substring(6,8));
        Calendar cal = Calendar.getInstance();
        cal.set(yyyy, MM-1, dd);
        int last = cal.getActualMaximum(Calendar.DATE);
        return last;

    }

    /**
     * 現在日付の月末日を取得する
     * @return 対象月末日
     */
    static public int getEndMonthDay() {

        String strDate = getFisicalDay();

        int yyyy = Integer.parseInt(strDate.substring(0,4));
        int MM = Integer.parseInt(strDate.substring(4,6));
        int dd = Integer.parseInt(strDate.substring(6,8));
        Calendar cal = Calendar.getInstance();
        cal.set(yyyy,MM-1,dd);
        int last = cal.getActualMaximum(Calendar.DATE);
        return last;

    }

    /**
     * 現在日付の取得
     * @return 現在日付 yyyyMMdd
     */
    public static String getFisicalDay() {
        StringBuffer formattedDate = new StringBuffer();
        Calendar cal1 = Calendar.getInstance();  //(1)オブジェクトの生成

        int year = cal1.get(Calendar.YEAR);        //(2)現在の年を取得
        int month = cal1.get(Calendar.MONTH) + 1;  //(3)現在の月を取得
        int day = cal1.get(Calendar.DATE);         //(4)現在の日を取得

        formattedDate.append(padWithZero(String.valueOf(year), 4));
        formattedDate.append(padWithZero(String.valueOf(month), 2));
        formattedDate.append(padWithZero(String.valueOf(day), 2));

        return formattedDate.toString();
    }

    /**
     * 現在日付の取得(フォーマット指定)
     * @param frm フォーマット
     * @return 現在日付
     */
    public static String getFisicalDay(String frm) {
        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat(frm);
        return sdf1.format(date1);
    }

    /**
     * 対象の表示形式を変更する。
     * @param target 対象
     * @param inFrm 入力文字列のフォーマット
     * @param outFrm 返却文字列のフォーマット
     * @return フォーマットしたデータ
     * @throws ParseException
     */
    public static String changeFormat(String target, String inFrm, String outFrm) throws ParseException {

        SimpleDateFormat inFormat = new SimpleDateFormat(inFrm);
        SimpleDateFormat outFormat = new SimpleDateFormat(outFrm);

        Date date = inFormat.parse(target);
        String dateString = outFormat.format(date);

        return dateString;
    }

    /**
     * 年月日にスラッシュを追加する
     * @param target 対象
     * @return フォーマットしたデータ
     */
    public static String addSlash(String target) {
        StringBuffer buf = new StringBuffer();
        buf.append(target.substring(0, 4));
        buf.append("/");
        buf.append(target.substring(4, 6));

        if (target.length() == 8) {
            buf.append("/");
            buf.append(target.substring(6, 8));
        }

        return buf.toString();
    }

    /**
     * 対象年月の1ヶ月分の日付情報格納クラスのリストを取得する。
     * @param yearMonth yyyyMM
     * @return
     * @author naraki
     */
    public static List<DateBean> getDateBeanList(String yearMonth) throws CommonException, SQLException {

        if (CheckUtils.isEmpty(yearMonth) || yearMonth.length() != 6) {
            throw new CommonException("引数の値が不正です。");
        }

        List<DateBean> dateBeanList = new ArrayList<DateBean>();

        // 年
        int intYear = Integer.parseInt(yearMonth.substring(0, 4));
        // 月
        int intMonth = Integer.parseInt(yearMonth.substring(4, 6)) - 1;
        // 日
        final int DAY = 1;

        // 月末
        int endDay = getEndMonthDay(yearMonth.concat("01"));

        // 祝日情報取得
        CommonUtilsDao commonUtilsDao = new CommonUtilsDao();
        List<String> publicHolidayList = commonUtilsDao.getPublicHolidayList(yearMonth);
        Set<String> publicHolidaySet = new HashSet<String>();
        if (!CheckUtils.isEmpty(publicHolidayList)) {
            publicHolidaySet.addAll(publicHolidayList);
        }

        for (int i = DAY; i <= endDay; i++) {
            DateBean dateBean = new DateBean();
            Calendar calendar = new GregorianCalendar(intYear, intMonth, i);
            int intDay = calendar.get(Calendar.DATE);
            String strDay;
            if (intDay < 10) {
                strDay = "0".concat(String.valueOf(intDay));
            } else {
                strDay = String.valueOf(intDay);
            }

            StringBuffer dayOfWeek = new StringBuffer();
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
              case Calendar.SUNDAY:
            	  dayOfWeek.append(DayOfWeek.SUNDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.SUNDAY);
                  break;
              case Calendar.MONDAY:
            	  dayOfWeek.append(DayOfWeek.MONDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.MONDAY);
                  break;
              case Calendar.TUESDAY:
            	  dayOfWeek.append(DayOfWeek.TUESDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.TUESDAY);
                  break;
              case Calendar.WEDNESDAY:
            	  dayOfWeek.append(DayOfWeek.WEDNESDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.WEDNESDAY);
                  break;
              case Calendar.THURSDAY:
            	  dayOfWeek.append(DayOfWeek.THURESDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.THURESDAY);
                  break;
              case Calendar.FRIDAY:
            	  dayOfWeek.append(DayOfWeek.FRIDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.FRIDAY);
                  break;
              case Calendar.SATURDAY:
            	  dayOfWeek.append(DayOfWeek.SATURDAY.getWeekdayShort());
                  dateBean.setWeekDayEnum(DayOfWeek.SATURDAY);
                  break;
            }

            String yearMonthDay = yearMonth.concat(strDay);
            dateBean.setPublicHolidayFlg(publicHolidaySet.contains(yearMonthDay));
            dateBean.setYearMonthDay(yearMonthDay);
            dateBean.setWeekDay(dayOfWeek.toString());

            dateBeanList.add(dateBean);
        }

        return dateBeanList;
    }

    /**
     * 日付の計算を行う
     *
     * @param cal 日付時刻の指定があればセットする。
     *     nullの場合、現在の日付時刻で新しいCalendarインスタンスを生成する。
     * @param addYera 加算・減算する年数
     * @param addMonth 加算・減算する月数
     * @param addDate 加算・減算する日数
     * @return    計算後の Calendar インスタンス。
     */
    public static Calendar add(Calendar cal,
                               int addYera,int addMonth,int addDate){
        if (cal == null) {
            cal = Calendar.getInstance();
        }
        cal.add(Calendar.YEAR, addYera);
        cal.add(Calendar.MONTH, addMonth);
        cal.add(Calendar.DATE, addDate);
        return cal;
    }

    /**
     * 日付の計算を行う
     *
     * @param yearMonthDay 対象日付 yyyyMMdd
     * @param addYear 加算・減算する年数
     * @param addMonth 加算・減算する月数
     * @param addDate 加算・減算する日数
     * @return    計算後文字列。
     */
    public static String add(String yearMonthDay,
                               int addYear,int addMonth,int addDate) throws CommonException {
        if (CheckUtils.isEmpty(yearMonthDay)) {
            throw new CommonException("引数の文字列["+ yearMonthDay +"]は不正です。");
        }
        if (yearMonthDay.length() != 8) {
            throw new CommonException("引数の文字列["+ yearMonthDay +"]は不正です。");
        }

        SimpleDateFormat sdf= new SimpleDateFormat(CommonConstant.YEARMONTHDAY_NOSL);

        int year = Integer.parseInt(yearMonthDay.substring(0, 4));
        // 月は0始まりなので1マイナスする。
        int month = Integer.parseInt(yearMonthDay.substring(4, 6)) - 1;
        int day = Integer.parseInt(yearMonthDay.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        cal.add(Calendar.YEAR, addYear);
        cal.add(Calendar.MONTH, addMonth);
        cal.add(Calendar.DATE, addDate);

        Date date = cal.getTime();

        return (String) sdf.format(date);
    }

    /**
     * パラメタ文字列の先頭を指定された桁数に
     * なるまで0埋めする
     *
     * @param String param パラメタ文字列
     * @param int digit 桁数
     */
    public static String padWithZero(String param, int digit) {
        // パラメタ文字列の文字列長を取得
        int paramLength = 0;
        if (null != param) {
            paramLength = param.length();
        }

        // 指定された桁数になるまで0埋め
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digit - paramLength; i++) {
            buffer.append("0");
        }
        if (null != param) {
            buffer.append(param);
        }

        if (0 < buffer.length()) {
            return buffer.toString();
        } else {
            //パラメタ文字列がnullで、かつ指定桁数が0の場合
            return param;
        }
    }

    /**
     * 最大ページ数を計算して取得する。
     * @param length データ数
     * @return show 1ページあたりのデータ表示数
     * @author naraki
     */
    public static int getMaxPage (int length, int show) {
        int rtn = 0;

        int mod = length % show;

        rtn = length / show;

        if (mod != 0) {
            // 割り切れなかった場合は１加算する
            rtn++;
        }

        return rtn;
    }

    /**
     * hh:mm 形式の時間を秒に変換する
     * @param time 対象の時間 hh:mm
     * @return 変換後の値
     */
    public static long getSecond(String time) {
        String colon = ":";
        String[] timeArr = time.split(colon);

        // 時間 と 分 に分割する
        long hh = Long.parseLong(timeArr[0]);
        long mm = Long.parseLong(timeArr[1]);


        return (hh * 3600) + (mm * 60);
    }
}
