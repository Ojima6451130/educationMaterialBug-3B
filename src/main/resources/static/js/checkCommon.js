/**
 * ファイル名：check.js
 * 各種チェック処理
 *
 * 変更履歴
 * 1.0  2010/09/10 Kazuya.Naraki
 */

/**
 * 渡された引数が半角カナであるかチェックする
 * param str チェック対象文字列
 * return true:半角カナ false:半角カナ以外
 */
function checkHalfWidthKana(str) {

    if (str.match(/^[\uFF61-\uFF9F]*$/)) {
        return true;
    }

    return false;
}

/**
 * 必須チェック
 * param str チェック対象文字列
 * return true:空でない false:から
 */
function checkRequired(str) {

    if (!str) {
        return false;
    }

    return true;
}

/**
 * 時刻フォーマットチェック
 * param str チェック対象文字列
 * return true:空でない false:から
 */
function checkTime(str) {
	
    if (typeof str !== 'string') {
        str = String(str);
    }
	
    // 正規表現による書式チェック
    if(!str.match(/^\d{2}\:\d{2}$/)){
        return false;
    }
    var vHour = str.substr(0, 2);
    var vMinutes = str.substr(3, 2);

    if(vHour >= 0 && vHour <= 99 && vMinutes >= 0 && vMinutes <= 59){
        return true;
    }else{
        return false;
    }
}

/**
 * 時刻の大小をチェックする
 * param str チェック対象文字列
 * return true:開始が終了以下 false:開始が終了より大きい
 */
function checkTimeCompare(end,start) {

    var vStart = start.replace(":", "");
    var vEnd = end.replace(":", "");

    if (vStart <= vEnd) {
        return true;
    } else {
        return false;
    }
}