/**
 * ファイル名：MenuController.java
 *
 * 変更履歴
 * 1.0  2010/09/13 Kazuya.Naraki
 * 2.0  2024/04/16 Atsuko.Yoshioka
 * Spring  2025/04/01 Kyo.Hanada
 */
package jp.co.kikin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;

/**
 * 説明：メニュー画面コントローラ
 *
 * @author 
 *
 */
@Controller
@RequestMapping(value = "/kikin")
public class MenuController {

    @RequestMapping(value = "/menu")
    public String init(HttpSession session, HttpServletRequest request, Model model)
            throws Exception {
        return view("init", session, request, model);
    }

    /**
     * 画面情報取得処理.
     *
     * @param req リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    private String view(String processType, HttpSession session, HttpServletRequest req, Model model) throws Exception {

        // ログインユーザー情報セット
        model.addAttribute("authority_id",
                session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_AUTHORITY_ID));

        // 定数
        model.addAttribute("userAuthority", CommonConstant.Authority.USER.getId());
        model.addAttribute("adminAuthority", CommonConstant.Authority.ADMIN.getId());

        return "menu";
    }
}
