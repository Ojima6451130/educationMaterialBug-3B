/**
 * ファイル名：DailyShiftInitController.java
 *
 * 変更履歴
 * 1.0  2010/10/25 Kazuya.Naraki
 * Spring 2025/04/16 Satoshi.Tsurusawa
 * 
 */
package jp.co.kikin.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.BaseShiftDto;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.ShiftMstMntDto;
import jp.co.kikin.model.BaseShiftMstMntBean;
import jp.co.kikin.model.BaseShiftMstMntForm;
import jp.co.kikin.model.BaseShiftPatternBean;
import jp.co.kikin.service.BaseShiftLogic;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.MonthlyShiftLogic;
import jp.co.kikin.service.ShiftMstMntLogic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.text.ParseException;

@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class BaseShiftMstMntCheckController {
    // 基本シフト入力画面共通URL
    // public static final String SCREEN_PATH = "/baseShiftCheck";
    @Autowired
    CommonUtils util;

    @Autowired
    MonthlyShiftLogic monthlyShiftLogic;
    @Autowired
    CommonConstant commonConstant;

    /** 画面URL */
    public static final String SCREEN_PATH = "/BaseShiftCheck";

    public static final String PATH = "/kikin";

    /** サービス機能名={@value} */
    public static final String CONTENTS = "基本シフト確認";

    /**
     * 画面初期表示時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH)
    public String init(HttpServletRequest request, HttpSession session, Model model, BaseShiftMstMntForm form, BindingResult bindingResult)
            throws Exception {
        return view("init", request, session, model, form, bindingResult);
    }

    // 表示
    public String view(String processType, HttpServletRequest request, HttpSession session,  Model model, BaseShiftMstMntForm form,
            BindingResult bindingResult)
            throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // セレクトボックス（シフトマスタ）の取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
        // シフトマスタより凡例表示用のデータを取得する。
        // シフトマスタからデータを取得
        // ShiftMstMntLogic.getShiftData()を呼び出す
        // シフトマスタロジック
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();
        // シフトマスタの取得
        List<ShiftMstMntDto> shiftMstMntDto = shiftMstMntLogic.getShiftData(loginUserDto);

        Map<String, String> shiftCmbMap = comboListUtils.getComboShift(true);

        // 基本シフトマスタロジック
        BaseShiftLogic baseShiftLogic = new BaseShiftLogic();

        // 設定済み基本シフトデータの取得
        Map<String, BaseShiftDto> baseShiftDataMap = baseShiftLogic.getBaseShiftData();

        // データを変換する（基本シフト凡例）
        List<BaseShiftPatternBean> shiftPatternBeanList = this.shiftPatternDataToBean(shiftMstMntDto);
        // データを変換する（設定済み基本シフト）
        List<BaseShiftMstMntBean> dateBeanList = this.listDataDtoToBean(baseShiftDataMap, loginUserDto);

        // フォーム
        BaseShiftMstMntForm baseShiftMstMntForm = (BaseShiftMstMntForm) form;
        // フォームにデータをセットする
        baseShiftMstMntForm.setBaseShiftMstMntBeanList(dateBeanList);
        baseShiftMstMntForm.setShiftCmbMap(shiftCmbMap);
        baseShiftMstMntForm.setBaseShiftPatternBeanList(shiftPatternBeanList);
        model.addAttribute("baseShiftMstMntForm", baseShiftMstMntForm);

        model.addAttribute("baseShiftMstMntBeanList", dateBeanList);
        model.addAttribute("shiftCmbMap", shiftCmbMap);
        model.addAttribute("baseShiftPatternBeanList", shiftPatternBeanList);



        return "BaseShiftMstMntCheck";
    }

    /**
     * dtoデータをBeanのリストへ変換する
     *
     * @param shiftMstMntDtoList 勤務実績マップ key 稼働日, val 勤務実績Dto
     * @return
     * @author nishioka
     * @throws ParseException
     */
    private List<BaseShiftPatternBean> shiftPatternDataToBean(
            List<ShiftMstMntDto> shiftMstMntDtoList) throws ParseException {

        // 戻り値
        List<BaseShiftPatternBean> returnList = new ArrayList<BaseShiftPatternBean>(shiftMstMntDtoList.size());

        for (ShiftMstMntDto shiftMstMntDto : shiftMstMntDtoList) {

            // 勤務実績Bean
            BaseShiftPatternBean baseShiftPatternBean = new BaseShiftPatternBean();
            baseShiftPatternBean.setShiftName(shiftMstMntDto.getShiftName());
            baseShiftPatternBean.setSymbol(shiftMstMntDto.getSymbol());
            baseShiftPatternBean
                    .setTimeZone(shiftMstMntDto.getStartTime() + "&nbsp;&#xFF5E;&nbsp;" + shiftMstMntDto.getEndTime());
            baseShiftPatternBean.setBreakTime(shiftMstMntDto.getBreakTime());

            returnList.add(baseShiftPatternBean);
        }

        return returnList;
    }

    /**
     * dtoデータをBeanのリストへ変換する
     *
     * @param baseShiftDtoMap 基本シフトマップ key 社員ID, val 基本シフトDto
     * @return
     * @author nishioka
     * @throws ParseException
     */
    private List<BaseShiftMstMntBean> listDataDtoToBean(
            Map<String, BaseShiftDto> baseShiftDtoMap,
            LoginUserDto loginUserDto) throws ParseException {

        // 戻り値
        List<BaseShiftMstMntBean> returnList = new ArrayList<BaseShiftMstMntBean>(baseShiftDtoMap.size());

        Collection<BaseShiftDto> values = baseShiftDtoMap.values();
        for (BaseShiftDto baseShiftDto : values) {

            // 基本シフトBean
            BaseShiftMstMntBean baseShiftMstMntBean = new BaseShiftMstMntBean();
            baseShiftMstMntBean.setEmployeeId(baseShiftDto.getEmployeeId());
            baseShiftMstMntBean.setEmployeeName(baseShiftDto.getEmployeeName());

            baseShiftMstMntBean.setShiftIdOnMonday(baseShiftDto.getShiftIdOnMonday());
            baseShiftMstMntBean.setShiftIdOnTuesday(baseShiftDto.getShiftIdOnTuesday());
            baseShiftMstMntBean.setShiftIdOnWednesday(baseShiftDto.getShiftIdOnWednesday());
            baseShiftMstMntBean.setShiftIdOnThursday(baseShiftDto.getShiftIdOnThursday());
            baseShiftMstMntBean.setShiftIdOnFriday(baseShiftDto.getShiftIdOnFriday());
            baseShiftMstMntBean.setShiftIdOnSaturday(baseShiftDto.getShiftIdOnSunday());
            baseShiftMstMntBean.setShiftIdOnSunday(baseShiftDto.getShiftIdOnSaturday());

            returnList.add(baseShiftMstMntBean);
        }

        return returnList;
    }

}