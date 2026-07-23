/**
 * ファイル名：M_employee.java
 *
 * 変更履歴
 * 1.0  2010/12/25 Kazuya.Naraki
 */
package jp.co.kikin.constant;

import java.sql.Timestamp;

/**
 * 説明：社員マスタ エンティティ
 * @author naraki
 *
 */
public class M_employee {
    public static final String TABLE = "m_employee";

    // 社員ＩＤ
    private String employee_id;
    // パスワード
    private String password;
    // 社員名
    private String employee_name;
    // 社員名カナ
    private String employee_name_kana;
    // 権限ＩＤ
    private String authority_id;
    // 作成社員ＩＤ
    private String create_employee_id;
    // 作成日付
    private Timestamp create_dt;
    // 更新社員ＩＤ
    private String updater_employee_id;
    // 更新日付
    private Timestamp update_datetime;

    /**
     * 社員ＩＤ
     */
    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * パスワード
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 社員名
     */
    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    /**
     * 社員名カナ
     */
    public String getEmployee_name_kana() {
        return employee_name_kana;
    }

    public void setEmployee_name_kana(String employee_name_kana) {
        this.employee_name_kana = employee_name_kana;
    }

    /**
     * 権限ＩＤ
     */
    public String getAuthority_id() {
        return authority_id;
    }

    public void setAuthority_id(String authority_id) {
        this.authority_id = authority_id;
    }

    /**
     * 作成社員ＩＤ
     */
    public String getCreate_employee_id() {
        return create_employee_id;
    }

    public void setCreate_employee_id(String create_employee_id) {
        this.create_employee_id = create_employee_id;
    }

    /**
     * 作成日付
     */
    public Timestamp getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Timestamp create_dt) {
        this.create_dt = create_dt;
    }

    /**
     * 更新社員ＩＤ
     */
    public String getUpdater_Employee_Id() {
        return updater_employee_id;
    }

    public void setUpdater_Employee_Id(String updater_employee_id) {
        this.updater_employee_id = updater_employee_id;
    }

    /**
     * 更新日付
     */
    public Timestamp getUpdate_Datetime() {
        return update_datetime;
    }

    public void setUpdate_Datetime(Timestamp update_datetime) {
        this.update_datetime = update_datetime;
    }
}
