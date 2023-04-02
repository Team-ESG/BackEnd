package esgback.esg.Service.Member;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MessageService {
    private DefaultMessageService post;

    public MessageService(@Value("${COOL_SMS_API_KEY}") String api_key, @Value("${COOL_SMS_API_SECRET}") String api_secret) {
        this.post = NurigoApp.INSTANCE.initialize(api_key, api_secret, "https://api.coolsms.co.kr");
    }

    public String sendOneMsg(String num) {
        Message message = new Message();
        String sendNum = makeRandom();
        Boolean isSend = checkNum(num);

        if (!isSend) {
            throw new IllegalStateException("번호를 다시 입력하세요");
        }

        message.setFrom("01067939330");
        message.setTo(num);
        message.setText("인증코드는 " + sendNum);


        SingleMessageSentResponse response = post.sendOne(new SingleMessageSendingRequest(message));

        System.out.println(response);

        return "test";
    }

    public String makeRandom() {
        Random random = new Random();
        StringBuilder randNum = new StringBuilder();

        for (int i = 0; i <= 3; i++) {
            randNum.append(Integer.toString(random.nextInt()));
        }

        return randNum.toString();
    }

    public Boolean checkNum(String num) {

        if(num.length()>11)
            return false;

        for (int i = 0; i < num.length(); i++) {
            if((int)num.charAt(i) < 48 | (int)num.charAt(i) >57)
                return false;
        }

        return true;
    }
}