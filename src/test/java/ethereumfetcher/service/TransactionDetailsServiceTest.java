package ethereumfetcher.service;

import ethereumfetcher.persistence.model.TransactionDetails;
import ethereumfetcher.persistence.model.TransactionStatus;
import ethereumfetcher.persistence.model.dto.ResponseTransactionDetails;
import ethereumfetcher.persistence.repository.TransactionDetailsRepository;
import ethereumfetcher.properties.EthereumFetcherProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.springframework.test.context.ActiveProfiles;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.List;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TransactionDetailsServiceTest {

    @Mock
    private TransactionDetailsRepository transactionDetailsRepositoryMock;

    @Mock
    private EthereumFetcherProperties ethereumFetcherPropertiesMock;

    @Mock
    private Web3j web3jClientMock;

    @InjectMocks
    private TransactionDetailsService transactionDetailsService;

    private final String TRANSACTION_HASH =
            "0xcbc920e7bb89cbcb540a469a16226bf1057825283ab8eac3f45d00811eef8a64";

    private final List<ResponseTransactionDetails> transactions =
            List.of(new ResponseTransactionDetails(
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
            ));

    private TransactionDetails createTransactionDetails() {
        return new TransactionDetails(
                TRANSACTION_HASH,
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
    public void testGetTransactionsByHashWhenTransactionExistsInDB() {
        when(transactionDetailsRepositoryMock.findByTransactionHash(anyString()))
                .thenReturn(createTransactionDetails());

        List<ResponseTransactionDetails> result =
                transactionDetailsService.getTransactionsByHash(List.of(TRANSACTION_HASH));

        assertEquals(1, result.size());
        assertEquals(TRANSACTION_HASH, result.get(0).getTransactionHash());
        verify(transactionDetailsRepositoryMock, times(1))
                .findByTransactionHash(TRANSACTION_HASH);
    }

    @Test
    public void testGetAllTransactions() {
        when(transactionDetailsRepositoryMock.findAllResponseTransactionDetails())
                .thenReturn(transactions);

        assertEquals(transactions, transactionDetailsService.getAllTransactions());
        verify(transactionDetailsRepositoryMock, times(1))
                .findAllResponseTransactionDetails();
    }

    @Test
    public void testGetTransactionsByRlpListWithValidRlpHex() {
        String rlphex = "f844b842307863626339323065376262383963626362353430613436396131363232366266313035373832353238336162386561633366343564303038313165656638613634";

        when(transactionDetailsRepositoryMock.findByTransactionHash(anyString()))
                .thenReturn(createTransactionDetails());

        List<ResponseTransactionDetails> result =
                transactionDetailsService.getTransactionsByRlpList(rlphex);

        assertEquals(1, result.size());
        assertEquals(TRANSACTION_HASH, result.get(0).getTransactionHash());
        verify(transactionDetailsRepositoryMock, times(1))
                .findByTransactionHash(TRANSACTION_HASH);
    }

    @Test
    public void testGetTransactionsByRlpListWithInValidRlpHex() {
        String invalidRlpEncoding = "fasdf1234";

        assertThrows(IllegalArgumentException.class, () -> {
            transactionDetailsService.getTransactionsByRlpList(invalidRlpEncoding);
        });
        verify(transactionDetailsRepositoryMock, never())
                .findByTransactionHash(TRANSACTION_HASH);
    }
}
