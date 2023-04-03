package esgback.esg.Service.Member;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class MessageService {
    private final DefaultMessageService post;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${SEND_NUM}")
    String sendFrom;

    public MessageService(@Value("${COOL_SMS_API_KEY}") String api_key, @Value("${COOL_SMS_API_SECRET}") String api_secret) {
        this.post = NurigoApp.INSTANCE.initialize(api_key, api_secret, "https://api.coolsms.co.kr");
    }

    public String sendOneMsg(String num) {
        Message message = new Message();
        String sendNum = makeRandom();
        String isSend = checkNum(num);

        if (isSend != "ok") {
            return isSend;
        }

        message.setFrom(sendFrom);
        message.setTo(num);
        message.setText("인증코드는 " + sendNum);

        ValueOperations<String, String> vop = redisTemplate.opsForValue();

        vop.set(num, sendNum, Duration.ofSeconds(300));//redis에 값 저장, 5분 이내

        SingleMessageSentResponse response = post.sendOne(new SingleMessageSendingRequest(message));

        return sendNum;
    }

    public String makeRandom() {
        Random random = new Random();
        StringBuilder randNum = new StringBuilder();

        for (int i = 0; i <= 3; i++) {
            randNum.append(random.nextInt(10));
        }

        return randNum.toString();
    }

    public String checkNum(String num) {

        if (num.length() < 11) {
            return "전화번호 길이가 너무 짧습니다.";
        } else if (num.length() > 11) {
            return "전화번호 길이가 너무 깁니다.";
        }

        for (int i = 0; i < num.length(); i++) {
            if((int)num.charAt(i) < 48 | (int)num.charAt(i) >57)
                return "특수문자 (-) 입력하지 않아도 됩니다.";
        }

        return "ok";
    }
}