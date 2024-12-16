package com.xxxx.crm.utils;

import com.xxxx.crm.exceptions.ParamsException;

public class AssertUtil {

    /**
     * 判断条件是否满足
     *  如果条件满足，则抛出参数异常
     * @param flag
     * @param msg
     * @return void
     */
    public static void isTrue(Boolean flag, String msg) {
        if (flag) {
            throw new ParamsException(msg);
        }
    }

}
