/**
 * ファイル名：message.js
 * メッセージ処理
 *
 * 変更履歴
 * 1.0  2010/09/10 Kazuya.Naraki
 */

/**
 * メッセージの定義
 */
var messageArr = {
        'E-MSG-000001':'{0}は必須です。',
        'E-MSG-000002':'ログインIDまたはパスワードが違います。',
        'E-MSG-000003':'{0}は半角カナ入力です。',
        'E-MSG-000004':'{0}は「hh:mm」形式で入力してください。',
        'E-MSG-000005':'開始日が終了日より後の日付となっています。'
};

/**
 * エラーメッセージを表示する。
 * param str エラーメッセージ
 * param repStrArr 置換文字列の配列
 */
function showMessage(errorCode, repStrArr) {
    var message = messageArr[errorCode];

    // 置換する文字列の配列分処理を繰り返す
    for (var i = 0; i < repStrArr.length; i++) {
        var patern = new RegExp("\\{" + i + "\\}", "g");
        message = message.replace(patern, repStrArr[i]);
    }

    message = errorCode + '：' + message;

    alert(message);
}

/**
 * エラーメッセージを取得する。
 * param str エラーメッセージ
 * param repStrArr 置換文字列の配列
 * return エラーメッセージ
 */
function getMessage(errorCode, repStrArr) {
    var message = messageArr[errorCode];

    // 置換する文字列の配列分処理を繰り返す
    for (var i = 0; i < repStrArr.length; i++) {
        var patern = new RegExp("\\{" + i + "\\}", "g");
        message = message.replace(patern, repStrArr[i]);
    }

    message = errorCode + '：' + message + '\n';

    return message;
}

/**
 * エラーメッセージを取得する。
 * param str エラーメッセージ
 * param repStrArr 置換文字列の配列
 * return エラーメッセージ
 */
function getMessageCodeOnly(errorCode) {
    var message = messageArr[errorCode];

    message = errorCode + '：' + message + '\n';

    return message;
}

