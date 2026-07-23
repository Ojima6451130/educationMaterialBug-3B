package jp.co.kikin.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.ShiftMstMntDto;
import jp.co.kikin.model.BaseShiftMstMntForm;
import jp.co.kikin.model.ShiftPatternBean;
import jp.co.kikin.model.ShiftPatternForm;
import jp.co.kikin.model.WorkRecordInputForm;
import jp.co.kikin.service.ShiftMstMntLogic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")

public class ShiftPatternController {
    // 凡例表示画面共通URL
    public static final String SCREEN_PATH = "/ShiftPattern";
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

    @RequestMapping(value = SCREEN_PATH)
    public String init(HttpServletRequest request, Model model, ShiftPatternForm form, BindingResult bindingResult)
            throws Exception {

        return view("init", request, model, form, bindingResult);
    }

    public String view(String processType, HttpServletRequest request, Model model, ShiftPatternForm form,
            BindingResult bindingResult)
            throws Exception {

        // セッション
        HttpSession session = request.getSession();

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        ShiftPatternForm baseShiftMstMntForm = (ShiftPatternForm) form;

        // シフトマスタロジック
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();
        // シフトマスタの取得
        List<ShiftMstMntDto> shiftMstMntDto = shiftMstMntLogic.getShiftData(loginUserDto);

        // データを変換する（基本シフト凡例）
        List<ShiftPatternBean> shiftPatternBeanList = this.shiftPatternDataToBean(shiftMstMntDto);

        // フォームにデータをセットする
        baseShiftMstMntForm.setShiftPatternBeanList(shiftPatternBeanList);
        model.addAttribute("shiftPatternBeanList", shiftPatternBeanList);

        return "ShiftPattern";
    }

    /**
     * dtoデータをBeanのリストへ変換する
     * 
     * @param shiftMstMntDtoList 勤務実績マップ key 稼働日, val 勤務実績Dto
     * @return
     * @author nishioka
     * @throws ParseException
     */
    private List<ShiftPatternBean> shiftPatternDataToBean(
            List<ShiftMstMntDto> shiftMstMntDtoList) throws ParseException {

        // 戻り値
        List<ShiftPatternBean> returnList = new ArrayList<ShiftPatternBean>(shiftMstMntDtoList.size());

        for (ShiftMstMntDto shiftMstMntDto : shiftMstMntDtoList) {

            // 勤務実績Bean
            ShiftPatternBean shiftPatternBean = new ShiftPatternBean();
            shiftPatternBean.setShiftName(shiftMstMntDto.getShiftName());
            shiftPatternBean.setSymbol(shiftMstMntDto.getSymbol());
            shiftPatternBean
                    .setTimeZone(shiftMstMntDto.getStartTime() + "~" + shiftMstMntDto.getEndTime());
            shiftPatternBean.setBreakTime(shiftMstMntDto.getBreakTime());

            returnList.add(shiftPatternBean);
        }

        return returnList;
    }
    /**
     * 説明：凡例表示初期処理
     * 
     * @author yokota
     *
     */
    // @Controller
    // @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    // @RequestMapping(method = RequestMethod.GET, value = SCREEN_PATH)
    // /** 処理区分 */
    // private enum PROCCESS_TYPE {INIT}
    // /**
    // * 凡例表示のアクション
    // *
    // * @param mapping アクションマッピング
    // * @param form アクションフォーム
    // * @param req リクエスト
    // * @param res レスポンス
    // * @return アクションフォワード
    // * @author yokota
    // */
    // @RequestMapping(method = RequestMethod.GET, value = SCREEN_PATH)
    // public String init(HttpServletRequest req,BaseShiftMstMntForm form)
    // throws Exception {

    // return view(PROCCESS_TYPE.INIT,req,form);
    // }
    // /**
    // * 画面情報取得処理.
    // *
    // * @param req リクエストスコープ上にオブジェクトを載せるためのmap
    // * @return view名称
    // * @throws Exception
    // */
    // private String view(PROCCESS_TYPE processType, HttpServletRequest
    // req,BaseShiftMstMntForm form) throws Exception {

    // return "ShiftPattern";

}
