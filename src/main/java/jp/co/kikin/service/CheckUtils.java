/**
 * ファイル名：CheckUtils.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 */
package jp.co.kikin.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import jp.co.kikin.constant.CommonConstant;



/**
 * 説明：チェック処理
 *
 * @author naraki
 *
 */
public class CheckUtils {

    /**
     * 文字列の空チェック
     *
     * @param チェック対象文字列
     * @return true：nullもしくは""、false：null、""でない
     * @author
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        if (CommonConstant.BLANK.equals(str)) {
            return true;
        }

        return false;
    }

    /**
     * コレクションの空チェック
     *
     * @param チェック対象のコレクション
     * @return true：nullもしくはsizeが0以下、false：null、でなくsizeが0より大きい
     * @author
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null) {
            return true;
        }

        if (collection.size() <= 0) {
            return true;
        }

        return false;
    }

    /**
     * マップの空チェック
     *
     * @param チェック対象のマップ
     * @return true：nullもしくはsizeが0以下、false：null、でなくsizeが0より大きい
     * @author
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null) {
            return true;
        }

        if (map.size() <= 0) {
            return true;
        }

        return false;
    }

    /**
     * オブジェクトの null チェック
     *
     * @param チェック対象のオブジェクト
     * @return true：null、false：nullでない
     * @author
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        return false;
    }

    /**
     * 配列オブジェクトの null チェック
     *
     * @param チェック対象のオブジェクト
     * @return true：null、false：nullでない
     * @author
     */
    public static boolean isEmpty(Object[] objArr) {
        if (objArr == null) {
            return true;
        }

        if (objArr.length <= 0) {
            return true;
        }

        return false;
    }
}
