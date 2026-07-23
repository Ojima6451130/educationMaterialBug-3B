/**
 * ファイル名：MonthlyShiftInputController.java
 *
 * 変更履歴
 * Spring 2025/04/16 Shuta.Hashimoto
 */
package jp.co.kikin.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.kikin.CheckUtils;
import jp.co.kikin.CommonConstant.DayOfWeek;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.BaseShiftDto;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.MonthlyShiftDto;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.MonthlyShiftInputBean;
import jp.co.kikin.model.MonthlyShiftInputForm;
import jp.co.kikin.model.WorkDateRequestInputBean;
import jp.co.kikin.service.BaseShiftLogic;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.MethodComparator;
import jp.co.kikin.service.MonthlyShiftLogic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 説明：月別シフト確認画面
 *
 * @author
 *
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class MonthlyShiftInputController {

    @Autowired
    CommonUtils util;

    @Autowired
    MonthlyShiftLogic monthlyShiftLogic;

    @Autowired
    CommonConstant commonConstant;

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    ComboListUtilLogic comboListUtilLogic;

    @Autowired
    CheckUtils checkUtils;

    /** 画面URL */
    public static final String SCREEN_PATH = "/monthlyShiftInput";
    /** 「検索」押下時 */
    public static final String SCREEN_PATH_SEARCH = "/monthlyShiftInput/search";
    /** 「登録」押下時 */
    public static final String SCREEN_PATH_REGIST = "/monthlyShiftInput/regist";
    /** 「基本シフト反映」押下時 */
    public static final String SCREEN_PATH_SUBMITIMPORTKIHON = "/monthlyShiftInput/submitImportKihon";
    /** 「出勤希望反映」押下時 */
    public static final String SCREEN_PATH_DATEREQUEST = "/monthlyShiftInput/dateRequest";

    // public static final String SCREEN_PATH_PAGE =
    // "/monthlyShiftInput/monthlyShiftInputPage";

    public static final String PATH = "/kikin";

    /** サービス機能名={@value} */
    public static final String CONTENTS = "月別シフト入力画面";

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
    public String init(HttpServletRequest request, HttpSession session, Model model, MonthlyShiftInputForm form,
            BindingResult bindingResult)
            throws Exception {
        return view();
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
    public String search(HttpServletRequest request, HttpSession session, Model model, MonthlyShiftInputForm form,
            BindingResult bindingResult) throws Exception {
        return view("search", request, session, model, form, bindingResult);
    }

    // 表示
    private String view(String processType, HttpServletRequest request, HttpSession session, Model model,
            MonthlyShiftInputForm form,
            BindingResult bindingResult)
            throws Exception {

        // 対象年月日
        String yearMonth;
        // 日付Benリスト
        List<DateBean> dateBeanList = new ArrayList<>();

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
        Set<String> yearMonthSet = new LinkedHashSet<>();
        Map<String, String> yearMonthCmbMap = new LinkedHashMap<>();
        List<String> yearMonthValues;

        if (processType == "init") {
            // システム日付より対象年月を取得する。
            yearMonth = CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL);
            dateBeanList = CommonUtils.getDateBeanList(yearMonth);
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 1, false);
            yearMonthSet.addAll(yearMonthCmbMap.values());
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 0, false);
            yearMonthSet.addAll(yearMonthCmbMap.values());
            yearMonthValues = new ArrayList<>(yearMonthSet);
            yearMonthValues.sort(Comparator.naturalOrder());

            // 対象年月選択のため、初期表示年月を画面フォームにセット
            String initYearMonth = CommonUtils.changeFormat(yearMonth, CommonConstant.YEARMONTH_NOSL,
                    CommonConstant.YEARMONTH);
            form.setYearMonth(initYearMonth);
        } else {
            // 共通部品で対象年月の1ヶ月分の日付情報格納クラスのリストを取得する。
            String searchYearMonth = form.getYearMonth();
            yearMonth = CommonUtils.changeFormat(searchYearMonth, CommonConstant.YEARMONTH,
                    CommonConstant.YEARMONTH_NOSL);
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

        // シフトテーブルより各社員の月別シフトデータを取得する。
        Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap = monthlyShiftLogic.getMonthlyShiftDtoMap(yearMonth,
                true);

        List<MonthlyShiftInputBean> monthlyShiftInputBean = new ArrayList<MonthlyShiftInputBean>();
        Map<String, String> shiftCmbMap = comboListUtils.getComboShift(true);
        if (CheckUtils.isEmpty(monthlyShiftDtoMap)) {
            // データなし
            MonthlyShiftInputBean monthlyShiftBean = new MonthlyShiftInputBean();
            monthlyShiftBean.setEmployeeId(loginUserDto.getEmployeeId());
            monthlyShiftBean.setEmployeeName(loginUserDto.getEmployeeName());
            monthlyShiftBean.setRegisterFlg(true);
            monthlyShiftInputBean.add(monthlyShiftBean);
        } else {
            // データあり
            if (form.getMonthlyShiftInputBeanList() != null && form.getDateBeanList() != null) {
                monthlyShiftInputBean = form.getMonthlyShiftInputBeanList();
            } else {

                monthlyShiftInputBean = dtoToBean(monthlyShiftDtoMap, loginUserDto);
            }

        }

        if (form.getCountPage() == 0) {
            form.setCountPage(1);
            form.setOffset(16);
        }

        form.setMaxPage(CommonUtils.getMaxPage(monthlyShiftDtoMap.size(), 16));

        // フォームにデータをセットする
        form.setShiftCmbMap(shiftCmbMap);
        form.setYearMonthCmbMap(yearMonthCmbMap);

        // int offset = form.getOffset();
        // int limit = 16;
        // int startIndex = Math.max(0, offset - limit);
        // int endIndex = Math.min(offset, monthlyShiftInputBean.size());
        // List<MonthlyShiftInputBean> subList =
        // monthlyShiftInputBean.subList(startIndex, endIndex);

        // form.setMonthlyShiftInputBeanList(subList);
        form.setMonthlyShiftInputBeanList(monthlyShiftInputBean);

        form.setDateBeanList(dateBeanList);

         //----------------------
        // 曜日定数を取得
        //----------------------
        // 土曜日
        String saturday = DayOfWeek.SATURDAY.getWeekdayShort();
        // 日曜日
        String sunday   = DayOfWeek.SUNDAY.getWeekdayShort();

        model.addAttribute("satuuuurday", saturday);
        model.addAttribute("sunday", sunday);
        model.addAttribute("monthlyShiftInputForm", form);
        model.addAttribute("shiftCmbMap", shiftCmbMap);
        model.addAttribute("monthlyShiftInputBean", monthlyShiftInputBean);
        model.addAttribute("yearMonthValues", yearMonthValues);
        model.addAttribute("monthlyShiftDtoMap", monthlyShiftDtoMap);
        model.addAttribute("dateBeanList", dateBeanList);

        return "monthlyShiftInput";
    }
    
    private String view() {
		System.out.println("残念、ハズレです");
		return null;
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
    public String regist(HttpServletRequest request, HttpSession session, Model model,
            MonthlyShiftInputForm form, BindingResult bindingResult) throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        MonthlyShiftInputForm monthlyShiftForm = (MonthlyShiftInputForm) form;

        // 画面のリスト情報
        List<MonthlyShiftInputBean> monthlyShiftBeanList = monthlyShiftForm.getMonthlyShiftInputBeanList();

        // 対象年月
        String yearMonthRaw = monthlyShiftForm.getYearMonth();

        String yearMonth = yearMonthRaw.replace("/", "");

        // ロジック生成
        MonthlyShiftLogic monthlyShiftLogic = new MonthlyShiftLogic();

        // 対象年月の月情報を取得する。
        List<DateBean> dateBeanList = CommonUtils.getDateBeanList(yearMonth);

        Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap = monthlyShiftLogic.getMonthlyShiftDtoMap(yearMonth,
                false);

        monthlyShiftBeanList = dtoToBean(monthlyShiftDtoMap, loginUserDto);

        // 画面の希望シフト情報をList<MonthlyShiftInputBean>に格納 ※対象社員分のみ変更される
        String[] shiftIds = form.getShiftIdList();
        List<MonthlyShiftInputBean> monthlyShiftBeanList2 = this.getEditedBeanList(monthlyShiftBeanList, shiftIds,
                loginUserDto, dateBeanList);

        // フォームデータをDtoに変換する
        List<List<MonthlyShiftDto>> monthlyShiftDtoNestedList = this.formToDto(monthlyShiftBeanList2, dateBeanList);

        // 登録・更新処理
        monthlyShiftLogic.registerMonthlyShift(monthlyShiftDtoNestedList, loginUserDto);

        return view("regist", request, session, model, monthlyShiftForm, bindingResult);
    }

    /**
     * 出勤希望反映時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_DATEREQUEST)
    public String dateRequest(HttpServletRequest request, HttpSession session, Model model,
            MonthlyShiftInputForm form, BindingResult bindingResult) throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        MonthlyShiftInputForm monthlyShiftForm = (MonthlyShiftInputForm) form;

        // 対象年月
        String yearMonthRaw = monthlyShiftForm.getYearMonth();

        String yearMonth = yearMonthRaw.replace("/", "");

        // ロジック生成
        MonthlyShiftLogic monthlyShiftLogic = new MonthlyShiftLogic();

        // 対象年月の月情報を取得する。
        List<DateBean> dateBeanList = CommonUtils.getDateBeanList(yearMonth);

        // 希望シフトIDを取得する
        Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap = monthlyShiftLogic.getMonthlyShiftDtoMap(yearMonth,
                false);

        List<MonthlyShiftInputBean> monthlyShiftBeanList = new ArrayList<MonthlyShiftInputBean>();

        // セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();

        Map<String, String> shiftCmbMap = comboListUtils.getComboShift(true);

        Map<String, String> yearMonthCmbMap = comboListUtils.getComboYearMonth(
                CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL), 2, ComboListUtilLogic.KBN_YEARMONTH_NEXT,
                false);

        if (CheckUtils.isEmpty(monthlyShiftDtoMap)) {
            // データなし
            MonthlyShiftInputBean monthlyShiftBean = new MonthlyShiftInputBean();
            monthlyShiftBean.setEmployeeId(loginUserDto.getEmployeeId());
            monthlyShiftBean.setEmployeeName(loginUserDto.getEmployeeName());
            monthlyShiftBean.setRegisterFlg(true);

            monthlyShiftBeanList.add(monthlyShiftBean);
        } else {
            // データあり
            monthlyShiftBeanList = dtoToBean(monthlyShiftDtoMap, loginUserDto);
        }
        yearMonth = yearMonthRaw;
        // フォームにデータをセットする
        form.setShiftCmbMap(shiftCmbMap);
        form.setYearMonthCmbMap(yearMonthCmbMap);
        form.setMonthlyShiftInputBeanList(monthlyShiftBeanList);
        form.setDateBeanList(dateBeanList);
        form.setYearMonth(yearMonth);
        // ページング用
        form.setMaxPage(CommonUtils.getMaxPage(monthlyShiftDtoMap.size(), 16));

        return search(request, session, model, form, bindingResult);
    }

    /**
     * 基本シフト反映時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_SUBMITIMPORTKIHON)
    public String submitImportKihon(HttpServletRequest request, HttpSession session, Model model,
            MonthlyShiftInputForm form, BindingResult bindingResult) throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        MonthlyShiftInputForm monthlyShiftForm = (MonthlyShiftInputForm) form;

        // 対象年月
        String yearMonthRaw = monthlyShiftForm.getYearMonth();

        String yearMonth = yearMonthRaw.replace("/", "");

        // 基本シフトデータから値を取得してbeanに格納する処理
        BaseShiftLogic baseShiftLogic = new BaseShiftLogic();

        // 対象年月の月情報を取得する。
        List<DateBean> dateBeanList = CommonUtils.getDateBeanList(yearMonth);

        // 社員分の基本シフトマスタデータを取得する
        Map<String, BaseShiftDto> baseShiftDtoMap = baseShiftLogic.getBaseShiftData();

        // データベースに登録されている月別シフト情報を呼び出すためのリスト作成
        List<MonthlyShiftInputBean> monthlyShiftBeanList = new ArrayList<MonthlyShiftInputBean>();

        // セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
        Map<String, String> shiftCmbMap = comboListUtils.getComboShift(true);

        Map<String, String> yearMonthCmbMap = comboListUtils.getComboYearMonth(
                CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL), 2, ComboListUtilLogic.KBN_YEARMONTH_NEXT,
                false);

        // 社員ごとの基本シフト情報を格納するデータ
        Map<String, List<String>> weekShift = new LinkedHashMap<String, List<String>>();

        for (BaseShiftDto ShiftDto : baseShiftDtoMap.values()) {
            // 社員ごとに基本シフトデータをリスト化
            List<String> weekDayShift = new ArrayList<String>();
            weekDayShift.add(ShiftDto.getShiftIdOnSaturday());
            weekDayShift.add(ShiftDto.getShiftIdOnSunday());
            weekDayShift.add(ShiftDto.getShiftIdOnMonday());
            weekDayShift.add(ShiftDto.getShiftIdOnTuesday());
            weekDayShift.add(ShiftDto.getShiftIdOnWednesday());
            weekDayShift.add(ShiftDto.getShiftIdOnThursday());
            weekDayShift.add(ShiftDto.getShiftIdOnFriday());
            weekShift.put(ShiftDto.getEmployeeId(), weekDayShift);
        }
        // 1か月分のシフトデータを作成
        if (CheckUtils.isEmpty(baseShiftDtoMap)) {
            // データなし
            MonthlyShiftInputBean monthlyShiftBean = new MonthlyShiftInputBean();
            monthlyShiftBean.setEmployeeId(loginUserDto.getEmployeeId());
            monthlyShiftBean.setEmployeeName(loginUserDto.getEmployeeName());
            monthlyShiftBean.setRegisterFlg(true);
            monthlyShiftBeanList.add(monthlyShiftBean);
        } else {
            // データあり
            // 社員ごとに曜日ごとのシフトをMonthlyShiftInputBeanに格納するループを行う

            for (BaseShiftDto data : baseShiftDtoMap.values()) {// 社員ごとのループ
                MonthlyShiftInputBean monthlyShiftBean = new MonthlyShiftInputBean();
                int count = 0;// MonthlyShiftInputBeanに値を入れる処理を行う際に使用
                for (int k = 0; k < dateBeanList.size(); k++) {// 日にち毎にshiftIdに登録する処理
                    // 該当する日付の曜日を取得し、数値化
                    int weekDay = 0;
                    if ((dateBeanList.get(k).getWeekDay()).equals("土")) {
                        weekDay = 0;
                    } else if ((dateBeanList.get(k).getWeekDay()).equals("日")) {
                        weekDay = 1;
                    } else if ((dateBeanList.get(k).getWeekDay()).equals("月")) {
                        weekDay = 2;
                    } else if ((dateBeanList.get(k).getWeekDay()).equals("火")) {
                        weekDay = 3;
                    } else if ((dateBeanList.get(k).getWeekDay()).equals("水")) {
                        weekDay = 4;
                    } else if ((dateBeanList.get(k).getWeekDay()).equals("木")) {
                        weekDay = 5;
                    } else if ((dateBeanList.get(k).getWeekDay()).equals("金")) {
                        weekDay = 6;
                    }
                    // 以下、MonthlyShiftInputBean内のメソッドを動的に実行し1日から順番に値を入れる処理
                    // 実行するオブジェクトの生成
                    Method[] methods = monthlyShiftBean.getClass().getMethods();
                    // メソッドのソートを行う
                    Comparator<Method> sortAsc = new MethodComparator();
                    Arrays.sort(methods, sortAsc); // 配列をソート
                    int index = 0;
                    int listSize = dateBeanList.size();

                    for (int i = count; i < methods.length; i++) {
                        // "setShiftIdXX" のメソッドを動的に実行する
                        if (methods[i].getName().startsWith("setShiftId") && listSize > index) {
                            // メソッド実行
                            methods[i].invoke(monthlyShiftBean, weekShift.get(data.getEmployeeId()).get(weekDay));

                            index++;
                            i++;
                            count = i;
                            break;
                        } // if
                    } // for(動的なメソッド実行)
                } // for（日にち毎）
                monthlyShiftBean.setEmployeeId(data.getEmployeeId());
                monthlyShiftBean.setEmployeeName(data.getEmployeeName());
                monthlyShiftBean.setRegisterFlg(true);
                monthlyShiftBeanList.add(monthlyShiftBean);
            } // for(社員ごと)

        } // if(データあり)

        // 社員番号順に並び替える
        Collections.sort(monthlyShiftBeanList, new Comparator<MonthlyShiftInputBean>() {
            @Override
            public int compare(MonthlyShiftInputBean o1, MonthlyShiftInputBean o2) {
                return o1.getEmployeeId().compareTo(o2.getEmployeeId());
            }
        });
        yearMonth = yearMonthRaw;
        // フォームにデータをセットする
        form.setShiftCmbMap(shiftCmbMap);
        form.setYearMonthCmbMap(yearMonthCmbMap);
        form.setMonthlyShiftInputBeanList(monthlyShiftBeanList);
        form.setDateBeanList(dateBeanList);
        form.setYearMonth(yearMonth);
        // ページング用
        form.setMaxPage(CommonUtils.getMaxPage(baseShiftDtoMap.size(), 16));

        return search(request, session, model, form, bindingResult);

    }

    // @RequestMapping(value = SCREEN_PATH_PAGE)
    // public String monthlyshiftinputpage(HttpServletRequest request, HttpSession
    // session, Model model,
    // MonthlyShiftInputForm form, BindingResult bindingResult) throws Exception {

    // LoginUserDto loginUserDto = (LoginUserDto) session
    // .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

    // Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap =
    // monthlyShiftLogic.getMonthlyShiftDtoMap(
    // form.getYearMonth(),
    // true);
    // List<MonthlyShiftInputBean> monthlyShiftInputBean = new
    // ArrayList<MonthlyShiftInputBean>();

    // monthlyShiftInputBean = dtoToBean(monthlyShiftDtoMap, loginUserDto);

    // form.setMonthlyShiftInputBeanList(monthlyShiftInputBean);
    // // フォーム フォーマット
    // MonthlyShiftInputForm monthlyShiftForm = (MonthlyShiftInputForm) form;

    // // ページング
    // String paging = monthlyShiftForm.getPaging();

    // int listSize = monthlyShiftForm.getMonthlyShiftInputBeanList().size();
    // int MaxPage = form.getMaxPage();
    // int offset = monthlyShiftForm.getOffset();

    // int countPage = monthlyShiftForm.getCountPage();

    // int nextOffset = 0;

    // if (CommonConstant.NEXT.equals(paging)) {
    // // 次ページ

    // if (countPage == MaxPage) {
    // offset = listSize;
    // } else {
    // nextOffset = offset + 16;

    // offset = nextOffset;
    // countPage++;
    // }
    // } else {
    // // 前ページ
    // nextOffset = offset - 16;

    // if (countPage != 0) {
    // if (nextOffset < 0) {
    // offset = 0;
    // } else {
    // offset = nextOffset;
    // countPage--;
    // }
    // }

    // }
    // monthlyShiftForm.setOffset(offset);
    // monthlyShiftForm.setCountPage(countPage);

    // // 登録フラグ初期化
    // List<MonthlyShiftInputBean> monthlyShiftBeanList =
    // monthlyShiftForm.getMonthlyShiftInputBeanList();
    // for (MonthlyShiftInputBean monthlyShiftBean : monthlyShiftBeanList) {
    // monthlyShiftBean.setRegisterFlg(false);
    // }

    // return view("init", request, session, model, monthlyShiftForm,
    // bindingResult);
    // }

    /**
     * DtoからBeanへ変換する
     *
     * @param monthlyShiftDtoMap
     * @param loginUserDto
     * @return 一覧に表示するリスト
     * @author naraki
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private List<MonthlyShiftInputBean> dtoToBean(Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap,
            LoginUserDto loginUserDto)
            throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException {
        Collection<List<MonthlyShiftDto>> collection = monthlyShiftDtoMap.values();

        List<MonthlyShiftInputBean> monthlyShiftBeanList = new ArrayList<MonthlyShiftInputBean>();

        for (List<MonthlyShiftDto> monthlyShiftDtoList : collection) {

            // 実行するオブジェクトの生成
            MonthlyShiftInputBean monthlyShiftBean = new MonthlyShiftInputBean();

            // メソッドの取得
            Method[] methods = monthlyShiftBean.getClass().getMethods();

            // メソッドのソートを行う
            Comparator<Method> sortAsc = new MethodComparator();
            Arrays.sort(methods, sortAsc); // 配列をソート

            int index = 0;
            int listSize = monthlyShiftDtoList.size();

            String employeeId = "";
            String employeeName = "";

            for (int i = 0; i < methods.length; i++) {
                // "setShiftIdXX" のメソッドを動的に実行する
                if (methods[i].getName().startsWith("setShiftId") && listSize > index) {
                    MonthlyShiftDto dto = monthlyShiftDtoList.get(index);
                    // メソッド実行
                    methods[i].invoke(monthlyShiftBean, dto.getShiftId());

                    employeeId = dto.getEmployeeId();
                    employeeName = dto.getEmployeeName();

                    index++;
                }
            }

            monthlyShiftBean.setEmployeeId(employeeId);
            monthlyShiftBean.setEmployeeName(employeeName);
            monthlyShiftBean.setRegisterFlg(true);

            monthlyShiftBeanList.add(monthlyShiftBean);

        }

        return monthlyShiftBeanList;
    }

    /**
     * DtoからBeanへ変換する
     *
     * @param monthlyShiftBeanList
     * @return DtoList
     * @author naraki
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private List<List<MonthlyShiftDto>> formToDto(List<MonthlyShiftInputBean> monthlyShiftBeanList,
            List<DateBean> dateBeanList) throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException {
        // 戻り値
        List<List<MonthlyShiftDto>> monthlyShiftDtoNestedList = new ArrayList<List<MonthlyShiftDto>>();

        for (MonthlyShiftInputBean monthlyShiftBean : monthlyShiftBeanList) {

            List<MonthlyShiftDto> monthlyShiftDtoList = new ArrayList<MonthlyShiftDto>();

            // 登録フラグ
            boolean registerFlg = monthlyShiftBean.getRegisterFlg();

            // if (!registerFlg) {
            //     continue;
            // }

            // メソッドの取得
            Method[] methods = monthlyShiftBean.getClass().getMethods();

            // ソートを行う
            Comparator<Method> sortAsk = new MethodComparator();
            Arrays.sort(methods, sortAsk); // 配列をソート

            int listSize = dateBeanList.size();

            int index = 0;

            for (int i = 0; i < methods.length; i++) {
                // "getShiftIdXX" のメソッドを動的に実行する
                if (methods[i].getName().startsWith("getShiftId") && index < listSize) {
                    String yearMonthDay = "";

                    // 対象年月取得
                    yearMonthDay = dateBeanList.get(index).getYearMonthDay();

                    MonthlyShiftDto dto = new MonthlyShiftDto();
                    String shiftId = (String) methods[i].invoke(monthlyShiftBean);

                    if (CommonConstant.BLANK_ID.equals(shiftId)) {
                        // 空白が選択されている場合
                        shiftId = null;
                    }

                    dto.setShiftId(shiftId);
                    dto.setEmployeeId(monthlyShiftBean.getEmployeeId());
                    dto.setYearMonthDay(yearMonthDay);
                    monthlyShiftDtoList.add(dto);

                    index++;
                }
            }

            monthlyShiftDtoNestedList.add(monthlyShiftDtoList);

        }

        return monthlyShiftDtoNestedList;
    }

    /**
     * @throws Exception
     *
     *
     */
    private List<MonthlyShiftInputBean> getEditedBeanList(List<MonthlyShiftInputBean> monthlyShiftBeanList,
            String[] shiftIds, LoginUserDto loginUserDto, List<DateBean> dateBeanList) throws Exception {

           // 社員数を推定（1日目のデータを split して数える）
    int employeeCount = shiftIds[0].split(",", -1).length;

    // 社員ごとのBeanを用意（元のデータがあれば再利用）
    List<MonthlyShiftInputBean> editedList = new ArrayList<>();
    for (int i = 0; i < employeeCount; i++) {
        MonthlyShiftInputBean bean = new MonthlyShiftInputBean();

        // 元のリストから社員情報をコピー
        if (i < monthlyShiftBeanList.size()) {
            MonthlyShiftInputBean original = monthlyShiftBeanList.get(i);
            bean.setEmployeeId(original.getEmployeeId());
            bean.setEmployeeName(original.getEmployeeName());
        }

        editedList.add(bean);
    }

    // shiftIds[i] は各日（1日〜）
    for (int dayIndex = 0; dayIndex < shiftIds.length; dayIndex++) {
        String[] dailyShifts = shiftIds[dayIndex].split(",", -1); // 空文字も考慮

        for (int empIndex = 0; empIndex < dailyShifts.length; empIndex++) {
            String shiftId = dailyShifts[empIndex];
            MonthlyShiftInputBean bean = editedList.get(empIndex);

            if (dayIndex < 31) {
                String setterMethod = "setShiftId" + String.format("%02d", dayIndex + 1);
                try {
                    Method method = MonthlyShiftInputBean.class.getMethod(setterMethod, String.class);
                    method.invoke(bean, shiftId);
                } catch (Exception e) {
                    e.printStackTrace(); // ログだけ出して処理は継続
                }
            }
        }
    }

    return editedList;
}
}