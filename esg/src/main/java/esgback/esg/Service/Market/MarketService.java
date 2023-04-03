package esgback.esg.Service.Market;

import esgback.esg.DTO.MarketDto;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketService {
    private final MarketRepository marketRepository;

    public MarketDto searchById(Long id) {
        Market market = marketRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 가게는 존재하지 않습니다."));

        return new MarketDto(market.getEmail(), market.getPassword(), market.getPhoneNumber(), market.getPhotoUrl(), market.getAddress(), market.getOwnerName());
    }
}
