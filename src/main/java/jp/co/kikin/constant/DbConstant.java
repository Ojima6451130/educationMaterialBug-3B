/**
 * ファイル名：DbConstant.java
 *
 * 変更履歴
 * 1.0  2010/09/06 Kazuya.Naraki
 */
package jp.co.kikin.constant;

/**
 * 説明：テーブル定義コンスタント
 * @author naraki
 *
 */
public class DbConstant {

    /**
     * 社員マスタ
     */
    public enum M_employee {

        TABLE_NAME("m_employee"),
        PREFIX("sh"),
        EMPLOYEE_ID("employee_id"),
        PASSWORD("password"),
        EMPLOYEE_NAME("employee_name"),
        EMPLOYEE_NAME_KANA("employee_name_kana"),
        AUTHORITY_ID("authority_id"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private M_employee(String name) {
            this.name = name;
        }

        /**
         * 社員マスタの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }

    /**
     * シフトマスタ
     */
    public enum M_shift {
        TABLE_NAME("mst_shift"),
        PREFIX("sf"),
        SHIFT_ID("shift_id"),
        SHIFT_NAME("shift_name"),
        SYMBOL("symbol"),
        START_TIME("start_time"),
        END_TIME("end_time"),
        BREAK_TIME("break_time"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private M_shift(String name) {
            this.name = name;
        }

        /**
         * シフトマスタの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }

    /**
     * 基本シフトマスタ
     */
    public enum M_base_shift {
        TABLE_NAME("m_base_shift"),
        EMPLOYEE_ID("employee_id"),
        MONDAY("monday"),
        TUESDAY("tuesday"),
        WEDNESDAY("wednesday"),
        THURSDAY("thursday"),
        FRIDAY("friday"),
        SATURDAY("saturday"),
        SUNDAY("sunday"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private M_base_shift(String name) {
            this.name = name;
        }

        /**
         * 基本シフトマスタの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }

    /**
     * 分類マスタ
     */
    public enum Mcategory {

        TABLE_NAME("m_category"),
        PREFIX("bu"),
        CATEGORY_ID("category_id"),
        CODE("code"),
        NAME("name"),
        DISPLAY_ORDER("display_order"),
        DISPLAY("display"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private Mcategory(String name) {
            this.name = name;
        }

        /**
         * 社員マスタの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }

    /**
     * 祝日マスタ
     */
    public enum M_public_holiday {

        TABLE_NAME("m_public_holiday"),
        YEAR_MONTH_DAY("year_month_day"),
        PUBLIC_HOLIDAY_NAME("public_holiday_name"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private M_public_holiday(String name) {
            this.name = name;
        }

        /**
         * 祝日マスタの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }

    /**
     * シフトテーブル
     */
    public enum T_Shift {

        TABLE_NAME("t_shift"),
        EMPLOYEE_ID("employee_id"),
        YEAR_MONTH_DAY("year_month_day"),
        SHIFT_ID("shift_id"),
        REQUEST_SHIFT_ID("request_shift_id"),
        REMARK("remark"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private T_Shift(String name) {
            this.name = name;
        }

        /**
         * シフトテーブルの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }

    /**
     * 勤務実績テーブル
     */
    public enum T_work_record {

        TABLE_NAME("t_work_record"),
        EMPLOYEE_ID("employee_id"),
        WORK_DAY("work_day"),
        START_TIME("start_time"),
        END_TIME("end_time"),
        BREAK_TIME("break_time"),
        ACTUAL_WORK_TIME("actual_work_time"),
        OVER_TIME("over_time"),
        HOLIDAY_WORK_TIME("holiday_work_time"),
        REMARK("remark"),
        CREATOR_EMPLOYEE_ID("creator_employee_id"),
        CREATION_DATETIME("creation_datetime"),
        UPDATER_EMPLOYEE_ID("updater_employee_id"),
        UPDATE_DATETIME("update_datetime");

        // 名称
        private String name;

        // コンストラクタ
        private T_work_record(String name) {
            this.name = name;
        }

        /**
         * シフトテーブルの名称を取得する
         * @return value
         */
        public String getName() {
            return name;
        }
    }
}
