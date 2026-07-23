/**
 * ファイル名：EmployeeMstMntInitAction.java
 *
 * 変更履歴
 * 1.0  2010/08/23 Kazuya.Naraki
 * Spring 2025/04/16 Satoshi.Tsurusawa
 */
package jp.co.kikin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.naming.Binding;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.CommonConstant.CategoryId;
import jp.co.kikin.constant.DbConstant.Mcategory;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.EmployeeMstMntDto;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.model.EmployeeMstMntBean;
import jp.co.kikin.model.EmployeeMstMntForm;
import jp.co.kikin.service.CheckUtils;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.EmployeeMstMntLogic;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 説明：社員マスタメンテナンス初期表示アクションクラス
 *
 * @author naraki
 *
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class EmployeeMstMntController {
    /** 社員マスタメンテ画面検索URL */
    public static final String SCREEN_PATH_UPDATE = "/employeeMstMnt/update";
    public static final String SCREEN_PATH_REGIST = "/employeeMstMnt/regist";

    /** 処理区分 */
    private enum PROCCESS_TYPE {
        INIT, UPDATE, REGIST, PAGE, INPORT, REFLECT
    }

    // 社員マスタメンテナンス入力画面共通URL
    public static final String SCREEN_PATH = "/employeeemployee";

    /**
     * 社員マスタメンテナンス初期表示アクションクラス
     *
     * @param mapping アクションマッピング
     * @param form    アクションフォーム
     * @param req     リクエスト
     * @param res     レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @RequestMapping(value = SCREEN_PATH)
    public String Init(HttpServletRequest req, HttpSession session, EmployeeMstMntForm form, Model model)
            throws Exception {
        return view(PROCCESS_TYPE.INIT, req, session, form, model);
    }

    private String view(PROCCESS_TYPE init, HttpServletRequest req, HttpSession session, EmployeeMstMntForm form, Model model)
            throws Exception {
        // 社員マスタのデータを取得する。
        EmployeeMstMntLogic employeeMstMntLogic = new EmployeeMstMntLogic();

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // 社員情報を取得する
        Collection<EmployeeMstMntDto> m_employeeList = employeeMstMntLogic.getEmployeeData(loginUserDto);

        // 権限の選択肢を取得する
        ComboListUtilLogic comboListUtilLogic = new ComboListUtilLogic();
        Map<String, String> comboMap = comboListUtilLogic.getCombo(CategoryId.AUTHORITY.getCategoryId(),
                Mcategory.DISPLAY.getName(), false);

        // 取得したセレクトボックスのマップをフォームへセットする。
        EmployeeMstMntForm employeeMstMntForm = new EmployeeMstMntForm();
        employeeMstMntForm.setAuthorityCmbMap(comboMap);

        // 社員情報を取得する
        if (CheckUtils.isEmpty(m_employeeList)) {
            return CommonConstant.NODATA;
        }

        // 社員情報をフォームに格納
        employeeMstMntForm.setEmployeeMstMntBeanList(dtoToForm(m_employeeList));

        // 戻り先を保存
        model.addAttribute("m_employeeList", m_employeeList);
        model.addAttribute("employeeMstMntForm", employeeMstMntForm);
        return "employeeMstMnt";
    }

    /**
     * 更新時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_UPDATE, method = RequestMethod.POST)
    public String update(HttpServletRequest request, HttpSession session,
                                EmployeeMstMntForm employeeMstMntForm, Model model, BindingResult bindingResult) throws Exception {
        
        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

        // リクエスト内容をDtoに変換する
        List<EmployeeMstMntDto> m_employeeDtoList = this.formToDto(employeeMstMntForm);
        // ロジック生成
        EmployeeMstMntLogic employeeMstMntLogic = new EmployeeMstMntLogic();

        // 権限セレクトボックスの取得
        ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
        Map<String, String> comboMap = comboListUtils.getCombo(CategoryId.AUTHORITY.getCategoryId(),
                Mcategory.DISPLAY.getName(), false);

        // 取得したセレクトボックスのマップをフォームへセットする。
        employeeMstMntForm.setAuthorityCmbMap(comboMap);

        // 更新・削除処理
        employeeMstMntLogic.updateM_employee(m_employeeDtoList, loginUserDto);

        // 社員情報を再検索する
        m_employeeDtoList = employeeMstMntLogic.getEmployeeData(loginUserDto);

        if (CheckUtils.isEmpty(m_employeeDtoList)) {
            // データなし
            // forward = CommonConstant.NODATA;
        } else {
            // フォームへ一覧をセットする
            employeeMstMntForm.setEmployeeMstMntBeanList(dtoToForm(m_employeeDtoList));
        }

        return view(PROCCESS_TYPE.INIT, request, session, employeeMstMntForm, model);
    }
     /**
     * 更新時に動作する処理.
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     */
    @RequestMapping(value = SCREEN_PATH_REGIST)
    public String regist(HttpServletRequest request, HttpSession session,
                                EmployeeMstMntForm employeeMstMntForm, Model model, BindingResult bindingResult) throws Exception {

        return "employeeMstMntRegister";
    }


    /**
     * DtoからFormへ変換する
     *
     * @param
     * @return
     * @author naraki
     */
    private List<EmployeeMstMntBean> dtoToForm(Collection<EmployeeMstMntDto> colection) {

        List<EmployeeMstMntBean> employeeMstMntBeanList = new ArrayList<EmployeeMstMntBean>();

        for (EmployeeMstMntDto dto : colection) {
            EmployeeMstMntBean employee = new EmployeeMstMntBean();
            employee.setEmployeeId(dto.getEmployeeId());
            employee.setEmployeeName(dto.getEmployeeName());
            employee.setEmployeeNameKana(dto.getEmployeeNameKana());
            employee.setPassword(dto.getPassword());
            employee.setAuthorityId(dto.getAuthorityId());
            employee.setDeleteFlg(dto.getDeleteFlg());
            employeeMstMntBeanList.add(employee);

        }
        return employeeMstMntBeanList;
    }

    /**
     * リクエスト情報をDtoのリストにセットする。
     *
     * @param employeeMstMntForm シフトマスタフォーム
     * @return シフトマスタDtoリスト
     * @author naraki
     */
    private List<EmployeeMstMntDto> formToDto(EmployeeMstMntForm employeeMstMntForm) {
        // 返却用Dtoリスト
        // List<EmployeeMstMntDto> EmployeeMstMntDtoList = new
        // ArrayList<EmployeeMstMntDto>();
        List<EmployeeMstMntDto> EmployeeMstMntDtoList = new ArrayList<>();

        List<EmployeeMstMntBean> EmployeeMstMntBeanList = employeeMstMntForm.getEmployeeMstMntBeanList();
        // 新しいリストを作成して、Beanを追加する
        List<EmployeeMstMntBean> updatedEmployeeMstMntBeanList = new ArrayList<>();
        // 画面一覧のサイズ分処理を繰り返す
        for (EmployeeMstMntBean employeeMstMntBean : EmployeeMstMntBeanList) {
            EmployeeMstMntDto employeeMstMntDto = new EmployeeMstMntDto();

            // Dtoに値をセットする
            
            employeeMstMntDto.setEmployeeName(employeeMstMntBean.getEmployeeName());
            employeeMstMntDto.setEmployeeNameKana(employeeMstMntBean.getEmployeeNameKana());
            employeeMstMntDto.setPassword(employeeMstMntBean.getPassword());
            employeeMstMntDto.setAuthorityId(employeeMstMntBean.getAuthorityId());
            employeeMstMntDto.setDeleteFlg(employeeMstMntBean.getDeleteFlg());

            // Dtoリストに追加
            EmployeeMstMntDtoList.add(employeeMstMntDto);

            // Beanのリストを更新
            EmployeeMstMntBean updatedEmployee = new EmployeeMstMntBean();
            updatedEmployee.setEmployeeId(employeeMstMntBean.getEmployeeId());
            updatedEmployee.setEmployeeName(employeeMstMntBean.getEmployeeName());
            updatedEmployee.setEmployeeNameKana(employeeMstMntBean.getEmployeeNameKana());
            updatedEmployee.setPassword(employeeMstMntBean.getPassword());
            updatedEmployee.setAuthorityId(employeeMstMntBean.getAuthorityId());
            updatedEmployee.setDeleteFlg(employeeMstMntBean.getDeleteFlg());

            // 新しいリストに追加
            updatedEmployeeMstMntBeanList.add(updatedEmployee);
        }

        // 必要であれば、更新されたリストをフォームにセットする
        employeeMstMntForm.setEmployeeMstMntBeanList(updatedEmployeeMstMntBeanList);

        return EmployeeMstMntDtoList;
    }
}
