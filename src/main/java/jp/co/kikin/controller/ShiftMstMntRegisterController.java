/**
 * ファイル名：ShiftMstMntRegisterAction.java
 *
 * 変更履歴
 * 1.0  2010/09/04 Kazuya.Naraki
 * 1.1  2025/04/11 Yokota.Yuito
 */
package jp.co.kikin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.dto.ShiftMstMntDto;
import jp.co.kikin.model.ShiftMstMntBean;
import jp.co.kikin.model.ShiftMstMntForm;
import jp.co.kikin.model.ShiftMstMntRegisterForm;
import jp.co.kikin.service.ShiftMstMntLogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




/**
 * 説明：シフトマスタメンテナンス登録アクションクラス
 *
 * @author naraki
 *
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class ShiftMstMntRegisterController {
    // シフトマスタメンテナンス入力画面共通URL
    public static final String SCREEN_PATH = "/shiftMstMnt/regist";
    /** シフトマスタメンテ画面検索URL */
    public static final String SCREEN_PATH_REGIST = "/shiftMstMnt/register";

    /** 処理区分 */
    private enum PROCCESS_TYPE {
        INIT, REGIST
    }

    /**
     * シフトマスタメンテナンス登録初期表示アクションクラス
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @RequestMapping(value = SCREEN_PATH, method = RequestMethod.GET)
    public String shiftMstMntRegisterInit(ShiftMstMntForm form, HttpServletRequest req, HttpSession session, Model model)
            throws Exception {
        return view(PROCCESS_TYPE.INIT, req, session, form, model);
    }

    private String view(PROCCESS_TYPE INIT, HttpServletRequest req,HttpSession session, ShiftMstMntForm form, Model model)
            throws Exception {
        // 1シフトマスタのデータを取得する
        // インスタンスを生成する
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();

        // 1.1 ShiftMstMntLogic.getShiftData()を呼び出す
        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // シフトマスタ情報を取得する
        List<ShiftMstMntDto> shiftMstMntDtoList = shiftMstMntLogic.getShiftData(loginUserDto);

        // 2 1で取得したデータをフォームに変換する。
        // インスタンスの生成
        ShiftMstMntForm shiftForm = new ShiftMstMntForm();
        // フォームへ一覧をセットする
        shiftForm.setShiftMstMntBeanList(dtoToForm(shiftMstMntDtoList));
        // 戻り先を保存
        model.addAttribute("shiftMstMntDtoList", shiftMstMntDtoList);

        return SCREEN_PATH;
    }

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * シフトマスタメンテナンス登録アクションクラス
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @PostMapping(value = "/shiftMstMntRegister")
    public String shiftMstMntRegister(@RequestParam("shiftName") String shiftName,
    @RequestParam("symbol") String symbol, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
     @RequestParam("breakTime") String breakTime,
     ShiftMstMntRegisterForm form, HttpServletRequest req,HttpSession session,
            HttpServletResponse res, BindingResult bindingResult) throws Exception {

        // public ActionForward execute(ActionMapping mapping, ActionForm form,
        // HttpServletRequest req, HttpServletResponse res) throws Exception {

        log.info(new Throwable().getStackTrace()[0].getMethodName());

        // フォワードキー
        String forward = CommonConstant.SUCCESS;


        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        ShiftMstMntRegisterForm shiftMstMntRegisterForm = (ShiftMstMntRegisterForm) form;

        // リクエスト内容をDtoに変換する
        ShiftMstMntDto shiftMstMntDto = this.formToDto(shiftMstMntRegisterForm);

        // ロジック生成
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();

        // 登録
        shiftMstMntLogic.registerMshift(shiftMstMntDto, loginUserDto);

        String redirectUrl = "redirect:/kikin/shiftMstMnt";

        return redirectUrl;
    }

    /**
     * リクエスト情報をDtoのリストにセットする。
     *
     * @param shiftMstMntRegisterForm シフトマスタ登録フォーム
     * @return シフトマスタDtoリスト
     * @author naraki
     */
    private ShiftMstMntDto formToDto(ShiftMstMntRegisterForm shiftMstMntRegisterForm) {
        // シフトマスタDto
        ShiftMstMntDto shiftMstMntDto = new ShiftMstMntDto();

        String shiftName = shiftMstMntRegisterForm.getShiftName();
        String symbol = shiftMstMntRegisterForm.getSymbol();
        String startTime = shiftMstMntRegisterForm.getStartTime();
        String endTime = shiftMstMntRegisterForm.getEndTime();
        String breakTime = shiftMstMntRegisterForm.getBreakTime();

        // Dtoに値をセットする
        shiftMstMntDto.setShiftName(shiftName);
        shiftMstMntDto.setSymbol(symbol);
        shiftMstMntDto.setStartTime(startTime);
        shiftMstMntDto.setEndTime(endTime);
        shiftMstMntDto.setBreakTime(breakTime);

        return shiftMstMntDto;
    }

    /**
     * 戻る処理のアクション
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @RequestMapping(value = "/shiftMstMntBack", method = RequestMethod.GET)
    public String shiftMstMnt(@RequestParam String param, ShiftMstMntForm form, HttpServletRequest req,
            HttpServletResponse res, BindingResult bindingResult) throws Exception {

        // public ActionForward execute(ActionMapping mapping, ActionForm form,
        // HttpServletRequest req, HttpServletResponse res) throws Exception {
        log.info(new Throwable().getStackTrace()[0].getMethodName());
        // フォワードキー
        String forward = "";

        forward = CommonConstant.SUCCESS;

        // return mapping.findForward(forward);
        return "shiftMstMnt";
    }
    /**
     * DtoからFormへ変換する
     *
     * @param
     * @return
     * @author naraki
     */
    private List<ShiftMstMntBean> dtoToForm(Collection<ShiftMstMntDto> colection) {
        List<ShiftMstMntBean> shiftMstMntBeanList = new ArrayList<ShiftMstMntBean>();

        for (ShiftMstMntDto dto : colection) {
            ShiftMstMntBean shiftMstMntBean = new ShiftMstMntBean();
            shiftMstMntBean.setShiftId(dto.getShiftId());
            shiftMstMntBean.setShiftName(dto.getSymbol());
            shiftMstMntBean.setSymbol(dto.getShiftName());
            shiftMstMntBean.setStartTime(dto.getStartTime());
            shiftMstMntBean.setEndTime(dto.getEndTime());
            shiftMstMntBean.setBreakTime(dto.getBreakTime());
            shiftMstMntBean.setDeleteFlg(dto.getDeleteFlg());
            shiftMstMntBeanList.add(shiftMstMntBean);

        }
        return shiftMstMntBeanList;
    }
}
