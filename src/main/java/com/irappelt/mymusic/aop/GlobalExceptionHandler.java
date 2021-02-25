package com.irappelt.mymusic.aop;


import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.aop.exception.FileAnalysisException;
import com.irappelt.mymusic.aop.exception.ParamVerifyException;
import com.irappelt.mymusic.common.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author huaiyu
 * @date 2021/1/29 11:41
 */
@Component
@RestControllerAdvice(annotations = ExceptionCapture.class)
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public WebResponse defaultExceptionHandler(Exception e) {
        printExceptionMessage(e);

        WebResponse vo = new WebResponse();
        vo.setStatusCode(10010);
        vo.setStatusMsg("系统异常:"+e.getMessage());
        return vo;
    }

    @ExceptionHandler(ParamVerifyException.class)
    public WebResponse paramVerifyExceptionHandler(ParamVerifyException e) {
        printExceptionMessage(e);

        WebResponse vo = new WebResponse();
        vo.setStatusCode(10010);
        vo.setStatusMsg("参数校验异常:"+e.getMessage());
        return vo;
    }

    @ExceptionHandler(FileAnalysisException.class)
    public WebResponse fileAnalysisException(FileAnalysisException e) {
        printExceptionMessage(e);

        WebResponse vo = new WebResponse();
        vo.setStatusCode(10010);
        vo.setStatusMsg("文件解析异常:"+e.getMessage());
        return vo;
    }


    private void printExceptionMessage(Exception e) {
        LOGGER.info(String.format("Global capture exception: %s", e.getMessage()), e);
    }
}
