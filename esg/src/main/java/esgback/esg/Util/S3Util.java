package esgback.esg.Util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;
    private final JWTUtil jwtUtil;
    private final MarketRepository marketRepository;
    private final ItemRepository itemRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public void uploadMarketImg(MultipartFile multipartFile, String authorization) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String token = authorization.substring(7);

        Map<String, Object> tokenValue = jwtUtil.validateToken(token);
        String marketId = String.valueOf(tokenValue.get("id"));

        Optional<Market> find = marketRepository.findByEmail(marketId);
        Market market = find.orElseThrow(() -> new IllegalArgumentException("해당 마켓은 존재하지 않습니다."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

        String photoUrl = amazonS3Client.getUrl(bucket, fileName).toString();//저장된 사진 url인데 이것을 디비에 저장하면 되겠다
        Market newMarket = Market.updatePhoto(market, photoUrl);

        marketRepository.save(newMarket);
    }

    public void uploadItemImg(MultipartFile multipartFile, Long itemId) throws IOException {
        Optional<Item> find = itemRepository.findById(itemId);
        Item item = find.orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));

        String fileName = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        String photoUrl = amazonS3Client.getUrl(bucket, fileName).toString();//저장된 사진 url인데 이것을 디비에 저장하면 되겠다

        Item newItem = Item.updateItemImg(item, photoUrl);
        itemRepository.save(newItem);
    }
}
