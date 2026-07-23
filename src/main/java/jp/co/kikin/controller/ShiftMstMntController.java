/**
 * ファイル名：EmployeeMstMntInitAction.java
 *
 * 変更履歴
 * 1.0  2010/08/23 Kazuya.Naraki
 * 1.1  2025/04/11 Yokota.Yuito
 */
package jp.co.kikin.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.ShiftMstMntDto;
import jp.co.kikin.model.ShiftMstMntBean;
import jp.co.kikin.model.ShiftMstMntForm;
import jp.co.kikin.service.ShiftMstMntLogic;

/**
 * 説明：シフトマスタメンテナンス初期表示アクションクラス
 *
 * @author naraki
 *
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class ShiftMstMntController {

    // シフトマスタメンテナンス入力画面共通URL
    public static final String SCREEN_PATH = "/shiftMstMnt";
    /** シフトマスタメンテ画面検索URL */
    public static final String SCREEN_PATH_REGIST = "/shiftMstMnt/regist";
    public static final String SCREEN_PATH_UPDATE = "/shiftMstMnt/update";
    public static final String REDIRECT = "redirect:/kikin/shiftMstMnt";
    /** 処理区分 */
    private enum PROCCESS_TYPE {
        INIT, SEARCH, REGIST, PAGE, INPORT, REFLECT, UPDATE
    }


    /**
     * シフトマスタメンテナンス初期表示アクションクラス
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @RequestMapping(value = SCREEN_PATH, method = RequestMethod.GET)
    public String Init(HttpServletRequest req, HttpSession session, ShiftMstMntForm form, Model model,@RequestParam(name = "deleteFlg", required = false, defaultValue = "false") boolean deleteFlg)
            throws Exception {
        return view(PROCCESS_TYPE.INIT, req,session, form, model,deleteFlg);
    }

    private String view(PROCCESS_TYPE init, HttpServletRequest req, HttpSession session,ShiftMstMntForm form, Model model,boolean deleteFlg)
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
        // フォームへ一覧をセットする
        form.setShiftMstMntBeanList(dtoToForm(shiftMstMntDtoList));

        // 戻り先を保存
        model.addAttribute("shiftMstMntDtoList", shiftMstMntDtoList);

       return "しふとますためんて";
    }
    /**
     * シフトマスタメンテナンス登録画面遷移処理
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @RequestMapping(method = RequestMethod.POST, value = SCREEN_PATH_REGIST)
    private String regist(HttpServletRequest req, HttpSession session,ShiftMstMntForm form, Model model,@RequestParam("regist") String action)
    throws Exception {
        if (action.equals("新規登録")) {

            return "shiftMstMntRegister";

        }

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        ShiftMstMntForm shiftMstMntForm = (ShiftMstMntForm) form;

        // リクエスト内容をDtoに変換する
        List<ShiftMstMntDto> shiftMstMntDtoList = this.formToDto(shiftMstMntForm);

        // ロジック生成
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();

        // シフトマスタ情報を再検索する
        shiftMstMntDtoList = shiftMstMntLogic.getShiftData(loginUserDto);

        // フォームへ一覧をセットする
        shiftMstMntForm.setShiftMstMntBeanList(dtoToForm(shiftMstMntDtoList));
        // 戻り先を保存
        model.addAttribute("shiftMstMntDtoList", shiftMstMntDtoList);
        return "shiftMstMnt";
    }
    /**
     * 更新時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST,value = SCREEN_PATH_UPDATE)
    public String update(ShiftMstMntForm form,HttpSession session,
                         HttpServletRequest req,
                         RedirectAttributes redirectAttributes) throws Exception {

        LoginUserDto loginUserDto = (LoginUserDto) session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        if (loginUserDto == null) {
            return null;
        }

        // DTOに変換して更新処理
        List<ShiftMstMntDto> dtoList = formToDto(form);
        ShiftMstMntLogic shiftMstMntLogic = new ShiftMstMntLogic();
        shiftMstMntLogic.updateShiftMst(dtoList, loginUserDto);

        // 更新後のフォームをリダイレクト先に渡す
        redirectAttributes.addFlashAttribute("shiftMstMntForm", form);
        return null;
    }


    /**
     * リクエスト情報をDtoのリストにセットする。
     *
     * @param employeeMstMntForm シフトマスタフォーム
     * @return シフトマスタDtoリスト
     * @author naraki
     */
    private List<ShiftMstMntDto> formToDto(ShiftMstMntForm shiftMstMntForm) {
        List<ShiftMstMntDto> shiftMstMntDtoList = new ArrayList<>();

        if (shiftMstMntForm.getShiftMstMntBeanList() == null) {
            return shiftMstMntDtoList;
        }

        for (ShiftMstMntBean bean : shiftMstMntForm.getShiftMstMntBeanList()) {
            ShiftMstMntDto dto = new ShiftMstMntDto();

            // 入力された値をそのまま移す
            dto.setShiftId(bean.getShiftId());
            dto.setShiftName(bean.getShiftName());
            dto.setSymbol(bean.getSymbol());
            dto.setStartTime(bean.getStartTime());
            dto.setEndTime(bean.getEndTime());
            dto.setBreakTime(bean.getBreakTime());

            // 削除フラグは null チェックして true/false 設定
            dto.setDeleteFlg(Boolean.TRUE.equals(bean.getDeleteFlg()));
            shiftMstMntDtoList.add(dto);
        }

        return shiftMstMntDtoList;
    }
    /**
     * DtoからFormへ変換する
     *
     * @param
     * @return
     * @author naraki
     */
    private List<ShiftMstMntBean> dtoToForm(Collection<ShiftMstMntDto> collection) {
        List<ShiftMstMntBean> shiftMstMntBeanList = new ArrayList<>();

        for (ShiftMstMntDto dto : collection) {
            ShiftMstMntBean bean = new ShiftMstMntBean();

            bean.setShiftId(dto.getShiftId());
            bean.setShiftName(dto.getShiftName());
            bean.setSymbol(dto.getSymbol());
            bean.setStartTime(dto.getStartTime());
            bean.setEndTime(dto.getEndTime());
            bean.setBreakTime(dto.getBreakTime());

            // deleteFlgがnullならfalse、それ以外はtrue/falseを反映
            bean.setDeleteFlg(dto.getDeleteFlg() != null && dto.getDeleteFlg());
            shiftMstMntBeanList.add(bean);
        }

        return shiftMstMntBeanList;

}
}
