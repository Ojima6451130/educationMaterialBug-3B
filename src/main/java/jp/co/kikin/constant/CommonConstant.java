/**
 * ファイル名：CommonConstant.java
 *
 * 変更履歴
 * 1.0  2010/07/29 Kazuya.Naraki
 */
package jp.co.kikin.constant;

import org.springframework.stereotype.Component;

/**
 * 説明：共通定数定義クラス
 * @author naraki
 *
 */
@Component
public class CommonConstant {

    /** 成功 */
    public static final String SUCCESS = "success";
    /** 失敗 */
    public static final String ERROR = "error";
    /** データなし */
    public static final String NODATA = "nodata";

    /** 空白 */
    public static final String BLANK = "";
    /** 空白ID */
    public static final String BLANK_ID = "-1";
    /** ハイフン */
    public static final String HYPHEN = "?";

    /** 次 **/
    public static final String NEXT = "next";
    /** 前 **/
    public static final String BACK = "back";

    /** 年月日 yyyy/MM/dd */
    public static final String YEARMONTHDAY = "yyyy/MM/dd";
    /** 年月日 yyyyMMdd */
    public static final String YEARMONTHDAY_NOSL = "yyyyMMdd";
    /** 年月 yyyy/MM */
    public static final String YEARMONTH ="yyyy/MM";
    /** 年月 yyyyMM */
    public static final String YEARMONTH_NOSL ="yyyyMM";


    /**
     * 権限定数
     */
    public enum Authority {
        ADMIN("管理者", "01"),
        USER("一般", "02");

        // 名称
        private String name;
        // ID
        private String id;

        // コンストラクタ
        private Authority(String name, String id) {
            this.name = name;
            this.id = id;
        }

        /**
         * 権限の名称を取得する
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * 権限のIDを取得する
         * @return value
         */
        public String getId() {
            return id;
        }
    }

    /**
     * 分類ＩＤ
     */
    public enum CategoryId {
        AUTHORITY("BU0001");

        // 分類ＩＤ
        private String categoryId;

        // コンストラクタ
        private CategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        /**
         * 分類ＩＤを取得する
         * @return categoryId
         */
        public String getCategoryId() {
            return categoryId;
        }
    }

    /**
     * 曜日
     */
    public enum DayOfWeek {
        SUNDAY("日曜日", "日日日日日"),
        MONDAY("月曜日", "月"),
        TUESDAY("火曜日", "火"),
        WEDNESDAY("水曜日", "水"),
        THURESDAY("木曜日", "木"),
        FRIDAY("金曜日", "金"),
        SATURDAY("土曜日", "土");

        // 曜日名
        String weekdayName;
        // 略名
        String weekdayShort;

        // コンストラクタ
        private DayOfWeek(String weekdayName, String weekdayShort) {
            this.weekdayName = weekdayName;
            this.weekdayShort = weekdayShort;
        }

        public String getWeekdayShort() {
            return weekdayShort;
        }
    }
}
