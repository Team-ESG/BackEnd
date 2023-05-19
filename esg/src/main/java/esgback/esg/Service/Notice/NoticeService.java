package esgback.esg.Service.Notice;

import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Notice.Notice;
import esgback.esg.Domain.Notice.Reader;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Repository.NoticeRepository;
import esgback.esg.Repository.ReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final ReadRepository readRepository;

    public List<Notice> getAllNotice() {
        return noticeRepository.findAll();
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
    }

    public void readNotice(String memberId, Long noticeId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
        Reader read = readRepository.findBymemberIdAndNoticeId(member.getId(), noticeId);

        // 사용자가 해당 공지사항을 읽은 기록이 없을 때만 read repository 업데이트 및 조회수 올려줌
        if (read == null) {
            Reader updateRead = new Reader();
            updateRead.setMember(member);
            updateRead.setNotice(notice);
            updateRead.setReadDate(new Date());
            updateRead.setIsRead(State.True);

            readRepository.save(updateRead);
            noticeRepository.increaseView(noticeId);
        }
    }
}
