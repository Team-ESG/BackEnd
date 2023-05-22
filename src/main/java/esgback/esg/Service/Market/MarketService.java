package esgback.esg.Service.Market;

import esgback.esg.DTO.Market.MarketDto;
import esgback.esg.DTO.Market.UpdateMarketDto;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MarketRepository;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Repository.WishRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketService {
    private final MarketRepository marketRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;

    public MarketDto searchById(String memberId, Long marketId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new IllegalArgumentException("해당 가게는 존재하지 않습니다."));
        Boolean isWished = wishRepository.findByMemberIdAndMarketId(member.getId(), marketId) != null;

        return new MarketDto(market.getName(), market.getPhoneNumber(), market.getPhotoUrl(), market.getAddress(), market.getOwnerName(), isWished, market.getStartTime(), market.getEndTime(), market.getItems().size());
    }

    @Transactional
    public void updateMarketDetail(String email, UpdateMarketDto updateMarketDto) {
        Market market = marketRepository.findByEmail(email).orElseThrow(() -> new NoResultException("해당 가게는 존재하지 않습니다."));

        market.setName(updateMarketDto.getName());
        market.setPhoneNumber(updateMarketDto.getPhoneNumber());
        market.setPhotoUrl(updateMarketDto.getPhotoUrl());
        market.setAddress(updateMarketDto.getAddress());
        market.setOwnerName(updateMarketDto.getOwnerName());
        market.setStartTime(updateMarketDto.getStartTime());
        market.setEndTime(updateMarketDto.getEndTime());
    }
}
