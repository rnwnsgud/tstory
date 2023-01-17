package blogProject.tstroy.util;

import blogProject.tstroy.handler.ex.CustomException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class UtilValid {

    public static void reqErrorResolve(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomException(errorMap.toString());
        }
    }

    public static void reqApiErrorResolve() {}
}
