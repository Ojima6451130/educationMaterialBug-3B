/**
 * ファイル名：Dao.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 * 2.0  2025/04/11 SpringBootに移行 Hironori.Itaki
 */
package jp.co.kikin.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.PropertyHelper;

/**
 * 説明：DBアクセス部品
 *
 * @author naraki
 *
 */
public abstract class Dao {

    // ログ出力クラス
    private Log log = LogFactory.getLog(this.getClass());

    protected Connection connection;
    private static  String jdbcUrl;
    private static  String jdbcUser;
    private static  String jdbcPassword;

    protected Dao() {
        try {
        // 	InitialContext = new InitialContext();
        //     refDataSource = (DataSource) InitialContext.lookup("java:comp/env/MySQL_DBCP");
        this.jdbcUrl = PropertyHelper.get("spring.datasource.url");
        this.jdbcUser = PropertyHelper.get("spring.datasource.username");
        this.jdbcPassword = PropertyHelper.get("spring.datasource.password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DBコネクションの接続を行います。
     *
     * @param なし
     * @return なし
     * @author naraki
     * @throws SQLException
     */
    protected void connect() {
        log.info("start");
        try {
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            // connection = refDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("end");
    }

    /**
     * DBコネクションの切断を行います。
     *
     * @param なし
     * @return なし
     * @throws SQLException
     * @author naraki
     */
    protected void disConnect() {
        log.info("start");
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("end");
    }

    public Connection getConnection() {

        try {
            // コネクション取得
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
