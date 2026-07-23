/**
 * ファイル名：WorkRecordInputController.java
 *
 * 変更履歴
 * 1.0  2010/11/02 Kazuya.Naraki
 * Spring 2025/04/16 Satoshi.Tsurusawa
 */
package jp.co.kikin.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.dto.WorkRecordDto;
import jp.co.kikin.model.WorkRecordInputBean;
import jp.co.kikin.model.WorkRecordInputForm;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.WorkRecordLogic;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.service.CheckUtils;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.model.DateBean;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 説明：勤務実績入力
 *
 * @author naraki
 *
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class WorkRecordInputController {

    /** 画面URL */
    public static final String SCREEN_PATH = "/workRecordInput";
    /** 「検索」押下時 */
    public static final String SCREEN_PATH_SEARCH = "/workRecordInput/search";
    /** 「登録」押下時 */
    public static final String SCREEN_PATH_REGIST = "/workRecordInput/regist";

    public static final String PATH = "/kikin";

    /**
     * Viewに共通URLを渡す.
     *
     * @return 保守画面共通URL
     */
    @ModelAttribute("path")
    public String getPath() {
        return PATH;
    }

    /**
     * 画面初期表示時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH)
    public String init(HttpServletRequest request, HttpSession session, Model model, WorkRecordInputForm form, BindingResult bindingResult)
            throws Exception {
        return view("init", request, session, model, form);
    }
    
    /**
     * 画面検索時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_SEARCH)
    public String search(HttpServletRequest request, HttpSession session, Model model, WorkRecordInputForm form, BindingResult bindingResult)
            throws Exception {
        return view("search", request, session, model, form, bindingResult);
    }

    // 表示
    public String view(String processType, HttpServletRequest request, HttpSession session, Model model, WorkRecordInputForm form,
            BindingResult bindingResult)
            throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);
        form.setEmployeeId(loginUserDto.getEmployeeId());

        // 社員ID
        String employeeId = loginUserDto.getEmployeeId();

        // 対象年月
        String yearMonth = CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL);

        // 対象年月の月情報を取得する。
        List<DateBean> dateBeanList = CommonUtils.getDateBeanList(yearMonth);

        // 勤務実績ロジック
        WorkRecordLogic workRecordLogic = new WorkRecordLogic();

        // 勤務実績データの取得
        Map<String, WorkRecordDto> workRecordMap = workRecordLogic.getWorkRecordShiftData(employeeId, yearMonth);

        // セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();

        Map<String, String> yearMonthCmbMap = comboListUtils.getComboYearMonth(
                CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL), 22, ComboListUtilLogic.KBN_YEARMONTH_PRE,
                false);
        List<WorkRecordInputBean> workRecordList = this.dtoToBean(dateBeanList, workRecordMap, loginUserDto);

        if (processType == "init") {
            // システム日付より対象年月を取得する。
            yearMonth = CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL);
            dateBeanList = CommonUtils.getDateBeanList(yearMonth);
            yearMonthCmbMap = comboListUtils.getComboYearMonth(CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL),
                    22, ComboListUtilLogic.KBN_YEARMONTH_PRE, false);


        } else if (processType == "search") {

            WorkRecordInputForm workRecordForm = (WorkRecordInputForm) form;

            // 対象年月
            yearMonth = workRecordForm.getYearMonth();

            // 対象年月の月情報を取得する。
            dateBeanList = CommonUtils.getDateBeanList(yearMonth);

            // 勤務実績データの取得
            workRecordMap = workRecordLogic.getWorkRecordData(employeeId, yearMonth);

            yearMonthCmbMap = comboListUtils.getComboYearMonth(CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL),
                    22, ComboListUtilLogic.KBN_YEARMONTH_PRE, false);

        }

        // データを変換する
        workRecordList = this.dtoToBean(dateBeanList, workRecordMap, loginUserDto);

        // フォームにデータをセットする

        model.addAttribute("dateBeanList", dateBeanList);
        // model.addAttribute("WorkRecordInputList", workRecordList);
        model.addAttribute("yearMonthCmbMap", yearMonthCmbMap);

        model.addAttribute("loginUserDto", loginUserDto);

        
        form.setDateBeanList(dateBeanList);
        form.setWorkRecordInputList(workRecordList);
        form.setEmployeeId(employeeId);
        form.setYearMonth(yearMonth);
        model.addAttribute("workRecordInputForm",form);
        return "workRecordInput";
    }

    /**
     * 画面データ登録時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_REGIST)
    public String regist(HttpServletRequest request, HttpSession session, Model model, WorkRecordInputForm form,
            BindingResult bindingResult)
            throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // 勤務実績ロジック
        WorkRecordLogic workRecordLogic = new WorkRecordLogic();

        // フォームデータをDtoに変換する
        List<WorkRecordDto> workRecordDtoList = this.formToDto(form);

        // 時間計算を行う
        workRecordLogic.calculation(workRecordDtoList);

        // 勤務実績データの更新・登録を行う
        workRecordLogic.registerWorkRecord(workRecordDtoList, loginUserDto);

        return view("init", request, session, model, form, bindingResult);

    }

    /**
     * dtoデータをBeanのリストへ変換する
     * 
     * @param workRecordMap 勤務実績マップ key 稼働日, val 勤務実績Dto
     * @return
     * @author naraki
     * @throws ParseException
     */
    private List<WorkRecordInputBean> dtoToBean(
            List<DateBean> dateBeanList,
            Map<String, WorkRecordDto> workRecordMap,
            LoginUserDto loginUserDto) throws ParseException {

        // 戻り値
        List<WorkRecordInputBean> returnList = new ArrayList<WorkRecordInputBean>();

        for (DateBean dateBean : dateBeanList) {

            // 勤務実績Bean
            WorkRecordInputBean workRecordInputBean = new WorkRecordInputBean();

            // 年月日
            String yearMonthDay = dateBean.getYearMonthDay();

            // 表示用の月日
            String monthDay = CommonUtils.changeFormat(
                    yearMonthDay,
                    CommonConstant.YEARMONTHDAY_NOSL,
                    CommonConstant.YEARMONTHDAY).substring(5, 10);

            // 月日をセットする
            workRecordInputBean.setWorkDay(yearMonthDay);
            workRecordInputBean.setWorkDayDisp(monthDay);
            // 曜日をセットする
            workRecordInputBean.setWeekDay(dateBean.getWeekDay());
            // 祝日フラグをセットする
            workRecordInputBean.setPublicHolidayFlg(dateBean.getPublicHolidayFlg());
            // 社員IDをセットする
            workRecordInputBean.setEmployeeId(loginUserDto.getEmployeeId());

            // Dtoを取得する
            WorkRecordDto workRecordDto = workRecordMap.get(yearMonthDay);

            if (CheckUtils.isEmpty(workRecordDto)) {

                // データが存在しなかった場合
                returnList.add(workRecordInputBean);
                // 次へ
                continue;
            }

            workRecordInputBean.setShiftId(workRecordDto.getShiftId());
            workRecordInputBean.setSymbol(workRecordDto.getSymbol());
            workRecordInputBean.setStartTimeShift(workRecordDto.getStartTimeShift());
            workRecordInputBean.setEndTimeShift(workRecordDto.getEndTimeShift());
            workRecordInputBean.setBreakTimeShift(workRecordDto.getBreakTimeShift());
            workRecordInputBean.setStartTime(workRecordDto.getStartTime());
            workRecordInputBean.setEndTime(workRecordDto.getEndTime());
            workRecordInputBean.setBreakTime(workRecordDto.getBreakTime());
            workRecordInputBean.setActualWorkTime(workRecordDto.getActualWorkTime());
            workRecordInputBean.setOverTime(workRecordDto.getOverTime());
            workRecordInputBean.setHolidayTime(workRecordDto.getHolidayTime());
            workRecordInputBean.setRemark(workRecordDto.getRemark());

            returnList.add(workRecordInputBean);
        }

        return returnList;
    }

    /**
     * formデータをDtoに変化する
     * 
     * @param
     * @return
     * @author naraki
     */
    private List<WorkRecordDto> formToDto(WorkRecordInputForm workRecordForm) {

        // 戻り値のリスト
        List<WorkRecordDto> dtoList = new ArrayList<WorkRecordDto>();
        // 画面の一覧
        List<WorkRecordInputBean> workRecordList = workRecordForm.getWorkRecordInputList();

        for (WorkRecordInputBean bean : workRecordList) {
            WorkRecordDto workRecordDto = new WorkRecordDto();

            // 計算以外の部分をセットする
            workRecordDto.setEmployeeId(bean.getEmployeeId()); // 社員ID
            workRecordDto.setWorkDay(bean.getWorkDay()); // 稼働日
            workRecordDto.setShiftId(bean.getShiftId()); // シフトID
            workRecordDto.setSymbol(bean.getSymbol()); // シンボル
            workRecordDto.setStartTimeShift(bean.getStartTimeShift()); // 開始時間(シフト)
            workRecordDto.setEndTimeShift(bean.getEndTimeShift()); // 終了時間(シフト)
            workRecordDto.setBreakTimeShift(bean.getBreakTimeShift()); // 休憩時間(シフト)
            workRecordDto.setStartTime(bean.getStartTime()); // 開始時間
            workRecordDto.setEndTime(bean.getEndTime()); // 終了時間
            workRecordDto.setBreakTime(bean.getBreakTime()); // 休憩時間
            workRecordDto.setRemark(bean.getRemark()); // 備考

            dtoList.add(workRecordDto);

        }

        return dtoList;

    }
    
    public String view(String processType, HttpServletRequest request, HttpSession session, Model model, WorkRecordInputForm form)throws Exception {
    	return "残念でした";
    }

}
