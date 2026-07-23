/**
 * ファイル名：DailyShiftInitController.java
 *
 * 変更履歴
 * 1.0  2010/10/25 Kazuya.Naraki
 * Spring 2025/04/16 Shuta.Hashimoto
 * 
 */
package jp.co.kikin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.CheckUtils;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.dto.DailyShiftDto;
import jp.co.kikin.model.DailyShiftBean;
import jp.co.kikin.model.DailyShiftForm;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.DailyShiftLogic;
import jp.co.kikin.service.MonthlyShiftLogic;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 説明：日別シフト初期処理のアクション
 *
 * @author naraki
 *
 */

@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class DailyShiftController {

    @Autowired
    CommonUtils util;

    @Autowired
    MonthlyShiftLogic monthlyShiftLogic;

    @Autowired
    CommonConstant commonConstant;

    /** 画面URL */
    public static final String SCREEN_PATH = "/dailyShift";
    /** 「検索」押下時 */
    public static final String SCREEN_PATH_SEARCH = "/dailyShift/search";

    public static final String PATH = "/kikin";

    /** サービス機能名={@value} */
    public static final String CONTENTS = "日別シフト画面";
        /** 00:00 */
        private final String TIME00 = "00:00";
        /** 01:00 */
        private final String TIME01 = "01:00";
        /** 02:00 */
        private final String TIME02 = "02:00";
        /** 03:00 */
        private final String TIME03 = "03:00";
        /** 04:00 */
        private final String TIME04 = "04:00";
        /** 05:00 */
        private final String TIME05 = "05:00";
        /** 06:00 */
        private final String TIME06 = "06:00";
        /** 07:00 */
        private final String TIME07 = "07:00";
        /** 08:00 */
        private final String TIME08 = "08:00";
        /** 09:00 */
        private final String TIME09 = "09:00";
        /** 10:00 */
        private final String TIME10 = "10:00";
        /** 11:00 */
        private final String TIME11 = "11:00";
        /** 12:00 */
        private final String TIME12 = "12:00";
        /** 13:00 */
        private final String TIME13 = "13:00";
        /** 14:00 */
        private final String TIME14 = "14:00";
        /** 15:00 */
        private final String TIME15 = "15:00";
        /** 16:00 */
        private final String TIME16 = "16:00";
        /** 17:00 */
        private final String TIME17 = "17:00";
        /** 18:00 */
        private final String TIME18 = "18:00";
        /** 19:00 */
        private final String TIME19 = "19:00";
        /** 20:00 */
        private final String TIME20 = "20:00";
        /** 21:00 */
        private final String TIME21 = "21:00";
        /** 22:00 */
        private final String TIME22 = "22:00";
        /** 23:00 */
        private final String TIME23 = "23:00";
        /** 24:00 */
        private final String TIME24 = "24:00";
        /** 出勤 */
        private final String WORKHOURS = "出勤";
        /** 休憩 */
        private final String BREAKTIME = "休憩";

    /**
     * Viewに共通URLを渡す.
     *
     * @return 保守画面共通URL
     *
     *         /**
     *         画面初期表示時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH)
    public String init(HttpServletRequest request, Model model, DailyShiftForm form, BindingResult bindingResult, DailyShiftBean time)
            throws Exception {
        
        return view("init", request, model, form, bindingResult, time);
    }

    // 表示
    public String view(String processType, HttpServletRequest request, Model model, DailyShiftForm form,
            BindingResult bindingResult,DailyShiftBean time)
            throws Exception {

        String yearMonthDay;
        String yearMonthDayDisplay;

         if (processType == "init") {
            // [1]システム日付を取得する。
            // (1)Com画面表示用の形式に成型するmonUtils.getFisicalDay()を呼び出す
            yearMonthDay = CommonUtils.getFisicalDay();

            // (2) 画面表示用の形式に成型する
            // CommonUtils.changeFormat()を呼び出す
            String tempYearMonthDayDisplay = CommonUtils.changeFormat(yearMonthDay, CommonConstant.YEARMONTHDAY_NOSL,
                    CommonConstant.YEARMONTHDAY);
            yearMonthDayDisplay = dateFormat(tempYearMonthDayDisplay);
        } else {
            // DailyShiftForm dailyShiftForm = new DailyShiftForm();
            // 表示年月
            yearMonthDayDisplay = form.getYearMonthDayDisplay();
            yearMonthDay = yearMonthDayDisplay != null ? form.getYearMonthDayDisplay().replaceAll("[^0-9]", "") : "";
        }

        // 対象日付の社員分のシフトデータを、シフトテーブル、シフトマスタより取得する。
        // (1)HibetsuShiftLogic.getHibetsuShiftDtoList()を呼び出す
        DailyShiftLogic dailyShiftLogic = new DailyShiftLogic();
        List<DailyShiftDto> dailyShiftDtoList = dailyShiftLogic.getDailyShiftDtoList(yearMonthDay);

        List<DailyShiftBean> dailyShiftBeanList = dtoToForm(dailyShiftDtoList);
        
        //画面上の時間の表示
            String TimeZoneArray[] = new String[24];
            for (int i = 0; i < 24; i++) {
                StringBuilder val = new StringBuilder();
                val.append(String.format("%02d", i));
                val.append("：00 ~");
                val.append(String.format("%02d", i + 1));
                val.append("：00");
    
                TimeZoneArray[i] = val.toString(); 
            }
        
        // 必要情報を画面FormとModelにセットする
        form.setYearMonthDay(yearMonthDay);
        form.setYearMonthDayDisplay(yearMonthDayDisplay);
        form.setDailyShiftBeanList(dailyShiftBeanList);

        model.addAttribute("TimeZoneArray", TimeZoneArray);
        model.addAttribute("yearMonthDay", yearMonthDayDisplay);
        model.addAttribute("dailyShiftBeanList", dailyShiftBeanList);

        return "dailyShift";
    }
    
    // 表示
    @RequestMapping(value = SCREEN_PATH_SEARCH)
    public String search(HttpServletRequest request, HttpSession session, Model model, DailyShiftForm form,
            BindingResult bindingResult, DailyShiftBean time)
            throws Exception {

        // model.addAttribute("yearMonthDay", yearMonthDay );
        return view("search", request, model, form, bindingResult,time);
    }

    // yyyy/MM/dd　→　yyyy―MM-dd
    private String dateFormat(String yearMonthDay)  throws Exception{
        // 変換元と変換先のフォーマットを定義
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    
        String formattedDate = null;  // 変換後の日付を格納する変数
    
        try {
            // 入力文字列をDateオブジェクトに変換
            Date date = inputFormat.parse(yearMonthDay);
    
            // Dateオブジェクトを新しいフォーマットで文字列に変換
            formattedDate = outputFormat.format(date);
    
        } catch (ParseException e) {
            System.err.println("日付の形式が正しくありません: " + e.getMessage());
        }
    
        return formattedDate;  // 変換された日付、またはnullが返される
    }

     /**
     * DtoからFormへ変換する
     * @param
     * @return
     * @author naraki
     */
    protected List<DailyShiftBean> dtoToForm(List<DailyShiftDto> list) {
        List<DailyShiftBean> employeeMstMntBeanList = new ArrayList<DailyShiftBean>();

        for (DailyShiftDto dto : list) {
            DailyShiftBean dailyShiftBean = new DailyShiftBean();

            String startTime = dto.getStartTime();
            String endTime = dto.getEndTime();
            String breakTime = dto.getBreakTime();

            // 時刻が""でなく㎜：ddでなければ0埋め
            if (!CheckUtils.isEmpty(startTime)) {
                startTime = CommonUtils.padWithZero(startTime, 5);
            }
            if (!CheckUtils.isEmpty(endTime)) {
                endTime = CommonUtils.padWithZero(endTime, 5);
            }
            if (!CheckUtils.isEmpty(breakTime)) {
                breakTime = CommonUtils.padWithZero(breakTime, 5);
            }

            if (TIME00.compareTo(endTime) < 0 && TIME01.compareTo(startTime) > 0) {
                // 00:00～01:00
                dailyShiftBean.setBoolTime00(true);

                if (TIME00.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime00(WORKHOURS);
                }
            }
            if (TIME01.compareTo(endTime) < 0 && TIME02.compareTo(startTime) > 0) {
                // 01:00～02:00
                dailyShiftBean.setBoolTime01(true);

                if (TIME01.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime01(WORKHOURS);
                }
            }
            if (TIME02.compareTo(endTime) < 0 && TIME03.compareTo(startTime) > 0) {
                // 02:00～03:00
                dailyShiftBean.setBoolTime02(true);

                if (TIME02.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime02(WORKHOURS);
                }
            }
            if (TIME03.compareTo(endTime) < 0 && TIME04.compareTo(startTime) > 0) {
                // 03:00～04:00
                dailyShiftBean.setBoolTime03(true);

                if (TIME03.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime03(WORKHOURS);
                }
            }
            if (TIME04.compareTo(endTime) < 0 && TIME05.compareTo(startTime) > 0) {
                // 05:00～05:00
                dailyShiftBean.setBoolTime04(true);

                if (TIME04.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime04(WORKHOURS);
                }
            }
            if (TIME05.compareTo(endTime) < 0 && TIME06.compareTo(startTime) > 0) {
                // 05:00～06:00
                dailyShiftBean.setBoolTime05(true);

                if (TIME05.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime05(WORKHOURS);
                }
            }
            if (TIME06.compareTo(endTime) < 0 && TIME07.compareTo(startTime) > 0) {
                // 06:00～07:00
                dailyShiftBean.setBoolTime06(true);

                if (TIME06.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime06(WORKHOURS);
                }
            }
            if (TIME07.compareTo(endTime) < 0 && TIME08.compareTo(startTime) > 0) {
                // 07:00～08:00
                dailyShiftBean.setBoolTime07(true);

                if (TIME07.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime07(WORKHOURS);
                }
            }
            if (TIME08.compareTo(endTime) < 0 && TIME09.compareTo(startTime) > 0) {
                // 08:00～09:00
                dailyShiftBean.setBoolTime08(true);

                if (TIME08.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime08(WORKHOURS);
                }
            }
            if (TIME09.compareTo(endTime) < 0 && TIME10.compareTo(startTime) > 0) {
                // 09:00～10:00
                dailyShiftBean.setBoolTime09(true);

                if (TIME09.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime09(WORKHOURS);
                }
            }
            if (TIME10.compareTo(endTime) < 0 && TIME11.compareTo(startTime) > 0) {
                // 10:00～11:00
                dailyShiftBean.setBoolTime10(true);

                if (TIME10.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime10(WORKHOURS);
                }
            }
            if (TIME11.compareTo(endTime) < 0 && TIME12.compareTo(startTime) > 0) {
                // 11:00～12:00
                dailyShiftBean.setBoolTime11(true);

                if (TIME11.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime11(WORKHOURS);
                }
            }
            if (TIME12.compareTo(endTime) < 0 && TIME13.compareTo(startTime) > 0) {
                // 12:00～13:00
                dailyShiftBean.setBoolTime12(true);

                if (TIME12.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime12(WORKHOURS);
                }
            }
            if (TIME13.compareTo(endTime) < 0 && TIME14.compareTo(startTime) > 0) {
                // 13:00～14:00
                dailyShiftBean.setBoolTime13(true);

                if (TIME13.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime13(WORKHOURS);
                }
            }
            if (TIME14.compareTo(endTime) < 0 && TIME15.compareTo(startTime) > 0) {
                // 14:00～15:00
                dailyShiftBean.setBoolTime14(true);

                if (TIME14.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime14(WORKHOURS);
                }
            }
            if (TIME15.compareTo(endTime) < 0 && TIME16.compareTo(startTime) > 0) {
                // 15:00～16:00
                dailyShiftBean.setBoolTime15(true);

                if (TIME15.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime15(WORKHOURS);
                }
            }
            if (TIME16.compareTo(endTime) < 0 && TIME17.compareTo(startTime) > 0) {
                // 16:00～17:00
                dailyShiftBean.setBoolTime16(true);

                if (TIME16.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime16(WORKHOURS);
                }
            }
            if (TIME17.compareTo(endTime) < 0 && TIME18.compareTo(startTime) > 0) {
                // 17:00～18:00
                dailyShiftBean.setBoolTime17(true);

                if (TIME17.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime17(WORKHOURS);
                }
            }
            if (TIME18.compareTo(endTime) < 0 && TIME19.compareTo(startTime) > 0) {
                // 18:00～19:00
                dailyShiftBean.setBoolTime18(true);

                if (TIME18.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime18(WORKHOURS);
                }
            }
            if (TIME19.compareTo(endTime) < 0 && TIME20.compareTo(startTime) > 0) {
                // 19:00～20:00
                dailyShiftBean.setBoolTime19(true);

                if (TIME19.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime19(WORKHOURS);
                }
            }
            if (TIME20.compareTo(endTime) < 0 && TIME21.compareTo(startTime) > 0) {
                // 20:00～21:00
                dailyShiftBean.setBoolTime20(true);

                if (TIME20.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime20(WORKHOURS);
                }
            }
            if (TIME21.compareTo(endTime) < 0 && TIME22.compareTo(startTime) > 0) {
                // 21:00～22:00
                dailyShiftBean.setBoolTime21(true);

                if (TIME21.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime21(WORKHOURS);
                }
            }
            if (TIME22.compareTo(endTime) < 0 && TIME23.compareTo(startTime) > 0) {
                // 22:00～23:00
                dailyShiftBean.setBoolTime22(true);

                if (TIME22.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime22(WORKHOURS);
                }
            }
            if (TIME23.compareTo(endTime) < 0 && TIME24.compareTo(startTime) > 0) {
                // 23:00～24:00
                dailyShiftBean.setBoolTime23(true);

                if (TIME23.compareTo(startTime) <= 0) {
                    // 開始時刻の時間帯の場合
                    dailyShiftBean.setStrTime23(WORKHOURS);
                }
            }

            dailyShiftBean.setEmployeeName(dto.getEmployeeName());
            dailyShiftBean.setStartTime(startTime);
            dailyShiftBean.setEndTime(endTime);

            StringBuffer breakTimeBuf = new StringBuffer();
            breakTimeBuf.append(BREAKTIME).append("： ").append(breakTime);
            dailyShiftBean.setBreakTime(breakTimeBuf.toString());

            StringBuffer workTimeBuf = new StringBuffer();
            workTimeBuf.append(WORKHOURS).append("： ").append(startTime).append(" ~ ").append(endTime);
            dailyShiftBean.setWorkTime(workTimeBuf.toString());

            employeeMstMntBeanList.add(dailyShiftBean);

        }
        return employeeMstMntBeanList;
    }
}
