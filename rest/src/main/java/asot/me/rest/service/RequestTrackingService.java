package asot.me.rest.service;

import asot.me.rest.dom.DailyRequestCount;
import asot.me.rest.dom.RequestHistory;
import asot.me.rest.repository.DailyRequestCountRepository;
import asot.me.rest.repository.RequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestTrackingService {

    private final RequestHistoryRepository requestHistoryRepository;
    private final DailyRequestCountRepository dailyRequestCountRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    @Transactional
    public void trackRequest(String url, String queryParams) {
        // Create and save request history
        RequestHistory history = RequestHistory.builder()
                .executionDateTime(LocalDateTime.now())
                .url(url)
                .queryParams(queryParams)
                .build();
        requestHistoryRepository.save(history);

        // Update daily count
        String today = LocalDate.now().format(dateFormatter);
        Optional<DailyRequestCount> dailyCountOpt = dailyRequestCountRepository.findById(today);

        if (dailyCountOpt.isPresent()) {
            DailyRequestCount dailyCount = dailyCountOpt.get();
            dailyCount.setTotalRequests(dailyCount.getTotalRequests() + 1);
            dailyRequestCountRepository.save(dailyCount);
        } else {
            DailyRequestCount newDailyCount = DailyRequestCount.builder()
                    .date(today)
                    .totalRequests(1L)
                    .build();
            dailyRequestCountRepository.save(newDailyCount);
        }
    }
}