package esgback.esg.Controller.Notice;

import esgback.esg.DTO.Notice.SimpleNoticeDto;
import esgback.esg.DTO.Response;
import esgback.esg.Service.Notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final Response response;

    @GetMapping("/notice/all")
    public ResponseEntity<?> getAllNotice() {
        List<SimpleNoticeDto> simpleNoticeDtoList = noticeService.getAllNotice().stream()
                .map(notice -> new SimpleNoticeDto(notice.getTitle()))
                .toList();

        if (simpleNoticeDtoList.isEmpty()) return response.fail("공지사항 목록이 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        else return response.success(simpleNoticeDtoList);
    }


}
