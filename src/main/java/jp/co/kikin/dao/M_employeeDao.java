/**
 * ファイル名：M_employeeDao.java
 *
 * 変更履歴
 * 1.0  2010/12/25 Kazuya.Naraki
 */
package jp.co.kikin.dao;
import java.util.List;

import jp.co.kikin.constant.M_employee;
/**
 * 説明：ログイン処理のロジック
 * @author naraki
 *
 */
public interface M_employeeDao {

    public Class<M_employee> BEAN = M_employee.class;

    /**
     * 更新
     * @param m_Employee エンティティ
     * @return
     * @author naraki
     */
    public int update(M_employee m_Employee);
    /**
     * 登録
     * @param m_Employee エンティティ
     * @return
     * @author naraki
     */
    public int insert(M_employee m_Employee);
    /**
     * 削除
     * @param m_Employee エンティティ
     * @return
     * @author naraki
     */
    public int delete(M_employee m_Employee);
    /**
     * 一括取得
     * @return 取得リスト
     * @author naraki
     */
    public List<M_employee> getAll();

    /**
     * プライマリキーを指定してデータを取得する。
     * @param employeeId 社員ＩＤ
     * @param password パスワード
     * @return
     * @author naraki
     */
    public M_employee getData(String employeeId, String password);
    public static final String getData_QUERY = "employee_id = ? AND password = ?";

}
