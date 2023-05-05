package esgback.esg.Service.Wish;

import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Wish.Wish;
import esgback.esg.Repository.MarketRepository;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Repository.WishRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    public void wishControl(String memberId, Long marketId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        Wish wish = wishRepository.findByMemberIdAndMarketId(member.getId(), marketId);
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new NoResultException("해당 가게는 존재하지 않습니다."));

        if (wish == null) {
            wish = Wish.createWish(member, market);
            wishRepository.save(wish);
        } else {
            wishRepository.delete(wish);
        }
    }

    public List<SimpleMarketDto> wishList(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        List<Wish> wishList = wishRepository.findByMemberId(member.getId());
        List<SimpleMarketDto> result = new ArrayList<>();

        for (Wish wish : wishList) {
            result.add(new SimpleMarketDto(wish.getMarket().getId(), wish.getMarket().getName(), wish.getMarket().getPhotoUrl()));
        }

        return result;
    }
}
