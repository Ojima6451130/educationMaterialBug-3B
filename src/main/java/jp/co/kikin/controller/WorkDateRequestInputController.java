/**
 * ファイル名：WorkDateRequestInputController.java
 *
 * 変更履歴
 * 1.0  2010/09/04 Kazuya.Naraki
 * Spring 2025/04/16 Shuta.Hashimoto
 */
package jp.co.kikin.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.dto.WorkDateRequestCheckDto;
import jp.co.kikin.dto.WorkDateRequestInputDto;
import jp.co.kikin.model.WorkDateRequestInputBean;
import jp.co.kikin.model.WorkDateRequestInputForm;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.MethodComparator;
import jp.co.kikin.service.WorkDateRequestLogic;

import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.CommonConstant.DayOfWeek;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.model.DateBean;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 説明：出勤希望日入力画面初期表示アクションクラス
 *
 * @author naraki
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class WorkDateRequestInputController extends WorkDateRequestAbstractController {
    /** 画面URL */
    public static final String SCREEN_PATH = "/workDateRequestInput";
    /** 「検索」押下時 */
    public static final String SCREEN_PATH_SEARCH = "/workDateRequestInput/search";
    /** 「登録」押下時 */
    public static final String SCREEN_PATH_REGIST = "/workDateRequestInput/regist";

    public static final String PATH = "/kikin";

    /** サービス機能名={@value} */
    public static final String CONTENTS = "出勤希望日入力";

    /* 出勤希望サービスクラス */
    @Autowired
    WorkDateRequestLogic workDateRequestLogic;
    /* 出勤希望サービスクラス */
    @Autowired
    ComboListUtilLogic comboListUtilLogic;

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
    // 初期表示
    // @RequestMapping(method = RequestMethod.POST, value = SCREEN_PATH)
    @RequestMapping(value = SCREEN_PATH)
    public String init(HttpServletRequest request, HttpSession session, Model model, WorkDateRequestInputForm form, BindingResult bindingResult)
            throws Exception {
        return view("init", request, session, model, form, bindingResult);
    }

    // 表示
    public String view(String processType, HttpServletRequest request, HttpSession session, Model model, WorkDateRequestInputForm form, BindingResult bindingResult) throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        
        // 対象年月日
        String yearMonth;
        // 日付Benリスト
        List<DateBean> dateBeanList = new ArrayList<>();

        // セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
        Set<String> yearMonthSet = new LinkedHashSet<>();
        Map<String, String> yearMonthCmbMap = new LinkedHashMap<>();
        List<String> yearMonthValues;

        if (processType == "init") {
            // システム日付より対象年月を取得する。
            yearMonth = CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL);
            dateBeanList = CommonUtils.getDateBeanList(yearMonth);
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 0, 1, false);
            yearMonthSet.addAll(yearMonthCmbMap.values());
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 0, false);
            yearMonthSet.addAll(yearMonthCmbMap.values());
            yearMonthValues = new ArrayList<>(yearMonthSet);
            yearMonthValues.sort(Comparator.naturalOrder());

            // 対象年月選択のため、初期表示年月を画面フォームにセット
            String initYearMonth = CommonUtils.changeFormat(yearMonth, CommonConstant.YEARMONTH_NOSL, CommonConstant.YEARMONTH);
            form.setYearMonth(initYearMonth);
        } else {
            // 共通部品で対象年月の1ヶ月分の日付情報格納クラスのリストを取得する。
            String searchYearMonth = form.getYearMonth();
            yearMonth = CommonUtils.changeFormat(searchYearMonth, CommonConstant.YEARMONTH, CommonConstant.YEARMONTH_NOSL);
            dateBeanList = CommonUtils.getDateBeanList(yearMonth);
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 1, false);
            yearMonthSet.addAll(yearMonthCmbMap.values());
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 0, false);
            yearMonthSet.addAll(yearMonthCmbMap.values());
            yearMonthValues = new ArrayList<>(yearMonthSet);
            yearMonthValues.sort(Comparator.naturalOrder());
            
            // 対象年月選択のため、選択年月を画面フォームにセット
            form.setYearMonth(searchYearMonth);
        }

        //----------------------
        // シフトデータ取得
        //----------------------
        Map<String, String> comboShiftMap = new LinkedHashMap<>();
        comboShiftMap = comboListUtils.getComboShift(false);
        //List<String> comboShiftList = new ArrayList<>(comboShiftMap.values());

        //------------------------
        // 対象社員シフトデータ取得
        //------------------------
        List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList = workDateRequestLogic.getWorkDateRequestCheckDtoList(yearMonth);

        List<WorkDateRequestInputBean> workDateRequestInputBeanList = this.dtoToBean(workRequestCheckDtoNestedList, loginUserDto);

        //----------------------
        // 曜日定数を取得
        //---------------------- 
        // 土曜日
        String saturday = DayOfWeek.SATURDAY.getWeekdayShort();
        // 日曜日
        String sunday   = DayOfWeek.SUNDAY.getWeekdayShort();

        //----------------
        // 画面への受渡し
        //----------------
        model.addAttribute("yearMonthValues", yearMonthValues);
        form.setDateBeanList(dateBeanList);
        model.addAttribute("datebeanList", dateBeanList);
        form.setShiftCmbMap(comboShiftMap);
        model.addAttribute("comboShift", comboShiftMap);
        form.setWorkDateRequestInputBeanList(workDateRequestInputBeanList);
        model.addAttribute("workDateRequestInputBeanList", workDateRequestInputBeanList);
        model.addAttribute("saturday", sunday);
        model.addAttribute("sunday", saturday);
        model.addAttribute("userId", session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_ID));

       
        return "workDateRequestInput";
    }

    /**
     * 画面日付検索時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_SEARCH)
    public String search(HttpServletRequest request, HttpSession session, Model model, WorkDateRequestInputForm form, BindingResult bindingResult)
            throws Exception {
        return view("search", request, session, model, form, bindingResult);
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
    public String regist(HttpServletRequest request, HttpSession session, Model model, WorkDateRequestInputForm form, BindingResult bindingResult)
            throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // 対象年月
        String yearMonth = form.getYearMonth();
        String yearMonthParam = CommonUtils.changeFormat(yearMonth, CommonConstant.YEARMONTH, CommonConstant.YEARMONTH_NOSL);
                
        // 対象年月の月情報を取得する。
        List<DateBean> dateBeanList = CommonUtils.getDateBeanList(yearMonthParam);

        //------------------------
        // 対象社員シフトデータ取得
        //------------------------
        List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList = workDateRequestLogic.getWorkDateRequestCheckDtoList(yearMonthParam);

        List<WorkDateRequestInputBean> workDateRequestInputBeanList = this.dtoToBean(workRequestCheckDtoNestedList, loginUserDto);

        // 画面の希望シフト情報をList<WorkDateRequestInputBean>に格納　※対象社員分のみ変更される
        String[] shiftIds = form.getShiftIdList();
        List<WorkDateRequestInputBean> editedWorkDateRequestInputBeanList = this.getEditedBeanList(workDateRequestInputBeanList, shiftIds, loginUserDto, dateBeanList);


        // フォームデータをDtoに変換する
        List<List<WorkDateRequestInputDto>> requestDtoNestedList = this.formToDto(editedWorkDateRequestInputBeanList, dateBeanList);

        // 登録・更新処理
        workDateRequestLogic.registerRequestShift(requestDtoNestedList, loginUserDto);

        return view("regist", request, session, model, form, bindingResult);
    }

    /**
     * @throws Exception 
     * 
     * 
     */
    private List<WorkDateRequestInputBean> getEditedBeanList(List<WorkDateRequestInputBean>workDateRequestInputBeanList, String[] shiftIds, LoginUserDto loginUserDto, List<DateBean> dateBeanList) throws Exception {

        for (WorkDateRequestInputBean workDateRequestInputBean : workDateRequestInputBeanList) {
            
            if (workDateRequestInputBean.getEmployeeId().equals(loginUserDto.getEmployeeId())) {
                // 対象社員の希望シフト情報のみ画面データをもって編集する
                for(int i = 0; i < dateBeanList.size(); i++) {
                    int dayIndex = i + 1;
                    workDateRequestLogic.setWorkRequestShiftId(dayIndex, shiftIds[i], workDateRequestInputBean);
                }
            }else {
                continue;
            }
        }

        return workDateRequestInputBeanList;
    }

    /**
     * DtoからBeanへ変換する
     *
     * @param workDateRequestInputDtoMap
     * @param loginUserDto
     * @return 一覧に表示するリスト
     * @author naraki
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private List<WorkDateRequestInputBean> dtoToBean(List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList,
            LoginUserDto loginUserDto)
            throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException {

        List<WorkDateRequestInputBean> workDateRequestInputBeanList = new ArrayList<WorkDateRequestInputBean>();
        for (List<WorkDateRequestCheckDto> workRequestCheckDtoList : workRequestCheckDtoNestedList) {
            // 実行するオブジェクトの生成
            WorkDateRequestInputBean workDateRequestInputBean = new WorkDateRequestInputBean();
            // メソッドの取得
            Method[] methods = workDateRequestInputBean.getClass().getMethods();
            // メソッドのソートを行う
            Comparator<Method> sortAsc = new MethodComparator();
            Arrays.sort(methods, sortAsc); // 配列をソート
            int index = 0;
            int listSize = workRequestCheckDtoList.size();
            String employeeId = "";
            String employeeName = "";
            for (int i = 0; i < methods.length; i++) {
                // "setShiftIdXX" のメソッドを動的に実行する
                if (methods[i].getName().startsWith("setShiftId") && listSize > index) {
                    WorkDateRequestCheckDto workDateRequestCheckDto = workRequestCheckDtoList.get(index);
                    // メソッド実行
                    methods[i].invoke(workDateRequestInputBean, workDateRequestCheckDto.getMyRequestShiftId());
                    employeeId = workDateRequestCheckDto.getEmployeeId();
                    employeeName = workDateRequestCheckDto.getEmployeeName();
                    index++;
                }
            }
            workDateRequestInputBean.setEmployeeId(employeeId);
            workDateRequestInputBean.setEmployeeName(employeeName);
            workDateRequestInputBean.setRegisterFlg(false);
            workDateRequestInputBeanList.add(workDateRequestInputBean);
        }
        return workDateRequestInputBeanList;
    }

    /**
     * DtoからBeanへ変換する
     * @param monthlyShiftBeanList
     * @return DtoList
     * @author naraki
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private List<List<WorkDateRequestInputDto>> formToDto(List<WorkDateRequestInputBean> workDateRequestInputBeanList
                                                      , List<DateBean> dateBeanList) throws IllegalArgumentException,
                                                                        IllegalAccessException,
                                                                        InvocationTargetException {
        // 戻り値
        List<List<WorkDateRequestInputDto>> requestDtoNestedList = new ArrayList<List<WorkDateRequestInputDto>>();

        for (WorkDateRequestInputBean workDateRequestInputBean : workDateRequestInputBeanList) {

            List<WorkDateRequestInputDto> workDateRequestInputDtoList = new ArrayList<WorkDateRequestInputDto>();

            // メソッドの取得
            Method[] methods = workDateRequestInputBean.getClass().getMethods();

            // ソートを行う
            Comparator<Method> sortAsc = new MethodComparator();
            Arrays.sort(methods, sortAsc); // 配列をソート

            int listSize = dateBeanList.size();

            int index = 0;

            for (int i = 0; i < methods.length; i++) {
                // "getShiftIdXX" のメソッドを動的に実行する
                if (methods[i].getName().startsWith("getShiftId") && index < listSize) {
                    String yearMonthDay = "";

                    // 対象年月取得
                    yearMonthDay = dateBeanList.get(index).getYearMonthDay();

                    WorkDateRequestInputDto workDateRequestInputDto = new WorkDateRequestInputDto();
                    String myRequestShiftId = (String) methods[i].invoke(workDateRequestInputBean);

                    if (CommonConstant.BLANK_ID.equals(myRequestShiftId)) {
                        // 空白が選択されている場合
                    	myRequestShiftId = null;
                    }

                    workDateRequestInputDto.setMyRequestShiftId(myRequestShiftId);
                    workDateRequestInputDto.setEmployeeId(workDateRequestInputBean.getEmployeeId());
                    workDateRequestInputDto.setYearMonthDay(yearMonthDay);
                    workDateRequestInputDtoList.add(workDateRequestInputDto);

                    index++;
                    requestDtoNestedList.add(workDateRequestInputDtoList);
                }
            }          
        }
        return requestDtoNestedList;
    }
}