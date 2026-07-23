/**
 * ファイル名：EmployeeMstMntRegisterInitAction.java
 *
 * 変更履歴
 * 1.0  2010/09/04 Kazuya.Naraki
 */
package jp.co.kikin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import jp.co.kikin.constant.DbConstant.Mcategory;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.EmployeeMstMntDto;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.model.EmployeeMstMntRegisterForm;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.EmployeeMstMntLogic;

import org.springframework.web.bind.annotation.RequestParam;

import jp.co.kikin.constant.CommonConstant.CategoryId;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * 説明：社員マスタメンテナンス登録初期表示アクションクラス
 *
 * @author naraki
 *
 */
@Controller

public class EmployeeMstMntRegisterController {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    // 社員マスタメンテナンス入力画面共通URL
    public static final String SCREEN_PATH = "/kikin/employeeMstMntRegister";

    /**
     * 社員マスタメンテナンス登録アクションクラス
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @PostMapping(value = SCREEN_PATH)
    public String employeeMstMntRegister(HttpServletRequest request, HttpSession session, Model model, 
            @RequestParam("password") String password,
            @RequestParam("employeeName") String employeeName,
            @RequestParam("employeeNameKana") String employeeNameKana,
            @RequestParam("authorityId") String authorityId
            ) throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // フォーム
        EmployeeMstMntRegisterForm employeeMstMntRegisterForm = new EmployeeMstMntRegisterForm();

        employeeMstMntRegisterForm.setPassword(password);
        employeeMstMntRegisterForm.setEmployeeName(employeeName);
        employeeMstMntRegisterForm.setEmployeeNameKana(employeeNameKana);
        employeeMstMntRegisterForm.setAuthorityId(authorityId);

        // リクエスト内容をDtoに変換する
        EmployeeMstMntDto m_employeeDto = this.formToDto(employeeMstMntRegisterForm);

        // ロジック生成
        EmployeeMstMntLogic employeeMstMntLogic = new EmployeeMstMntLogic();

        // 権限セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
        Map<String, String> comboMap = comboListUtils.getCombo(CategoryId.AUTHORITY.getCategoryId(),
                Mcategory.DISPLAY.getName(), false);

        // 取得したセレクトボックスのマップをフォームへセットする。
        employeeMstMntRegisterForm.setAuthorityCmbMap(comboMap);

       

        String redirectUrl = "redirect:/kikin/employeeMstMnt";

        return redirectUrl;
    }

    /**
     * リクエスト情報をDtoのリストにセットする。
     *
     * @param employeeMstMntRegisterForm 社員マスタ登録フォーム
     * @return 社員マスタDtoリスト
     * @author naraki
     */
    private EmployeeMstMntDto formToDto(EmployeeMstMntRegisterForm employeeMstMntRegisterForm) {
        EmployeeMstMntDto employeeMstMntDto = new EmployeeMstMntDto();

        String password = employeeMstMntRegisterForm.getPassword();
        String employeeName = employeeMstMntRegisterForm.getEmployeeName();
        String employeeNameKana = employeeMstMntRegisterForm.getEmployeeNameKana();
        String authorityId = employeeMstMntRegisterForm.getAuthorityId();

        // Dtoに値をセットする
        employeeMstMntDto.setPassword(password);
        employeeMstMntDto.setEmployeeName(employeeName);
        employeeMstMntDto.setEmployeeNameKana(employeeNameKana);
        employeeMstMntDto.setAuthorityId(authorityId);

        return employeeMstMntDto;
    }

}