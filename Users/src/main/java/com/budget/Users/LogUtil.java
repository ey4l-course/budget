package com.budget.Users;

import com.budget.Users.model.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

//General log policy:
//Specific predictable logs - caught locally and handled.
//Unpredictable exceptions - bubbled up to controller where it's caught as general exception and sent to debug logger.

@Component
public class LogUtil {

    //This method is used to log unpredicted exception thrown anywhere and not explicitly handled.
    public String debugGeneral(Exception e){
        String errorId = UUID.randomUUID().toString();
        StackTraceElement origin = Arrays.stream(e.getStackTrace())
                .filter(frame -> frame.getClassName().startsWith("com.budget"))
                .findFirst()
                .orElse(e.getStackTrace()[0]);
        String originInfo = origin.getClassName() + ":" + origin.getMethodName() + "() line: " + origin.getLineNumber();
        Logger logger = LoggerFactory.getLogger(origin.getClassName());
        logger.debug("[ELK][LogID: {}] Origin: {} - {} - Cause: {}", errorId, originInfo, e.getMessage(), e.toString());
        return errorId;
    }

    public void infoGeneral (String message){
        String logID = UUID.randomUUID().toString();
        Logger logger = LoggerFactory.getLogger("[info]");
        logger.info("[ELK][LogID: {}] - {}", logID, message);
    }

    public String warnGeneral (Exception e, String msg){
        String errorId = UUID.randomUUID().toString();
        StackTraceElement origin = Arrays.stream(e.getStackTrace())
                .filter(frame -> frame.getClassName().startsWith("com.budget"))
                .findFirst()
                .orElse(e.getStackTrace()[0]);
        String originInfo = origin.getClassName() + ":" + origin.getMethodName() + "() line: " + origin.getLineNumber();
        Logger logger = LoggerFactory.getLogger(origin.getClassName());
        logger.warn("[ELK][LogID: {}] Origin: {} - {}", errorId, originInfo, msg);
        return errorId;
    }

    public void loginInfo (UserLogin user, String result){
        String logId = UUID.randomUUID().toString();
        Logger logger = LoggerFactory.getLogger("[info]");
        logger.info("[sec][log ID: {}] - login {} - user id: {} - user IP: {}", logId, result, user.getEmail(), user.getIp());
    }

//    public void warnSec (String message){
//        logger.info("[sec] " + message);
//    }

//    public void errorSec (Exception e){
//        logger.error("[sec] " + e.getMessage() + e);
//    }

}
