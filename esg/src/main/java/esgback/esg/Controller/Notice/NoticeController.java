package esgback.esg.Controller.Notice;

import esgback.esg.DTO.Notice.NoticeDto;
import esgback.esg.DTO.Notice.SimpleNoticeDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Notice.Notice;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Service.Notice.NoticeService;
import esgback.esg.Util.JWTUtil;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final Response response;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/notice/all")
    public ResponseEntity<?> getAllNotice() {
        List<SimpleNoticeDto> simpleNoticeDtoList = noticeService.getAllNotice().stream()
                .map(notice -> new SimpleNoticeDto(notice.getId(), notice.getTitle()))
                .toList();

        if (simpleNoticeDtoList.isEmpty()) return response.fail("공지사항 목록이 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        else return response.success(simpleNoticeDtoList);
    }

    @GetMapping("/notice/{notice_id}")
    public ResponseEntity<?> getNoticeDetail(@RequestHeader("authorization") String authorization, @PathVariable("notice_id") Long noticeId) {
        String token = authorization.substring(7);

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String memberId = String.valueOf(stringObjectMap.get("id"));

        try {
            noticeService.readNotice(memberId, noticeId);

            Notice notice = noticeService.findById(noticeId);

            NoticeDto noticeDto = new NoticeDto(notice.getTitle(), notice.getViewNum(), notice.getWriteDate(), notice.getContent(), notice.getPhotoUrl());

            return response.success(noticeDto);
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
