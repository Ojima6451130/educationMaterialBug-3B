/**
 * ファイル名：LoginAction.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 * Spring 2025/04/09 Hironori.itaki
 */
package jp.co.kikin.controller;

import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.ApplicationRoot;
import jp.co.kikin.CheckUtils;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.LoginDto;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.model.LoginForm;
import jp.co.kikin.service.LoginLogic;

/**
 * 説明：ログイン処理のアクション
 *
 * @author naraki
 *
 */
@Controller
@RequestMapping(value = "/kikin")
public class LoginController {

    private final ApplicationRoot applicationRoot;

    public static final String PATH = "/kikin";

    // ログインサービスクラス
    @Autowired
    protected LoginLogic loginLogic;

    /** サービス機能名={@value} */
    public static final String CONTENTS = "ログイン画面";

    LoginController(ApplicationRoot applicationRoot) {
        this.applicationRoot = applicationRoot;
    }

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
    @RequestMapping(value = "/")
    public String init(Model model)
            throws Exception {
        return "login";
    }

    @RequestMapping(value = "/dologin")
    public String login(HttpServletRequest request, HttpSession session, Model model, LoginForm form,
            BindingResult bindingResult) throws Exception {

        // 全てのセッションを削除する。
        Enumeration<String> sessionEnum = session.getAttributeNames();

        while (sessionEnum.hasMoreElements()) {
            String sessionKey = sessionEnum.nextElement();
            session.removeAttribute(sessionKey);
        }

        // フォーム
        LoginForm loginForm = form;

        // 社員情報を取得する
        LoginDto loginDto = loginLogic.getEmployeeData(loginForm);

        String redirectUrl = "";

        if (CheckUtils.isEmpty(loginDto)) {

            model.addAttribute("error", "E-MSG-000002");
            redirectUrl = "login";

        } else {

            // ログインユーザ保持用Dtoを作成する
            this.createLoginUserData(session, loginDto);

            // ログインユーザー情報セット
            model.addAttribute("loginUserInfo", loginDto);
            // 権限定数セット
            model.addAttribute("userAuthority", CommonConstant.Authority.USER.getId());
            model.addAttribute("adminAuthority", CommonConstant.Authority.ADMIN.getId());

            redirectUrl = "redirect:/kikin/menu";
        }

        return redirectUrl;
    }

    /**
     * ログインユーザ情報をセッションに登録する。
     *
     * @param session  セッション
     * @param loginDto 取得したログイン処理Dto
     * @author naraki
     */
    private void createLoginUserData(HttpSession session, LoginDto loginDto) {

        // ログインユーザの社員ID
        session.setAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_ID, loginDto.getEmployeeId());
        // ログインユーザの社員名
        session.setAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_NAME, loginDto.getEmployeeName());
        // ログインユーザの社員名カナ
        session.setAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_NAME_KANA,
                loginDto.getEmployeeNameKana());
        // ログインユーザの権限ID
        session.setAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_AUTHORITY_ID, loginDto.getAuthorityId());

        // ログインユーザ情報の設定
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmployeeId(loginDto.getEmployeeId());
        loginUserDto.setEmployeeName(loginDto.getEmployeeName());
        loginUserDto.setEmployeeNameKana(loginDto.getEmployeeNameKana());
        loginUserDto.setAuthorityId(loginDto.getAuthorityId());

        session.setAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO, loginUserDto);
    }
}