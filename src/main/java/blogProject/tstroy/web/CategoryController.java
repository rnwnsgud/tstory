package blogProject.tstroy.web;

import blogProject.tstroy.config.auth.LoginUser;
import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.user.User;
import blogProject.tstroy.service.CategoryService;
import blogProject.tstroy.util.Script;
import blogProject.tstroy.util.UtilValid;
import blogProject.tstroy.web.dto.category.CategoryReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/s/category/writeForm")
    public String writeForm() {
        return "/category/writeForm";
    }

    @PostMapping("/s/category")
    public @ResponseBody String registerCategory(
            @Valid CategoryReqDto categoryReqDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {

        UtilValid.reqErrorResolve(bindingResult);

        User principal = loginUser.getUser();

        Category category = categoryReqDto.toEntity(principal);

        categoryService.registerCategory(category);

        return Script.href("/s/category/writeForm", "카테고리 등록 완료");


    }
}
