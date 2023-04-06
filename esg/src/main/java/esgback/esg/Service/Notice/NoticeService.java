package esgback.esg.Service.Notice;

import esgback.esg.Domain.Notice.Notice;
import esgback.esg.Repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<Notice> getAllNotice() {
        return noticeRepository.findAll();
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
    }
}
