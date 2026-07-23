/**
 * ファイル名：RequestSessionNameConstant.java
 *
 * 変更履歴
 * 1.0  2010/08/09 Kazuya.Naraki
 */
package jp.co.kikin.constant;

/**
 * 説明：共通セッション名定義
 * @author naraki
 *
 */
public class RequestSessionNameConstant {

    /************* セッション ****************/

    /************* ログイン画面 ****************/

    /** ログインユーザの社員情報 */
    public static final String SESSION_CMN_LOGIN_USER_INFO = "session_cmn_login_user_info";
    /** ログインユーザの社員ID */
    public static final String SESSION_CMN_LOGIN_USER_ID = "session_cmn_login_user_id";
    /** ログインユーザの社員名 */
    public static final String SESSION_CMN_LOGIN_USER_NAME = "session_cmn_login_user_name";
    /** ログインユーザの社員名カナ */
    public static final String SESSION_CMN_LOGIN_USER_NAME_KANA = "session_cmn_login_user_kana";
    /** ログインユーザの権限ID */
    public static final String SESSION_CMN_LOGIN_USER_AUTHORITY_ID = "session_cmn_login_user_authority_id";

    /************* 社員マスタメンテナンス画面 ****************/

    /** 社員マスタメンテナンス画面 社員情報リスト */
    public static final String SESSION_MTH_EMPLOYEE_MST_MNT_INFO_LIST = "session_mth_employee_mst_mnt_info_list";

    /************* リクエスト ****************/
    public static final String REQUEST_BACK_PAGE = "request_mth_employee_mst_mnt_info_list";

}
