package blogProject.tstroy.web;

import blogProject.tstroy.config.auth.LoginUser;
import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.handler.ex.CustomException;
import blogProject.tstroy.service.PostService;
import blogProject.tstroy.web.dto.love.LoveRespDto;
import blogProject.tstroy.web.dto.post.PostDetailRespDto;
import blogProject.tstroy.web.dto.post.PostReqDto;
import blogProject.tstroy.web.dto.post.PostRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {


    private final PostService postService;

    @PostMapping("/s/post")
    public String sendForm(PostReqDto postReqDto, @AuthenticationPrincipal LoginUser loginUser) {

        postService.sendPost(postReqDto, loginUser.getUser());
        return "redirect:/user/" + loginUser.getUser().getId() + "/post";
    }

    @GetMapping("/user/{pageOwnerId}/post")
    public String postList(@PathVariable Integer pageOwnerId,
                           Model model,
                           Integer categoryId,
                           @PageableDefault(size = 3) Pageable pageable) {

        PostRespDto postRespDto = null;

        if (categoryId == null) {
            postRespDto = postService.getPostList(pageOwnerId, pageable);
        } else {
            postRespDto = postService.getPostWithCategory(pageOwnerId, pageable, categoryId);
        }


        model.addAttribute("postRespDto", postRespDto);
        return "/post/list";
    }

    @GetMapping("/s/post/write-form")
    public String writeForm(@AuthenticationPrincipal LoginUser loginUser, Model model) {

        List<Category> categories = postService.getPostWriteForm(loginUser.getUser());

        if (categories.size() == 0) {
            throw new CustomException("카테고리등록이 필요합니다.");
        }

        model.addAttribute("categories", categories);
        return "/post/writeForm";
    }

    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable Integer postId ,@AuthenticationPrincipal LoginUser loginUser, Model model) {

        PostDetailRespDto postDetailRespDto = null;

        if (loginUser == null) {
            postDetailRespDto = postService.detailPost(postId);
        } else {
            postDetailRespDto=  postService.detailPost(postId, loginUser.getUser());
        }


        model.addAttribute("data", postDetailRespDto);
        return "/post/detail";
    }

    @DeleteMapping("/s/api/post/{postId}")
    public ResponseEntity<?> deleteForm(@PathVariable Integer postId) {

        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/s/api/post/{postId}/love")
    public ResponseEntity<?> love(@PathVariable Integer postId, @AuthenticationPrincipal LoginUser loginUser) {

        LoveRespDto loveRespDto = postService.love(postId, loginUser.getUser());
        return new ResponseEntity<>(loveRespDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/s/api/post/{postId}/love/{loveId}")
    public ResponseEntity<?> deleteLove(@PathVariable Integer loveId) {

        postService.deleteLove(loveId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
