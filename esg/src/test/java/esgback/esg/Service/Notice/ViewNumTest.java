package esgback.esg.Service.Notice;

import esgback.esg.Domain.Notice.Notice;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ViewNumTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    void checkViewNum() {
        Notice notice = noticeService.findById(1L);
        int viewNum = notice.getViewNum();

        noticeService.readNotice(String.valueOf(1L), 1L);

        Assertions.assertThat(notice.getViewNum()).isNotEqualTo(viewNum + 1);
    }
}
