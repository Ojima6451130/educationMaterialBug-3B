/**
 * ファイル名：MethodComparator.java
 *
 * 変更履歴
 * 1.0  2010/10/19 Kazuya.Naraki
 */
package jp.co.kikin.service;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * ソート順
 * @author naraki
 *
 */
public class MethodComparator implements Comparator<Method> {
    public int compare(Method obj0, Method obj1) {
        String name0 = ((Method) obj0).getName();
        String name1 = ((Method) obj1).getName();
        int ret = name0.compareTo(name1);
        if (ret == 0) {
            ret = -1;
        }
        return ret;
    }
}