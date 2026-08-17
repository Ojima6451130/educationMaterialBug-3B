/**
 * ファイル名：WorkDateRequestCheckController.java
 *
 * 変更履歴
 * 1.0  2010/09/04 Kazuya.Naraki
 * Spring 2025/04/16 Shuta.Hashimoto
 * 
 */
package jp.co.kikin.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.model.WorkDateRequestCheckForm;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.MethodComparator;
import jp.co.kikin.service.WorkDateRequestLogic;
import jp.co.kikin.CommonConstant.DayOfWeek;
import jp.co.kikin.bean.ResWorkDateRequestLogic;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.WorkDateRequestCheckDto;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.WorkDateRequestCheckBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 説明：出勤希望日確認画面
 * @author hashimoto
 */
// @Log4j2
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class WorkDateRequestCheckController {
    /** 画面URL */
    public static final String SCREEN_PATH = "/workDateRequestCheck";
    /** 表示年月変更時・前へ／次へ共通（物理名：WorkDateRequestCheckSearchAction） */
    public static final String SCREEN_PATH_SEARCH = "/workDateRequestCheck/search";
    public static final String PATH = "/kikin";
    /** セッションに保存する対象年月のキー */
    private static final String SESSION_YEAR_MONTH = "workDateRequestYearMonth";
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
     * 説明：出勤希望日確認画面初期表示に動作する処理
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     * @author hashimoto
     */
    @RequestMapping(value = SCREEN_PATH)
    public String init(
            @PageableDefault(page = 0, size = 19) Pageable pageable,
            HttpServletRequest request, HttpSession session, Model model,
            WorkDateRequestCheckForm form, BindingResult result) throws Exception {
        return view("init", pageable, request, session, model, form, result);
    }
    
    @RequestMapping(value = SCREEN_PATH_SEARCH)
    public String search(
            @PageableDefault(page = 0, size = 19) Pageable pageable,
            HttpServletRequest request, HttpSession session, Model model,
            WorkDateRequestCheckForm form, BindingResult result) throws Exception {
        return view("search", pageable, request, session, model, form, result);
    }
    /**
     * 説明：出勤希望日確認画面表示処理
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     * @author hashimoto
     */
    private String view(String processType, Pageable pageable, HttpServletRequest request, HttpSession session,
            Model model, WorkDateRequestCheckForm form, BindingResult result) throws Exception {
        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);
        
        // 対象年月日
        String yearMonth;
        // 日付Beanリスト
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
            yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 1, false);
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
            // 前へ・次へリンク押下時など、フォームに年月が乗ってこない場合はセッションから復元する
            if (searchYearMonth == null || searchYearMonth.isEmpty()) {
                searchYearMonth = (String) session.getAttribute(SESSION_YEAR_MONTH);
            }
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
        // 曜日定数を取得
        //---------------------- 
        // 土曜日
        String saturday = DayOfWeek.SATURDAY.getWeekdayShort();
        // 日曜日
        String sunday   = DayOfWeek.SUNDAY.getWeekdayShort();
        //----------------------
        // 出勤希望データ取得
        //----------------------  
        WorkDateRequestLogic workDateRequestLogic = new WorkDateRequestLogic();
        List<List<WorkDateRequestCheckDto>> workRequestCheckDtoList = workDateRequestLogic.getWorkDateRequestCheckDtoList(yearMonth);
        List<WorkDateRequestCheckBean> workDateRequestCheckBeanList = this.dtoToBean(workRequestCheckDtoList, loginUserDto);
        //No014 東國原 ページング処理のコードを追記
        //----------------------
        // ページング処理
        //----------------------
        Page<WorkDateRequestCheckBean> workDateRequestPage = this.findByPageable(workDateRequestCheckBeanList, pageable);
        
        //----------------
        // 画面への受渡し
        //----------------
        model.addAttribute("yearMonthValues", yearMonthValues);
        model.addAttribute("datebeanList", dateBeanList);
        model.addAttribute("saturday", saturday);
        model.addAttribute("sunday", sunday);
        
        //No014 東國原 ページング処理の結果をmodelに加える
        model.addAttribute("workDateRequestPage", workDateRequestPage);
        model.addAttribute("workDateRequestCheckBeanList", workDateRequestPage.getContent());
        session.setAttribute(SESSION_YEAR_MONTH, form.getYearMonth());
        return "workDateRequestCheck";
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
    private List<WorkDateRequestCheckBean> dtoToBean(List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList,
            LoginUserDto loginUserDto)
            throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException {
        List<WorkDateRequestCheckBean> workDateRequestCheckBeanList = new ArrayList<WorkDateRequestCheckBean>();
        for (List<WorkDateRequestCheckDto> workRequestCheckDtoList : workRequestCheckDtoNestedList) {
            // 実行するオブジェクトの生成
            WorkDateRequestCheckBean workDateRequestCheckBean = new WorkDateRequestCheckBean();
            // メソッドの取得
            Method[] methods = workDateRequestCheckBean.getClass().getMethods();
            // メソッドのソートを行う
            Comparator<Method> sortAsc = new MethodComparator();
            Arrays.sort(methods, sortAsc); // 配列をソート
            int index = 0;
            int listSize = workRequestCheckDtoList.size();
            String employeeId = "";
            String employeeName = "";
            for (int i = 0; i < methods.length; i++) {
                // "setShiftIdXX" のメソッドを動的に実行する
                if (methods[i].getName().startsWith("setSymbol") && listSize > index) {
                    WorkDateRequestCheckDto workDateRequestCheckDto = workRequestCheckDtoList.get(index);
                    // メソッド実行
                    methods[i].invoke(workDateRequestCheckBean, workDateRequestCheckDto.getMyRequestSymbol());
                    employeeId = workDateRequestCheckDto.getEmployeeId();
                    employeeName = workDateRequestCheckDto.getEmployeeName();
                    index++;
                }
            }
            workDateRequestCheckBean.setEmployeeId(employeeId);
            workDateRequestCheckBean.setEmployeeName(employeeName);
            workDateRequestCheckBeanList.add(workDateRequestCheckBean);
        }
        return workDateRequestCheckBeanList;
    }
    //No026 東國原 ページング機能を追加
    private Page<WorkDateRequestCheckBean> findByPageable(
            List<WorkDateRequestCheckBean> workDateRequestCheckBeanList, Pageable pageable) {
        // 該当レコード数取得
        int totalRows = workDateRequestCheckBeanList.size();
        // 先頭レコードの位置設定
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        // 末尾レコードの位置設定
        int lastResult = Math.min(firstResult + pageable.getPageSize(), totalRows);
        List<WorkDateRequestCheckBean> content = (firstResult >= totalRows)
                ? new ArrayList<>()
                : workDateRequestCheckBeanList.subList(firstResult, lastResult);
        Page<WorkDateRequestCheckBean> workDateRequestPage = new PageImpl<>(content, pageable, totalRows);
        return workDateRequestPage;
    }
}