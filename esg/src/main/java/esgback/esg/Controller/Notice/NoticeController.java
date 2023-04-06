package esgback.esg.Controller.Notice;

import esgback.esg.DTO.Notice.NoticeDto;
import esgback.esg.Domain.Notice.Notice;
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
                .map(notice -> new SimpleNoticeDto(notice.getId(), notice.getTitle()))
                .toList();

        if (simpleNoticeDtoList.isEmpty()) return response.fail("공지사항 목록이 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        else return response.success(simpleNoticeDtoList);
    }

    @GetMapping("/notice/{notice_id}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable("notice_id") Long noticeId) {

        try {
            Notice notice = noticeService.findById(noticeId);

            NoticeDto noticeDto = new NoticeDto(notice.getTitle(), notice.getViewNum(), notice.getWriteDate(), notice.getContent(), notice.getPhotoUrl());

            return response.success(noticeDto);
        } catch (IllegalArgumentException e) {
            return response.fail("존재하지 않는 게시물입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
