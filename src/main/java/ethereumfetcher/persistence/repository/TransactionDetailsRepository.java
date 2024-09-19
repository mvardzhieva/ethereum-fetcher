package ethereumfetcher.persistence.repository;

import ethereumfetcher.persistence.model.TransactionDetails;
import ethereumfetcher.persistence.model.dto.ResponseTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

    TransactionDetails findByTransactionHash(String transactionHash);

    @Query(value = "SELECT new ethereumfetcher.persistence.model.dto.ResponseTransactionDetails(" +
            "t.transactionHash, t.transactionStatus, t.blockHash, " +
            "t.blockNumber, t.from, t.to, t.contractAddress, " +
            "t.logsCount, t.input, t.value) FROM TransactionDetails t")
    List<ResponseTransactionDetails> findAllResponseTransactionDetails();
}
