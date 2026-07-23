/**
 * ファイル名：WorkDateRequestAbstractAction.java
 *
 * 変更履歴
 * 1.0  2010/11/22 Kazuya.Naraki
 */
package jp.co.kikin.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 説明：ログイン処理のロジック
 * @author naraki
 *
 */
public abstract class WorkDateRequestAbstractController {
    // ログ出力クラス
    protected Log log = LogFactory.getLog(this.getClass());
    // 表示データ数
    protected final int SHOW_LENGTH = 18;
}
