/**
 * ファイル名：PropertyHelper.java
 *
 * ymlファイルの設定値をキーを基に取得するクラス
 *
 * 変更履歴
 * 1.0  2025/4/11 itaki
 */
package jp.co.kikin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyHelper {

    private static Environment environment;

    @Autowired
    public PropertyHelper(Environment env) {
        PropertyHelper.environment = env;
    }

    public static String get(String key) {
        return environment.getProperty(key);
    }
}
