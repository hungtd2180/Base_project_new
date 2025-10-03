package org.example.base.services.user;

import org.example.base.constants.Constant;
import org.example.base.models.dto.Event;
import org.example.base.models.entity.user.UserAttempt;
import org.example.base.repositories.user.UserAttemptRepository;
import org.example.base.services.CrudService;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 15/07/2025
 * Time: 10:10 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class UserAttemptService extends CrudService<UserAttempt, Long> {
    private UserAttemptRepository userAttemptRepository;
    @Value("${tokenTime.attempt}")
    private Long attempts;
    @Value("${tokenTime.retry}")
    private Long retryTime;
    @Autowired
    public UserAttemptService(UserAttemptRepository repository){
        super(UserAttempt.class);
        this.repository = this.userAttemptRepository = repository;
    }

    public Event checkUserAttemptGetAccessToken(String username) {
        UserAttempt userAttempt = userAttemptRepository.findFirstByUsername(username);
        long currentTime = System.currentTimeMillis();
        Event result = new Event();
        //Lần đầu
        if (ObjectUtils.isEmpty(userAttempt)) {
            userAttempt = new UserAttempt(username, 1);
            result.payload = this.create(userAttempt);
            result.errorCode = Constant.ResultStatus.SUCCESS;
            return result;
        }
        if (!ObjectUtils.isEmpty(userAttempt.getTime())) {
            // Chưa đến giới hạn
            if (userAttempt.getAttempts() <= attempts) {
                userAttempt.setAttempts(userAttempt.getAttempts() + 1);
                userAttempt.setTime(currentTime);
                result.payload = this.update(userAttempt.getId(), userAttempt);
                result.errorCode = Constant.ResultStatus.SUCCESS;
                return result;
            }
            // nếu thời gian lâu thì reset
            if (currentTime - userAttempt.getUpdated() > retryTime) {
                userAttempt.setAttempts(1);
                userAttempt.setTime(currentTime);
                result.payload = this.update(userAttempt.getId(), userAttempt);
                result.errorCode = Constant.ResultStatus.SUCCESS;
                return result;
            }
            //nếu quá giới hạn thì block 5p
            if (userAttempt.getAttempts() > attempts && (currentTime - userAttempt.getUpdated() < retryTime)) {
                result.errorCode = Constant.ResultStatus.ERROR;
                return result;
            }
        }
        result.errorCode = Constant.ResultStatus.ERROR;
        return result;
    }
}
