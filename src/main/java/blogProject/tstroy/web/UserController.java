package blogProject.tstroy.web;

import blogProject.tstroy.config.auth.LoginUser;
import blogProject.tstroy.domain.user.User;
import blogProject.tstroy.handler.ex.CustomException;
import blogProject.tstroy.service.UserService;
import blogProject.tstroy.util.UtilValid;
import blogProject.tstroy.web.dto.user.UserReqDto;
import blogProject.tstroy.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/login-form")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "/user/joinForm";
    }

    @GetMapping("/s/user/{id}")
    public String updateForm(@PathVariable Integer id) {
        return "/user/updateForm";
    }


    @PostMapping("/join")
    public String join(@Valid UserReqDto userReqDto, BindingResult bindingResult) {

        UtilValid.reqErrorResolve(bindingResult);
        userService.signUp(userReqDto.toEntity());

        return "redirect:/login-form";

    }

    @GetMapping("/api/user/username-same-check")
    public ResponseEntity<?> checkDuplicateUsername(String username) {
        boolean isPresent = userService.checkDuplicateUsername(username);
        return new ResponseEntity<>(isPresent, HttpStatus.OK);
    }

    @PutMapping("/s/api/user/profile-img")
    public ResponseEntity<?> changeProfileImg(
            @AuthenticationPrincipal LoginUser loginUser,
            MultipartFile profileImgFile) {

        // 세션값 변경
        userService.changeProfileImg(loginUser.getUser(), profileImgFile, session);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/s/api/user/update")
    public ResponseEntity<?> updateUserInfo(
            @Valid @RequestBody UserUpdateDto userUpdateDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        userService.updateUserInfo(userUpdateDto, loginUser.getUser(),session);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
