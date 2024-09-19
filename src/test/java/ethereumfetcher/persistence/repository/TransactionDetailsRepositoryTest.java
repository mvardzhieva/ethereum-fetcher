package ethereumfetcher.persistence.repository;

import ethereumfetcher.persistence.model.TransactionDetails;
import ethereumfetcher.persistence.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionDetailsRepositoryTest {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    private TransactionDetails createTransactionDetails() {
        return new TransactionDetails(
                "0xcbc920e7bb89cbcb540a469a16226bf1057825283ab8eac3f45d00811eef8a64",
                TransactionStatus.SUCCESS,
                "0xc5a3664f031da2458646a01e18e6957fd1f43715524d94b7336a004b5635837d",
                BigInteger.valueOf(5702816),
                "0xd5e6f34bbd4251195c03e7bf3660677ed2315f70",
                "0x4c16d8c078ef6b56700c1be19a336915962df072",
                null,
                1,
                "0x6a627842000000000000000000000000d5e6f34bbd4251195c03e7bf3660677ed2315f70",
                "0"
        );
    }

    @Test
    public void testFindByTransactionHash() {
        TransactionDetails transactionDetails = createTransactionDetails();
        transactionDetailsRepository.save(transactionDetails);

        String transactionHash =
                "0xcbc920e7bb89cbcb540a469a16226bf1057825283ab8eac3f45d00811eef8a64";
        TransactionDetails result = transactionDetailsRepository
                .findByTransactionHash(transactionHash);

        assertNotNull(result);
        assertEquals(transactionHash, result.getTransactionHash());
    }

    @Test
    public void testFindAllResponseTransactionDetails() {
        TransactionDetails transactionDetails = createTransactionDetails();
        transactionDetailsRepository.save(transactionDetails);

        assertEquals(1, transactionDetailsRepository
                .findAllResponseTransactionDetails().size());
    }
}
