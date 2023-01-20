package blogProject.tstroy.service;

import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.category.CategoryRepository;
import blogProject.tstroy.domain.love.Love;
import blogProject.tstroy.domain.love.LoveRepository;
import blogProject.tstroy.domain.post.Post;
import blogProject.tstroy.domain.post.PostRepository;
import blogProject.tstroy.domain.Visit.Visit;
import blogProject.tstroy.domain.Visit.VisitRepository;
import blogProject.tstroy.domain.user.User;
import blogProject.tstroy.handler.ex.CustomApiException;
import blogProject.tstroy.handler.ex.CustomException;
import blogProject.tstroy.util.UtilFileUpload;
import blogProject.tstroy.web.dto.love.LoveRespDto;
import blogProject.tstroy.web.dto.post.PostDetailRespDto;
import blogProject.tstroy.web.dto.post.PostReqDto;
import blogProject.tstroy.web.dto.post.PostRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    @Value("${file.path}")
    private String uploadFolder;

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final VisitRepository visitRepository;
    private final LoveRepository loveRepository;

    public PostRespDto getPostList(Integer pageOwnerId, Pageable pageable) {

        // pageOwnerId의 User 의 모든 Post 엔티티를 pageable 값 만큼 페이징
        Page<Post> postEntities = postRepository.findByUserId(pageOwnerId, pageable);
        // pageOwnerId의 카테고리를 다 땡겨옴
        List<Category> categoryEntities = categoryRepository.findByUserId(pageOwnerId);

        // Post 엔티티의 총 페이지개수 N을 0부터 N-1까지 담음
        // e.g. Post 엔티티가 4개고 pageable 값이 3이면 페이지가 2개. 그러면 0,1이 pageNumbers 에 담김
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < postEntities.getTotalPages(); i++) {
            pageNumbers.add(i);
        }

        // 방문자 카운트 증가
        Visit visitEntity = visitIncrease(pageOwnerId);

        //getNumber() : 현재 페이지
        PostRespDto postRespDto = new PostRespDto(
                postEntities,
                categoryEntities,
                pageOwnerId,
                postEntities.getNumber() - 1,
                postEntities.getNumber() + 1,
                pageNumbers,
                visitEntity.getTotalCount()
        );

        return postRespDto;


    }

    public PostRespDto getPostWithCategory(Integer pageOwnerId, Pageable pageable, Integer categoryId) {

        Page<Post> postEntities = postRepository.findByUserIdAndCategoryId(pageOwnerId, categoryId, pageable);
        List<Category> categoryEntities = categoryRepository.findByUserId(pageOwnerId);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < postEntities.getTotalPages(); i++) {
            pageNumbers.add(i);
        }

        // 방문자 카운트 증가
        Visit visitEntity = visitIncrease(pageOwnerId);

        //getNumber() : 현재 페이지
        PostRespDto postRespDto = new PostRespDto(
                postEntities,
                categoryEntities,
                pageOwnerId,
                postEntities.getNumber() - 1,
                postEntities.getNumber() + 1,
                pageNumbers,
                visitEntity.getTotalCount()
        );

        return postRespDto;

    }

    public void sendPost(PostReqDto postReqDto, User principal) {

        // 1. UUID로 파일쓰고 경로 리턴 받기
        String thumbnail = null;

        // 썸네일 저장
        // 썸네일이 null 이거나, 공백이 들어오면 실행할 필요가 없음
        if(!(postReqDto.getThumbnailFile() == null|| postReqDto.getThumbnailFile().isEmpty())) {
            thumbnail = UtilFileUpload.write(uploadFolder, postReqDto.getThumbnailFile());
        }

        // 2. 카테고리 있는지 확인
        Optional<Category> categoryOp = categoryRepository.findById(postReqDto.getCategoryId());

        // 3. Post DB에 저장
        if (categoryOp.isPresent()) {
            Post post = postReqDto.toEntity(thumbnail, principal, categoryOp.get());
            postRepository.save(post);
            log.info("post content : " + post.getContent());

        } else {
            throw new CustomException("해당 카테고리가 존재하지 않습니다.");
        }

    }

    public List<Category> getPostWriteForm(User principal) {
        return categoryRepository.findByUserId(principal.getId());
    }

    public PostDetailRespDto detailPost(Integer postId) {

        Optional<Post> postOp = postRepository.findById(postId);
        if (!postOp.isPresent()) {
            throw new CustomException("해당 포스팅이 존재하지 않습니다.");
        }

        Post postEntity = postOp.get();

        visitIncrease(postEntity.getUser().getId());

        PostDetailRespDto postDetailRespDto = new PostDetailRespDto();
        postDetailRespDto.setPost(postOp.get());
        postDetailRespDto.setPageOwner(false);

        // 좋아요 유무 추가
        postDetailRespDto.setLove(false);

        return postDetailRespDto;
    }

    public PostDetailRespDto detailPost(Integer postId, User principal) {

        Optional<Post> postOp = postRepository.findById(postId);
        if (!postOp.isPresent()) {
            throw new CustomException("해당 포스팅이 존재하지 않습니다.");
        }

        Post postEntity = postOp.get();

        visitIncrease(postEntity.getUser().getId());

        // 권한체크 추가
        boolean isAuth = authCheck(principal.getId(), postEntity.getUser().getId());

        PostDetailRespDto postDetailRespDto = new PostDetailRespDto();
        postDetailRespDto.setPost(postOp.get());
        postDetailRespDto.setPageOwner(isAuth);

        // 좋아요 유무 추가
        Optional<Love> loveOp = loveRepository.findByUserIdAndPostId(principal.getId(), postId);
        if (loveOp.isEmpty()) {
            postDetailRespDto.setLove(false);
        } else {
            Love loveEntity = loveOp.get();
            postDetailRespDto.setLoveId(loveEntity.getId());
            postDetailRespDto.setLove(true);
        }

        return postDetailRespDto;
    }

    public void deletePost(Integer postId) {
        Optional<Post> postOp = postRepository.findById(postId);
        if (postOp.isEmpty()) {
            throw new CustomApiException("게시글이 존재하지 않습니다.");
        }

        postRepository.delete(postOp.get());
    }

    public LoveRespDto love(Integer postId, User user) {

        Optional<Post> postOp = postRepository.findById(postId);

        if (postOp.isEmpty()) {
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }

        Post postEntity = postOp.get();

        Love love = new Love();
        love.setPost(postEntity);
        love.setUser(user);
        Love loveEntity = loveRepository.save(love);

        LoveRespDto loveRespDto = new LoveRespDto();
        loveRespDto.setLoveId(loveEntity.getId());

        LoveRespDto.PostDto postDto = loveRespDto.new PostDto();
        postDto.setPostId(postId);
        postDto.setTitle(postEntity.getTitle());

        loveRespDto.setPost(postDto);

        return loveRespDto;

    }

    public void deleteLove(Integer loveId) {

        Optional<Love> loveOp = loveRepository.findById(loveId);
        if (loveOp.isEmpty()) {
            throw new CustomApiException("해당 좋아요가 존재하지 않습니다.");
        }

        loveRepository.deleteById(loveId);

    }

    // 방문자수 증가
    private Visit visitIncrease(Integer pageOwnerId) {

        Optional<Visit> visitOp = visitRepository.findByUserId(pageOwnerId);
        if (visitOp.isPresent()) {
            Visit visitEntity = visitOp.get();
            Long totalCount = visitEntity.getTotalCount();
            visitEntity.setTotalCount(totalCount + 1);
            return visitEntity;
        } else {
            log.error("미친 심각", "회원가입할때 Visit이 안 만들어지는 심각한 오류가 있습니다.");
            // sms 메시지 전송
            // email 전송
            // file 쓰기
            throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
        }
    }

    // 로그인 유저가 게시글 주인인지 확인하는 메서드
    private boolean authCheck(Integer principalId, Integer pageOwnerId) {
        boolean isAuth = false;
        if (principalId == pageOwnerId) {
            isAuth = true;
        } else {
            isAuth = false;
        }
        return isAuth;
    }
}
