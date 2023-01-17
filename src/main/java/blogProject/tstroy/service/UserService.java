package blogProject.tstroy.service;

import blogProject.tstroy.domain.Visit.Visit;
import blogProject.tstroy.domain.Visit.VisitRepository;
import blogProject.tstroy.domain.user.User;
import blogProject.tstroy.domain.user.UserRepository;
import blogProject.tstroy.handler.ex.CustomApiException;
import blogProject.tstroy.util.UtilFileUpload;
import blogProject.tstroy.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final VisitRepository visitRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.path}")
    private String uploadFolder;

    public User signUp(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        User userEntity = userRepository.save(user);

        Visit visit = new Visit(0L, user);
        visitRepository.save(visit);

        return userEntity;
    }

    public boolean checkDuplicateUsername(String username) {

        Optional<User> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public void changeProfileImg(User principal, MultipartFile profileImgFile, HttpSession session) {

        // 파일을 upload 폴더에 저장완료
        String profileImg = UtilFileUpload.write(uploadFolder, profileImgFile);

        Optional<User> userOp = userRepository.findByUsername(principal.getUsername());
        if (userOp.isPresent()) {
            User userEntity = userOp.get();
            userEntity.setProfileImg(profileImg);

            session.setAttribute("principal", userEntity);
        } else {
            throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
        }


    }

    public void updateUserInfo(UserUpdateDto userUpdateDto, User principal, HttpSession session) {


        if (!bCryptPasswordEncoder.matches(userUpdateDto.getRawPassword(), principal.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다.");
        }

        String rawPassword = userUpdateDto.getNewPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        Optional<User> userOp = userRepository.findByUsername(principal.getUsername());
        if (userOp.isPresent()) {
            User userEntity = userOp.get();
            userEntity.setUsername(userUpdateDto.getUsername());
            userEntity.setPassword(encodedPassword);
            userEntity.setEmail(userUpdateDto.getEmail());

            session.setAttribute("principal", userEntity);
        } else {
            throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
        }
    }




}
