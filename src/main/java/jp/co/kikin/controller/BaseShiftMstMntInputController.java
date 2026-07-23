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
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.ParseException;


/**
 * 説明：基本シフト入力初期処理のアクション
 *
 * @author naraki
 *
 */

@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class BaseShiftMstMntInputController extends DailyShiftAbstractController {

    @Autowired
    CommonUtils util;

    @Autowired
    MonthlyShiftLogic monthlyShiftLogic;
    @Autowired
    CommonConstant commonConstant;

    /** 画面URL */
    public static final String SCREEN_PATH = "/baseShiftInput";
    /** 「登録」押下時 */
    public static final String SCREEN_PATH_REGIST = "/baseShiftInput/regist";

    public static final String PATH = "/kikin";

    /** サービス機能名={@value} */
    public static final String CONTENTS = "基本シフト登録";

    /**
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

        Map<String, String> shiftCmbMap = comboListUtils.getComboShift(true);

        // シフトマスタロジック
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();
        // シフトマスタの取得
        List<ShiftMstMntDto> shiftMstMntDto = shiftMstMntLogic.getShiftData(loginUserDto);

        // 基本シフトマスタロジック
        BaseShiftLogic baseShiftLogic = new BaseShiftLogic();
        // 設定済み基本シフトデータの取得
        Map<String, BaseShiftDto> baseShiftDataMap = baseShiftLogic.getBaseShiftData();

        // データを変換する（基本シフト凡例）
        List<BaseShiftPatternBean> shiftPatternBeanList = this.shiftPatternDataToBean(shiftMstMntDto);
        // データを変換する（設定済み基本シフト）
        List<BaseShiftMstMntBean> dateBeanList = this.listDataDtoToBean(baseShiftDataMap, loginUserDto);

        // フォーム
        // BaseShiftMstMntForm baseShiftMstMntForm = (BaseShiftMstMntForm) form;
        // フォームにデータをセットする
        form.setBaseShiftMstMntBeanList(dateBeanList);
        form.setShiftCmbMap(shiftCmbMap);
        form.setBaseShiftPatternBeanList(shiftPatternBeanList);
        model.addAttribute("baseShiftMstMntForm", form);

        model.addAttribute("baseShiftMstMntBeanList", dateBeanList);
        model.addAttribute("shiftCmbMap", shiftCmbMap);
        model.addAttribute("baseShiftPatternBeanList", shiftPatternBeanList);

        return "baseShiftMntInput";
    }
    
    /**
     * 勤務実績入力確認登録処理のアクション
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author nishioka
     */
    @RequestMapping(value = SCREEN_PATH_REGIST)
    public String regist(HttpServletRequest request, HttpSession session, Model model, BaseShiftMstMntForm form, BindingResult bindingResult)
            throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        BaseShiftMstMntForm baseShiftMstMntForm = form;
        log.info(baseShiftMstMntForm);

        // 基本シフトロジック
        BaseShiftLogic baseShiftLogic = new BaseShiftLogic();

        // フォームデータをDtoに変換する
        List<BaseShiftDto> baseShiftDto = this.formToDto(baseShiftMstMntForm);

        // 基本シフトデータの更新・登録を行う
        baseShiftLogic.registerBaseShift(baseShiftDto, loginUserDto);

        return view("regist", request, session, model, form, bindingResult);

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
            baseShiftMstMntBean.setShiftIdOnSaturday(baseShiftDto.getShiftIdOnSaturday());
            baseShiftMstMntBean.setShiftIdOnSunday(baseShiftDto.getShiftIdOnSunday());

            returnList.add(baseShiftMstMntBean);
        }

        return returnList;
    }


    /**
     * formデータをDtoに変化する
     *
     * @param
     * @return
     * @author nishioka
     */
    private List<BaseShiftDto> formToDto(BaseShiftMstMntForm baseShiftMstMntForm) {

        // 戻り値のリスト
        List<BaseShiftDto> dtoList = new ArrayList<BaseShiftDto>();
        // 画面の一覧
        List<BaseShiftMstMntBean> baseShiftMstMntBean = baseShiftMstMntForm.getBaseShiftMstMntBeanList();

        for (BaseShiftMstMntBean bean : baseShiftMstMntBean) {
            BaseShiftDto baseShiftDto = new BaseShiftDto();

            // 計算以外の部分をセットする
            baseShiftDto.setEmployeeId(bean.getEmployeeId()); // 社員ID
            baseShiftDto.setShiftIdOnMonday(bean.getShiftIdOnMonday()); // 月曜シフト
            baseShiftDto.setShiftIdOnTuesday(bean.getShiftIdOnTuesday()); // 火曜シフト
            baseShiftDto.setShiftIdOnWednesday(bean.getShiftIdOnWednesday()); // 水曜シフト
            baseShiftDto.setShiftIdOnThursday(bean.getShiftIdOnThursday()); // 木曜シフト
            baseShiftDto.setShiftIdOnFriday(bean.getShiftIdOnFriday()); // 金曜シフト
            baseShiftDto.setShiftIdOnSaturday(bean.getShiftIdOnSaturday()); // 土曜シフト
            baseShiftDto.setShiftIdOnSunday(bean.getShiftIdOnSunday()); // 日曜シフト
            dtoList.add(baseShiftDto);
        }
        return dtoList;
    }
}
